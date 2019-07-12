package be.valuya.comptoir.api.domain.commercial;

import be.valuya.comptoir.model.common.WithId;
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
@XmlRootElement(name = "ItemVariantRef")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsItemVariantRef implements WithId {

    //@InjectLink(value = "itemVariant/${instance.id}")
    @XmlElement
    private URI link;
    @XmlElement
    private Long id;

    public WsItemVariantRef() {
    }

    public WsItemVariantRef(Long id) {
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
