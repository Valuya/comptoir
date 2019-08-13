package be.valuya.comptoir.ws.rest;

import be.valuya.comptoir.model.commercial.AttributeValue;
import be.valuya.comptoir.ws.rest.api.ComptoirWsApplicationApi;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.ws.rest.control.AccountResource;
import be.valuya.comptoir.ws.rest.control.AccountingEntryResource;
import be.valuya.comptoir.ws.rest.control.AttributeDefinitionResource;
import be.valuya.comptoir.ws.rest.control.AttributeValueResource;
import be.valuya.comptoir.ws.rest.control.AuthResource;
import be.valuya.comptoir.ws.rest.control.BalanceResource;
import be.valuya.comptoir.ws.rest.control.CompanyResource;
import be.valuya.comptoir.ws.rest.control.CountryResource;
import be.valuya.comptoir.ws.rest.control.CustomerResource;
import be.valuya.comptoir.ws.rest.control.EmployeeResource;
import be.valuya.comptoir.ws.rest.control.ImportResource;
import be.valuya.comptoir.ws.rest.control.InvoiceResource;
import be.valuya.comptoir.ws.rest.control.ItemResource;
import be.valuya.comptoir.ws.rest.control.ItemVariantResource;
import be.valuya.comptoir.ws.rest.control.ItemVariantSaleResource;
import be.valuya.comptoir.ws.rest.control.ItemVariantStockResource;
import be.valuya.comptoir.ws.rest.control.MoneyPileResource;
import be.valuya.comptoir.ws.rest.control.PictureResource;
import be.valuya.comptoir.ws.rest.control.PosResource;
import be.valuya.comptoir.ws.rest.control.RegistrationResource;
import be.valuya.comptoir.ws.rest.control.SaleResource;
import be.valuya.comptoir.ws.rest.control.StockResource;
import be.valuya.comptoir.ws.rest.control.WebNotificationsResource;
import be.valuya.comptoir.ws.rest.provider.ComptoirWsParamConverterProvider;
import be.valuya.comptoir.ws.rest.provider.CrossOriginResourceSharingPreflightRequestFilter;
import be.valuya.comptoir.ws.rest.provider.CrossOriginResourceSharingResponseFilter;
import be.valuya.comptoir.ws.rest.provider.UnauthenticatedExceptionMapper;
import be.valuya.comptoir.ws.rest.provider.WsExceptionMapper;

import javax.annotation.security.DeclareRoles;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.ws.rs.ApplicationPath;
import java.util.Set;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@DeclareRoles({
        ComptoirRoles.ANONYMOUS,
        ComptoirRoles.ACTIVE,
        ComptoirRoles.EMPLOYEE,
        ComptoirRoles.MAINTENANCE,
})
@ServletSecurity(@HttpConstraint(
        transportGuarantee = ServletSecurity.TransportGuarantee.CONFIDENTIAL
))
@ApplicationPath("/comptoir-ws")
public class ComptoirWsApplication extends ComptoirWsApplicationApi {


    @Override
    public Set<Object> getSingletons() {
        return Set.of(

        );
    }

    @Override
    public Set<Class<?>> getClasses() {
        // FIXME: autoscanning tries to instanciate interfaces
        return Set.of(
                AccountingEntryResource.class,
                AccountResource.class,
                AttributeDefinitionResource.class,
                AttributeValueResource.class,
                AttributeValue.class,
                AuthResource.class,
                BalanceResource.class,
                CompanyResource.class,
                CountryResource.class,
                CustomerResource.class,
                EmployeeResource.class,
                ImportResource.class,
                InvoiceResource.class,
                ItemResource.class,
                ItemVariantResource.class,
                ItemVariantSaleResource.class,
                ItemVariantStockResource.class,
                MoneyPileResource.class,
                PictureResource.class,
                PosResource.class,
                RegistrationResource.class,
                SaleResource.class,
                StockResource.class,

                WebNotificationsResource.class,

                ComptoirWsParamConverterProvider.class,
                CrossOriginResourceSharingResponseFilter.class,
                CrossOriginResourceSharingPreflightRequestFilter.class,
                WsExceptionMapper.class,
                UnauthenticatedExceptionMapper.class
        );
    }
}
