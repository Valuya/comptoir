package be.valuya.comptoir.model.factory;

import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import javax.ejb.Singleton;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Singleton
public class CompanyFactory {

    @Inject
    private LocaleTextFactory localeTextFactory;

    public Company createCompany() {
        LocaleText description = localeTextFactory.createLocaleText();
        LocaleText companyName = localeTextFactory.createLocaleText();

        Company company = new Company();
        company.setDescription(description);
        company.setName(companyName);

        return company;
    }

}
