package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.company.WsCompany;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.service.CompanyService;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.NoId;
import be.valuya.comptoir.ws.security.Roles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/company")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@RolesAllowed({Roles.EMPLOYEE})
public class CompanyResource {

    @EJB
    private CompanyService companyService;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;
    @Inject
    private IdChecker idChecker;
    @Inject
    private EmployeeAccessChecker accessChecker;

    @POST
    @Valid  //TODO: limit to some role
    public WsCompanyRef createCompany(@NoId @Valid WsCompany wsCompany) {
        Company company = fromWsCompanyConverter.convert(wsCompany);
        Company savedCompany = companyService.saveCompany(company);

        WsCompanyRef companyRef = toWsCompanyConverter.reference(savedCompany);

        return companyRef;
    }

    @Path("{id}")
    @PUT
    @Valid
    public WsCompanyRef saveCompany(@PathParam("id") long id, @Valid WsCompany wsCompany) {
        idChecker.checkId(id, wsCompany);
        accessChecker.checkOwnCompany(id);
        Company existingCompany = companyService.findCompanyById(id);
        Company company = fromWsCompanyConverter.patch(existingCompany, wsCompany);
        Company savedCompany = companyService.saveCompany(company);
        WsCompanyRef wsCompanyRef = toWsCompanyConverter.reference(savedCompany);

        return wsCompanyRef;
    }

    @Path("{id}")
    @Valid
    @GET
    public WsCompany getCompany(@PathParam("id") Long id) {
        accessChecker.checkOwnCompany(id);
        WsCompany wsCompany = Optional.ofNullable(companyService.findCompanyById(id))
                .map(toWsCompanyConverter::convert)
                .orElseThrow(NotFoundException::new);

        return wsCompany;
    }

    @POST
    @Path("{id}/import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public WsCompanyRef importItems(@PathParam("id") Long companyId, byte[] data) {
        accessChecker.checkOwnCompany(companyId);
        return Optional.ofNullable(companyService.findCompanyById(companyId))
                .map(toWsCompanyConverter::reference)
                .orElseThrow(NotFoundException::new);
    }
}
