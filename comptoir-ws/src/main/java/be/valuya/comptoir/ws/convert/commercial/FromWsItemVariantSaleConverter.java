package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsItemVariantSale;
import be.valuya.comptoir.api.domain.commercial.WsItemVariantSaleRef;
import be.valuya.comptoir.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.ItemVariant;
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
public class FromWsItemVariantSaleConverter {

    @Inject
    private FromWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private FromWsSaleConverter fromWsSaleConverter;
    @Inject
    private FromWsItemVariantConverter fromWsItemConverter;
    @EJB
    private SaleService saleService;

    public ItemVariantSale convert(WsItemVariantSale wsItemSale) {
        if (wsItemSale == null) {
            return null;
        }
        ItemVariantSale itemSale = new ItemVariantSale();

        return patch(itemSale, wsItemSale);
    }

    public ItemVariantSale patch(ItemVariantSale itemSale, WsItemVariantSale wsItemSale) {
        Long id = wsItemSale.getId();

        ZonedDateTime dateTime = wsItemSale.getDateTime();
        BigDecimal quantity = wsItemSale.getQuantity();

        List<WsLocaleText> wsComment = wsItemSale.getComment();
        LocaleText comment = fromWsLocaleTextConverter.convert(wsComment);

        WsItemVariantRef itemVariantRef = wsItemSale.getItemVariantRef();
        ItemVariant itemVariant = fromWsItemConverter.find(itemVariantRef);

        WsSaleRef saleRef = wsItemSale.getSaleRef();
        Sale sale = fromWsSaleConverter.find(saleRef);

        BigDecimal vatExclusive = wsItemSale.getVatExclusive();
        BigDecimal vatRate = wsItemSale.getVatRate();

        BigDecimal total = wsItemSale.getTotal();

        BigDecimal discountRatio = wsItemSale.getDiscountRatio();

        Price price = new Price();
        price.setVatExclusive(vatExclusive);
        price.setVatRate(vatRate);
        price.setDiscountRatio(discountRatio);

        itemSale.setId(id);
        itemSale.setItemVariant(itemVariant);
        itemSale.setQuantity(quantity);
        itemSale.setSale(sale);
        itemSale.setComment(comment);
        itemSale.setDateTime(dateTime);
        itemSale.setPrice(price);
        itemSale.setTotal(total);

        return itemSale;
    }

    public ItemVariantSale find(WsItemVariantSaleRef itemSaleRef) {
        if (itemSaleRef == null) {
            return null;
        }
        Long id = itemSaleRef.getId();
        return saleService.findItemSaleById(id);
    }

}
