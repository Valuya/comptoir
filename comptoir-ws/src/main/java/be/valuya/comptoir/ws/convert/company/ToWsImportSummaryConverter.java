package be.valuya.comptoir.ws.convert.company;

import be.valuya.comptoir.service.ImportSummary;
import be.valuya.comptoir.ws.rest.api.domain.company.WsImportSummary;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ToWsImportSummaryConverter {

    public WsImportSummary toWsImportSummary(ImportSummary importSummary) {
        long attributeDefinitionCount = importSummary.getAttributeDefinitionCount();
        long attributeValueCount = importSummary.getAttributeValueCount();
        long itemVariantCount = importSummary.getItemVariantCount();
        long itemCount = importSummary.getItemCount();
        long defaultItemVariantCount = importSummary.getDefaultItemVariantCount();

        WsImportSummary wsImportSummary = new WsImportSummary();
        wsImportSummary.setAttributeDefinitionCount(attributeDefinitionCount);
        wsImportSummary.setAttributeValueCount(attributeValueCount);
        wsImportSummary.setItemVariantCount(itemVariantCount);
        wsImportSummary.setItemCount(itemCount);
        wsImportSummary.setDefaultItemVariantCount(defaultItemVariantCount);
        return wsImportSummary;
    }
}
