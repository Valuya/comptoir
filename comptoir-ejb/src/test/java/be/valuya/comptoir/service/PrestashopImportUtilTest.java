package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.AttributeDefinition;
import be.valuya.comptoir.model.commercial.AttributeValue;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.company.Company;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class PrestashopImportUtilTest {

    private PrestashopImportUtil prestashopImportUtil;

    @Before
    public void setup() {
        Company company = new Company();
        PrestashopImportParams prestashopImportParams = new PrestashopImportParams();
        prestashopImportParams.setDatabase("petitnapo");
        prestashopImportParams.setHost("localhost");
        prestashopImportParams.setPort(3306);
        prestashopImportParams.setUsername("root");
        prestashopImportParams.setPassword("zzzzzz");

        prestashopImportUtil = new PrestashopImportUtil(company, prestashopImportParams);
    }

    @Test
    public void testImportAll() {
        prestashopImportUtil.importAll();

        Locale locale = new Locale("fr");

        prestashopImportUtil.getAttributeValueStore().getBackendEntityStream().forEach(externalEntity -> {
            AttributeValue attributeValue = externalEntity.getValue();
            String attributeValueStr = renderAttributeValue(attributeValue, locale);
            System.out.println(attributeValueStr);
        });

        prestashopImportUtil.getItemStore().getBackendEntityStream().forEach(externalEntity -> {
            ItemVariant itemVariant = externalEntity.getValue();
            String reference = itemVariant.getReference();
            BigDecimal vatExclusive = itemVariant.getCurrentPrice().getVatExclusive();
            String name = itemVariant.getName().get(locale);
            String description = itemVariant.getDescription().get(locale);
            String itemStr = MessageFormat.format("{0,number,0}: {1}, {2,number,0.00} € - {3}", 0, reference, vatExclusive, name, description);
            System.out.println(itemStr);
        });

        prestashopImportUtil.getItemVariantStore().getBackendEntityStream().forEach(externalEntity -> {
            ItemVariant itemVariant = externalEntity.getValue();
            String reference = itemVariant.getReference();
            BigDecimal vatExclusive = itemVariant.getCurrentPrice().getVatExclusive();
            String name = itemVariant.getName().get(locale);
            String description = itemVariant.getDescription().get(locale);
            String itemStr = MessageFormat.format("{0,number,0}: {1}, {2,number,0.00} € - {3}", 0, reference, vatExclusive, name, description);
            for (AttributeValue attributeValue : itemVariant.getAttributeValues()) {
                String attributeValueStr = renderAttributeValue(attributeValue, locale);
                System.out.println("* " + attributeValueStr);
            }
            System.out.println(itemStr);
        });
    }

    private String renderAttributeValue(AttributeValue attributeValue, Locale locale) {
        AttributeDefinition attributeDefinition = attributeValue.getAttributeDefinition();
        String name = attributeDefinition.getName().get(locale);
        String value = attributeValue.getValue().get(locale);
        String attributeValueStr = name + ": " + value;
        return attributeValueStr;
    }

}
