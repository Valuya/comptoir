package be.valuya.comptoir.ws.rest.api.domain.accounting;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement
@Schema(name = "WsAccountType", type = SchemaType.STRING)
public enum WsAccountType {

    PAYMENT,
    VAT,
    OTHER;

}
