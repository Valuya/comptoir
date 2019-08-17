package be.valuya.comptoir.service;

import be.valuya.comptoir.model.accounting.CustomerLoyaltyAccountingEntry;
import be.valuya.comptoir.model.cash.Balance;
import be.valuya.comptoir.model.cash.MoneyPile;
import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.ItemVariantSalePriceDetails;
import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.commercial.SalePriceDetails;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Customer;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Yannick
 */
public class AccountingUtils {

    public static ItemVariantSalePriceDetails calcItemVariantSalePriceDetails(ItemVariantSale variantSale) {
        int quantity = variantSale.getQuantity().toBigInteger().intValue();
        Price unitPrice = variantSale.getPrice();
        BigDecimal unitDiscountRate = Optional.ofNullable(unitPrice.getDiscountRatio())
                .orElse(BigDecimal.ZERO);
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

    public static SalePriceDetails calcSalePriceDetails(Sale sale, List<ItemVariantSale> itemVariantSales) {
        List<ItemVariantSalePriceDetails> itemPriceDetails = itemVariantSales.stream()
                .map(AccountingUtils::calcItemVariantSalePriceDetails)
                .collect(Collectors.toList());

        BigDecimal itemsTotalVatExclusive = itemPriceDetails.stream()
                .map(ItemVariantSalePriceDetails::getTotalVatExclusive)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        BigDecimal itemTotalVat = itemPriceDetails.stream()
                .map(ItemVariantSalePriceDetails::getVatAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        SalePriceDetails salePriceDetails = new SalePriceDetails();
        salePriceDetails.setTotalPriceVatExclusivePriorSaleDiscount(itemsTotalVatExclusive);

        BigDecimal saleDiscountRatio = Optional.ofNullable(sale.getDiscountRatio())
                .orElse(BigDecimal.ZERO);
        BigDecimal totalVatExclusiveWithDiscount = applyDiscount(itemsTotalVatExclusive, saleDiscountRatio);
        BigDecimal vatTotalWithDiscount = applyDiscount(itemTotalVat, saleDiscountRatio);
        BigDecimal saleDiscountAmount = itemsTotalVatExclusive.multiply(saleDiscountRatio)
                .setScale(4, RoundingMode.HALF_UP);
        salePriceDetails.setSaleDiscountRatio(saleDiscountRatio);
        salePriceDetails.setSaleDiscountAmount(saleDiscountAmount);
        salePriceDetails.setTotalPriceVatExclusive(totalVatExclusiveWithDiscount);
        salePriceDetails.setVatAmount(vatTotalWithDiscount);

        BigDecimal totalVatInclusive = totalVatExclusiveWithDiscount.add(vatTotalWithDiscount);
        salePriceDetails.setTotalPriceVatInclusive(totalVatInclusive);
        return salePriceDetails;
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
        BigDecimal discountRate = discountAmount.divide(totalVatExclusive, 4, RoundingMode.HALF_UP);
        return discountRate;
    }


    public static BigDecimal calcDiscountRateFromDiscountAmount(Sale sale, List<ItemVariantSale> itemVariantSales, BigDecimal discountAmount) {
        List<ItemVariantSalePriceDetails> itemPriceDetails = itemVariantSales.stream()
                .map(AccountingUtils::calcItemVariantSalePriceDetails)
                .collect(Collectors.toList());

        BigDecimal itemsTotalVatExclusive = itemPriceDetails.stream()
                .map(ItemVariantSalePriceDetails::getTotalVatExclusive)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        BigDecimal discountRate = discountAmount.divide(itemsTotalVatExclusive, 4, RoundingMode.HALF_UP);
        return discountRate;
    }


    public static BigDecimal calcDiscountRateFromTotalVatInclusive(Sale sale, List<ItemVariantSale> itemVariantSales, BigDecimal totalVatInclusive) {
        List<ItemVariantSalePriceDetails> itemPriceDetails = itemVariantSales.stream()
                .map(AccountingUtils::calcItemVariantSalePriceDetails)
                .collect(Collectors.toList());

        BigDecimal itemsTotalVatInclusive = itemPriceDetails.stream()
                .map(ItemVariantSalePriceDetails::getTotalVatInclusive)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        BigDecimal totalDiscountAmount = itemsTotalVatInclusive.subtract(totalVatInclusive);
        BigDecimal discountRate = totalDiscountAmount.divide(itemsTotalVatInclusive, 4, RoundingMode.HALF_UP);
        return discountRate;
    }


    public static BigDecimal calcUnitPriceVatExclusiveFromTotalVatExclusive(ItemVariantSale variantSale, BigDecimal totalVatExclusive) {
        BigDecimal quantity = variantSale.getQuantity();
        BigDecimal discountRate = getEffectiveDiscountRate(variantSale);

        BigDecimal totalVatExclusivePriorDiscount = removeDiscount(totalVatExclusive, discountRate);
        BigDecimal unitPriceVatExclusive = totalVatExclusivePriorDiscount.divide(quantity, 4, RoundingMode.HALF_UP);
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

        BigDecimal vatRate = totalVatExclusive.divide(vatAmount, 4, RoundingMode.HALF_UP);
        return vatRate;
    }


    public static BigDecimal calcUnitPriceVatExclusiveFromTotalVatInclusive(ItemVariantSale variantSale, BigDecimal totalVatInclusive) {
        Price unitPrice = variantSale.getPrice();
        BigDecimal vatRate = unitPrice.getVatRate();
        BigDecimal totalVatExclusive = removeVat(totalVatInclusive, vatRate);

        return calcUnitPriceVatExclusiveFromTotalVatExclusive(variantSale, totalVatExclusive);
    }

    public static ItemVariantSale calcItemVariantSale(ItemVariantSale variantSale) {
        ItemVariantSalePriceDetails priceDetails = calcItemVariantSalePriceDetails(variantSale);
        BigDecimal unitPriceVatExclusive = priceDetails.getUnitPriceVatExclusive();
        BigDecimal vatRate = priceDetails.getVatRate();
        BigDecimal discountRatio = priceDetails.getDiscountRatio();
        BigDecimal totalVatExclusivePriorDiscount = priceDetails.getTotalVatExclusivePriorDiscount();

        Price price = variantSale.getPrice();
        price.setVatExclusive(unitPriceVatExclusive);
        price.setVatRate(vatRate);
        price.setDiscountRatio(discountRatio);

        variantSale.setTotal(totalVatExclusivePriorDiscount);
        return variantSale;
    }

    public static Sale calcSale(Sale sale, List<ItemVariantSale> itemSales) {
        List<ItemVariantSale> updatedItemSales = itemSales.stream()
                .map(AccountingUtils::calcItemVariantSale)
                .collect(Collectors.toList());

        SalePriceDetails salePriceDetails = calcSalePriceDetails(sale, updatedItemSales);
        BigDecimal vatAmount = salePriceDetails.getVatAmount();
        BigDecimal totalVatExclusive = salePriceDetails.getTotalPriceVatExclusive();
        BigDecimal discountAmount = salePriceDetails.getSaleDiscountAmount();

        sale.setVatExclusiveAmount(totalVatExclusive);
        sale.setVatAmount(vatAmount);
        sale.setDiscountAmount(discountAmount);

        return sale;
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

        BigDecimal ratio = BigDecimal.ONE.subtract(discountRate)
                .setScale(4, RoundingMode.HALF_DOWN);
        BigDecimal valueIncludingDiscount = discountedValue.divide(ratio, 4, RoundingMode.HALF_UP);
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
        BigDecimal ratio = BigDecimal.ONE.add(vatRate)
                .setScale(4, RoundingMode.HALF_DOWN);
        BigDecimal valueIncludingVat = value.divide(ratio, 4, RoundingMode.HALF_UP);
        return valueIncludingVat;
    }

    private static BigDecimal getEffectiveDiscountRate(ItemVariantSale variantSale) {
        Price price = variantSale.getPrice();
        Sale sale = variantSale.getSale();
        Optional<Customer> customerOptional = Optional.ofNullable(sale.getCustomer());
        boolean customerDiscountCumulable = customerOptional
                .map(Customer::isDiscountCumulable)
                .orElse(false);

        Optional<BigDecimal> itemDiscountRateOptional = Optional.ofNullable(price.getDiscountRatio())
                .filter(AccountingUtils::isStrictlyPositive);
        Optional<BigDecimal> customerDiscountRateOptional = customerOptional
                .map(Customer::getDiscountRate)
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
                    .setScale(4, RoundingMode.HALF_UP);
        } else if (includeCustomerDiscount) {
            return itemDiscountRateOptional
                    .or(() -> customerDiscountRateOptional)
                    .orElse(BigDecimal.ZERO)
                    .setScale(4, RoundingMode.HALF_UP);
        } else {
            return itemDiscountRateOptional
                    .orElse(BigDecimal.ZERO)
                    .setScale(4, RoundingMode.HALF_UP);
        }
    }

}
