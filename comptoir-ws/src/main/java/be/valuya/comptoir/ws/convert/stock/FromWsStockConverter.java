/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.ws.convert.stock;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.api.domain.stock.WsStock;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.ws.convert.text.FromWsLocaleTextConverter;
import java.util.List;
import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author cghislai
 */
@ApplicationScoped
public class FromWsStockConverter {

    @Inject
    private FromWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;

    public Stock convert(WsStock wsStock) {
        if (wsStock == null) {
            return null;
        }
        Stock stock = new Stock();
        return patch(stock, wsStock);
    }

    public Stock patch(@Nonnull Stock stock, @Nonnull WsStock wsStock) {
        Long id = wsStock.getId();
        WsCompanyRef companyRef = wsStock.getCompanyRef();
        List<WsLocaleText> description = wsStock.getDescription();

        Company company = fromWsCompanyConverter.find(companyRef);

        LocaleText existingDescription = stock.getDescription();
        LocaleText updatedDescription = fromWsLocaleTextConverter.update(description, existingDescription);

        stock.setId(id);
        stock.setCompany(company);
        stock.setDescription(updatedDescription);
        return stock;
    }
}
