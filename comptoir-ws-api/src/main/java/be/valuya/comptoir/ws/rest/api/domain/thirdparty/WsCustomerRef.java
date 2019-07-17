package be.valuya.comptoir.ws.rest.api.domain.thirdparty;

import be.valuya.comptoir.ws.rest.api.util.WithId;
import java.net.URI;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
//import org.glassfish.jersey.linking.InjectLink;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "CustomerRef")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsCustomerRef implements WithId {

    //@InjectLink(value = "customer/${instance.id}")
    @XmlElement
    private URI link;
    @XmlElement
    private Long id;

    public WsCustomerRef() {
    }

    public WsCustomerRef(Long id) {
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
