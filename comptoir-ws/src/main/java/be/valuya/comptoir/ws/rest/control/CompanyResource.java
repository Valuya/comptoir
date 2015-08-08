package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.company.WsCompany;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.service.CompanyService;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.NoId;
import java.util.Optional;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/company")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class CompanyResource {

    @EJB
    private CompanyService companyService;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;
    @Inject
    private IdChecker idChecker;

    @POST
    public WsCompanyRef createCompany(@NoId WsCompany wsCompany) {
        Company company = fromWsCompanyConverter.convert(wsCompany);
        Company savedCompany = companyService.saveCompany(company);

        WsCompanyRef companyRef = toWsCompanyConverter.reference(savedCompany);

        return companyRef;
    }

    @Path("{id}")
    @PUT
    public WsCompanyRef saveCompany(@PathParam("id") long id, WsCompany wsCompany) {
        idChecker.checkId(id, wsCompany);

        Company existingCompany = companyService.findCompanyById(id);

        Company company = fromWsCompanyConverter.updateCompany(wsCompany, existingCompany);
        company = companyService.saveCompany(company);

        WsCompanyRef wsCompanyRef = new WsCompanyRef(id);

        return wsCompanyRef;
    }

    @Path("{id}")
    @GET
    public WsCompany getCompany(@PathParam("id") Long id) {
        Company company = Optional.ofNullable(companyService.findCompanyById(id))
                .orElseThrow(NotFoundException::new);

        WsCompany wsCompany = toWsCompanyConverter.convert(company);

        return wsCompany;
    }

}
