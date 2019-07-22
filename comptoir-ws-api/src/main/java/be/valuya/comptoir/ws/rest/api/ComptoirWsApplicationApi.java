package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.accounting.WsBalance;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsBalanceRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPos;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsPosRef;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompany;
import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsCustomer;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsCustomerRef;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsEmployee;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsEmployeeRef;
import org.eclipse.microprofile.openapi.annotations.Components;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.servers.ServerVariable;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


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
        ),
        components = @Components(
                schemas = {
                        @Schema(implementation = WsLocaleText.class),
                        @Schema(implementation = WsCompany.class),
                        @Schema(implementation = WsCompanyRef.class),
                        @Schema(implementation = WsEmployee.class),
                        @Schema(implementation = WsEmployeeRef.class),
                        @Schema(implementation = WsCustomer.class),
                        @Schema(implementation = WsCustomerRef.class),
                        @Schema(implementation = WsBalance.class),
                        @Schema(implementation = WsBalanceRef.class),
                        @Schema(implementation = WsPos.class),
                        @Schema(implementation = WsPosRef.class),

                }
        )
)
public abstract class ComptoirWsApplicationApi extends Application {
}
