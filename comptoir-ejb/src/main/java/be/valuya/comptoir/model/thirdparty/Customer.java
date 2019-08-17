package be.valuya.comptoir.model.thirdparty;

import be.valuya.comptoir.model.common.WithCompany;
import be.valuya.comptoir.model.company.Company;

import javax.annotation.Nonnull;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
@Table(name = "customer")
public class Customer implements Serializable, WithCompany {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Nonnull
    @ManyToOne(optional = false)
    private Company company;
    @Column(name = "first_name", length = 200)
    @Size(max = 200)
    private String firstName;
    @Column(name = "last_name", length = 200)
    @Size(max = 200)
    private String lastName;
    @Column(length = 200)
    @Size(max = 200)
    private String adress1;
    @Column(length = 200)
    @Size(max = 200)
    private String adress2;
    @Column(length = 20)
    @Size(max = 20)
    private String zip;
    @Column(length = 80)
    @Size(max = 82)
    private String city;
    @Column(length = 32)
    @Size(max = 32)
    private String phone1;
    @Column(length = 32)
    @Size(max = 32)
    private String phone2;
    @Column(length = 200)
    @Size(max = 200)
    private String email;
    @Column(columnDefinition = "TEXT")
    private String notes;
    @Column(name = "DISCOUNT_RATE")
    private BigDecimal discountRate;
    @Column(name = "DISCOUNT_CUMULABLE")
    private boolean discountCumulable;

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

    public String getAdress1() {
        return adress1;
    }

    public void setAdress1(String adress1) {
        this.adress1 = adress1;
    }

    public String getAdress2() {
        return adress2;
    }

    public void setAdress2(String adress2) {
        this.adress2 = adress2;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public boolean isDiscountCumulable() {
        return discountCumulable;
    }

    public void setDiscountCumulable(boolean discountCumulable) {
        this.discountCumulable = discountCumulable;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.id);
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
        final Customer other = (Customer) obj;
        return Objects.equals(this.id, other.id);
    }

}
