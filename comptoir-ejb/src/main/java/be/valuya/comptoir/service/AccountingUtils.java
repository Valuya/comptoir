package be.valuya.comptoir.service;

import be.valuya.comptoir.model.accounting.CustomerLoyaltyAccountingEntry;
import be.valuya.comptoir.model.cash.Balance;
import be.valuya.comptoir.model.cash.MoneyPile;
import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.ItemVariantSalePriceDetails;
import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.commercial.SalePrice;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Customer;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Yannick
 */
public class AccountingUtils {

    public static ItemVariantSalePriceDetails calcItemVariantSalePriceDetails(ItemVariantSale variantSale) {
        int quantity = variantSale.getQuantity().toBigInteger().intValue();
        Price unitPrice = variantSale.getPrice();
        BigDecimal unitDiscountRate = unitPrice.getDiscountRatio();
        BigDecimal unitVatExclusive = unitPrice.getVatExclusive();
        BigDecimal unitVatRate = unitPrice.getVatRate();

        ItemVariantSalePriceDetails priceDetails = new ItemVariantSalePriceDetails();

        BigDecimal totalVatExclusivePriorDiscount = unitVatExclusive.multiply(BigDecimal.valueOf(quantity))
                .setScale(4, RoundingMode.HALF_UP);
        priceDetails.setQuantity(quantity);
        priceDetails.setUnitPriceVatExclusive(unitVatExclusive);
        priceDetails.setTotalVatExclusivePriorDiscount(totalVatExclusivePriorDiscount);

        BigDecimal effectiveDiscountRate = AccountingUtils.getEffectiveDiscountRate(variantSale);
        BigDecimal discountAmount = totalVatExclusivePriorDiscount.multiply(effectiveDiscountRate)
                .setScale(4, RoundingMode.HALF_UP);
        BigDecimal totalVatExclusive = AccountingUtils.applyDiscount(totalVatExclusivePriorDiscount, effectiveDiscountRate);
        priceDetails.setDiscountRatio(unitDiscountRate);
        priceDetails.setEffectiveDiscountRatio(effectiveDiscountRate);
        priceDetails.setDiscountAmount(discountAmount);
        priceDetails.setTotalVatExclusive(totalVatExclusive);

        BigDecimal vatAmount = totalVatExclusive.multiply(unitVatRate)
                .setScale(4, RoundingMode.HALF_UP);
        BigDecimal totalVatInclusive = applyVat(totalVatExclusive, unitVatRate);
        priceDetails.setVatRate(unitVatRate);
        priceDetails.setVatAmount(vatAmount);
        priceDetails.setTotalVatInclusive(totalVatInclusive);
        return priceDetails;
    }

    public static BigDecimal calcUnitPriceVatExclusiveFromTotalVatExclusivePriorDiscount(ItemVariantSale variantSale, BigDecimal totalVatExclusivePriorDiscount) {
        BigDecimal quantity = variantSale.getQuantity();
        BigDecimal unitPrice = totalVatExclusivePriorDiscount.divide(quantity)
                .setScale(4, RoundingMode.HALF_UP);
        return unitPrice;
    }


    public static BigDecimal calcDiscountRateFromDiscountAmount(ItemVariantSale variantSale, BigDecimal discountAmount) {
        BigDecimal quantity = variantSale.getQuantity();
        Price price = variantSale.getPrice();
        BigDecimal vatExclusive = price.getVatExclusive();
        BigDecimal totalVatExclusive = vatExclusive.multiply(quantity)
                .setScale(4, RoundingMode.HALF_UP);
        BigDecimal discountRate = totalVatExclusive.divide(discountAmount, 2, RoundingMode.HALF_UP);
        return discountRate;
    }

    public static BigDecimal calcUnitPriceVatExclusiveFromTotalVatExclusive(ItemVariantSale variantSale, BigDecimal totalVatExclusive) {
        BigDecimal quantity = variantSale.getQuantity();
        BigDecimal discountRate = getEffectiveDiscountRate(variantSale);

        BigDecimal totalVatExclusivePriorDiscount = removeDiscount(totalVatExclusive, discountRate);
        BigDecimal unitPriceVatExclusive = totalVatExclusivePriorDiscount.divide(quantity, 2, RoundingMode.HALF_UP);
        return unitPriceVatExclusive;
    }

    public static BigDecimal calcVatRateFromVatAmount(ItemVariantSale variantSale, BigDecimal vatAmount) {
        int quantity = variantSale.getQuantity().toBigInteger().intValue();
        Price unitPrice = variantSale.getPrice();
        BigDecimal unitVatExclusive = unitPrice.getVatExclusive();
        BigDecimal totalVatExclusivePriorDiscount = unitVatExclusive.multiply(BigDecimal.valueOf(quantity))
                .setScale(4, RoundingMode.HALF_UP);
        BigDecimal effectiveDiscountRate = AccountingUtils.getEffectiveDiscountRate(variantSale);
        BigDecimal totalVatExclusive = AccountingUtils.applyDiscount(totalVatExclusivePriorDiscount, effectiveDiscountRate);

        BigDecimal vatRate = totalVatExclusive.divide(vatAmount, 2, RoundingMode.HALF_UP);
        return vatRate;
    }


