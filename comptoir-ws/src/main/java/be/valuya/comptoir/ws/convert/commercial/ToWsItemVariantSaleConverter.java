package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.ws.convert.stock.ToWsStockConverter;
import be.valuya.comptoir.ws.convert.text.ToWsLocaleTextConverter;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSale;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSaleRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.ws.rest.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsStockRef;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 *
 */
@ApplicationScoped
public class ToWsItemVariantSaleConverter {

    @Inject
    private ToWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private ToWsSaleConverter toWsSaleConverter;
    @Inject
    private ToWsItemVariantConverter toWsItemConverter;
    @Inject
    private ToWsStockConverter toWsStockConverter;

    public WsItemVariantSale convert(ItemVariantSale itemSale) {
        if (itemSale == null) {
            return null;
        }
        Long id = itemSale.getId();
        ZonedDateTime dateTime = itemSale.getDateTime();

        Sale sale = itemSale.getSale();
        WsSaleRef saleRef = toWsSaleConverter.reference(sale);

        Stock stock = itemSale.getStock();
        WsStockRef stockRef = toWsStockConverter.reference(stock);

        ItemVariant itemVariant = itemSale.getItemVariant();
        WsItemVariantRef itemVariantRef = toWsItemConverter.reference(itemVariant);

        LocaleText comment = itemSale.getComment();
        List<WsLocaleText> wsComment = fromWsLocaleTextConverter.convert(comment);

        Boolean includeCustomerLyalty = itemSale.getIncludeCustomerLoyalty();
        Boolean includeCustomerDiscount = itemSale.getIncludeCustomerDiscount();

        WsItemVariantSale wsItemSale = new WsItemVariantSale();
        wsItemSale.setId(id);
        wsItemSale.setComment(wsComment);
        wsItemSale.setDateTime(dateTime);
        wsItemSale.setItemVariantRef(itemVariantRef);
        wsItemSale.setSaleRef(saleRef);
        wsItemSale.setStockRef(stockRef);
        wsItemSale.setIncludeCustomerLoyalty(includeCustomerLyalty);
        wsItemSale.setIncludeCustomerDiscount(includeCustomerDiscount);
        return wsItemSale;
    }

    public WsItemVariantSaleRef reference(ItemVariantSale itemSale) {
        Long id = itemSale.getId();
        WsItemVariantSaleRef wsItemSaleRef = new WsItemVariantSaleRef(id);
        return wsItemSaleRef;
    }

}
