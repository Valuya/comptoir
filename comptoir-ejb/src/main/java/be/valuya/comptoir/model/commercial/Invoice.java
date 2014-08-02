package be.valuya.comptoir.model.commercial;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
public class Invoice implements Serializable {

    @Id
    private Long id;
    private String number;
    @OneToOne
    private Sale sale;

}
