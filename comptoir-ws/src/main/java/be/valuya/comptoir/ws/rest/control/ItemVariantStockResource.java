package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.ws.rest.api.domain.search.WsItemVariantStockSearch;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsItemVariantStock;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsItemVariantStockRef;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.search.ItemStockSearch;
import be.valuya.comptoir.model.stock.ItemStock;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.stock.StockChangeType;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsItemVariantStockSearchResult;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.util.pagination.ItemVariantStockColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.convert.search.FromWsItemVariantStockSearchConverter;
import be.valuya.comptoir.ws.convert.stock.FromWsItemVariantStockConverter;
import be.valuya.comptoir.ws.convert.stock.ToWsItemVariantStockConverter;
import be.valuya.comptoir.ws.rest.api.ItemVariantStockResourceApi;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.StockChangeChecker;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class ItemVariantStockResource implements ItemVariantStockResourceApi {

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

    public WsItemVariantStockRef createItemVariantStock(WsItemVariantStock wsItemVariantStock) {
        ItemStock itemStock = fromWsItemVariantStockConverter.convert(wsItemVariantStock);
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
        WsItemVariantStockRef wsItemVariantStockRef = toWsItemVariantStockConverter.reference(adaptedItemStock);
        return wsItemVariantStockRef;
    }

    public WsItemVariantStock getItemVariantStock(long id) {
        ItemStock itemStock = stockService.findItemStockById(id);
        accessChecker.checkOwnCompany(itemStock.getStock());
        WsItemVariantStock wsItemVariantStock = toWsItemVariantStockConverter.convert(itemStock);
        return wsItemVariantStock;
    }

    public WsItemVariantStockSearchResult findItemVariantStocks(WsItemVariantStockSearch wsItemVariantStockSearch) {
        Pagination<ItemStock, ItemVariantStockColumn> pagination = restPaginationUtil.extractPagination(uriInfo, ItemVariantStockColumn::valueOf);

        ItemStockSearch variantStockSearch = fromWsItemVariantStockSearchConverter.convert(wsItemVariantStockSearch);
        accessChecker.checkOwnCompany(variantStockSearch);
        List<ItemStock> itemStocks = stockService.findItemStocks(variantStockSearch, pagination);

        List<WsItemVariantStockRef> wsItemVariantStocks = itemStocks.stream()
                .map(toWsItemVariantStockConverter::reference)
                .collect(Collectors.toList());

        restPaginationUtil.addResultCount(response, pagination);
        return restPaginationUtil.setResults(new WsItemVariantStockSearchResult(), wsItemVariantStocks, pagination);
    }
}
