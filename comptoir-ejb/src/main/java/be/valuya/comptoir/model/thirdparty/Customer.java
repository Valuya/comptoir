package be.valuya.comptoir.model.thirdparty;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
public class Customer implements Serializable {

    @Id
    private Long id;
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

}
