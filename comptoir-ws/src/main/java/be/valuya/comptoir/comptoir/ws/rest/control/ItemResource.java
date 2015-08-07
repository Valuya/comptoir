package be.valuya.comptoir.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.commercial.WsItem;
import be.valuya.comptoir.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.comptoir.ws.convert.commercial.FromWsItemConverter;
import be.valuya.comptoir.comptoir.ws.convert.commercial.ToWsItemConverter;
import be.valuya.comptoir.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.comptoir.ws.rest.validation.NoId;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.util.pagination.ItemColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.Sort;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/item")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ItemResource {

    @EJB
    private StockService stockService;
    @Inject
    private FromWsItemConverter fromWsItemConverter;
    @Inject
    private ToWsItemConverter toWsItemConverter;
    @Inject
    private IdChecker idChecker;
    @Context
    private HttpServletResponse response;

    @POST

    public WsItemRef createItem(@NoId WsItem wsItem) {
        return saveItem(wsItem);
    }

    @Path("{id}")
    @PUT
    public WsItemRef saveItem(@PathParam("id") long id, WsItem wsItem) {
        idChecker.checkId(id, wsItem);
        return saveItem(wsItem);
    }

    @Path("{id}")
    @GET
    public WsItem getItem(@PathParam("id") long id) {
        Item item = stockService.findItemById(id);

        WsItem wsItem = toWsItemConverter.convert(item);

        return wsItem;
    }

    @POST
    @Path("search")
    public List<WsItem> findItems(ItemSearch itemSearch, @QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("maxResults") @DefaultValue("10") int maxResults, @QueryParam("sorts") List<Sort<ItemColumn>> sorts) {
        Pagination<Item, ItemColumn> pagination = new Pagination(offset, maxResults, sorts);
        List<Item> items = stockService.findItems(itemSearch, pagination);

        List<WsItem> wsItems = items.stream()
                .map(toWsItemConverter::convert)
                .collect(Collectors.toList());

        //response.setHeader("size", 1234);
        return wsItems;
    }

    private WsItemRef saveItem(WsItem wsItem) {
        Item item = fromWsItemConverter.convert(wsItem);
        Item savedItem = stockService.saveItem(item);

        WsItemRef itemRef = toWsItemConverter.reference(savedItem);

        return itemRef;
    }

}
