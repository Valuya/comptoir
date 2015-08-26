package be.valuya.comptoir.api.domain.commercial;

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
@XmlRootElement(name = "AttributeDefinitionRef")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsAttributeDefinitionRef implements WithId {

    @InjectLink(value = "attributeDefinition/${instance.id}")
    @XmlElement
    private URI link;
    @XmlElement
    private Long id;

    public WsAttributeDefinitionRef() {
    }

    public WsAttributeDefinitionRef(Long id) {
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

    public void setId(Long id) {
        this.id = id;
    }

}
