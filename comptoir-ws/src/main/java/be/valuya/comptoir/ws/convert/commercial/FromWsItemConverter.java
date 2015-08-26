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
    private FromWsItemPictureConverter fromWsItemPictureConverter;

    public ItemVariant convert(WsItemVariant wsItem) {
        if (wsItem == null) {
            return null;
        }
        Long id = wsItem.getId();

        String model = wsItem.getModel();
        String reference = wsItem.getReference();
        BigDecimal vatExclusive = wsItem.getVatExclusive();
        BigDecimal vatRate = wsItem.getVatRate();

        List<WsLocaleText> description = wsItem.getDescription();
        LocaleText wsDescription = fromWsLocaleTextConverter.convert(description);

        List<WsLocaleText> name = wsItem.getName();
        LocaleText wsName = fromWsLocaleTextConverter.convert(name);

        WsCompanyRef companyRef = wsItem.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);

        WsItemPictureRef mainPictureRef = wsItem.getMainPictureRef();

        ItemPicture mainPicture = fromWsItemPictureConverter.find(mainPictureRef);

        Price price = new Price();
        price.setVatExclusive(vatExclusive);
        price.setVatRate(vatRate);

        ItemVariant item = new ItemVariant();
        item.setId(id);
        item.setCompany(company);
        item.setDescription(wsDescription);
        item.setMainPicture(mainPicture);
        item.setName(wsName);
        item.setModel(model);
        item.setReference(reference);
        item.setCurrentPrice(price);

        return item;
    }

    public ItemVariant find(WsItemVariantRef itemVariantRef) {
        if (itemVariantRef == null) {
            return null;
        }
        Long itemId = itemVariantRef.getId();
        ItemVariant item = stockService.findItemById(itemId);
        return item;
    }

}
