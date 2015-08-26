package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsItemPictureRef;
import be.valuya.comptoir.api.domain.commercial.WsItemVariant;
import be.valuya.comptoir.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.commercial.ItemPicture;
import be.valuya.comptoir.model.commercial.ItemVariant;
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
    private ToWsItemPictureConverter toWsItemPictureConverter;

    public WsItemVariant convert(ItemVariant item) {
        if (item == null) {
            return null;
        }
        Long id = item.getId();
        Company company = item.getCompany();
        LocaleText description = item.getDescription();
        Price currentPrice = item.getCurrentPrice();
        String model = item.getModel();
        LocaleText name = item.getName();
        String reference = item.getReference();

        WsCompanyRef companyRef = toWsCompanyConverter.reference(company);
        List<WsLocaleText> wsDescription = fromWsLocaleTextConverter.convert(description);
        List<WsLocaleText> wsName = fromWsLocaleTextConverter.convert(name);

        ItemPicture mainPicture = item.getMainPicture();
        WsItemPictureRef mainPictureRef = toWsItemPictureConverter.reference(mainPicture);

        BigDecimal vatExclusive = currentPrice.getVatExclusive();
        BigDecimal vatRate = currentPrice.getVatRate();

        WsItemVariant wsItem = new WsItemVariant();
        wsItem.setId(id);
        wsItem.setCompanyRef(companyRef);
        wsItem.setDescription(wsDescription);
        wsItem.setMainPictureRef(mainPictureRef);
        wsItem.setModel(model);
        wsItem.setName(wsName);
        wsItem.setReference(reference);
        wsItem.setVatExclusive(vatExclusive);
        wsItem.setVatRate(vatRate);

        return wsItem;
    }

    public WsItemVariantRef reference(ItemVariant item) {
        if (item == null) {
            return null;
        }
        Long id = item.getId();
        WsItemVariantRef itemVariantRef = new WsItemVariantRef(id);
        return itemVariantRef;
    }

}
