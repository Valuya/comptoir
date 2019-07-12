package be.valuya.comptoir.api.domain.stock;

import be.valuya.comptoir.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.api.utils.ZonedDateTimeXmlAdapter;
import be.valuya.comptoir.model.common.WithId;
//import org.glassfish.jersey.linking.InjectLink;

import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "ItemStockRef")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsItemStockRef implements Serializable, WithId {

    @XmlElement
    private Long id;
    //@InjectLink(value = "itemStock/${instance.id}")
    @XmlElement
    private URI link;

    public WsItemStockRef() {
    }

    public WsItemStockRef(Long id) {
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
        final WsItemStockRef other = (WsItemStockRef) obj;
        return Objects.equals(this.id, other.id);
    }

}
