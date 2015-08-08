package be.valuya.comptoir.ws.convert.company;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import javax.ws.rs.ext.ParamConverter;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class WsCompanyParameterProvider implements ParamConverter<WsCompanyRef> {

    @Override
    public WsCompanyRef fromString(String idStr) {
        long companyId = Long.parseLong(idStr);
        return new WsCompanyRef(companyId);
    }

    @Override
    public String toString(WsCompanyRef wsCompanyRef) {
        long id = wsCompanyRef.getId();
        return Long.toString(id);
    }

}
