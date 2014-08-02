package be.valuya.comptoir.model.accounting;

import be.valuya.comptoir.model.misc.LocaleText;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Entity
public class Account implements Serializable {

    @Id
    private Long id;
    private String name;
    @ManyToOne
    private LocaleText description;

}
