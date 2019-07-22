package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSale;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSaleRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSaleSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsItemVariantSaleSearch;
import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.search.ItemVariantSaleSearch;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsItemVariantStockSearchResult;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
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

}
