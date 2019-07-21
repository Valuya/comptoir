package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItem;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPictureRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.Picture;import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.ws.convert.text.FromWsLocaleTextConverter;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsItemConverter {

    @EJB
    private StockService stockService;
    @Inject
    private FromWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;
    @Inject
    private FromWsPictureConverter fromWsPictureConverter;

    public Item convert(WsItem wsItem) {
        if (wsItem == null) {
            return null;
        }
        Long id = wsItem.getId();

        String reference = wsItem.getReference();
        BigDecimal vatExclusive = wsItem.getVatExclusive();
        BigDecimal vatRate = wsItem.getVatRate();
        boolean multipleSale = wsItem.isMultipleSale();

        List<WsLocaleText> description = wsItem.getDescription();
        LocaleText wsDescription = fromWsLocaleTextConverter.convert(description);

        List<WsLocaleText> name = wsItem.getName();
        LocaleText wsName = fromWsLocaleTextConverter.convert(name);

        WsCompanyRef companyRef = wsItem.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);

        WsPictureRef mainPictureRef = wsItem.getMainPictureRef();

        Picture mainPicture = fromWsPictureConverter.find(mainPictureRef);
        
        Price price = new Price();
        price.setVatExclusive(vatExclusive);
        price.setVatRate(vatRate);

        Item item = new Item();
        item.setId(id);
        item.setCompany(company);
        item.setDescription(wsDescription);
        item.setMainPicture(mainPicture);
        item.setName(wsName);
        item.setReference(reference);
        item.setCurrentPrice(price);
        item.setActive(true);
        item.setMultipleSale(multipleSale);

        return item;
    }

    public Item find(WsItemRef itemRef) {
        if (itemRef == null) {
            return null;
        }
        Long itemId = itemRef.getId();
        Item item = stockService.findItemById(itemId);
        return item;
    }

}
