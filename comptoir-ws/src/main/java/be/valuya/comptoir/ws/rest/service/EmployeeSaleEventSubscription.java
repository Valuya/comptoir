package be.valuya.comptoir.ws.rest.service;

import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSaleRef;

import javax.ws.rs.sse.SseEventSink;

public class EmployeeSaleEventSubscription {

    private Employee employee;
    private WsSaleRef saleRef;
    private SseEventSink eventSink;

    public EmployeeSaleEventSubscription(Employee employee, WsSaleRef saleRef, SseEventSink eventSink) {
        this.employee = employee;
        this.saleRef = saleRef;
        this.eventSink = eventSink;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public WsSaleRef getSaleRef() {
        return saleRef;
    }

    public void setSaleRef(WsSaleRef saleRef) {
        this.saleRef = saleRef;
    }

    public SseEventSink getEventSink() {
        return eventSink;
    }

    public void setEventSink(SseEventSink eventSink) {
        this.eventSink = eventSink;
    }
}
