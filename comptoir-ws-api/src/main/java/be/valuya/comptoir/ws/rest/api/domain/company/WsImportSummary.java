package be.valuya.comptoir.ws.rest.api.domain.company;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@XmlRootElement
@XmlAccessorType
public class WsImportSummary {

    private long attributeDefinitionCount;
    private long attributeValueCount;
    private long itemVariantCount;
    private long itemCount;
    private long defaultItemVariantCount;

    public long getAttributeDefinitionCount() {
        return attributeDefinitionCount;
    }

    public void setAttributeDefinitionCount(long attributeDefinitionCount) {
        this.attributeDefinitionCount = attributeDefinitionCount;
    }

    public long getAttributeValueCount() {
        return attributeValueCount;
    }

    public void setAttributeValueCount(long attributeValueCount) {
        this.attributeValueCount = attributeValueCount;
    }

    public long getItemVariantCount() {
        return itemVariantCount;
    }

    public void setItemVariantCount(long itemVariantCount) {
        this.itemVariantCount = itemVariantCount;
    }

    public long getItemCount() {
        return itemCount;
    }

    public void setItemCount(long itemCount) {
        this.itemCount = itemCount;
    }

    public long getDefaultItemVariantCount() {
        return defaultItemVariantCount;
    }

    public void setDefaultItemVariantCount(long defaultItemVariantCount) {
        this.defaultItemVariantCount = defaultItemVariantCount;
    }
}
