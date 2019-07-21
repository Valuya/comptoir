package be.valuya.comptoir.ws.rest.api.domain.stock;

import be.valuya.comptoir.ws.rest.api.util.WsSearchResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "WsStockSearchResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsStockSearchResult implements WsSearchResult<WsStockRef> {

    private List<WsStockRef> list = new ArrayList<>();
    private long totalCount;

    @Override
    public List<WsStockRef> getList() {
        return list;
    }

    public void setList(List<WsStockRef> list) {
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
