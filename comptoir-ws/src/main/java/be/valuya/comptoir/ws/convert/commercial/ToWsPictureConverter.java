package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPictureRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPicture;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.model.commercial.Picture;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.ws.convert.company.ToWsCompanyConverter;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsPictureConverter {

    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;

    public WsPicture convert(Picture picture) {
        Long id = picture.getId();
        String contentType = picture.getContentType();
        byte[] data = picture.getData();

        Company company = picture.getCompany();
        WsCompanyRef wsCompanyRef = toWsCompanyConverter.reference(company);

        WsPicture wsPicture = new WsPicture();
        wsPicture.setId(id);
        wsPicture.setContentType(contentType);
        wsPicture.setData(data);
        wsPicture.setCompanyRef(wsCompanyRef);

        return wsPicture;
    }

    public WsPictureRef reference(Picture picture) {
        if (picture == null) {
            return null;
        }
        Long id = picture.getId();
        WsPictureRef wsPictureRef = new WsPictureRef(id);
        return wsPictureRef;
    }

}
