/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.ws.convert.stock;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.api.domain.stock.WsStock;
import be.valuya.comptoir.api.domain.stock.WsStockRef;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
import be.valuya.comptoir.ws.convert.text.ToWsLocaleTextConverter;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author cghislai
 */
@ApplicationScoped
public class ToWsStockConverter {

    @Inject
    private ToWsLocaleTextConverter toWsLocaleTextConverter;
    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;

    public WsStock convert(Stock stock) {
        Company company = stock.getCompany();
        LocaleText description = stock.getDescription();
        Long id = stock.getId();

        WsCompanyRef companyRef = toWsCompanyConverter.reference(company);

        List<WsLocaleText> wsDescription = toWsLocaleTextConverter.convert(description);

        WsStock wsStock = new WsStock();
        wsStock.setId(id);
        wsStock.setCompanyRef(companyRef);
        wsStock.setDescription(wsDescription);
        return wsStock;
    }

    public WsStockRef reference(Stock stock) {
        Long id = stock.getId();
        WsStockRef wsStocKRef = new WsStockRef(id);
        return wsStocKRef;
    }
}
