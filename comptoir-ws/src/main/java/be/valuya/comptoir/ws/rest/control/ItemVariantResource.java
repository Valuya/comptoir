package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.commercial.WsItemVariant;
import be.valuya.comptoir.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.api.domain.search.WsItemSearch;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.util.pagination.ItemVariantColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemVariantConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsItemVariantConverter;
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
    private FromWsItemSearchConverter fromWsItemSearchConverter;
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
        ItemVariant savedItem = stockService.saveItem(itemVariant);

        WsItemVariantRef itemVariantRef = toWsItemConverter.reference(savedItem);

        return itemVariantRef;
    }

    @Path("{id}")
    @PUT
    @Valid
    public WsItemVariantRef updateItemVariant(@PathParam("id") long id, @Valid WsItemVariant wsItem) {
        idChecker.checkId(id, wsItem);
        ItemVariant itemVariant = fromWsItemConverter.convert(wsItem);
        ItemVariant savedItem = stockService.saveItem(itemVariant);

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

    @POST
    @Path("search")
    @Valid
    public List<WsItemVariant> findItemVariants(@Valid WsItemSearch wsItemSearch) {
        Pagination<ItemVariant, ItemVariantColumn> pagination = restPaginationUtil.extractPagination(uriInfo, ItemVariantColumn::valueOf);

        ItemSearch itemSearch = fromWsItemSearchConverter.convert(wsItemSearch);

        List<ItemVariant> items = stockService.findItemVariants(itemSearch, pagination);

        List<WsItemVariant> wsItems = items.stream()
                .map(toWsItemConverter::convert)
                .collect(Collectors.toList());

        restPaginationUtil.addResultCount(response, pagination);

        return wsItems;
    }

}
