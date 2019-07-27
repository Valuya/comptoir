package be.valuya.comptoir.ws.rest.api.domain.commercial;

import be.valuya.comptoir.ws.rest.api.util.WithId;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.Objects;
//import org.glassfish.jersey.linking.InjectLink;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "SaleRef")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsSaleRef implements WithId {

    //@InjectLink(value = "sale/${instance.id}")
    @XmlElement
    private URI link;
    @XmlElement
    private Long id;

    public WsSaleRef() {
    }

    public WsSaleRef(Long id) {
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

    @Override
    public String toString() {
        return "WsSaleRef{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WsSaleRef wsSaleRef = (WsSaleRef) o;
        return Objects.equals(id, wsSaleRef.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
