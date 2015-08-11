package be.valuya.comptoir.service;

import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.accounting.AccountType;
import be.valuya.comptoir.model.commercial.Pos;
import be.valuya.comptoir.model.commercial.PosPaymentAccount;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.factory.LocaleTextFactory;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.thirdparty.Employee;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class RegistrationService {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private LocaleTextFactory localeTextFactory;
    @EJB
    private EmployeeService employeeService;

    public Company register(Company company, Employee employee, String password) {
        Company managedCompany = entityManager.merge(company);
        employee.setCompany(managedCompany);
        Employee managedEmployee = entityManager.merge(employee);

        employeeService.setPassword(managedEmployee, password);

        LocaleText stockDescription = localeTextFactory.createLocaleText();

        Map<Locale, String> stockDescriptionLocaleTextMap = stockDescription.getLocaleTextMap();
        stockDescriptionLocaleTextMap.put(Locale.ENGLISH, "Default stock");
        stockDescriptionLocaleTextMap.put(Locale.FRENCH, "Stock par défaut");

        Stock stock = new Stock();
        stock.setDescription(stockDescription);
        stock.setCompany(managedCompany);

        Stock managedStock = entityManager.merge(stock);

        List<Account> otherAccounts = createDefaultAccountsOther(managedCompany);
        List<Account> paymentAccounts = createDefaultAccountsPayment(managedCompany);

        otherAccounts = otherAccounts.stream()
                .map(entityManager::merge)
                .collect(Collectors.toList());
        paymentAccounts = paymentAccounts.stream()
                .map(entityManager::merge)
                .collect(Collectors.toList());

        Pos pos = new Pos();
        pos.setCompany(managedCompany);
        LocaleText posDescription = localeTextFactory.createLocaleText();
        posDescription.put(Locale.ENGLISH, "Point of sale");
        posDescription.put(Locale.FRENCH, "Point de vente");
        pos.setDescription(posDescription);

        Pos managedPos = entityManager.merge(pos);
        paymentAccounts.stream()
                .map(paymentAccount -> createPosPaymentAccount(managedPos, paymentAccount))
                .map(entityManager::merge);

        return managedCompany;
    }

    public PosPaymentAccount createPosPaymentAccount(Pos pos, Account account) {
        PosPaymentAccount posPaymentAccount = new PosPaymentAccount();
        posPaymentAccount.setPointOfSale(pos);
        posPaymentAccount.setAccount(account);

        return posPaymentAccount;
    }

    public List<Account> createDefaultAccountsPayment(Company managedCompany) {
        List<Account> accounts = new ArrayList<>();

        Account cashAccount = new Account();
        cashAccount.setAccountType(AccountType.PAYMENT);
        cashAccount.setAccountingNumber("530000");
        cashAccount.setName("cash");
        LocaleText cashAccountLocaleText = localeTextFactory.createLocaleText();
        cashAccountLocaleText.put(Locale.FRENCH, "Cash");
        cashAccountLocaleText.put(Locale.ENGLISH, "Cash");
        cashAccount.setDescription(cashAccountLocaleText);
        cashAccount.setCompany(managedCompany);
        accounts.add(cashAccount);

        Account debitCardAccount = new Account();
        debitCardAccount.setAccountType(AccountType.VAT);
        debitCardAccount.setAccountingNumber("512001");
        debitCardAccount.setName("debit_card");
        LocaleText debitCardLocaleText = localeTextFactory.createLocaleText();
        debitCardLocaleText.put(Locale.FRENCH, "Debit card");
        debitCardLocaleText.put(Locale.ENGLISH, "Bancontact");
        debitCardAccount.setDescription(debitCardLocaleText);
        debitCardAccount.setCompany(managedCompany);
        accounts.add(debitCardAccount);

        return accounts;
    }

    public List<Account> createDefaultAccountsOther(Company managedCompany) {
        List<Account> accounts = new ArrayList<>();

        Account vatAccount = new Account();
        vatAccount.setAccountType(AccountType.VAT);
        vatAccount.setAccountingNumber("445710");
        vatAccount.setName("vat");
        LocaleText vatAccountLocaleText = localeTextFactory.createLocaleText();
        vatAccountLocaleText.put(Locale.FRENCH, "TVA");
        vatAccountLocaleText.put(Locale.ENGLISH, "VAT");
        vatAccount.setDescription(vatAccountLocaleText);
        vatAccount.setCompany(managedCompany);
        accounts.add(vatAccount);

        Account customerAccount = new Account();
        customerAccount.setAccountType(AccountType.OTHER);
        customerAccount.setAccountingNumber("411000");
        customerAccount.setName("customer");
        LocaleText customerAccountLocaleText = localeTextFactory.createLocaleText();
        customerAccountLocaleText.put(Locale.FRENCH, "Customer");
        customerAccountLocaleText.put(Locale.ENGLISH, "Client");
        customerAccount.setDescription(customerAccountLocaleText);
        customerAccount.setCompany(managedCompany);
        accounts.add(customerAccount);

        Account productAccount = new Account();
        productAccount.setAccountType(AccountType.OTHER);
        productAccount.setAccountingNumber("445710");
        productAccount.setName("product");
        LocaleText productLocaleText = localeTextFactory.createLocaleText();
        productLocaleText.put(Locale.FRENCH, "Finished product sale");
        productLocaleText.put(Locale.ENGLISH, "Vente de produit fini");
        productAccount.setDescription(productLocaleText);
        productAccount.setCompany(managedCompany);
        accounts.add(productAccount);

        Account goodsAccount = new Account();
        goodsAccount.setAccountType(AccountType.OTHER);
        goodsAccount.setAccountingNumber("1234567");
        goodsAccount.setName("goods");
        LocaleText goodsAccountLocaleText = localeTextFactory.createLocaleText();
        goodsAccountLocaleText.put(Locale.FRENCH, "Goods sale");
        goodsAccountLocaleText.put(Locale.ENGLISH, "Vente de biens");
        goodsAccount.setDescription(goodsAccountLocaleText);
        goodsAccount.setCompany(managedCompany);
        accounts.add(goodsAccount);

        return accounts;
    }
}
