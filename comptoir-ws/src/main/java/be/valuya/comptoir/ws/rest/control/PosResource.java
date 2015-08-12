package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.commercial.WsPos;
import be.valuya.comptoir.api.domain.commercial.WsPosRef;
import be.valuya.comptoir.api.domain.search.WsPosSearch;
import be.valuya.comptoir.model.commercial.Pos;
import be.valuya.comptoir.model.search.PosSearch;
import be.valuya.comptoir.service.PosService;
import be.valuya.comptoir.ws.convert.commercial.FromWsPosConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsPosConverter;
import be.valuya.comptoir.ws.convert.search.FromWsPosSearchConverter;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.NoId;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/pos")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
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

    @POST
    public WsPosRef createPos(@NoId WsPos wsPos) {
        Pos pos = fromWsPosConverter.convert(wsPos);
        Pos savedPos = posService.savePos(pos);

        WsPosRef posRef = toWsPosConverter.reference(savedPos);

        return posRef;
    }

    @Path("{id}")
    @PUT
    public WsPosRef savePos(@PathParam("id") long id, WsPos wsPos) {
        idChecker.checkId(id, wsPos);
        Pos pos = fromWsPosConverter.convert(wsPos);
        Pos savedPos = posService.savePos(pos);

        WsPosRef posRef = toWsPosConverter.reference(savedPos);

        return posRef;
    }

    @Path("{id}")
    @GET
    public WsPos getPos(@PathParam("id") long id) {
        Pos pos = posService.findPosById(id);

        WsPos wsPos = toWsPosConverter.convert(pos);

        return wsPos;
    }

    @POST
    @Path("search")
    public List<WsPos> findPosList(WsPosSearch wsPosSearch) {
        PosSearch posSearch = fromWsPosSearchConverter.convert(wsPosSearch);
        List<Pos> poss = posService.findPosList(posSearch);

        List<WsPos> wsPoss = poss.stream()
                .map(toWsPosConverter::convert)
                .collect(Collectors.toList());

        return wsPoss;
    }

}
