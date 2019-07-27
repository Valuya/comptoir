package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.notification.WebNotificationSubscriptionRequest;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/web-notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface WebNotificationApi {


    @POST
    @Operation(summary = "Subscribe to web-notifications", operationId = "subscriptionWebNotifications")
    void subscriptionWebNotifications(
            @RequestBody(description = "The subscription", required = true)
                    WebNotificationSubscriptionRequest subscription);


}
