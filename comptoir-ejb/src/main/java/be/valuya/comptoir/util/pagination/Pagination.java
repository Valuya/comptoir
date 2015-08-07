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
    private List<Sort<S>> sortings;

    public Pagination() {
    }

    public Pagination(int offset, int maxResults, List<Sort<S>> sortings) {
        this.offset = offset;
        this.maxResults = maxResults;
        this.sortings = sortings;
    }

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

    public List<Sort<S>> getSortings() {
        return sortings;
    }

    public void setSortings(List<Sort<S>> sortings) {
        this.sortings = sortings;
    }

}
