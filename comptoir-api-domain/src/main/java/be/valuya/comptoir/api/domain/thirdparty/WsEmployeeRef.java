package be.valuya.comptoir.api.domain.thirdparty;

import be.valuya.comptoir.model.common.WithId;
import java.net.URI;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.glassfish.jersey.linking.InjectLink;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "EmployeeRef")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsEmployeeRef implements WithId {

    @InjectLink(value = "employee/${instance.id}")
    @XmlElement
    private URI link;
    @XmlElement
    private Long id;

    public WsEmployeeRef() {
    }

    public WsEmployeeRef(Long id) {
        this.id = id;
    }

    public URI getLink() {
        return link;
    }

    public void setLink(URI link) {
        this.link = link;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
