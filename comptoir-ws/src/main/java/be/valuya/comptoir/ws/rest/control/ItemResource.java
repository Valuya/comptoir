package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.commercial.WsItem;
import be.valuya.comptoir.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.api.domain.search.WsItemSearch;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.util.pagination.ItemColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsItemConverter;
import be.valuya.comptoir.ws.convert.search.FromWsItemSearchConverter;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.NoId;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/item")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
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

    @POST
    @Valid
    public WsItemRef createItem(@NoId @Valid WsItem wsItem) {
        Item item = fromWsItemConverter.convert(wsItem);
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
        Item savedItem = stockService.saveItem(item);

        WsItemRef itemRef = toWsItemConverter.reference(savedItem);

        return itemRef;
    }

    @Path("{id}")
    @GET
    @Valid
    public WsItem getItem(@PathParam("id") long id) {
        Item item = stockService.findItemById(id);

        WsItem wsItem = toWsItemConverter.convert(item);

        return wsItem;
    }

    @Path("{id}")
    @DELETE
    public void deleteItem(@PathParam("id") long id) {
        Item item = stockService.findItemById(id);
        item.setActive(Boolean.FALSE);
        stockService.saveItem(item);
    }

    @POST
    @Path("search")
    @Valid
    public List<WsItem> findItems(@Valid WsItemSearch wsItemSearch) {
        Pagination<Item, ItemColumn> pagination = restPaginationUtil.extractPagination(uriInfo, ItemColumn::valueOf);

        ItemSearch itemSearch = fromWsItemSearchConverter.convert(wsItemSearch);

        List<Item> items = stockService.findItems(itemSearch, pagination);

        List<WsItem> wsItems = items.stream()
                .map(toWsItemConverter::convert)
                .collect(Collectors.toList());

        restPaginationUtil.addResultCount(response, pagination);

        return wsItems;
    }

}
