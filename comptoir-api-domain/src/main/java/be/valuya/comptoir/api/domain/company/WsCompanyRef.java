package be.valuya.comptoir.api.domain.company;

import be.valuya.comptoir.model.common.WithId;
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
@XmlRootElement(name = "companyRef")
@XmlAccessorType(XmlAccessType.FIELD)
@Schema(description = "Company reference")
public class WsCompanyRef implements WithId {

    //@InjectLink(value = "company/${instance.id}")
    @XmlElement
    private URI link;
    @XmlElement
    @Schema(required = true)
    private Long id;

    public WsCompanyRef() {
    }

    public WsCompanyRef(Long id) {
        this.id = id;
    }

    public URI getLink() {
        return link;
    }

    public void setLink(URI link) {
        this.link = link;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

}
