package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.model.commercial.ItemVariantSalePriceDetails;
import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.service.AccountingUtils;
import be.valuya.comptoir.ws.convert.commercial.ToWsItemVariantSalePriceDetailsConverter;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSale;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSalePriceDetails;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSaleRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSaleSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsItemVariantSaleSearch;
import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.search.ItemVariantSaleSearch;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.service.SaleService;
import be.valuya.comptoir.util.pagination.ItemVariantSaleColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemVariantSaleConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsItemVariantSaleConverter;
import be.valuya.comptoir.ws.convert.search.FromWsItemVariantSaleSearchConverter;
import be.valuya.comptoir.ws.rest.api.ItemVariantSaleResourceApi;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.SaleStateChecker;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class ItemVariantSaleResource implements ItemVariantSaleResourceApi {

    @EJB
    private SaleService saleService;
    @Inject
    private FromWsItemVariantSaleConverter fromWsItemVariantSaleConverter;
    @Inject
    private FromWsItemVariantSaleSearchConverter fromWsItemVariantSaleSearchConverter;
    @Inject
    private ToWsItemVariantSaleConverter toWsItemVariantSaleConverter;
    @Inject
    private ToWsItemVariantSalePriceDetailsConverter toWsItemVariantSalePriceDetailsConverter;
    @Inject
    private IdChecker idChecker;
    @Inject
    private SaleStateChecker saleStateChecker;
    @Context
    private HttpServletResponse response;
    @Context
    private UriInfo uriInfo;
    @Inject
    private RestPaginationUtil restPaginationUtil;
    @Inject
    private EmployeeAccessChecker accessChecker;

    public WsItemVariantSaleRef createItemVariantSale(WsItemVariantSale wsItemVariantSale) {
        ItemVariantSale itemVariantSale = fromWsItemVariantSaleConverter.convert(wsItemVariantSale);
        accessChecker.checkOwnCompany(itemVariantSale.getSale());

        ItemVariantSale savedItemVariantSale = saleService.saveItemSale(itemVariantSale);
        WsItemVariantSaleRef itemVariantSaleRef = toWsItemVariantSaleConverter.reference(savedItemVariantSale);
        WsItemVariantSaleRef savedItemVariantSaleRef = itemVariantSaleRef;
        return savedItemVariantSaleRef;
    }

    public WsItemVariantSaleRef updateItemVariantSale(long id, WsItemVariantSale wsItemVariantSale) {
        idChecker.checkId(id, wsItemVariantSale);

        ItemVariantSale existingItemVariantSale = saleService.findItemSaleById(id);
        accessChecker.checkOwnCompany(existingItemVariantSale.getSale());
        Sale sale = existingItemVariantSale.getSale();
        saleStateChecker.checkState(sale, false);

        ItemVariantSale updatedItemVariantSale = fromWsItemVariantSaleConverter.patch(existingItemVariantSale, wsItemVariantSale);

        ItemVariantSale savedItemVariantSale = saleService.saveItemSale(updatedItemVariantSale);
        WsItemVariantSaleRef itemVariantSaleRef = toWsItemVariantSaleConverter.reference(savedItemVariantSale);

        return itemVariantSaleRef;
    }

    public WsItemVariantSale getItemVariantSale(long id) {
        ItemVariantSale itemVariantSale = saleService.findItemSaleById(id);
        accessChecker.checkOwnCompany(itemVariantSale.getSale());

        WsItemVariantSale wsItemVariantSale = toWsItemVariantSaleConverter.convert(itemVariantSale);

        return wsItemVariantSale;
    }

    public WsItemVariantSaleSearchResult findItemVariantSales(WsItemVariantSaleSearch wsItemVariantSaleSearch) {
        Pagination<ItemVariantSale, ItemVariantSaleColumn> pagination = restPaginationUtil.extractPagination(uriInfo, ItemVariantSaleColumn::valueOf);
        ItemVariantSaleSearch itemVariantSaleSearch = fromWsItemVariantSaleSearchConverter.convert(wsItemVariantSaleSearch);
        accessChecker.checkOwnCompany(itemVariantSaleSearch);
        List<ItemVariantSale> itemVariantSales = saleService.findItemSales(itemVariantSaleSearch, pagination);

        List<WsItemVariantSaleRef> wsItemVariantSales = itemVariantSales.stream()
                .map(toWsItemVariantSaleConverter::reference)
                .collect(Collectors.toList());

        restPaginationUtil.addResultCount(response, pagination);
        return restPaginationUtil.setResults(new WsItemVariantSaleSearchResult(), wsItemVariantSales, pagination);
    }

    public void deleteItemVariantSale(long id) {
        ItemVariantSale itemVariantSale = saleService.findItemSaleById(id);
        if (itemVariantSale == null) {
            return;
        }
        accessChecker.checkOwnCompany(itemVariantSale.getSale());
        Sale sale = itemVariantSale.getSale();
        saleStateChecker.checkState(sale, false); // TODO: replace with bean validation

        saleService.removeItemSale(itemVariantSale);
    }

    @Override
    public WsItemVariantSalePriceDetails getItemVariantSalePrice(long id) {
        ItemVariantSale itemVariantSale = saleService.findItemSaleById(id);
        accessChecker.checkOwnCompany(itemVariantSale.getSale());

        ItemVariantSalePriceDetails priceDetails = AccountingUtils.calcItemVariantSalePriceDetails(itemVariantSale);
        WsItemVariantSalePriceDetails wsItemVariantSalePriceDetails = toWsItemVariantSalePriceDetailsConverter.convert(priceDetails, itemVariantSale);
        return wsItemVariantSalePriceDetails;
    }


    @Override
    public WsItemVariantSalePriceDetails setItemVariantSaleQuantity(long id, @NotNull Integer quantity) {
        ItemVariantSale itemVariantSale = saleService.findItemSaleById(id);
        accessChecker.checkOwnCompany(itemVariantSale.getSale());

        itemVariantSale.setQuantity(BigDecimal.valueOf(quantity));
        ItemVariantSale updatedVariantSale = saleService.saveItemSale(itemVariantSale);

        ItemVariantSalePriceDetails priceDetails = AccountingUtils.calcItemVariantSalePriceDetails(updatedVariantSale);
        WsItemVariantSalePriceDetails wsItemVariantSalePriceDetails = toWsItemVariantSalePriceDetailsConverter.convert(priceDetails, updatedVariantSale);
        return wsItemVariantSalePriceDetails;
    }

    @Override
    public WsItemVariantSalePriceDetails setItemVariantSaleUnitPriceVatExclusive(long id, @NotNull BigDecimal unitPriceVatExclusive) {
        ItemVariantSale itemVariantSale = saleService.findItemSaleById(id);
        accessChecker.checkOwnCompany(itemVariantSale.getSale());

        Price price = itemVariantSale.getPrice();
        price.setVatExclusive(unitPriceVatExclusive);
        ItemVariantSale updatedVariantSale = saleService.saveItemSale(itemVariantSale);

        ItemVariantSalePriceDetails priceDetails = AccountingUtils.calcItemVariantSalePriceDetails(updatedVariantSale);
        WsItemVariantSalePriceDetails wsItemVariantSalePriceDetails = toWsItemVariantSalePriceDetailsConverter.convert(priceDetails, updatedVariantSale);
        return wsItemVariantSalePriceDetails;
    }

    @Override
    public WsItemVariantSalePriceDetails setItemVariantSaleTotalVatExclusivePriorDiscount(long id, @NotNull BigDecimal totalVatExclusivePriorDiscount) {
        ItemVariantSale itemVariantSale = saleService.findItemSaleById(id);
        accessChecker.checkOwnCompany(itemVariantSale.getSale());

        BigDecimal unitPriceVatExclusice = AccountingUtils.calcUnitPriceVatExclusiveFromTotalVatExclusivePriorDiscount(itemVariantSale, totalVatExclusivePriorDiscount);
        Price price = itemVariantSale.getPrice();
        price.setVatExclusive(unitPriceVatExclusice);
        ItemVariantSale updatedVariantSale = saleService.saveItemSale(itemVariantSale);

        ItemVariantSalePriceDetails priceDetails = AccountingUtils.calcItemVariantSalePriceDetails(updatedVariantSale);
        WsItemVariantSalePriceDetails wsItemVariantSalePriceDetails = toWsItemVariantSalePriceDetailsConverter.convert(priceDetails, updatedVariantSale);
        return wsItemVariantSalePriceDetails;
    }

    @Override
    public WsItemVariantSalePriceDetails setItemVariantSaleDiscountRatio(long id, @NotNull BigDecimal discountRatio) {
        ItemVariantSale itemVariantSale = saleService.findItemSaleById(id);
        accessChecker.checkOwnCompany(itemVariantSale.getSale());

        Price price = itemVariantSale.getPrice();
        price.setDiscountRatio(discountRatio);
        ItemVariantSale updatedVariantSale = saleService.saveItemSale(itemVariantSale);

        ItemVariantSalePriceDetails priceDetails = AccountingUtils.calcItemVariantSalePriceDetails(updatedVariantSale);
        WsItemVariantSalePriceDetails wsItemVariantSalePriceDetails = toWsItemVariantSalePriceDetailsConverter.convert(priceDetails, updatedVariantSale);
        return wsItemVariantSalePriceDetails;
    }

    @Override
    public WsItemVariantSalePriceDetails setItemVariantSaleDiscountAmount(long id, @NotNull BigDecimal discountAmount) {
        ItemVariantSale itemVariantSale = saleService.findItemSaleById(id);
        accessChecker.checkOwnCompany(itemVariantSale.getSale());

        BigDecimal discountRate = AccountingUtils.calcDiscountRateFromDiscountAmount(itemVariantSale, discountAmount);
        Price price = itemVariantSale.getPrice();
        price.setDiscountRatio(discountAmount);
        ItemVariantSale updatedVariantSale = saleService.saveItemSale(itemVariantSale);

        ItemVariantSalePriceDetails priceDetails = AccountingUtils.calcItemVariantSalePriceDetails(updatedVariantSale);
        WsItemVariantSalePriceDetails wsItemVariantSalePriceDetails = toWsItemVariantSalePriceDetailsConverter.convert(priceDetails, updatedVariantSale);
        return wsItemVariantSalePriceDetails;
    }

    @Override
    public WsItemVariantSalePriceDetails setItemVariantSaleTotalVatExclusive(long id, @NotNull BigDecimal totalVatExclusive) {
        ItemVariantSale itemVariantSale = saleService.findItemSaleById(id);
        accessChecker.checkOwnCompany(itemVariantSale.getSale());

        BigDecimal unitPrice = AccountingUtils.calcUnitPriceVatExclusiveFromTotalVatExclusive(itemVariantSale, totalVatExclusive);
        Price price = itemVariantSale.getPrice();
        price.setVatExclusive(unitPrice);
        ItemVariantSale updatedVariantSale = saleService.saveItemSale(itemVariantSale);

        ItemVariantSalePriceDetails priceDetails = AccountingUtils.calcItemVariantSalePriceDetails(updatedVariantSale);
        WsItemVariantSalePriceDetails wsItemVariantSalePriceDetails = toWsItemVariantSalePriceDetailsConverter.convert(priceDetails, updatedVariantSale);
        return wsItemVariantSalePriceDetails;
    }

    @Override
    public WsItemVariantSalePriceDetails setItemVariantSaleVatRate(long id, @NotNull BigDecimal vatRate) {
        ItemVariantSale itemVariantSale = saleService.findItemSaleById(id);
        accessChecker.checkOwnCompany(itemVariantSale.getSale());

        Price price = itemVariantSale.getPrice();
        price.setVatRate(vatRate);
        ItemVariantSale updatedVariantSale = saleService.saveItemSale(itemVariantSale);

        ItemVariantSalePriceDetails priceDetails = AccountingUtils.calcItemVariantSalePriceDetails(updatedVariantSale);
        WsItemVariantSalePriceDetails wsItemVariantSalePriceDetails = toWsItemVariantSalePriceDetailsConverter.convert(priceDetails, updatedVariantSale);
        return wsItemVariantSalePriceDetails;
    }

    @Override
    public WsItemVariantSalePriceDetails setItemVariantSaleVatAmount(long id, @NotNull BigDecimal vatAmount) {
        ItemVariantSale itemVariantSale = saleService.findItemSaleById(id);
        accessChecker.checkOwnCompany(itemVariantSale.getSale());

        BigDecimal vatRate = AccountingUtils.calcVatRateFromVatAmount(itemVariantSale, vatAmount);
        Price price = itemVariantSale.getPrice();
        price.setVatRate(vatRate);
        ItemVariantSale updatedVariantSale = saleService.saveItemSale(itemVariantSale);

        ItemVariantSalePriceDetails priceDetails = AccountingUtils.calcItemVariantSalePriceDetails(updatedVariantSale);
        WsItemVariantSalePriceDetails wsItemVariantSalePriceDetails = toWsItemVariantSalePriceDetailsConverter.convert(priceDetails, updatedVariantSale);
        return wsItemVariantSalePriceDetails;
    }

    @Override
    public WsItemVariantSalePriceDetails setItemVariantSaleTotalVatInclusive(long id, @NotNull BigDecimal totalVatInclusive) {
        ItemVariantSale itemVariantSale = saleService.findItemSaleById(id);
        accessChecker.checkOwnCompany(itemVariantSale.getSale());

        BigDecimal unitPrice = AccountingUtils.calcUnitPriceVatExclusiveFromTotalVatInclusive(itemVariantSale, totalVatInclusive);
        Price price = itemVariantSale.getPrice();
        price.setVatExclusive(unitPrice);
        ItemVariantSale updatedVariantSale = saleService.saveItemSale(itemVariantSale);

        ItemVariantSalePriceDetails priceDetails = AccountingUtils.calcItemVariantSalePriceDetails(updatedVariantSale);
        WsItemVariantSalePriceDetails wsItemVariantSalePriceDetails = toWsItemVariantSalePriceDetailsConverter.convert(priceDetails, updatedVariantSale);
        return wsItemVariantSalePriceDetails;
    }

}
