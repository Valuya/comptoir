package be.valuya.comptoir.ws.rest.api.domain.commercial;

import be.valuya.comptoir.ws.rest.api.util.WsSearchResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "WsPictureSearchResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class WsPictureSearchResult implements WsSearchResult<WsPictureRef> {

    private List<WsPictureRef> list = new ArrayList<>();
    private long totalCount;

    @Override
    public List<WsPictureRef> getList() {
        return list;
    }

    public void setList(List<WsPictureRef> list) {
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
