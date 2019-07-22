package be.valuya.comptoir.ws.convert.company;

import be.valuya.comptoir.service.PrestashopImportParams;
import be.valuya.comptoir.ws.rest.api.domain.company.WsPrestashopImportParams;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FromWsPrestashopImportParamsConverter {

    public PrestashopImportParams fromWsPrestashopImportParams(WsPrestashopImportParams params) {
        String driverClassName = params.getDriverClassName();
        String dbUrl = params.getDbUrl();
        String dbUsername = params.getDbUsername();
        String dbPassword = params.getDbPassword();

        PrestashopImportParams prestashopImportParams = new PrestashopImportParams();
        prestashopImportParams.setDriverClassName(driverClassName);
        prestashopImportParams.setDbUrl(dbUrl);
        prestashopImportParams.setDbUsername(dbUsername);
        prestashopImportParams.setDbPassword(dbPassword);
        return prestashopImportParams;
    }
}
