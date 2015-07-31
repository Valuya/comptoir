package be.valuya.comptoir.util.pagination;

import java.util.List;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 * @param <T>
 * @param <S>
 */
public class Pagination<T, S extends Column<T>> {

    private int offset;
    private int maxResults;
    private List<Sorting<S>> sortings;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public List<Sorting<S>> getSortings() {
        return sortings;
    }

    public void setSortings(List<Sorting<S>> sortings) {
        this.sortings = sortings;
    }

}
