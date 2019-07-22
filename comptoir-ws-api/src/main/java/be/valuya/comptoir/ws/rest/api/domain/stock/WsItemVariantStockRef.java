package be.valuya.comptoir.ws.rest.api.domain.stock;

import be.valuya.comptoir.ws.rest.api.util.WithId;
//import org.glassfish.jersey.linking.InjectLink;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.net.URI;
import java.util.Objects;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "WsItemVariantStockRef")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsItemVariantStockRef implements Serializable, WithId {

    @XmlElement
    private Long id;
    //@InjectLink(value = "itemStock/${instance.id}")
    @XmlElement
    private URI link;

    public WsItemVariantStockRef() {
    }

    public WsItemVariantStockRef(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public URI getLink() {
        return link;
    }

    public void setLink(URI link) {
        this.link = link;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WsItemVariantStockRef other = (WsItemVariantStockRef) obj;
        return Objects.equals(this.id, other.id);
    }

}
