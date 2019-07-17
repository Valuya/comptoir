package be.valuya.comptoir.ws.rest.api.domain.company;

import be.valuya.comptoir.ws.rest.api.util.WsSearchResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "WsCountrySearchResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsCountrySearchResult implements WsSearchResult<WsCountryRef> {

    private List<WsCountryRef> list = new ArrayList<>();
    private long totalCount;

    @Override
    public List<WsCountryRef> getList() {
        return list;
    }

    public void setList(List<WsCountryRef> list) {
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
