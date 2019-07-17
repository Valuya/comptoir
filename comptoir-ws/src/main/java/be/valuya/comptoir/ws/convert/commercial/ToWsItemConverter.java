package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItem;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPictureRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.Picture;
import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
import be.valuya.comptoir.ws.convert.text.ToWsLocaleTextConverter;
import java.math.BigDecimal;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsItemConverter {

    @Inject
    private ToWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;
    @Inject
    private ToWsPictureConverter toWsPictureConverter;

    public WsItem convert(Item item) {
        if (item == null) {
            return null;
        }
        Long id = item.getId();
        Company company = item.getCompany();
        LocaleText description = item.getDescription();
        Price currentPrice = item.getCurrentPrice();
        LocaleText name = item.getName();
        String reference = item.getReference();
        boolean multipleSale = item.isMultipleSale();

        WsCompanyRef companyRef = toWsCompanyConverter.reference(company);
        List<WsLocaleText> wsDescription = fromWsLocaleTextConverter.convert(description);
        List<WsLocaleText> wsName = fromWsLocaleTextConverter.convert(name);

        Picture mainPicture = item.getMainPicture();
        WsPictureRef mainPictureRef = toWsPictureConverter.reference(mainPicture);

        BigDecimal vatExclusive = currentPrice.getVatExclusive();
        BigDecimal vatRate = currentPrice.getVatRate();

        WsItem wsItem = new WsItem();
        wsItem.setId(id);
        wsItem.setCompanyRef(companyRef);
        wsItem.setDescription(wsDescription);
        wsItem.setMainPictureRef(mainPictureRef);
        wsItem.setName(wsName);
        wsItem.setReference(reference);
        wsItem.setVatExclusive(vatExclusive);
        wsItem.setVatRate(vatRate);
        wsItem.setMultipleSale(multipleSale);

        return wsItem;
    }

    public WsItemRef reference(Item item) {
        if (item == null) {
            return null;
        }
        Long id = item.getId();
        WsItemRef itemRef = new WsItemRef(id);
        return itemRef;
    }

}
