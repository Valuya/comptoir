package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.commercial.WsItem;
import be.valuya.comptoir.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.api.domain.search.WsItemSearch;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.util.pagination.ItemColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsItemConverter;
import be.valuya.comptoir.ws.convert.search.FromWsItemSearchConverter;
import be.valuya.comptoir.ws.rest.validation.ActiveStateChecker;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.api.validation.NoId;
import be.valuya.comptoir.security.ComptoirRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class ItemResource {

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
    @Context
    private HttpServletResponse response;
    @Context
    private UriInfo uriInfo;
    @Inject
    private RestPaginationUtil restPaginationUtil;
    @Inject
    private ActiveStateChecker activeStateChecker;
    @Inject
    private EmployeeAccessChecker employeeAccessChecker;

    @POST
    @Valid
    public WsItemRef createItem(@NoId @Valid WsItem wsItem) {
        Item item = fromWsItemConverter.convert(wsItem);
        employeeAccessChecker.checkOwnCompany(item);
        Item savedItem = stockService.saveItem(item);

        WsItemRef itemRef = toWsItemConverter.reference(savedItem);

        return itemRef;
    }

    @Path("{id}")
    @PUT
    @Valid
    public WsItemRef updateItem(@PathParam("id") long id, @Valid WsItem wsItem) {
        idChecker.checkId(id, wsItem);
        Item item = fromWsItemConverter.convert(wsItem);
        employeeAccessChecker.checkOwnCompany(item);
        Item savedItem = stockService.saveItem(item);

        WsItemRef itemRef = toWsItemConverter.reference(savedItem);

        return itemRef;
    }

    @Path("{id}")
    @GET
    @Valid
    public WsItem getItem(@PathParam("id") long id) {
        Item item = stockService.findItemById(id);
        employeeAccessChecker.checkOwnCompany(item);

        WsItem wsItem = toWsItemConverter.convert(item);

        return wsItem;
    }

    @Path("{id}")
    @DELETE
    public void deleteItem(@PathParam("id") long id) {
        Item item = stockService.findItemById(id);
        employeeAccessChecker.checkOwnCompany(item);
        activeStateChecker.checkState(item, true);
        item.setActive(false);
        stockService.saveItem(item);
    }

    @POST
    @Path("search")
    @Valid
    public List<WsItem> findItems(@Valid WsItemSearch wsItemSearch) {
        Pagination<Item, ItemColumn> pagination = restPaginationUtil.extractPagination(uriInfo, ItemColumn::valueOf);

        ItemSearch itemSearch = fromWsItemSearchConverter.convert(wsItemSearch);
        employeeAccessChecker.checkOwnCompany(itemSearch);

        List<Item> items = stockService.findItems(itemSearch, pagination);

        List<WsItem> wsItems = items.stream()
                .map(toWsItemConverter::convert)
                .collect(Collectors.toList());

        restPaginationUtil.addResultCount(response, pagination);

        return wsItems;
    }

}
