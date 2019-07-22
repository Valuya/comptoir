package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.ws.convert.company.FromWsPrestashopImportParamsConverter;
import be.valuya.comptoir.ws.convert.company.ToWsImportSummaryConverter;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.service.ImportService;
import be.valuya.comptoir.service.ImportSummary;
import be.valuya.comptoir.service.PrestashopImportParams;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.ws.rest.api.ImportResourceApi;
import be.valuya.comptoir.ws.rest.api.domain.company.WsImportSummary;
import be.valuya.comptoir.ws.rest.api.domain.company.WsPrestashopImportParams;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class ImportResource implements ImportResourceApi {

    @EJB
    private ImportService importService;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private EmployeeAccessChecker accessChecker;
    @Inject
    private FromWsPrestashopImportParamsConverter fromWsPrestashopImportParamsConverter;
    @Inject
    private ToWsImportSummaryConverter toWsImportSummaryConverter;

    public WsImportSummary importItems(Long companyId, String backendName, WsPrestashopImportParams wsPrestashopImportParams) {
        WsCompanyRef wsCompanyRef = new WsCompanyRef(companyId);
        Company company = fromWsCompanyConverter.find(wsCompanyRef);
        accessChecker.checkOwnCompany(company);

        PrestashopImportParams prestashopImportParams = fromWsPrestashopImportParamsConverter.fromWsPrestashopImportParams(wsPrestashopImportParams);
        ImportSummary importSummary = importService.doImport(company, backendName, prestashopImportParams);
        WsImportSummary wsImportSummary = toWsImportSummaryConverter.toWsImportSummary(importSummary);
        return wsImportSummary;
    }
}
