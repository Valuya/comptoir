package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.commercial.SalePrice;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
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
        vatRate = Optional.ofNullable(vatRate).orElse(BigDecimal.valueOf(2100, 2));

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
        BigDecimal effectiveBase = effectiveUnitBase.multiply(quantity).setScale(2, RoundingMode.HALF_UP);

        BigDecimal vatRate = price.getVatRate();

        BigDecimal vatAmount = AccountingUtils.calcVatAmount(effectiveBase, vatRate).setScale(2, RoundingMode.HALF_UP);

        return new SalePrice(effectiveBase, vatAmount);

    }

    public static BigDecimal calcEffectivePriceWithoutTaxes(Price price) {
        // TODO: shouldn't happen
        BigDecimal vatExclusive = Optional.ofNullable(price.getVatExclusive()).orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
        BigDecimal discountRatio = Optional.ofNullable(price.getDiscountRatio()).orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
        BigDecimal ratio = BigDecimal.valueOf(1, 00).subtract(discountRatio);
        BigDecimal effectiveVatExclusive = vatExclusive.multiply(ratio).setScale(2, RoundingMode.HALF_UP);
        return effectiveVatExclusive;
    }

     public static SalePrice calcSaleDiscount(SalePrice salePrice, Sale sale) {
        BigDecimal discountRatio = Optional.ofNullable(sale.getDiscountRatio()).orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
        if (discountRatio == null) {
            return salePrice;
        }
        BigDecimal base = salePrice.getBase();
        BigDecimal taxes = salePrice.getTaxes();
        
        BigDecimal discountBaseAmount = base.multiply(discountRatio).setScale(2, RoundingMode.HALF_UP);
        BigDecimal discountTaxesAmount = taxes.multiply(discountRatio).setScale(2, RoundingMode.HALF_UP);
        
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

        SalePrice totalSalePriceWithDiscount  = AccountingUtils.calcSaleDiscount(totalSalePrice, sale);
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
}
