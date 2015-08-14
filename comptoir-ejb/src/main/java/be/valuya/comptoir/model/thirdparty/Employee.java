package be.valuya.comptoir.model.thirdparty;

import be.valuya.comptoir.model.company.Company;
import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
public class Employee implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private boolean active;
    @NotNull
    @Nonnull
    @ManyToOne(optional = false)
    private Company company;
    @NotNull
    @Nonnull
    @Column(length = 200)
    @Size(max = 200)
    
    private String login;
    @Column(name = "password_hash", length = 32)
    @Size(max = 32)
    private String passwordHash;
    @Column(name = "first_name", length = 200)
    @Size(max = 200)
    private String firstName;
    @Column(name = "last_name", length = 200)
    @Size(max = 200)
    private String lastName;
    @Column(columnDefinition = "VARCHAR(16)")
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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
        final Employee other = (Employee) obj;
        return Objects.equals(this.id, other.id);
    }

}
