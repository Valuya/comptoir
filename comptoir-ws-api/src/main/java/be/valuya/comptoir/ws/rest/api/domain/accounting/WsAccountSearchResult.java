package be.valuya.comptoir.ws.rest.api.domain.accounting;

import be.valuya.comptoir.ws.rest.api.util.WsSearchResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "WsAccountSearchResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsAccountSearchResult implements WsSearchResult<WsAccountRef> {

    private List<WsAccountRef> list = new ArrayList<>();
    private long totalCount;

    @Override
    public List<WsAccountRef> getList() {
        return list;
    }

    public void setList(List<WsAccountRef> list) {
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
