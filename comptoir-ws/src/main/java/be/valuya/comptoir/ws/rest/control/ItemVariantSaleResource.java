package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.commercial.WsItemVariantSale;
import be.valuya.comptoir.api.domain.commercial.WsItemVariantSaleRef;
import be.valuya.comptoir.api.domain.search.WsItemVariantSaleSearch;
import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.search.ItemVariantSaleSearch;
import be.valuya.comptoir.service.SaleService;
import be.valuya.comptoir.ws.config.HeadersConfig;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemVariantSaleConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsItemVariantSaleConverter;
import be.valuya.comptoir.ws.convert.search.FromWsItemVariantSaleSearchConverter;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.NoId;
import be.valuya.comptoir.ws.rest.validation.SaleStateChecker;
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

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/itemVariantSale")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ItemVariantSaleResource {

    @EJB
    private SaleService saleService;
    @Inject
    private FromWsItemVariantSaleConverter fromWsItemSaleConverter;
    @Inject
    private FromWsItemVariantSaleSearchConverter fromWsItemSaleSearchConverter;
    @Inject
    private ToWsItemVariantSaleConverter toWsItemSaleConverter;
    @Inject
    private IdChecker idChecker;
    @Inject
    private SaleStateChecker saleStateChecker;
    @Context
    private HttpServletResponse response;

    @POST
    @Valid
    public WsItemVariantSaleRef createItemSale(@NoId @Valid WsItemVariantSale wsItemSale) {
        ItemVariantSale itemSale = fromWsItemSaleConverter.convert(wsItemSale);
        ItemVariantSale savedItemSale = saleService.saveItemSale(itemSale);
        WsItemVariantSaleRef itemSaleRef = toWsItemSaleConverter.reference(savedItemSale);
        WsItemVariantSaleRef savedItemSaleRef = itemSaleRef;
        return savedItemSaleRef;
    }

    @Path("{id}")
    @PUT
    @Valid
    public WsItemVariantSaleRef updateItemSale(@PathParam("id") long id, @Valid WsItemVariantSale wsItemSale) {
        idChecker.checkId(id, wsItemSale);

        ItemVariantSale existingItemSale = saleService.findItemSaleById(id);
        ItemVariantSale updatedItemSale = fromWsItemSaleConverter.patch(existingItemSale, wsItemSale);

        ItemVariantSale savedItemSale = saleService.saveItemSale(updatedItemSale);
        WsItemVariantSaleRef itemSaleRef = toWsItemSaleConverter.reference(savedItemSale);

        return itemSaleRef;
    }

    @Path("{id}")
    @GET
    @Valid
    public WsItemVariantSale getItemSale(@PathParam("id") long id) {
        ItemVariantSale itemSale = saleService.findItemSaleById(id);

        WsItemVariantSale wsItemSale = toWsItemSaleConverter.convert(itemSale);

        return wsItemSale;
    }

    @POST
    @Path("search")
    @Valid
    public List<WsItemVariantSale> findItemSales(@Valid WsItemVariantSaleSearch wsItemSaleSearch) {
        ItemVariantSaleSearch itemSaleSearch = fromWsItemSaleSearchConverter.convert(wsItemSaleSearch);
        List<ItemVariantSale> itemSales = saleService.findItemSales(itemSaleSearch);

        List<WsItemVariantSale> wsItemSales = itemSales.stream()
                .map(toWsItemSaleConverter::convert)
                .collect(Collectors.toList());

        response.setHeader(HeadersConfig.LIST_RESULTS_COUNT_HEADER, "101");
        return wsItemSales;
    }

    @DELETE
    @Path("{id}")
    public void deleteItemSale(@PathParam("id") long id) {
        ItemVariantSale itemSale = saleService.findItemSaleById(id);
        if (itemSale == null) {
            return;
        }
        Sale sale = itemSale.getSale();
        saleStateChecker.checkState(sale, false); // TODO: replace with bean validation

        saleService.removeItemSale(itemSale);
    }

}
