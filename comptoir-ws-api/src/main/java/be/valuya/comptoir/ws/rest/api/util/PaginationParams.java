package be.valuya.comptoir.ws.rest.api.util;

import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

import javax.annotation.Nullable;
import javax.ws.rs.QueryParam;
import java.util.Optional;

public class PaginationParams {

    @Nullable
    @Parameter(name = ApiParameters.PAGINATION_OFFSET_QUERY_PARAM, description = "The page offset",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @QueryParam(ApiParameters.PAGINATION_OFFSET_QUERY_PARAM)
    private Integer offset;
    @Nullable
    @Parameter(name = ApiParameters.PAGINATION_LENGTH_QUERY_PARAM, description = "The page length",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.INTEGER))
    @QueryParam(ApiParameters.PAGINATION_LENGTH_QUERY_PARAM)
    private Integer length;
    @Nullable
    @Parameter(name = ApiParameters.PAGINATION_SORT_QUERY_PARAM, description = "The sort orders, in the format 'column:asc' or 'column:desc'",
            in = ParameterIn.QUERY, schema = @Schema(type = SchemaType.STRING))
    @QueryParam(ApiParameters.PAGINATION_SORT_QUERY_PARAM)
    private String sorts;


    public Optional<Integer> getOffsetOptional() {
        return Optional.ofNullable(offset);
    }

    public void setOffset(@Nullable Integer offset) {
        this.offset = offset;
    }

    public Optional<Integer> getLengthOptional() {
        return Optional.ofNullable(length);
    }

    public void setLength(@Nullable Integer length) {
        this.length = length;
    }

    public Optional<String> getSortsOptional() {
        return Optional.ofNullable(sorts);
    }

    public void setSorts(@Nullable String sorts) {
        this.sorts = sorts;
    }
}
