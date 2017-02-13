package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.commercial.WsSale;
import be.valuya.comptoir.api.domain.commercial.WsSalePrice;
import be.valuya.comptoir.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.api.domain.search.WsSaleSearch;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.commercial.SalePrice;
import be.valuya.comptoir.model.search.SaleSearch;
import be.valuya.comptoir.service.SaleService;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.SaleColumn;
import be.valuya.comptoir.ws.convert.commercial.FromWsSaleConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsSaleConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsSalePriceConverter;
import be.valuya.comptoir.ws.convert.search.FromWsSaleSearchConverter;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.NoId;
import be.valuya.comptoir.ws.rest.validation.SaleStateChecker;
import be.valuya.comptoir.ws.security.Roles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/sale")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@RolesAllowed({Roles.EMPLOYEE})
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
    private ToWsSalePriceConverter toWsSalePriceConverter;
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
    @Inject
    private EmployeeAccessChecker accessChecker;

    @POST
    @Valid
    public WsSaleRef createSale(@NoId @Valid @NotNull WsSale wsSale) {
        Sale sale = fromWsSaleConverter.convert(wsSale);
        accessChecker.checkOwnCompany(sale);
        Sale savedSale = saleService.saveSale(sale);

        WsSaleRef saleRef = toWsSaleConverter.reference(savedSale);

        return saleRef;
    }

    @Path("{id}")
    @PUT
    @Valid
    public WsSaleRef updateSale(@PathParam("id") long id, @Valid WsSale wsSale) {
        idChecker.checkId(id, wsSale);

        Sale existingSale = saleService.findSaleById(id);
        accessChecker.checkOwnCompany(existingSale);
        Sale updatedSale = fromWsSaleConverter.patch(existingSale, wsSale);

        Sale savedSale = saleService.saveSale(updatedSale);

        WsSaleRef saleRef = toWsSaleConverter.reference(savedSale);

        return saleRef;
    }

    @Path("{id}")
    @GET
    @Valid
    public WsSale getSale(@PathParam("id") long id) {
        Sale sale = saleService.findSaleById(id);
        accessChecker.checkOwnCompany(sale);
        WsSale wsSale = toWsSaleConverter.convert(sale);

        return wsSale;
    }

    @POST
    @Path("search")
    @Valid
    public List<WsSale> findSales(@Valid WsSaleSearch wsSaleSearch) {
        Pagination<Sale, SaleColumn> pagination = restPaginationUtil.extractPagination(uriInfo, SaleColumn::valueOf);
        SaleSearch saleSearch = fromWsSaleSearchConverter.convert(wsSaleSearch);
        accessChecker.checkOwnCompany(saleSearch);

        List<Sale> sales = saleService.findSales(saleSearch, pagination);

        List<WsSale> wsSales = sales.stream()
                .map(toWsSaleConverter::convert)
                .collect(Collectors.toList());

        restPaginationUtil.addResultCount(response, pagination);

        return wsSales;
    }

    @POST
    @Path("searchTotalPayed")
    @Valid
    public WsSalePrice findSalesTotalPayed(@Valid WsSaleSearch wsSaleSearch) {
        SaleSearch saleSearch = fromWsSaleSearchConverter.convert(wsSaleSearch);
        accessChecker.checkOwnCompany(saleSearch);

        SalePrice salesTotalPayed = saleService.getSalesTotalPayed(saleSearch);
        WsSalePrice wsSalePrice = toWsSalePriceConverter.convert(salesTotalPayed);
        return wsSalePrice;
    }

    @DELETE
    @Path("{id}")
    public void deleteSale(@PathParam("id") long id) {
        Sale sale = saleService.findSaleById(id);
        accessChecker.checkOwnCompany(sale);
        saleStateChecker.checkState(sale, false); // TODO: replace with bean validation

        saleService.cancelOpenSale(sale);
    }

    @PUT
    @Path("{id}/state/CLOSED")
    @Valid
    public WsSaleRef closeSale(@PathParam("id") long id) {
        Sale sale = saleService.findSaleById(id);
        accessChecker.checkOwnCompany(sale);
        saleStateChecker.checkState(sale, false); // TODO: replace with bean validation

        sale = saleService.closeSale(sale);

        WsSaleRef saleRef = toWsSaleConverter.reference(sale);

        return saleRef;
    }

    @PUT
    @Path("{id}/state/OPEN")
    @Valid
    public WsSaleRef openSale(@PathParam("id") long id) {
        Sale sale = saleService.findSaleById(id);
        accessChecker.checkOwnCompany(sale);
        saleStateChecker.checkState(sale, true); // TODO: replace with bean validation

        sale = saleService.reopenSale(sale);

        WsSaleRef saleRef = toWsSaleConverter.reference(sale);

        return saleRef;
    }

    @GET
    @Path("{id}/payed")
    public BigDecimal getSaleTotalPayed(@PathParam("id") long id) {
        Sale sale = saleService.findSaleById(id);
        accessChecker.checkOwnCompany(sale);
        return saleService.getSaleTotalPayed(sale);
    }

}
