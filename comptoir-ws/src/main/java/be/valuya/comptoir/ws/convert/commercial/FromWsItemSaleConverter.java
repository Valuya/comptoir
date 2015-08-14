package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.api.domain.commercial.WsItemSale;
import be.valuya.comptoir.api.domain.commercial.WsItemSaleRef;
import be.valuya.comptoir.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemSale;
import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.service.SaleService;
import be.valuya.comptoir.ws.convert.text.FromWsLocaleTextConverter;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsItemSaleConverter {

    @Inject
    private FromWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private FromWsSaleConverter fromWsSaleConverter;
    @Inject
    private FromWsItemConverter fromWsItemConverter;
    @EJB
    private SaleService saleService;

    public ItemSale convert(WsItemSale wsItemSale) {
        if (wsItemSale == null) {
            return null;
        }
        Long id = wsItemSale.getId();

        ZonedDateTime dateTime = wsItemSale.getDateTime();
        BigDecimal quantity = wsItemSale.getQuantity();

        List<WsLocaleText> wsComment = wsItemSale.getComment();
        LocaleText comment = fromWsLocaleTextConverter.convert(wsComment);

        WsItemRef itemRef = wsItemSale.getItemRef();
        Item item = fromWsItemConverter.find(itemRef);

        WsSaleRef saleRef = wsItemSale.getSaleRef();
        Sale sale = fromWsSaleConverter.find(saleRef);

        BigDecimal vatExclusive = wsItemSale.getVatExclusive();
        BigDecimal vatRate = wsItemSale.getVatRate();
        Price price = new Price();
        price.setVatExclusive(vatExclusive);
        price.setVatExclusive(vatRate);

        ItemSale itemSale = new ItemSale();
        itemSale.setId(id);
        itemSale.setItem(item);
        itemSale.setQuantity(quantity);
        itemSale.setSale(sale);
        itemSale.setComment(comment);
        itemSale.setDateTime(dateTime);
        itemSale.setPrice(price);

        return itemSale;
    }

    public ItemSale find(WsItemSaleRef itemSaleRef) {
        if (itemSaleRef == null) {
            return null;
        }
        Long id = itemSaleRef.getId();
        return saleService.findItemSaleById(id);
    }

}
