package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariant;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsItemVariantSearch;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.search.ItemVariantSearch;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.util.pagination.ItemVariantColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemVariantConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsItemVariantConverter;
import be.valuya.comptoir.ws.convert.search.FromWsItemVariantSearchConverter;
import be.valuya.comptoir.ws.rest.api.ItemVariantResourceApi;
import be.valuya.comptoir.ws.rest.api.util.PaginationParams;
import be.valuya.comptoir.ws.rest.validation.ActiveStateChecker;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;

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
public class ItemVariantResource implements ItemVariantResourceApi {

    @EJB
    private StockService stockService;
    @Inject
    private FromWsItemVariantConverter fromWsItemConverter;
    @Inject
    private FromWsItemVariantSearchConverter fromWsItemVariantSearchConverter;
    @Inject
    private ToWsItemVariantConverter toWsItemConverter;
    @Inject
    private IdChecker idChecker;
    @Context
    private HttpServletResponse response;
    @Context
    private UriInfo uriInfo;
    @Inject
    private RestPaginationUtil restPaginationUtil;
    @Inject
    private ActiveStateChecker activeStateChecker;
    @Inject
    private EmployeeAccessChecker accessChecker;

    public WsItemVariantRef createItemVariant(WsItemVariant wsItem) {
        ItemVariant itemVariant = fromWsItemConverter.convert(wsItem);
        accessChecker.checkOwnCompany(itemVariant.getItem());
        ItemVariant savedItem = stockService.saveItemVariant(itemVariant);

        WsItemVariantRef itemVariantRef = toWsItemConverter.reference(savedItem);

        return itemVariantRef;
    }

    public WsItemVariantRef updateItemVariant(long id, WsItemVariant wsItem) {
        idChecker.checkId(id, wsItem);
        ItemVariant itemVariant = fromWsItemConverter.convert(wsItem);
        accessChecker.checkOwnCompany(itemVariant.getItem());
        ItemVariant savedItem = stockService.saveItemVariant(itemVariant);

        WsItemVariantRef itemVariantRef = toWsItemConverter.reference(savedItem);

        return itemVariantRef;
    }

    public WsItemVariant getItemVariant(long id) {
        ItemVariant itemVariant = stockService.findItemVariantById(id);
        accessChecker.checkOwnCompany(itemVariant.getItem());

        WsItemVariant wsItem = toWsItemConverter.convert(itemVariant);

        return wsItem;
    }

    public void deleteItemVariant(long id) {
        ItemVariant itemVariant = stockService.findItemVariantById(id);
        accessChecker.checkOwnCompany(itemVariant.getItem());
        activeStateChecker.checkState(itemVariant, true);
        itemVariant.setActive(false);
        stockService.saveItemVariant(itemVariant);
    }

    public WsItemVariantSearchResult findItemVariants(PaginationParams paginationParams, WsItemVariantSearch wsItemVariantSearch) {
        Pagination<ItemVariant, ItemVariantColumn> pagination = restPaginationUtil.extractPagination(uriInfo, ItemVariantColumn::valueOf);

        ItemVariantSearch itemVariantSearch = fromWsItemVariantSearchConverter.convert(wsItemVariantSearch);
        accessChecker.checkOwnCompany(itemVariantSearch.getItemSearch());

        List<ItemVariant> items = stockService.findItemVariants(itemVariantSearch, pagination);

        List<WsItemVariantRef> wsItems = items.stream()
                .map(toWsItemConverter::reference)
                .collect(Collectors.toList());

        restPaginationUtil.addResultCount(response, pagination);

        return restPaginationUtil.setResults(new WsItemVariantSearchResult(), wsItems, pagination);
    }

}
