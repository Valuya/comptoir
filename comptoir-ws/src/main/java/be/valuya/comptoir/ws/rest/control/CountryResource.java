package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.company.WsCountry;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/country")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class CountryResource {

    @Path("{code}")
    @GET
    public WsCountry getCountry(@NotNull @PathParam("code") String code) {
        if (!code.equals("be")) {
            throw new BadRequestException("Only supported country is BELGIUM(be) for now! Vive le Grand Jojo !");
        }

        WsCountry belgiumWsCountry = new WsCountry();
        belgiumWsCountry.setCode("be");
        belgiumWsCountry.setDefaultVatRate(BigDecimal.valueOf(2100, 2));
        return belgiumWsCountry;
    }
}
