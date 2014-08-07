package be.valuya.comptoir.model.accounting;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public enum AccountType {

    CUSTOMER("411000"),
    DISCOUNT("665000"),
    FINISHED_PRODUCT("701000"),
    SERVICE("706000"),
    GOODS("707000"),
    TRANSPORT("708500"),
    CASH("530000"),
    CHECK("511200"),
    BANK("512000"),
    CREDIT_CARD("511500"),
    VAT("445710"),
    VAT_TRANSPORT("445660");
    
    private final String defaultAccountNumber;

    private AccountType(String defaultAccountNumber) {
        this.defaultAccountNumber = defaultAccountNumber;
    }

    public String getDefaultAccountNumber() {
        return defaultAccountNumber;
    }
    
}
