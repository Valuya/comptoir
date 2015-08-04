package be.valuya.gestemps.comptoir.api.domain.commercial;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement(name = "itemPicture")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsItemPicture implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private WsItemRef itemRef;
    @Column(name = "picture_data")
    @Lob
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

    public WsItemRef getItemRef() {
        return itemRef;
    }

    public void setItemRef(WsItemRef itemRef) {
        this.itemRef = itemRef;
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
        final WsItemPicture other = (WsItemPicture) obj;
        return Objects.equals(this.id, other.id);
    }

}
