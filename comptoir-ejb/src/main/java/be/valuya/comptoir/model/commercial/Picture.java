package be.valuya.comptoir.model.commercial;

import be.valuya.comptoir.model.common.WithCompany;
import be.valuya.comptoir.model.company.Company;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "picture")
public class Picture implements Serializable, WithCompany {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @ManyToOne(optional = false)
    private Company company;
    @Column(name = "picture_data")
//    @NotNull // cause issue with the jpa modelgen
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;
    @Column(name = "content_type")
    @Size(min = 1, max = 128)
    private String contentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final Picture other = (Picture) obj;
        return Objects.equals(this.id, other.id);
    }

}
