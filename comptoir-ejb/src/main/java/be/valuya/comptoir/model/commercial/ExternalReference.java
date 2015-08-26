package be.valuya.comptoir.model.commercial;

import be.valuya.comptoir.model.company.Company;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "external_reference")
public class ExternalReference implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @ManyToOne(optional = false)
    private Company company;
    @Column(name = "backend_id")
    @NotNull
    private String backendName; // name of the backend ("prestashop", ...)
    @NotNull
    @Column(length = 128, name = "external_id")
    @Size(max = 128)
    private String externalId; // id in backend
    @NotNull
    @Column(length = 128, name = "local_id")
    private Long localId;
    private ExternalReferenceType type;

    public ExternalReference() {
    }

    public ExternalReference(Company company, String backendId, ExternalReferenceType type, String externalId, Long localId) {
        this.company = company;
        this.backendName = backendId;
        this.externalId = externalId;
        this.localId = localId;
        this.type = type;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getBackendName() {
        return backendName;
    }

    public void setBackendName(String backendName) {
        this.backendName = backendName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public ExternalReferenceType getType() {
        return type;
    }

    public void setType(ExternalReferenceType type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final ExternalReference other = (ExternalReference) obj;
        return Objects.equals(this.id, other.id);
    }

}
