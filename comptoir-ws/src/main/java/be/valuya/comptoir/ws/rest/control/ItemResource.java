package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItem;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsItemSearch;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.util.pagination.ItemColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsItemConverter;
import be.valuya.comptoir.ws.convert.search.FromWsItemSearchConverter;
import be.valuya.comptoir.ws.rest.api.ItemResourceApi;
import be.valuya.comptoir.ws.rest.api.util.PaginationParams;
import be.valuya.comptoir.ws.rest.validation.ActiveStateChecker;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class ItemResource implements ItemResourceApi {

    @EJB
    private StockService stockService;
    @Inject
    private FromWsItemConverter fromWsItemConverter;
    @Inject
    private FromWsItemSearchConverter fromWsItemSearchConverter;
    @Inject
    private ToWsItemConverter toWsItemConverter;
    @Inject
    private IdChecker idChecker;
    @Inject
    private RestPaginationUtil restPaginationUtil;
    @Inject
    private ActiveStateChecker activeStateChecker;
    @Inject
    private EmployeeAccessChecker employeeAccessChecker;

    public WsItemRef createItem(WsItem wsItem) {
        Item item = fromWsItemConverter.convert(wsItem);
        employeeAccessChecker.checkOwnCompany(item);
        Item savedItem = stockService.saveItem(item);

        WsItemRef itemRef = toWsItemConverter.reference(savedItem);

        return itemRef;
    }

    public WsItemRef updateItem(long id, WsItem wsItem) {
        idChecker.checkId(id, wsItem);
        Item item = fromWsItemConverter.convert(wsItem);
        employeeAccessChecker.checkOwnCompany(item);
        Item savedItem = stockService.saveItem(item);

        WsItemRef itemRef = toWsItemConverter.reference(savedItem);

        return itemRef;
    }

    public WsItem getItem(long id) {
        Item item = stockService.findItemById(id);
        employeeAccessChecker.checkOwnCompany(item);

        WsItem wsItem = toWsItemConverter.convert(item);

        return wsItem;
    }

    public void deleteItem(long id) {
        Item item = stockService.findItemById(id);
        employeeAccessChecker.checkOwnCompany(item);
        activeStateChecker.checkState(item, true);
        item.setActive(false);
        stockService.saveItem(item);
    }

    public WsItemSearchResult findItems(PaginationParams paginationParams, WsItemSearch wsItemSearch) {
        Pagination<Item, ItemColumn> pagination = restPaginationUtil.parsePagination(paginationParams, ItemColumn::valueOf);

        ItemSearch itemSearch = fromWsItemSearchConverter.convert(wsItemSearch);
        employeeAccessChecker.checkOwnCompany(itemSearch);

        List<Item> items = stockService.findItems(itemSearch, pagination);

        List<WsItemRef> wsItems = items.stream()
                .map(toWsItemConverter::reference)
                .collect(Collectors.toList());

        return restPaginationUtil.setResults(new WsItemSearchResult(), wsItems, pagination);
    }

}
