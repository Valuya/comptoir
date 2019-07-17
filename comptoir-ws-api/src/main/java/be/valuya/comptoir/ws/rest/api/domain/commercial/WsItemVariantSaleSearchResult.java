package be.valuya.comptoir.ws.rest.api.domain.commercial;

import be.valuya.comptoir.ws.rest.api.util.WsSearchResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "WsItemVariantSaleSearchResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsItemVariantSaleSearchResult implements WsSearchResult<WsItemVariantSaleRef> {

    private List<WsItemVariantSaleRef> list = new ArrayList<>();
    private long totalCount;

    @Override
    public List<WsItemVariantSaleRef> getList() {
        return list;
    }

    public void setList(List<WsItemVariantSaleRef> list) {
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
