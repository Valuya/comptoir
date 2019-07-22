package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.ws.rest.api.domain.search.WsStockSearch;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsStock;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsStockRef;
import be.valuya.comptoir.model.search.StockSearch;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsStockSearchResult;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.StockColumn;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.convert.search.FromWsStockSearchConverter;
import be.valuya.comptoir.ws.convert.stock.FromWsStockConverter;
import be.valuya.comptoir.ws.convert.stock.ToWsStockConverter;
import be.valuya.comptoir.ws.rest.api.StockResourceApi;
import be.valuya.comptoir.ws.rest.validation.ActiveStateChecker;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class StockResource implements StockResourceApi {

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
    @Inject
    private EmployeeAccessChecker accessChecker;

    public WsStockRef createStock(WsStock wsStock) {
        Stock stock = fromWsStockConverter.convert(wsStock);
        accessChecker.checkOwnCompany(stock);
        Stock savedStock = stockService.saveStock(stock);

        WsStockRef stockRef = toWsStockConverter.reference(savedStock);
        return stockRef;
    }

    public WsStockRef updateStock(long id, WsStock wsStock) {
        idChecker.checkId(id, wsStock);
        Stock stock = fromWsStockConverter.convert(wsStock);
        accessChecker.checkOwnCompany(stock);
        Stock savedStock = stockService.saveStock(stock);

        WsStockRef stockRef = toWsStockConverter.reference(savedStock);
        return stockRef;
    }

    public WsStock getStock(long id) {
        Stock stock = stockService.findStockById(id);
        accessChecker.checkOwnCompany(stock);
        WsStock wsStock = toWsStockConverter.convert(stock);
        return wsStock;
    }

    public void deleteStock(long id) {
        Stock stock = stockService.findStockById(id);
        accessChecker.checkOwnCompany(stock);

        activeStateChecker.checkState(stock, true);
        stock.setActive(false);
        stockService.saveStock(stock);
    }

    public WsStockSearchResult findStocks(WsStockSearch wsStockSearch) {
        Pagination<Stock, StockColumn> pagination = restPaginationUtil.extractPagination(uriInfo, StockColumn::valueOf);

        StockSearch stockSearch = fromWsStockSearchConverter.convert(wsStockSearch);
        accessChecker.checkOwnCompany(stockSearch);

        List<Stock> stocks = stockService.findStocks(stockSearch, pagination);

        List<WsStockRef> wsStocks = stocks.stream()
                .map(toWsStockConverter::reference)
                .collect(Collectors.toList());
        restPaginationUtil.addResultCount(response, pagination);
        return restPaginationUtil.setResults(new WsStockSearchResult(), wsStocks, pagination);
    }

}