    public static BigDecimal calcUnitPriceVatExclusiveFromTotalVatInclusive(ItemVariantSale variantSale, BigDecimal totalVatInclusive) {
        Price unitPrice = variantSale.getPrice();
        BigDecimal vatRate = unitPrice.getVatRate();
        BigDecimal totalVatExclusive = removeVat(totalVatInclusive, vatRate);

        return calcUnitPriceVatExclusiveFromTotalVatExclusive(variantSale, totalVatExclusive);
    }


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
    public static CustomerLoyaltyAccountingEntry calcCustomerLoyalty(@Nonnull Sale sale, @Nonnull List<ItemVariantSale> itemSales) {
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


    private static boolean isStrictlyPositive(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }


    private static BigDecimal applyDiscount(@Min(0) BigDecimal value, @Min(0) @Max(1) BigDecimal discountRate) {
        if (!isStrictlyPositive(discountRate)) {
            return value;
        }
        BigDecimal ratio = BigDecimal.ONE.subtract(discountRate)
                .setScale(4, RoundingMode.HALF_DOWN);
        BigDecimal valueIncludingDiscount = value.multiply(ratio)
                .setScale(4, RoundingMode.HALF_UP);
        return valueIncludingDiscount;
    }

    private static BigDecimal removeDiscount(@Min(0) BigDecimal discountedValue, @Min(0) @Max(1) BigDecimal discountRate) {
        if (!isStrictlyPositive(discountRate)) {
            return discountedValue;
        }
        BigDecimal ratio = BigDecimal.ONE.add(discountRate)
                .setScale(4, RoundingMode.HALF_DOWN);
        BigDecimal valueIncludingDiscount = discountedValue.multiply(ratio)
                .setScale(4, RoundingMode.HALF_UP);
        return valueIncludingDiscount;
    }


    private static BigDecimal applyVat(@Min(0) BigDecimal value, @Min(0) BigDecimal vatRate) {
        if (!isStrictlyPositive(vatRate)) {
            return value;
        }
        BigDecimal ratio = BigDecimal.ONE.add(vatRate)
                .setScale(4, RoundingMode.HALF_DOWN);
        BigDecimal valueIncludingVat = value.multiply(ratio)
                .setScale(4, RoundingMode.HALF_UP);
        return valueIncludingVat;
    }


    private static BigDecimal removeVat(@Min(0) BigDecimal value, @Min(0) BigDecimal vatRate) {
        if (!isStrictlyPositive(vatRate)) {
            return value;
        }
        BigDecimal ratio = BigDecimal.ONE.subtract(vatRate)
                .setScale(4, RoundingMode.HALF_DOWN);
        BigDecimal valueIncludingVat = value.multiply(ratio)
                .setScale(4, RoundingMode.HALF_UP);
        return valueIncludingVat;
    }

    private static BigDecimal getEffectiveDiscountRate(ItemVariantSale variantSale) {
        Price price = variantSale.getPrice();
        Sale sale = variantSale.getSale();
        Customer customer = sale.getCustomer();
        boolean customerDiscountCumulable = customer.isDiscountCumulable();

        Optional<BigDecimal> itemDiscountRateOptional = Optional.ofNullable(price.getDiscountRatio())
                .filter(AccountingUtils::isStrictlyPositive);
        Optional<BigDecimal> customerDiscountRateOptional = Optional.ofNullable(customer.getDiscountRate())
                .filter(AccountingUtils::isStrictlyPositive);
        boolean includeCustomerDiscount = Optional.ofNullable(variantSale.getIncludeCustomerDiscount())
                .orElse(false);

        if (includeCustomerDiscount && customerDiscountCumulable) {
            // discounted price = price * (1 - rateTotal) = price * (1-rateA) * (1-rateB)
            // rateTotal = 1 - ((1-rateA)*(1-rateB))
            BigDecimal itemIncludedRate = itemDiscountRateOptional
                    .map(BigDecimal.ONE::subtract)
                    .orElse(BigDecimal.ONE);
            BigDecimal customerIncludedRate = customerDiscountRateOptional
                    .map(BigDecimal.ONE::subtract)
                    .orElse(BigDecimal.ONE);
            BigDecimal totalIncludedRate = itemIncludedRate.multiply(customerIncludedRate);
            BigDecimal totalDiscountRate = BigDecimal.ONE.subtract(totalIncludedRate);
            return totalDiscountRate
                    .setScale(2, RoundingMode.HALF_UP);
        } else if (includeCustomerDiscount) {
            return itemDiscountRateOptional
                    .or(() -> customerDiscountRateOptional)
                    .orElse(BigDecimal.ZERO)
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            return itemDiscountRateOptional
                    .orElse(BigDecimal.ZERO)
                    .setScale(2, RoundingMode.HALF_UP);
        }
    }

}
