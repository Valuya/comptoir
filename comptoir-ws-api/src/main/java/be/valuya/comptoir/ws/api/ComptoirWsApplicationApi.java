package be.valuya.comptoir.ws.api;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.servers.ServerVariable;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@ApplicationPath("/")
@OpenAPIDefinition(
        servers = {@Server(
                description = "Comptoir backend ws",
                url = "https://{url}/comptoir-ws",
                variables = {
                        @ServerVariable(name = "url",
                                description = "Backend url",
                                defaultValue = "comptoir.local:8443",
                                enumeration = {"comptoir.local:8443", "comptoir.valuya.be"})
                }
        )},
        info = @Info(
                title = "Comptoir backend ws",
                version = "1.0-SNAPSHOT"
        )
)
public abstract class ComptoirWsApplicationApi extends Application {
}
