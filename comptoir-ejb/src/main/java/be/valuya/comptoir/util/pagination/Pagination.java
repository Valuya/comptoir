package be.valuya.comptoir.util.pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @param <T>
 * @param <S>
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class Pagination<T, S extends Column<T>> {

    private int offset;
    private int maxResults;
    private Long allResultCount;
    private List<Sort<S>> sortings = new ArrayList<>();

    public Pagination() {
    }

    public Pagination(int offset, int maxResults) {
        this.offset = offset;
        this.maxResults = maxResults;
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

    public Long getAllResultCount() {
        return allResultCount;
    }

    public Optional<Long> getAllResultCountOptional() {
        return Optional.ofNullable(allResultCount);
    }

    public void setAllResultCount(Long allResultCount) {
        this.allResultCount = allResultCount;
    }

    public List<Sort<S>> getSortings() {
        return sortings;
    }

    public void setSortings(List<Sort<S>> sortings) {
        this.sortings = sortings;
    }

}
