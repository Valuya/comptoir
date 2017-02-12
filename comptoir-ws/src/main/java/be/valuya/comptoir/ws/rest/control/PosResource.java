package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.commercial.WsPos;
import be.valuya.comptoir.api.domain.commercial.WsPosRef;
import be.valuya.comptoir.api.domain.search.WsPosSearch;
import be.valuya.comptoir.model.commercial.Pos;
import be.valuya.comptoir.model.search.PosSearch;
import be.valuya.comptoir.service.PosService;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.PosColumn;
import be.valuya.comptoir.ws.convert.commercial.FromWsPosConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsPosConverter;
import be.valuya.comptoir.ws.convert.search.FromWsPosSearchConverter;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.NoId;
import be.valuya.comptoir.ws.security.Roles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/pos")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@RolesAllowed({Roles.EMPLOYEE})
public class PosResource {

    @EJB
    private PosService posService;
    @Inject
    private FromWsPosConverter fromWsPosConverter;
    @Inject
    private FromWsPosSearchConverter fromWsPosSearchConverter;
    @Inject
    private ToWsPosConverter toWsPosConverter;
    @Inject
    private IdChecker idChecker;
    @Context
    private UriInfo uriInfo;
    @Inject
    private RestPaginationUtil restPaginationUtil;
    @Context
    private HttpServletResponse response;

    @POST
    @Valid
    public WsPosRef createPos(@NoId @Valid WsPos wsPos) {
        Pos pos = fromWsPosConverter.convert(wsPos);
        Pos savedPos = posService.savePos(pos);

        WsPosRef posRef = toWsPosConverter.reference(savedPos);

        return posRef;
    }

    @Path("{id}")
    @PUT
    @Valid
    public WsPosRef savePos(@PathParam("id") long id, @Valid WsPos wsPos) {
        idChecker.checkId(id, wsPos);
        Pos pos = fromWsPosConverter.convert(wsPos);
        Pos savedPos = posService.savePos(pos);

        WsPosRef posRef = toWsPosConverter.reference(savedPos);

        return posRef;
    }

    @Path("{id}")
    @GET
    @Valid
    public WsPos getPos(@PathParam("id") long id) {
        Pos pos = posService.findPosById(id);

        WsPos wsPos = toWsPosConverter.convert(pos);

        return wsPos;
    }

    @Path("search")
    @POST
    @Valid
    public List<WsPos> findPosList(@Valid WsPosSearch wsPosSearch) {
        Pagination<Pos, PosColumn> pagination = restPaginationUtil.extractPagination(uriInfo, PosColumn::valueOf);
        PosSearch posSearch = fromWsPosSearchConverter.convert(wsPosSearch);
        List<Pos> posList = posService.findPosList(posSearch, pagination);

        List<WsPos> wsPosList = posList.stream()
                .map(toWsPosConverter::convert)
                .collect(Collectors.toList());

        restPaginationUtil.addResultCount(response, pagination);
        return wsPosList;
    }

}
