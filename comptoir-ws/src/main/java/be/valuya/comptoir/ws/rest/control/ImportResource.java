package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.service.ImportService;
import be.valuya.comptoir.service.ImportSummary;
import be.valuya.comptoir.service.PrestashopImportParams;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/import")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ImportResource {

    @EJB
    private ImportService importService;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;

    @POST
    @Path("{companyId}/{backendName}")
    public ImportSummary importItems(@PathParam("companyId") Long companyId, @PathParam("backendName") String backendName, PrestashopImportParams prestashopImportParams) {
        WsCompanyRef wsCompanyRef = new WsCompanyRef(companyId);
        Company company = fromWsCompanyConverter.find(wsCompanyRef);

        ImportSummary importSummary = importService.doImport(company, backendName, prestashopImportParams);
        return importSummary;
    }
}
