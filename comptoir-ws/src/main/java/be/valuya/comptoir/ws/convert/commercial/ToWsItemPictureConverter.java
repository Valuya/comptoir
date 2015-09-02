package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsItemPicture;
import be.valuya.comptoir.api.domain.commercial.WsItemPictureRef;
import be.valuya.comptoir.model.commercial.ItemPicture;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsItemPictureConverter {

    public WsItemPicture convert(ItemPicture itemPicture) {
        Long id = itemPicture.getId();
        String contentType = itemPicture.getContentType();
        byte[] data = itemPicture.getData();

        WsItemPicture wsItemPicture = new WsItemPicture();
        wsItemPicture.setId(id);
        wsItemPicture.setContentType(contentType);
        wsItemPicture.setData(data);

        return wsItemPicture;
    }

    public WsItemPictureRef reference(ItemPicture itemPicture) {
        if (itemPicture == null) {
            return null;
        }
        Long id = itemPicture.getId();
        WsItemPictureRef wsItemPictureRef = new WsItemPictureRef(id);
        return wsItemPictureRef;
    }

}
