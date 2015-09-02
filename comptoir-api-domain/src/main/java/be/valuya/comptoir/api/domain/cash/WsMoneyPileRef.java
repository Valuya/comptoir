package be.valuya.comptoir.api.domain.cash;

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
@XmlRootElement(name = "MoneyPileRef")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsMoneyPileRef implements WithId {

    @InjectLink(value = "moneyPile/${instance.id}")
    @XmlElement
    private URI link;
    @XmlElement
    private Long id;

    public WsMoneyPileRef() {
    }

    public WsMoneyPileRef(Long id) {
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
