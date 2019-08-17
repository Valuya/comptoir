package be.valuya.comptoir.ws.rest.api;

import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingEntry;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingEntryRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSale;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSalePriceDetails;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSalesSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.event.WsComptoirServerEvent;
import be.valuya.comptoir.ws.rest.api.domain.search.WsSaleSearch;
import be.valuya.comptoir.ws.rest.api.util.ApiParameters;
import be.valuya.comptoir.ws.rest.api.util.PaginationParams;
import be.valuya.comptoir.ws.rest.api.validation.NoId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.SseEventSink;
import java.math.BigDecimal;

@Path("/sale")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface SaleResourceApi {

    @POST
    @Operation(operationId = "createSale", description = "Create a sale")
    @Valid WsSaleRef createSale(
            @RequestBody(required = true, description = "sale to create")
            @NoId @Valid @NotNull WsSale wsSale
    );

    @Path("{id}")
    @PUT
    @Operation(operationId = "updateSale", description = "Update a sale")
    @Valid WsSaleRef updateSale(
            @Parameter(description = "The sale id", name = "id", required = true)
            @PathParam("id") long id,
            @RequestBody(required = true, description = "sale to update")
            @Valid WsSale wsSale
    );

    @Path("{id}")
    @GET
    @Operation(operationId = "getSale", description = "Get a sale")
    @Valid WsSale getSale(
            @Parameter(description = "The sale id", name = "id", required = true)
            @PathParam("id") long id);

    @POST
    @Path("search")
    @Operation(operationId = "findSales", description = "Search sales")
    // TODO: remove once BeanParam are handled
    @Parameter(name = ApiParameters.PAGINATION_OFFSET_QUERY_PARAM, description = "The page offset",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_LENGTH_QUERY_PARAM, description = "The page length",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @Parameter(name = ApiParameters.PAGINATION_SORT_QUERY_PARAM, description = "The sort orders, in the format 'column:asc' or 'column:desc'",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.STRING))
    @Valid WsSalesSearchResult findSales(
            @BeanParam PaginationParams paginationParams,
            @RequestBody(required = true, description = "search filter")
            @NotNull @Valid WsSaleSearch wsSaleSearch
    );

    @POST
    @Path("searchTotalPayed")
    @Operation(operationId = "findSalesTotalPayed", description = "get Total payed amont")
    @Valid BigDecimal findSalesTotalPayed(
            @RequestBody(required = true, description = "search filter")
            @Valid WsSaleSearch wsSaleSearch);

    @DELETE
    @Path("{id}")
    @Operation(operationId = "deleteSale", description = "Delete a sale")
    void deleteSale(
            @Parameter(description = "The sale id", name = "id", required = true)
            @PathParam("id") long id);

    @PUT
    @Path("{id}/state/CLOSED")
    @Operation(operationId = "closeSale", description = "Close a sale")
    @Valid WsSaleRef closeSale(
            @Parameter(description = "The sale id", name = "id", required = true)
            @PathParam("id") long id);

    @PUT
    @Path("{id}/state/OPEN")
    @Operation(operationId = "openSale", description = "Open a sale")
    @Valid WsSaleRef openSale(
            @Parameter(description = "The sale id", name = "id", required = true)
            @PathParam("id") long id);

    @GET
    @Path("{id}/payed")
    @Operation(operationId = "getSaleTotalPayed", description = "Get total paid on a sale")
    String getSaleTotalPayed(
            @Parameter(description = "The sale id", name = "id", required = true)
            @PathParam("id") long id);

    @POST
    @Path("{id}/payment")
    @Operation(operationId = "addSalePayment", description = "Add a sale payment entry")
    WsAccountingEntryRef addSalePayment(
            @Parameter(description = "The sale id", name = "id", required = true)
            @PathParam("id")
                    long id,
            @RequestBody(name = "paymentAccountingEntry", description = "The accounting entry", required = true)
                    WsAccountingEntry paymentEntry
    );

    @DELETE
    @Path("{id}/payment/{entryId}")
    @Operation(operationId = "deleteSalePayment", description = "Delete a sale payment entry")
    void deleteSalePayment(
            @Parameter(description = "The sale id", name = "id", required = true)
            @PathParam("id")
                    long id,
            @Parameter(description = "The accounting entry id", name = "entryId", required = true)
            @PathParam("entryId")
                    long entryId
    );


    @GET
    @Path("{id}/events")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @Operation(operationId = "registerToSaleEvents", description = "Register to server-sent sale events")
    @APIResponse(name = "sucess", description = "Registered to events",
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.SERVER_SENT_EVENTS,
                    schema = @Schema(
                            name = "CompoirServerEvents",
                            description = "Comptoir server events",
                            type = SchemaType.OBJECT,
                            // OneOf typescript generation seems broken
                            implementation = WsComptoirServerEvent.class
                    )
            ))
    void registerToSaleEvents(
            @Parameter(description = "The sale id", name = "id", required = true)
            @PathParam("id") long id,
            @Context SseEventSink eventSink
    );


    @GET
    @Path("{id}/price")
    @Operation(summary = "Get a sale price", operationId = "getSalePrice")
    WsSalePriceDetails getSalePrice(
            @Parameter(name = "id", description = "The sale id", required = true)
            @PathParam("id") long id
    );


    @PUT
    @Path("{id}/discountRatio")
    @Operation(summary = "Update a sale discountRatio", operationId = "setSaleDiscountRatio")
    WsSalePriceDetails setSaleDiscountRatio(
            @Parameter(name = "id", description = "The itemVariantSale id", required = true)
            @PathParam("id") long id,
            @RequestBody(required = true, description = "The amount")
            @NotNull BigDecimal discountRatio
    );


    @PUT
        @Path("{id}/discountAmount")
    @Operation(summary = "Update a sale discountAmount", operationId = "setSaleDiscountAmount")
    WsSalePriceDetails setSaleDiscountAmount(
            @Parameter(name = "id", description = "The itemVariantSale id", required = true)
            @PathParam("id") long id,
            @RequestBody(required = true, description = "The amount")
            @NotNull BigDecimal discountAmount
    );


    @PUT
    @Path("{id}/totalVatInclusive")
    @Operation(summary = "Update a sale totalVatInclusive", operationId = "setSaleTotalVatInclusive")
    WsSalePriceDetails setSaleTotalVatInclusive(
            @Parameter(name = "id", description = "The itemVariantSale id", required = true)
            @PathParam("id") long id,
            @RequestBody(required = true, description = "The amount")
            @NotNull BigDecimal totalVatInclusive
    );

}
