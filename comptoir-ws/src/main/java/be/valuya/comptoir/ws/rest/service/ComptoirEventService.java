package be.valuya.comptoir.ws.rest.service;

import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.accounting.AccountingTransaction;
import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.SaleService;
import be.valuya.comptoir.ws.convert.accounting.ToWsAccountingEntryConverter;
import be.valuya.comptoir.ws.convert.accounting.ToWsAccountingTransactionConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsItemVariantSaleConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsSaleConverter;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingEntry;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingEntryRef;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingTransactionRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSale;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSale;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.ws.rest.api.domain.event.WsComptoirEvent;
import be.valuya.comptoir.ws.rest.api.domain.event.WsComptoirServerEvent;
import be.valuya.comptoir.ws.rest.api.domain.event.WsSaleItemsUpdateEvent;
import be.valuya.comptoir.ws.rest.api.domain.event.WsSalePaymentEntriesUpdateEvent;
import be.valuya.comptoir.ws.rest.api.domain.event.WsSaleUpdateEvent;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class ComptoirEventService {

    @Inject
    private SaleService saleService;
    @Inject
    private ToWsSaleConverter toWsSaleConverter;
    @Inject
    private ToWsItemVariantSaleConverter toWsItemVariantSaleConverter;
    @Inject
    private ToWsAccountingEntryConverter toWsAccountingEntryConverter;
    @Inject
    private ToWsAccountingTransactionConverter toWsAccountingTransactionConverter;
    @Inject
    private Logger logger;

    private Map<Employee, EmployeeSaleEventSubscription> employeeWatchedSales;
    private Map<WsSaleRef, List<EmployeeSaleEventSubscription>> watchedSales;

    @PostConstruct
    public void init() {
        this.employeeWatchedSales = new HashMap<>();
        this.watchedSales = new HashMap<>();
    }

    public List<EmployeeSaleEventSubscription> getSaleSubscriptions(WsSaleRef saleRef) {
        return Optional.ofNullable(watchedSales.get(saleRef))
                .orElseGet(ArrayList::new);
    }

    public EmployeeSaleEventSubscription subscribeToSale(Employee employee, WsSaleRef saleRef, SseEventSink eventSink) {
        this.unsubscribeOtherEmployeeSales(employee, saleRef);
        EmployeeSaleEventSubscription eventSubscription = new EmployeeSaleEventSubscription(employee, saleRef, eventSink);
        subscribeEmployeeToSale(eventSubscription);
        return eventSubscription;
    }

    public void sendSaleItemsUpdate(Sse sse, EmployeeSaleEventSubscription employeeSaleEventSubscription) {
        SseEventSink eventSink = employeeSaleEventSubscription.getEventSink();
        WsSaleRef saleRef = employeeSaleEventSubscription.getSaleRef();
        Employee employee = employeeSaleEventSubscription.getEmployee();

        if (eventSink.isClosed()) {
            removeEmployeeSaleSubscription(employee, saleRef);
            return;
        }
        Sale sale = saleService.findSaleById(saleRef.getId());
        List<ItemVariantSale> itemSales = saleService.findItemSales(sale);
        List<WsItemVariantSale> wsItemVariantSales = itemSales.stream()
                .map(toWsItemVariantSaleConverter::convert)
                .collect(Collectors.toList());
        long itemCount = itemSales.size();

        WsSaleItemsUpdateEvent updateEvent = new WsSaleItemsUpdateEvent();
        updateEvent.setSaleRef(saleRef);
        updateEvent.setFirstPage(wsItemVariantSales);
        updateEvent.setPageSize(itemCount);
        updateEvent.setTotalCount(itemCount);

        dispatchEvent(sse, eventSink, updateEvent);
    }


    public void sendSaleUpdate(Sse sse, EmployeeSaleEventSubscription employeeSaleEventSubscription) {
        SseEventSink eventSink = employeeSaleEventSubscription.getEventSink();
        WsSaleRef saleRef = employeeSaleEventSubscription.getSaleRef();
        Employee employee = employeeSaleEventSubscription.getEmployee();

        if (eventSink.isClosed()) {
            removeEmployeeSaleSubscription(employee, saleRef);
            return;
        }

        Sale sale = saleService.findSaleById(saleRef.getId());
        BigDecimal totalPaid = saleService.getSaleTotalPayed(sale);
        BigDecimal saleTotalWithVat = sale.getVatExclusiveAmount()
                .add(sale.getVatAmount())
                .subtract(sale.getDiscountAmount());
        WsSale wsSale = toWsSaleConverter.convert(sale);


        WsSaleUpdateEvent updateEvent = new WsSaleUpdateEvent();
        updateEvent.setWsSale(wsSale);
        updateEvent.setTotalPaid(totalPaid);
        updateEvent.setTotalVatInclusive(saleTotalWithVat);

        dispatchEvent(sse, eventSink, updateEvent);
    }


    public void sendSaleAccountingEntriesUpdate(Sse sse, EmployeeSaleEventSubscription employeeSaleEventSubscription) {
        SseEventSink eventSink = employeeSaleEventSubscription.getEventSink();
        WsSaleRef saleRef = employeeSaleEventSubscription.getSaleRef();
        Employee employee = employeeSaleEventSubscription.getEmployee();

        if (eventSink.isClosed()) {
            removeEmployeeSaleSubscription(employee, saleRef);
            return;
        }
        Sale sale = saleService.findSaleById(saleRef.getId());
        @NotNull AccountingTransaction accountingTransaction = sale.getAccountingTransaction();
        WsAccountingTransactionRef wsAccountingTransactionRef = toWsAccountingTransactionConverter.reference(accountingTransaction);

        List<AccountingEntry> accountingEntries = saleService.findPaymentAccountingEntries(sale);
        List<WsAccountingEntry> wsAccountingEntries = accountingEntries.stream()
                .map(toWsAccountingEntryConverter::convert)
                .collect(Collectors.toList());
        long itemCount = accountingEntries.size();

        WsSalePaymentEntriesUpdateEvent updateEvent = new WsSalePaymentEntriesUpdateEvent();
        updateEvent.setSaleRef(saleRef);
        updateEvent.setAccountingTransactionRef(wsAccountingTransactionRef);
        updateEvent.setFirstPage(wsAccountingEntries);
        updateEvent.setPageSize(itemCount);
        updateEvent.setTotalCount(itemCount);

        dispatchEvent(sse, eventSink, updateEvent);
    }


    private <T extends WsComptoirServerEvent> void dispatchEvent(Sse sse, SseEventSink eventSink, T updateEvent) {
        OutboundSseEvent sseEvent = sse.newEventBuilder()
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .name(updateEvent.getEventType().name())
                .data(updateEvent)
                .build();
        eventSink.send(sseEvent);
    }

    private void unsubscribeOtherEmployeeSales(Employee employee, WsSaleRef saleRef) {
        EmployeeSaleEventSubscription subscriptionNullable = this.employeeWatchedSales.get(employee);
        Optional.ofNullable(subscriptionNullable)
                .filter(s -> !s.getSaleRef().equals(saleRef))
                .ifPresent(this::unsubscribe);
    }


    private void removeEmployeeSaleSubscription(Employee employee, WsSaleRef saleRef) {
        watchedSales.computeIfPresent(saleRef, (ref, subscriptions) -> removeEmplyeeFromWatchedSales(employee, subscriptions));
        employeeWatchedSales.computeIfPresent(employee, (e, s) -> removeEmployeeFromEmployeeWatchedSales(saleRef, s));
        logger.fine("Employee " + employee + " unsubscribed from sale " + saleRef);
    }

    private EmployeeSaleEventSubscription removeEmployeeFromEmployeeWatchedSales(WsSaleRef saleRef, EmployeeSaleEventSubscription s) {
        if (s.getSaleRef().equals(saleRef)) {
            return null;
        }
        return s;
    }

    private List<EmployeeSaleEventSubscription> removeEmplyeeFromWatchedSales(Employee employee, List<EmployeeSaleEventSubscription> subscriptions) {
        List<EmployeeSaleEventSubscription> remainingSubscriptions = subscriptions.stream()
                .filter(s -> !s.getEmployee().equals(employee))
                .collect(Collectors.toList());
        return remainingSubscriptions.isEmpty() ? null : remainingSubscriptions;
    }

    private void subscribeEmployeeToSale(EmployeeSaleEventSubscription eventSubscription) {
        Employee employee = eventSubscription.getEmployee();
        WsSaleRef saleRef = eventSubscription.getSaleRef();

        List<EmployeeSaleEventSubscription> subscriptionsToRemove = employeeWatchedSales.values().stream()
                .filter(subscription -> subscription.getSaleRef().equals(saleRef))
                .collect(Collectors.toList());
        subscriptionsToRemove.forEach(this::unsubscribe);

        employeeWatchedSales.put(employee, eventSubscription);
        List<EmployeeSaleEventSubscription> saleSubscriptions = watchedSales.computeIfAbsent(saleRef, ref -> new ArrayList<>());
        saleSubscriptions.add(eventSubscription);

        logger.fine("Employee " + employee + " subscribed to sale " + saleRef);
    }

    private void unsubscribe(EmployeeSaleEventSubscription employeeSaleEventSubscription) {
        WsSaleRef saleRef = employeeSaleEventSubscription.getSaleRef();
        Employee employee = employeeSaleEventSubscription.getEmployee();
        SseEventSink eventSink = employeeSaleEventSubscription.getEventSink();
        eventSink.close();

        this.removeEmployeeSaleSubscription(employee, saleRef);
    }


}
