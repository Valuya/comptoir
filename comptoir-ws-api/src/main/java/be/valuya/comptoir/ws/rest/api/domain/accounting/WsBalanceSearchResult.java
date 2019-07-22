package be.valuya.comptoir.ws.rest.api.domain.accounting;

import be.valuya.comptoir.ws.rest.api.domain.accounting.WsBalanceRef;
import be.valuya.comptoir.ws.rest.api.util.WsSearchResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "WsBalanceSearchResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsBalanceSearchResult implements WsSearchResult<WsBalanceRef> {

    private List<WsBalanceRef> list = new ArrayList<>();
    private long totalCount;

    @Override
    public List<WsBalanceRef> getList() {
        return list;
    }

    public void setList(List<WsBalanceRef> list) {
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
