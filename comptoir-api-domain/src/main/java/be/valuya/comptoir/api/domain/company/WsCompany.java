package be.valuya.comptoir.api.domain.company;

import be.valuya.comptoir.api.domain.lang.WsLocaleText;
import java.util.List;
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
    private List<WsLocaleText> name;
    private List<WsLocaleText> description;
    private WsCountryRef countryRef;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WsCountryRef getCountryRef() {
        return countryRef;
    }

    public void setCountryRef(WsCountryRef countryRef) {
        this.countryRef = countryRef;
    }

    public List<WsLocaleText> getDescription() {
        return description;
    }

    public void setDescription(List<WsLocaleText> description) {
        this.description = description;
    }

    public List<WsLocaleText> getName() {
        return name;
    }

    public void setName(List<WsLocaleText> name) {
        this.name = name;
    }

}
