package be.valuya.comptoir.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsItem;
import be.valuya.comptoir.api.domain.commercial.WsItemPictureRef;
import be.valuya.comptoir.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.comptoir.ws.convert.company.FromWsCompanyConverter;
import be.valuya.comptoir.comptoir.ws.convert.text.FromWsLocaleTextConverter;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemPicture;
import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.service.StockService;
import java.math.BigDecimal;
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

    public Item convert(WsItem wsItem) {
        Long id = wsItem.getId();

        String model = wsItem.getModel();
        String reference = wsItem.getReference();
        BigDecimal vatExclusive = wsItem.getVatExclusive();
        BigDecimal vatRate = wsItem.getVatRate();

        WsLocaleText description = wsItem.getDescription();
        LocaleText wsDescription = fromWsLocaleTextConverter.convert(description);

        WsLocaleText name = wsItem.getName();
        LocaleText wsName = fromWsLocaleTextConverter.convert(name);

        WsCompanyRef companyRef = wsItem.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);

        WsItemPictureRef mainPictureRef = wsItem.getMainPictureRef();

        ItemPicture mainPicture = fromWsItemPictureConverter.find(mainPictureRef);

        Price price = new Price();
        price.setVatExclusive(vatExclusive);
        price.setVatRate(vatRate);

        Item item = new Item();
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

    public Item find(WsItemRef itemRef) {
        Long itemId = itemRef.getId();
        Item item = stockService.findItemById(itemId);
        return item;
    }

}
