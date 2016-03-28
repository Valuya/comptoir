package be.valuya.comptoir.service;

import be.valuya.comptoir.model.accounting.CustomerLoyaltyAccountingEntry;
import be.valuya.comptoir.model.cash.Balance;
import be.valuya.comptoir.model.cash.MoneyPile;
import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.commercial.SalePrice;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Customer;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Yannick
 */
public class AccountingUtils {

    public static BigDecimal calcVatAmount(Price price) {
        BigDecimal vatExclusive = price.getVatExclusive();
        BigDecimal vatRate = price.getVatRate();
        BigDecimal vatAmount = calcVatAmount(vatExclusive, vatRate);
        return vatAmount;
    }

    public static BigDecimal calcVatAmount(BigDecimal vatExclusive, BigDecimal vatRate) {
        //TODO: remove when ItemSale is correctly validated
        vatRate = Optional.ofNullable(vatRate).orElse(BigDecimal.valueOf(210000, 4));

        BigDecimal vatAmount = vatExclusive.multiply(vatRate);
        return vatAmount;
    }

    public static BigDecimal calcVatInclusive(BigDecimal vatExclusive, BigDecimal vatRate) {
        BigDecimal vatAmount = calcVatAmount(vatExclusive, vatRate);
        BigDecimal vatInclusive = vatExclusive.add(vatAmount);
        return vatInclusive;
    }

    public static SalePrice calcSalePrice(ItemVariantSale itemSale) {
        Price price = itemSale.getPrice();
        BigDecimal quantity = itemSale.getQuantity();
        return AccountingUtils.calcSalePrice(price, quantity);
    }

    public static SalePrice calcSalePrice(Price price, BigDecimal quantity) {
        BigDecimal effectiveUnitBase = calcEffectivePriceWithoutTaxes(price);
        BigDecimal effectiveBase = effectiveUnitBase.multiply(quantity).setScale(4, RoundingMode.HALF_UP);

        BigDecimal vatRate = price.getVatRate();

        BigDecimal vatAmount = AccountingUtils.calcVatAmount(effectiveBase, vatRate).setScale(4, RoundingMode.HALF_UP);

        return new SalePrice(effectiveBase, vatAmount);

    }

    public static BigDecimal calcEffectivePriceWithoutTaxes(Price price) {
        // TODO: shouldn't happen
        BigDecimal vatExclusive = Optional.ofNullable(price.getVatExclusive()).orElse(BigDecimal.ZERO).setScale(4, RoundingMode.HALF_UP);
        BigDecimal discountRatio = Optional.ofNullable(price.getDiscountRatio()).orElse(BigDecimal.ZERO).setScale(4, RoundingMode.HALF_UP);
        BigDecimal ratio = BigDecimal.valueOf(1, 00).subtract(discountRatio);
        BigDecimal effectiveVatExclusive = vatExclusive.multiply(ratio).setScale(4, RoundingMode.HALF_UP);
        return effectiveVatExclusive;
    }

    public static SalePrice calcSaleDiscount(SalePrice salePrice, Sale sale) {
        BigDecimal discountRatio = Optional.ofNullable(sale.getDiscountRatio()).orElse(BigDecimal.ZERO).setScale(4, RoundingMode.HALF_UP);
        BigDecimal base = salePrice.getBase();
        BigDecimal taxes = salePrice.getTaxes();

        BigDecimal discountBaseAmount = base.multiply(discountRatio).setScale(4, RoundingMode.HALF_UP);
        BigDecimal discountTaxesAmount = taxes.multiply(discountRatio).setScale(4, RoundingMode.HALF_UP);

        BigDecimal baseWithDiscount = base.subtract(discountBaseAmount);
        BigDecimal taxesWithDiscount = taxes.subtract(discountTaxesAmount);
        salePrice.setBase(baseWithDiscount);
        salePrice.setTaxes(taxesWithDiscount);
        sale.setDiscountAmount(discountBaseAmount);
        return salePrice;
    }

