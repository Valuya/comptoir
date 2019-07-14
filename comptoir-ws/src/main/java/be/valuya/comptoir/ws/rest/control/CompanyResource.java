package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.company.WsCompany;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.service.CompanyService;
import be.valuya.comptoir.ws.api.CompanyResourceApi;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.api.validation.NoId;
import be.valuya.comptoir.security.ComptoirRoles;

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
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class CompanyResource implements CompanyResourceApi {

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

    public WsCompanyRef createCompany(WsCompany wsCompany) {
        Company company = fromWsCompanyConverter.convert(wsCompany);
        Company savedCompany = companyService.saveCompany(company);

        WsCompanyRef companyRef = toWsCompanyConverter.reference(savedCompany);

        return companyRef;
    }

    public WsCompanyRef saveCompany(long id, WsCompany wsCompany) {
        idChecker.checkId(id, wsCompany);
        accessChecker.checkOwnCompany(id);
        Company existingCompany = companyService.findCompanyById(id);
        Company company = fromWsCompanyConverter.patch(existingCompany, wsCompany);
        Company savedCompany = companyService.saveCompany(company);
        WsCompanyRef wsCompanyRef = toWsCompanyConverter.reference(savedCompany);

        return wsCompanyRef;
    }

    public WsCompany getCompany(Long id) {
        accessChecker.checkOwnCompany(id);
        WsCompany wsCompany = Optional.ofNullable(companyService.findCompanyById(id))
                .map(toWsCompanyConverter::convert)
                .orElseThrow(NotFoundException::new);

        return wsCompany;
    }

//    public WsCompanyRef importItems(Long companyId, byte[] data) {
//        accessChecker.checkOwnCompany(companyId);
//        return Optional.ofNullable(companyService.findCompanyById(companyId))
//                .map(toWsCompanyConverter::reference)
//                .orElseThrow(NotFoundException::new);
//    }
}
