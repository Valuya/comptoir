package be.valuya.comptoir.api.domain.company;

import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "company")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsCompany implements WithId {

    private Long id;
    private WsLocaleText name;
    private WsLocaleText description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WsLocaleText getDescription() {
        return description;
    }

    public void setDescription(WsLocaleText description) {
        this.description = description;
    }

    public WsLocaleText getName() {
        return name;
    }

    public void setName(WsLocaleText name) {
        this.name = name;
    }

}
