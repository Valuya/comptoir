package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPicture;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPictureRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPictureSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsPictureSearch;
import be.valuya.comptoir.model.commercial.Picture;
import be.valuya.comptoir.model.search.PictureSearch;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.ws.convert.commercial.FromWsPictureConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsPictureConverter;
import be.valuya.comptoir.ws.convert.search.FromWsPictureSearchConverter;
import be.valuya.comptoir.ws.rest.api.PictureResourceApi;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class PictureResource implements PictureResourceApi {

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
    @Context
    private UriInfo uriInfo;
    @Inject
    private RestPaginationUtil restPaginationUtil;


    public WsPictureRef createPicture(WsPicture wsPicture) {
        Picture picture = fromWsPictureConverter.convert(wsPicture);
        accessChecker.checkOwnCompany(picture);
        Picture savedPicture = stockService.savePicture(picture);
        WsPictureRef pictureRef = toWsPictureConverter.reference(savedPicture);
        return pictureRef;
    }

    public WsPictureRef updatePicture(long id, WsPicture wsPicture) {
        idChecker.checkId(id, wsPicture);
        Picture picture = fromWsPictureConverter.convert(wsPicture);
        accessChecker.checkOwnCompany(picture);
        Picture savedPicture = stockService.savePicture(picture);
        WsPictureRef pictureRef = toWsPictureConverter.reference(savedPicture);
        return pictureRef;
    }

    public WsPictureSearchResult findPictures(WsPictureSearch wsPictureSearch) {
        Pagination<Picture, ?> pagination = restPaginationUtil.extractPagination(uriInfo);
        PictureSearch pictureSearch = fromWsPictureSearchConverter.convert(wsPictureSearch);
        accessChecker.checkOwnCompany(pictureSearch);
        List<Picture> pictures = stockService.findPictures(pictureSearch);

        List<WsPictureRef> wsPictures = pictures.stream()
                .map(toWsPictureConverter::reference)
                .collect(Collectors.toList());

        return restPaginationUtil.setResults(new WsPictureSearchResult(), wsPictures, pagination);
    }

    public WsPicture getPicture(long id) {
        Picture picture = stockService.findPictureById(id);
        accessChecker.checkOwnCompany(picture);

        WsPicture wsPicture = toWsPictureConverter.convert(picture);

        return wsPicture;
    }

}
