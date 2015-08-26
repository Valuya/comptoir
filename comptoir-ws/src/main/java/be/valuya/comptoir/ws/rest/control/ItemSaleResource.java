package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.commercial.WsItemSale;
import be.valuya.comptoir.api.domain.commercial.WsItemSaleRef;
import be.valuya.comptoir.api.domain.search.WsItemSaleSearch;
import be.valuya.comptoir.model.commercial.ItemSale;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.search.ItemSaleSearch;
import be.valuya.comptoir.service.SaleService;
import be.valuya.comptoir.ws.config.HeadersConfig;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemSaleConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsItemSaleConverter;
import be.valuya.comptoir.ws.convert.search.FromWsItemSaleSearchConverter;
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
@Path("/itemSale")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ItemSaleResource {

    @EJB
    private SaleService saleService;
    @Inject
    private FromWsItemSaleConverter fromWsItemSaleConverter;
    @Inject
    private FromWsItemSaleSearchConverter fromWsItemSaleSearchConverter;
    @Inject
    private ToWsItemSaleConverter toWsItemSaleConverter;
    @Inject
    private IdChecker idChecker;
    @Inject
    private SaleStateChecker saleStateChecker;
    @Context
    private HttpServletResponse response;

    @POST
    @Valid
    public WsItemSaleRef createItemSale(@NoId @Valid WsItemSale wsItemSale) {
        ItemSale itemSale = fromWsItemSaleConverter.convert(wsItemSale);
        ItemSale savedItemSale = saleService.saveItemSale(itemSale);
        WsItemSaleRef itemSaleRef = toWsItemSaleConverter.reference(savedItemSale);
        WsItemSaleRef savedItemSaleRef = itemSaleRef;
        return savedItemSaleRef;
    }

    @Path("{id}")
    @PUT
    @Valid
    public WsItemSaleRef updateItemSale(@PathParam("id") long id, @Valid WsItemSale wsItemSale) {
        idChecker.checkId(id, wsItemSale);

        ItemSale existingItemSale = saleService.findItemSaleById(id);
        ItemSale updatedItemSale = fromWsItemSaleConverter.patch(existingItemSale, wsItemSale);

        ItemSale savedItemSale = saleService.saveItemSale(updatedItemSale);
        WsItemSaleRef itemSaleRef = toWsItemSaleConverter.reference(savedItemSale);

        return itemSaleRef;
    }

    @Path("{id}")
    @GET
    @Valid
    public WsItemSale getItemSale(@PathParam("id") long id) {
        ItemSale itemSale = saleService.findItemSaleById(id);

        WsItemSale wsItemSale = toWsItemSaleConverter.convert(itemSale);

        return wsItemSale;
    }

    @POST
    @Path("search")
    @Valid
    public List<WsItemSale> findItemSales(@Valid WsItemSaleSearch wsItemSaleSearch) {
        ItemSaleSearch itemSaleSearch = fromWsItemSaleSearchConverter.convert(wsItemSaleSearch);
        List<ItemSale> itemSales = saleService.findItemSales(itemSaleSearch);

        List<WsItemSale> wsItemSales = itemSales.stream()
                .map(toWsItemSaleConverter::convert)
                .collect(Collectors.toList());

        response.setHeader(HeadersConfig.LIST_RESULTS_COUNT_HEADER, "101");
        return wsItemSales;
    }

    @DELETE
    @Path("{id}")
    public void deleteItemSale(@PathParam("id") long id) {
        ItemSale itemSale = saleService.findItemSaleById(id);
        if (itemSale == null) {
            return;
        }
        Sale sale = itemSale.getSale();
        saleStateChecker.checkState(sale, false); // TODO: replace with bean validation

        saleService.removeItemSale(itemSale);
    }

}
