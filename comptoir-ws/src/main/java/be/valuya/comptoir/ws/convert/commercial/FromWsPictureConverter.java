package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPictureRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPicture;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.model.commercial.Picture;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsPictureConverter {

    @EJB
    private StockService stockService;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;

    public Picture convert(WsPicture wsPicture) {
        if (wsPicture == null) {
            return null;
        }
        Long id = wsPicture.getId();
        String contentType = wsPicture.getContentType();
        byte[] data = wsPicture.getData();

        WsCompanyRef companyRef = wsPicture.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);

        Picture picture = new Picture();
        picture.setId(id);
        picture.setContentType(contentType);
        picture.setData(data);
        picture.setCompany(company);

        return picture;
    }

    public Picture find(WsPictureRef pictureRef) {
        if (pictureRef == null) {
            return null;
        }
        Long id = pictureRef.getId();
        return stockService.findPictureById(id);
    }

}
