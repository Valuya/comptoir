package be.valuya.comptoir.ws.rest.api.domain.accounting;

import be.valuya.comptoir.ws.rest.api.util.WsSearchResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "WsAccountingEntrySearchResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsAccountingEntrySearchResult implements WsSearchResult<WsAccountingEntryRef> {

    private List<WsAccountingEntryRef> list = new ArrayList<>();
    private long totalCount;

    @Override
    public List<WsAccountingEntryRef> getList() {
        return list;
    }

    public void setList(List<WsAccountingEntryRef> list) {
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
