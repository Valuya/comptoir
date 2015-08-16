package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.commercial.WsSale;
import be.valuya.comptoir.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.api.domain.search.WsSaleSearch;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.search.SaleSearch;
import be.valuya.comptoir.service.SaleService;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.SaleColumn;
import be.valuya.comptoir.ws.config.HeadersConfig;
import be.valuya.comptoir.ws.convert.commercial.FromWsSaleConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsSaleConverter;
import be.valuya.comptoir.ws.convert.search.FromWsSaleSearchConverter;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.NoId;
import be.valuya.comptoir.ws.rest.validation.SaleStateChecker;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
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
@Path("/sale")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class SaleResource {
    
    @EJB
    private SaleService saleService;
    @Inject
    private FromWsSaleConverter fromWsSaleConverter;
    @Inject
    private FromWsSaleSearchConverter fromWsSaleSearchConverter;
    @Inject
    private ToWsSaleConverter toWsSaleConverter;
    @Inject
    private IdChecker idChecker;
    @Inject
    private SaleStateChecker saleStateChecker;
    @Inject
    private RestPaginationUtil restPaginationUtil;
    @Context
    private HttpServletResponse response;
    @Context
    private UriInfo uriInfo;
    
    
    @POST
    public WsSaleRef createSale(@NoId WsSale wsSale) {
        Sale sale = fromWsSaleConverter.convert(wsSale);
        
        Sale savedSale = saleService.saveSale(sale);
        
        WsSaleRef saleRef = toWsSaleConverter.reference(savedSale);
        
        return saleRef;
    }
    
    @Path("{id}")
    @PUT
    public WsSaleRef updateSale(@PathParam("id") long id, WsSale wsSale) {
        idChecker.checkId(id, wsSale);
        Sale sale = fromWsSaleConverter.convert(wsSale);
        Sale savedSale = saleService.saveSale(sale);
        
        WsSaleRef saleRef = toWsSaleConverter.reference(savedSale);
        
        return saleRef;
    }
    
    @Path("{id}")
    @GET
    public WsSale getSale(@PathParam("id") long id) {
        Sale sale = saleService.findSaleById(id);
        sale = saleService.calcSale(sale);
        WsSale wsSale = toWsSaleConverter.convert(sale);
        
        return wsSale;
    }
    
    @POST
    @Path("search")
    public List<WsSale> findSales(WsSaleSearch wsSaleSearch) {
        Pagination<Sale, SaleColumn> pagination = restPaginationUtil.extractPagination(uriInfo, SaleColumn::valueOf);
        SaleSearch saleSearch = fromWsSaleSearchConverter.convert(wsSaleSearch);

        List<Sale> sales = saleService.findSales(saleSearch, pagination);
        Long count = saleService.countSales(saleSearch);
        
        List<WsSale> wsSales = sales.stream()
                .map(toWsSaleConverter::convert)
                .collect(Collectors.toList());
        response.setHeader(HeadersConfig.LIST_RESULTS_COUNT_HEADER, count.toString());
        return wsSales;
    }
    
    @DELETE
    @Path("{id}/state/OPEN")
    public void deleteSale(@PathParam("id") long id) {
        Sale sale = saleService.findSaleById(id);
        saleStateChecker.checkState(SaleStateChecker.SaleState.OPEN, sale);
        saleService.cancelOpenSale(sale);
    }
            
}
