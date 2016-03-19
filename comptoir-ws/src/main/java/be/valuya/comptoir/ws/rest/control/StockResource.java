package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.search.WsStockSearch;
import be.valuya.comptoir.api.domain.stock.WsStock;
import be.valuya.comptoir.api.domain.stock.WsStockRef;
import be.valuya.comptoir.model.search.StockSearch;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.StockColumn;
import be.valuya.comptoir.ws.convert.search.FromWsStockSearchConverter;
import be.valuya.comptoir.ws.convert.stock.FromWsStockConverter;
import be.valuya.comptoir.ws.convert.stock.ToWsStockConverter;
import be.valuya.comptoir.ws.rest.validation.ActiveStateChecker;
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
@Path("/stock")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class StockResource {

    @EJB
    private StockService stockService;
    @Inject
    private FromWsStockConverter fromWsStockConverter;
    @Inject
    private ToWsStockConverter toWsStockConverter;
    @Inject
    private FromWsStockSearchConverter fromWsStockSearchConverter;
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

    @POST
    @Valid
    public WsStockRef createStock(@NoId @Valid WsStock wsStock) {
        Stock stock = fromWsStockConverter.convert(wsStock);
        Stock savedStock = stockService.saveStock(stock);

        WsStockRef stockRef = toWsStockConverter.reference(savedStock);
        return stockRef;
    }

    @Path("{id}")
    @PUT
    @Valid
    public WsStockRef updateStock(@PathParam("id") long id, @Valid WsStock wsStock) {
        idChecker.checkId(id, wsStock);
        Stock stock = fromWsStockConverter.convert(wsStock);
        Stock savedStock = stockService.saveStock(stock);

        WsStockRef stockRef = toWsStockConverter.reference(stock);
        return stockRef;
    }

    @Path("{id}")
    @GET
    @Valid
    public WsStock getStock(@PathParam("id") long id) {
        Stock stock = stockService.findStockById(id);
        WsStock wsStock = toWsStockConverter.convert(stock);
        return wsStock;
    }

    @Path("{id}")
    @DELETE
    public void deleteStock(@PathParam("id") long id) {
        Stock stock = stockService.findStockById(id);

        activeStateChecker.checkState(stock, true);
        stock.setActive(false);
        stockService.saveStock(stock);
    }

    @POST
    @Path("search")
    @Valid
    public List<WsStock> findStocks(@Valid WsStockSearch wsStockSearch) {
        Pagination<Stock, StockColumn> pagination = restPaginationUtil.extractPagination(uriInfo, StockColumn::valueOf);

        StockSearch stockSearch = fromWsStockSearchConverter.convert(wsStockSearch);

        List<Stock> stocks = stockService.findStocks(stockSearch, pagination);

        List<WsStock> wsStocks = stocks.stream()
                .map(toWsStockConverter::convert)
                .collect(Collectors.toList());
        restPaginationUtil.addResultCount(response, pagination);
        return wsStocks;
    }

}
