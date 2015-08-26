package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.service.ImportService;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/import")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ImportResource {

    @EJB
    private ImportService importService;
    @Inject
    private IdChecker idChecker;

    @POST
    @Path("{id}/import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void importItems(@PathParam("id") Long companyId, byte[] data) {
    }
}
