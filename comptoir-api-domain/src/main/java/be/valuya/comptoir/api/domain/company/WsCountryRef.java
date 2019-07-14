package be.valuya.comptoir.api.domain.company;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
//import org.glassfish.jersey.linking.InjectLink;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "countryRef")
@XmlAccessorType(XmlAccessType.FIELD)
@Schema(description = "A country reference")
public class WsCountryRef {

    //@InjectLink(value = "country/${instance.code}")
    @XmlElement
    private URI link;
    @XmlElement
    private String code;

    public WsCountryRef() {
    }

    public WsCountryRef(String code) {
        this.code = code;
    }

    public URI getLink() {
        return link;
    }

    public void setLink(URI link) {
        this.link = link;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
