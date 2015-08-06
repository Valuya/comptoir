package be.valuya.comptoir.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsItemPicture;
import be.valuya.comptoir.api.domain.commercial.WsItemPictureRef;
import be.valuya.comptoir.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemPicture;
import be.valuya.comptoir.service.StockService;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsItemPictureConverter {

    @EJB
    private StockService stockService;
    @Inject
    private FromWsItemConverter fromWsItemConverter;

    public ItemPicture convert(WsItemPicture wsItemPicture) {
        Long id = wsItemPicture.getId();
        String contentType = wsItemPicture.getContentType();
        byte[] data = wsItemPicture.getData();

        WsItemRef itemRef = wsItemPicture.getItemRef();
        Item item = fromWsItemConverter.find(itemRef);

        ItemPicture itemPicture = new ItemPicture();
        itemPicture.setId(id);
        itemPicture.setContentType(contentType);
        itemPicture.setData(data);
        itemPicture.setItem(item);

        return itemPicture;
    }

    public ItemPicture find(WsItemPictureRef itemPictureRef) {
        Long id = itemPictureRef.getId();
        return stockService.findItemPictureById(id);
    }

}
