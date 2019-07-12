package be.valuya.comptoir.api.domain.accounting;

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
@XmlRootElement(name = "BalanceRef")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsBalanceRef implements WithId {

    //@InjectLink(value = "balance/${instance.id}", style = InjectLink.Style.RELATIVE_PATH)
    @XmlElement
    private URI link;
    @XmlElement
    private Long id;

    public WsBalanceRef() {
    }

    public WsBalanceRef(Long id) {
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
