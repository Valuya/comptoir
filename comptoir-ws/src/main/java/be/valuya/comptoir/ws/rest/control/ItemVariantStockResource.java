package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.search.WsItemVariantStockSearch;
import be.valuya.comptoir.api.domain.stock.WsItemStock;
import be.valuya.comptoir.api.domain.stock.WsItemStockRef;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.search.ItemStockSearch;
import be.valuya.comptoir.model.stock.ItemStock;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.stock.StockChangeType;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.util.pagination.ItemVariantStockColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.convert.search.FromWsItemVariantStockSearchConverter;
import be.valuya.comptoir.ws.convert.stock.FromWsItemVariantStockConverter;
import be.valuya.comptoir.ws.convert.stock.ToWsItemVariantStockConverter;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.api.validation.NoId;
import be.valuya.comptoir.ws.rest.validation.StockChangeChecker;
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
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/itemVariantStock")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class ItemVariantStockResource {

    @EJB
    private StockService stockService;
    @Inject
    private FromWsItemVariantStockConverter fromWsItemVariantStockConverter;
    @Inject
    private ToWsItemVariantStockConverter toWsItemVariantStockConverter;
    @Inject
    private FromWsItemVariantStockSearchConverter fromWsItemVariantStockSearchConverter;
    @Inject
    private IdChecker idChecker;
    @Context
    private HttpServletResponse response;
    @Context
    private UriInfo uriInfo;
    @Inject
    private RestPaginationUtil restPaginationUtil;
    @Inject
    private StockChangeChecker stockChangeChecker;
    @Inject
    private EmployeeAccessChecker accessChecker;

    @POST
    @Valid
    public WsItemStockRef createItemStock(@NoId @Valid WsItemStock wsItemStock) {
        ItemStock itemStock = fromWsItemVariantStockConverter.convert(wsItemStock);
        accessChecker.checkOwnCompany(itemStock.getStock());
        // TODO: handle transfers in another resource
        stockChangeChecker.checkStockChangeType(itemStock, StockChangeType.INITIAL, StockChangeType.ADJUSTMENT);

        StockChangeType stockChangeType = itemStock.getStockChangeType();
        Stock stock = itemStock.getStock();
        String comment = itemStock.getComment();
        BigDecimal quantity = itemStock.getQuantity();
        ItemVariant itemVariant = itemStock.getItemVariant();
        ZonedDateTime now = ZonedDateTime.now();

        ItemStock adaptedItemStock = stockService.adaptStock(now, stock, itemVariant, quantity, comment, stockChangeType, null);
        WsItemStockRef wsItemStockRef = toWsItemVariantStockConverter.reference(adaptedItemStock);
        return wsItemStockRef;
    }

    @Path("{id}")
    @GET
    @Valid
    public WsItemStock getItemStock(@PathParam("id") long id) {
        ItemStock itemStock = stockService.findItemStockById(id);
        accessChecker.checkOwnCompany(itemStock.getStock());
        WsItemStock wsItemStock = toWsItemVariantStockConverter.convert(itemStock);
        return wsItemStock;
    }

    @POST
    @Path("search")
    @Valid
    public List<WsItemStock> findItemStocks(@Valid WsItemVariantStockSearch wsItemVariantStockSearch) {
        Pagination<ItemStock, ItemVariantStockColumn> pagination = restPaginationUtil.extractPagination(uriInfo, ItemVariantStockColumn::valueOf);

        ItemStockSearch variantStockSearch = fromWsItemVariantStockSearchConverter.convert(wsItemVariantStockSearch);
        accessChecker.checkOwnCompany(variantStockSearch);
        List<ItemStock> itemStocks = stockService.findItemStocks(variantStockSearch, pagination);

        List<WsItemStock> wsItemStocks = itemStocks.stream()
                .map(toWsItemVariantStockConverter::convert)
                .collect(Collectors.toList());

        restPaginationUtil.addResultCount(response, pagination);
        return wsItemStocks;
    }
}
