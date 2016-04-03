package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.AttributeDefinition;
import be.valuya.comptoir.model.commercial.AttributeDefinition_;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.lang.LocaleText_;
import be.valuya.comptoir.util.pagination.AttributeDefinitionColumn;

import javax.persistence.criteria.*;
import java.util.Locale;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class AttributeDefinitionColumnPersistenceUtil {

    public static Expression<?> getPath(From<?, AttributeDefinition> definitionFrom, AttributeDefinitionColumn column, Locale locale, CriteriaBuilder builder) {
        switch (column) {
            case NAME:
                Join<AttributeDefinition, LocaleText> definitionLocaleTextJoin = definitionFrom.join(AttributeDefinition_.name);

                MapJoin<LocaleText, Locale, String> localeTextLocaleStringMapJoin = definitionLocaleTextJoin.join(LocaleText_.localeTextMap);
                Path<Locale> localePath = localeTextLocaleStringMapJoin.key();
                Predicate localePredicate = builder.equal(localePath, locale);
                localeTextLocaleStringMapJoin.on(localePredicate);

                Path<String> nameValue = localeTextLocaleStringMapJoin.value();

                Predicate noNamePredicate = builder.isNull(definitionLocaleTextJoin);
                CriteriaBuilder.SimpleCase<Boolean, String> nameSelectCase = builder.selectCase(noNamePredicate);
                Expression<String> nameResult = nameSelectCase
                        .when(true, builder.nullLiteral(String.class))
                        .otherwise(nameValue);
                return nameResult;
            default:
                throw new AssertionError(column.name());
        }
    }

}
