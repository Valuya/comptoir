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
import java.math.BigDecimal;
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
@Path("/sale")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
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
    @Valid
    public WsSaleRef createSale(@NoId @Valid WsSale wsSale) {
        Sale sale = fromWsSaleConverter.convert(wsSale);

        Sale savedSale = saleService.saveSale(sale);

        WsSaleRef saleRef = toWsSaleConverter.reference(savedSale);

        return saleRef;
    }

    @Path("{id}")
    @PUT
    @Valid
    public WsSaleRef updateSale(@PathParam("id") long id, @Valid WsSale wsSale) {
        idChecker.checkId(id, wsSale);
        Sale sale = fromWsSaleConverter.convert(wsSale);
        Sale savedSale = saleService.saveSale(sale);

        WsSaleRef saleRef = toWsSaleConverter.reference(savedSale);

        return saleRef;
    }

    @Path("{id}")
    @GET
    @Valid
    public WsSale getSale(@PathParam("id") long id) {
        Sale sale = saleService.findSaleById(id);
        WsSale wsSale = toWsSaleConverter.convert(sale);

        return wsSale;
    }

    @POST
    @Path("search")
    @Valid
    public List<WsSale> findSales(@Valid WsSaleSearch wsSaleSearch) {
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
    @Path("{id}")
    public void deleteSale(@PathParam("id") long id) {
        Sale sale = saleService.findSaleById(id);
        saleStateChecker.checkState(sale, false); // TODO: replace with bean validation

        saleService.cancelOpenSale(sale);
    }

    @PUT
    @Path("{id}/state/CLOSED")
    @Valid
    public WsSaleRef closeSale(@PathParam("id") long id) {
        Sale sale = saleService.findSaleById(id);
        saleStateChecker.checkState(sale, false); // TODO: replace with bean validation

        sale = saleService.closeSale(sale);

        WsSaleRef saleRef = toWsSaleConverter.reference(sale);

        return saleRef;
    }

    @GET
    @Path("{id}/payed")
    public BigDecimal getSaleTotalPayed(@PathParam("id") long id) {
        Sale sale = saleService.findSaleById(id);
        //saleStateChecker.checkState(sale, false); // TODO: replace with bean validation

        return saleService.getSaleTotalPayed(sale);
    }

}
