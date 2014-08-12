package be.valuya.comptoir.web.view;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class Views {

    private static final String REDIRECT = "?faces-redirect=true";

    public static final String ITEM_LIST = "/stock/list" + REDIRECT;
    public static final String ITEM_DETAILS = "/stock/details" + REDIRECT;

    public static final String SALE_DETAILS = "/sale/details" + REDIRECT;
}
