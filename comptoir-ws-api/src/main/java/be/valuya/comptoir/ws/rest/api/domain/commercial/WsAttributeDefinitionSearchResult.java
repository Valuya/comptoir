package be.valuya.comptoir.ws.rest.api.domain.commercial;

import be.valuya.comptoir.ws.rest.api.util.WsSearchResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WsAttributeDefinitionSearchResult implements WsSearchResult<WsAttributeDefinitionRef> {
    private List<WsAttributeDefinitionRef> list = new ArrayList<>();
    private long totalCount;

    @Override
    public List<WsAttributeDefinitionRef> getList() {
        return list;
    }

    @Override
    public void setList(List<WsAttributeDefinitionRef> list) {
        this.list = list;
    }

    @Override
    public long getTotalCount() {
        return totalCount;
    }

    @Override
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
