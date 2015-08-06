package be.valuya.comptoir.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsItemPicture;
import be.valuya.comptoir.api.domain.commercial.WsItemPictureRef;
import be.valuya.comptoir.api.domain.commercial.WsItemRef;
import be.valuya.comptoir.comptoir.ws.convert.company.ToWsCompanyConverter;
import be.valuya.comptoir.comptoir.ws.convert.text.ToWsLocaleTextConverter;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemPicture;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsItemPictureConverter {

    @Inject
    private ToWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;
    @Inject
    private ToWsItemConverter toWsItemConverter;

    public WsItemPicture convert(ItemPicture itemPicture) {
        Long id = itemPicture.getId();
        String contentType = itemPicture.getContentType();
        byte[] data = itemPicture.getData();

        Item item = itemPicture.getItem();
        WsItemRef itemRef = toWsItemConverter.reference(item);

        WsItemPicture wsItemPicture = new WsItemPicture();
        wsItemPicture.setId(id);
        wsItemPicture.setContentType(contentType);
        wsItemPicture.setData(data);
        wsItemPicture.setItemRef(itemRef);

        return wsItemPicture;
    }

    public WsItemPictureRef reference(ItemPicture itemPicture) {
        Long id = itemPicture.getId();
        WsItemPictureRef wsItemPictureRef = new WsItemPictureRef(id);
        return wsItemPictureRef;
    }

}