    public static Sale calcSale(Sale sale, List<ItemVariantSale> itemSales) {
        SalePrice totalSalePrice = itemSales.stream()
                .map(AccountingUtils::calcSalePrice)
                .reduce(AccountingUtils::combineSalePrices)
                .orElseGet(() -> new SalePrice(BigDecimal.ZERO, BigDecimal.ZERO));

        SalePrice totalSalePriceWithDiscount = AccountingUtils.calcSaleDiscount(totalSalePrice, sale);
        BigDecimal vatExclusiveTotal = totalSalePriceWithDiscount.getBase();
        BigDecimal vatTotal = totalSalePriceWithDiscount.getTaxes();

        sale.setVatExclusiveAmount(vatExclusiveTotal);
        sale.setVatAmount(vatTotal);

        return sale;
    }

    private static SalePrice combineSalePrices(SalePrice salePrice1, SalePrice salePrice2) {
        BigDecimal base1 = salePrice1.getBase();
        BigDecimal base2 = salePrice2.getBase();

        BigDecimal taxes1 = salePrice1.getTaxes();
        BigDecimal taxes2 = salePrice2.getTaxes();

        BigDecimal baseTot = base1.add(base2);
        BigDecimal taxesTot = taxes1.add(taxes2);

        return new SalePrice(baseTot, taxesTot);
    }

    public static MoneyPile calcMoneyPile(MoneyPile moneyPile) {
        BigDecimal count = moneyPile.getCount();
        BigDecimal unitAmount = moneyPile.getUnitAmount();
        BigDecimal totalValue = count.multiply(unitAmount);
        moneyPile.setTotal(totalValue);
        return moneyPile;
    }

    public static Balance calcBalance(Balance balance, List<MoneyPile> moneyPiles) {
        BigDecimal balanceValue = moneyPiles.stream()
                .map((moneyPile) -> moneyPile.getTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        balance.setBalance(balanceValue);
        return balance;
    }

    @CheckForNull
    public static CustomerLoyaltyAccountingEntry calcCustomerLoyalty(@Nonnull  Sale sale, @Nonnull List<ItemVariantSale> itemSales) {
        Customer customer = sale.getCustomer();
        if (customer == null) {
            return null;
        }

        BigDecimal customerLoyaltyAmount = itemSales.stream()
                .map(AccountingUtils::calcItemVariantSaleCustomerLoyaltyAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (customerLoyaltyAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        ZonedDateTime dateTime = sale.getDateTime();

        CustomerLoyaltyAccountingEntry accountingEntry = new CustomerLoyaltyAccountingEntry();
        accountingEntry.setCustomer(customer);
        accountingEntry.setAmount(customerLoyaltyAmount);
        accountingEntry.setDateTime(dateTime);
        accountingEntry.setSale(sale);
        return accountingEntry;
    }

    public static boolean shouldIncludeCustomerLoyalty(ItemVariantSale itemVariantSale) {
        Price price = itemVariantSale.getPrice();
        if (price == null) {
            return false;
        }
        BigDecimal discountRatio = Optional.ofNullable(price.getDiscountRatio())
                .orElse(BigDecimal.ZERO);
        // Not included if there is a discount
        if (discountRatio.compareTo(BigDecimal.ZERO) > 0) {
            return false;
        }
        return true;
    }
    public static BigDecimal calcItemVariantSaleCustomerLoyaltyAmount(ItemVariantSale itemVariantSale) {
        Sale sale = itemVariantSale.getSale();
        Company company = sale.getCompany();
        BigDecimal customerLoyaltyRate = company.getCustomerLoyaltyRate();
        if (customerLoyaltyRate == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal quantity = itemVariantSale.getQuantity();
        Price price = itemVariantSale.getPrice();
        BigDecimal vatExclusive = price.getVatExclusive();
        Boolean customerLoyalty = itemVariantSale.getIncludeCustomerLoyalty();

        if (!customerLoyalty) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalVatExclusive = quantity.multiply(vatExclusive);
        BigDecimal totalLoyaltyAmount = totalVatExclusive.multiply(customerLoyaltyRate);
        return totalLoyaltyAmount;
    }
}