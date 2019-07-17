package be.valuya.comptoir.ws.rest.api.domain.thirdparty;

import be.valuya.comptoir.ws.rest.api.util.WsSearchResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "WsCustomerSearchResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsCustomerSearchResult implements WsSearchResult<WsCustomerRef> {

    private List<WsCustomerRef> list = new ArrayList<>();
    private long totalCount;

    @Override
    public List<WsCustomerRef> getList() {
        return list;
    }

    public void setList(List<WsCustomerRef> list) {
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
