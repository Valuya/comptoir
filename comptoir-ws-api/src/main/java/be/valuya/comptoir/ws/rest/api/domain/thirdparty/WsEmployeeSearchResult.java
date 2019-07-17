package be.valuya.comptoir.ws.rest.api.domain.thirdparty;

import be.valuya.comptoir.ws.rest.api.util.WsSearchResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "WsEmployeeSearchResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsEmployeeSearchResult implements WsSearchResult<WsEmployeeRef> {

    private List<WsEmployeeRef> list = new ArrayList<>();
    private long totalCount;

    @Override
    public List<WsEmployeeRef> getList() {
        return list;
    }

    public void setList(List<WsEmployeeRef> list) {
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
