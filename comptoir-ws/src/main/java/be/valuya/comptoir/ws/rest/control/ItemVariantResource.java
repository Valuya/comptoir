package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.commercial.WsItemVariant;
import be.valuya.comptoir.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.api.domain.search.WsItemVariantSearch;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.search.ItemVariantSearch;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.util.pagination.ItemVariantColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemVariantConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsItemVariantConverter;
import be.valuya.comptoir.ws.convert.search.FromWsItemVariantSearchConverter;
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
@Path("/itemVariant")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ItemVariantResource {

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

    @POST
    @Valid
    public WsItemVariantRef createItemVariant(@NoId @Valid WsItemVariant wsItem) {
        ItemVariant itemVariant = fromWsItemConverter.convert(wsItem);
        ItemVariant savedItem = stockService.saveItemVariant(itemVariant);

        WsItemVariantRef itemVariantRef = toWsItemConverter.reference(savedItem);

        return itemVariantRef;
    }

    @Path("{id}")
    @PUT
    @Valid
    public WsItemVariantRef updateItemVariant(@PathParam("id") long id, @Valid WsItemVariant wsItem) {
        idChecker.checkId(id, wsItem);
        ItemVariant itemVariant = fromWsItemConverter.convert(wsItem);
        ItemVariant savedItem = stockService.saveItemVariant(itemVariant);

        WsItemVariantRef itemVariantRef = toWsItemConverter.reference(savedItem);

        return itemVariantRef;
    }

    @Path("{id}")
    @GET
    @Valid
    public WsItemVariant getItemVariant(@PathParam("id") long id) {
        ItemVariant itemVariant = stockService.findItemVariantById(id);

        WsItemVariant wsItem = toWsItemConverter.convert(itemVariant);

        return wsItem;
    }

    @Path("{id}")
    @DELETE
    public void deleteItemVariant(@PathParam("id") long id) {
        ItemVariant itemVariant = stockService.findItemVariantById(id);
        itemVariant.setActive(Boolean.FALSE);
        stockService.saveItemVariant(itemVariant);
    }

    @POST
    @Path("search")
    @Valid
    public List<WsItemVariant> findItemVariants(@Valid WsItemVariantSearch wsItemVariantSearch) {
        Pagination<ItemVariant, ItemVariantColumn> pagination = restPaginationUtil.extractPagination(uriInfo, ItemVariantColumn::valueOf);

        ItemVariantSearch itemVariantSearch = fromWsItemVariantSearchConverter.convert(wsItemVariantSearch);

        List<ItemVariant> items = stockService.findItemVariants(itemVariantSearch, pagination);

        List<WsItemVariant> wsItems = items.stream()
                .map(toWsItemConverter::convert)
                .collect(Collectors.toList());

        restPaginationUtil.addResultCount(response, pagination);

        return wsItems;
    }

}
