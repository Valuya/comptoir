package be.valuya.comptoir.util.pagination;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 * @param <T>
 */
public class Sort<T extends Column> {

    private T sortColumn;
    private boolean ascending = true;

    public Sort() {
    }

    public Sort(T sortColumn, boolean ascending) {
        this.sortColumn = sortColumn;
        this.ascending = ascending;
    }

    public T getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(T sortColumn) {
        this.sortColumn = sortColumn;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

}
