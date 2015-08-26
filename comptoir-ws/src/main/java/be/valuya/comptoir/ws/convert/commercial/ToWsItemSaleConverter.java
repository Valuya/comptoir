package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsItemSale;
import be.valuya.comptoir.api.domain.commercial.WsItemSaleRef;
import be.valuya.comptoir.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.commercial.ItemSale;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
import be.valuya.comptoir.ws.convert.text.ToWsLocaleTextConverter;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 */
@ApplicationScoped
public class ToWsItemSaleConverter {

    @Inject
    private ToWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private ToWsSaleConverter toWsSaleConverter;
    @Inject
    private ToWsItemConverter toWsItemConverter;
    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;

    public WsItemSale convert(ItemSale itemSale) {
        if (itemSale == null) {
            return null;
        }
        Long id = itemSale.getId();
        ZonedDateTime dateTime = itemSale.getDateTime();
        BigDecimal quantity = itemSale.getQuantity();
        BigDecimal total = itemSale.getTotal();

        Price price = itemSale.getPrice();
        BigDecimal vatExclusive = price.getVatExclusive();
        BigDecimal vatRate = price.getVatRate();
        BigDecimal discountRatio = price.getDiscountRatio();

        AccountingEntry accountingEntry = itemSale.getAccountingEntry();

        Sale sale = itemSale.getSale();
        WsSaleRef saleRef = toWsSaleConverter.reference(sale);

        ItemVariant item = itemSale.getItemVariant();
        WsItemVariantRef itemVariantRef = toWsItemConverter.reference(item);

        LocaleText comment = itemSale.getComment();
        List<WsLocaleText> wsComment = fromWsLocaleTextConverter.convert(comment);

        WsItemSale wsItemSale = new WsItemSale();
        wsItemSale.setId(id);
        wsItemSale.setComment(wsComment);
        wsItemSale.setDateTime(dateTime);
        wsItemSale.setItemVariantRef(itemVariantRef);
        wsItemSale.setQuantity(quantity);
        wsItemSale.setVatExclusive(vatExclusive);
        wsItemSale.setVatRate(vatRate);
        wsItemSale.setSaleRef(saleRef);
        wsItemSale.setDiscountRatio(discountRatio);
        wsItemSale.setTotal(total);

        return wsItemSale;
    }

    public WsItemSaleRef reference(ItemSale itemSale) {
        Long id = itemSale.getId();
        WsItemSaleRef wsItemSaleRef = new WsItemSaleRef(id);
        return wsItemSaleRef;
    }

}
