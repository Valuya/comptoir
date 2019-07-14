package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.service.ImportService;
import be.valuya.comptoir.service.ImportSummary;
import be.valuya.comptoir.service.PrestashopImportParams;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.security.ComptoirRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/import")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class ImportResource {

    @EJB
    private ImportService importService;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private EmployeeAccessChecker accessChecker;

    @POST
    @Path("{companyId}/{backendName}")
    public ImportSummary importItems(@PathParam("companyId") Long companyId, @PathParam("backendName") String backendName, PrestashopImportParams prestashopImportParams) {
        WsCompanyRef wsCompanyRef = new WsCompanyRef(companyId);
        Company company = fromWsCompanyConverter.find(wsCompanyRef);
        accessChecker.checkOwnCompany(company);

        ImportSummary importSummary = importService.doImport(company, backendName, prestashopImportParams);
        return importSummary;
    }
}
