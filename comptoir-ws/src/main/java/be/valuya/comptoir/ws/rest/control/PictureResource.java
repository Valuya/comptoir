package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.commercial.WsPicture;
import be.valuya.comptoir.api.domain.commercial.WsPictureRef;
import be.valuya.comptoir.api.domain.search.WsPictureSearch;
import be.valuya.comptoir.model.commercial.Picture;
import be.valuya.comptoir.model.search.PictureSearch;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.ws.convert.commercial.FromWsPictureConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsPictureConverter;
import be.valuya.comptoir.ws.convert.search.FromWsPictureSearchConverter;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.api.validation.NoId;
import be.valuya.comptoir.security.ComptoirRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/picture")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class PictureResource {

    @EJB
    private StockService stockService;
    @Inject
    private FromWsPictureConverter fromWsPictureConverter;
    @Inject
    private ToWsPictureConverter toWsPictureConverter;
    @Inject
    private FromWsPictureSearchConverter fromWsPictureSearchConverter;
    @Inject
    private IdChecker idChecker;
    @Inject
    private EmployeeAccessChecker accessChecker;

    @POST
    @Valid
    public WsPictureRef createPicture(@NoId @Valid WsPicture wsPicture) {
        Picture picture = fromWsPictureConverter.convert(wsPicture);
        accessChecker.checkOwnCompany(picture);
        Picture savedPicture = stockService.savePicture(picture);
        WsPictureRef pictureRef = toWsPictureConverter.reference(savedPicture);
        return pictureRef;
    }

    @Path("{id}")
    @PUT
    @Valid
    public WsPictureRef updatePicture(@PathParam("id") long id, WsPicture wsPicture) {
        idChecker.checkId(id, wsPicture);
        Picture picture = fromWsPictureConverter.convert(wsPicture);
        accessChecker.checkOwnCompany(picture);
        Picture savedPicture = stockService.savePicture(picture);
        WsPictureRef pictureRef = toWsPictureConverter.reference(savedPicture);
        return pictureRef;
    }

    @POST
    @Path("search")
    @Valid
    public List<WsPicture> findPictures(WsPictureSearch wsPictureSearch) {
        PictureSearch pictureSearch = fromWsPictureSearchConverter.convert(wsPictureSearch);
        accessChecker.checkOwnCompany(pictureSearch);
        List<Picture> pictures = stockService.findPictures(pictureSearch);

        List<WsPicture> wsPictures = pictures.stream()
                .map(toWsPictureConverter::convert)
                .collect(Collectors.toList());

        return wsPictures;
    }

    @Path("{id}")
    @GET
    @Valid
    public WsPicture getPicture(@PathParam("id") long id) {
        Picture picture = stockService.findPictureById(id);
        accessChecker.checkOwnCompany(picture);

        WsPicture wsPicture = toWsPictureConverter.convert(picture);

        return wsPicture;
    }

}
