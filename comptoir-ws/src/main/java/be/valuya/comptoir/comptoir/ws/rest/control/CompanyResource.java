package be.valuya.comptoir.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.company.WsCompany;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.comptoir.ws.convert.company.ToWsCompanyConverter;
import be.valuya.comptoir.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.comptoir.ws.rest.validation.NoId;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.service.CompanyService;
import java.util.Optional;
import javax.ejb.EJB;
import javax.inject.Inject;
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
    public WsCompanyRef saveCompany(@PathParam("id") WsCompanyRef companyRef, WsCompany wsCompany) {
        idChecker.checkId(companyRef, wsCompany);

        Long id = companyRef.getId();
        Company existingCompany = companyService.findCompanyById(id);

        Company company = fromWsCompanyConverter.updateCompany(wsCompany, existingCompany);
        company = companyService.saveCompany(company);

        return companyRef;
    }

    @Path("{id}")
    @GET
    public WsCompany getCompany(@PathParam("id") long id) {
        Company company = Optional.ofNullable(companyService.findCompanyById(id))
                .orElseThrow(NotFoundException::new);

        WsCompany wsCompany = toWsCompanyConverter.convert(company);

        return wsCompany;
    }

}
