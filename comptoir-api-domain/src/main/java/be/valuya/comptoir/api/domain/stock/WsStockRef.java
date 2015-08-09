package be.valuya.comptoir.api.domain.stock;

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
@XmlRootElement(name = "StockRef")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsStockRef implements WithId {

    @InjectLink(value = "stock/${instance.id}")
    @XmlElement
    private URI link;
    @XmlElement
    private Long id;

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
