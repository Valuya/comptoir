package be.valuya.comptoir.api.domain.commercial;

import be.valuya.comptoir.api.domain.company.WithId;
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
@XmlRootElement(name = "ItemPictureRef")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsItemPictureRef implements WithId {

    @InjectLink(value = "itemPicture/${instance.id}")
    @XmlElement
    private URI link;
    @XmlElement
    private Long id;

    public WsItemPictureRef() {
    }

    public WsItemPictureRef(Long id) {
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
