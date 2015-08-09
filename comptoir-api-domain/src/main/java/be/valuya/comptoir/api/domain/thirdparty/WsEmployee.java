package be.valuya.comptoir.api.domain.thirdparty;

import be.valuya.comptoir.api.domain.company.WithId;
import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "Employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsEmployee implements Serializable, WithId {

    private Long id;
    private boolean active;
    @NotNull
    @Nonnull
    private WsCompanyRef companyRef;
    @NotNull
    @Nonnull
    @Size(max = 200)
    private String login;
    @Size(max = 200)
    private String firstName;
    @Size(max = 200)
    private String lastName;
    private Locale locale;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public WsCompanyRef getCompanyRef() {
        return companyRef;
    }

    public void setCompanyRef(WsCompanyRef companyRef) {
        this.companyRef = companyRef;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final WsEmployee other = (WsEmployee) obj;
        return Objects.equals(this.id, other.id);
    }

}
