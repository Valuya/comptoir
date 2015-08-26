package be.valuya.comptoir.model.commercial;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class ExternalEntity<K, V> {

    private K externalId;
    private V value;

    public ExternalEntity() {
    }

    public ExternalEntity(K backendId, V value) {
        this.externalId = backendId;
        this.value = value;
    }

    public K getExternalId() {
        return externalId;
    }

    public void setExternalId(K externalId) {
        this.externalId = externalId;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

}
