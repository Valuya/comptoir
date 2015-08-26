package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.ExternalEntity;
import be.valuya.comptoir.model.commercial.ExternalReference;
import be.valuya.comptoir.model.commercial.ExternalReferenceType;
import be.valuya.comptoir.model.commercial.ExternalReference_;
import be.valuya.comptoir.model.common.WithId;
import be.valuya.comptoir.model.company.Company;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class ImportService {

    @PersistenceContext
    private EntityManager entityManager;

    public void doImport(Company company, String backendName, PrestashopImportParams prestashopImportParams) {
        PrestashopImportUtil prestashopImportUtil = new PrestashopImportUtil(company, prestashopImportParams);
        prestashopImportUtil.importAll();

        long attributeDefinitionCount = prestashopImportUtil.getAttributeDefinitionStore()
                .getBackendEntityStream()
                .map(externalEntity -> this.load(company, backendName, externalEntity))
                .count();
        long attributeValueCount = prestashopImportUtil.getAttributeValueStore()
                .getBackendEntityStream()
                .map(externalEntity -> this.load(company, backendName, externalEntity))
                .count();
        long itemVariantCount = prestashopImportUtil.getItemVariantStore()
                .getBackendEntityStream()
                .map(externalEntity -> this.load(company, backendName, externalEntity))
                .count();

    }

    private <T extends WithId> T load(Company company, String backendName, ExternalEntity<Long, T> externalEntity) {
        T attributeValue = externalEntity.getValue();

        ExternalReference externalReference = loadExternalReference(company, backendName, externalEntity);

        T managedAttributeValue = entityManager.merge(attributeValue);
        Long localId = externalReference.getLocalId();
        attributeValue.setId(localId);

        return managedAttributeValue;
    }

    private <T extends WithId> ExternalReference loadExternalReference(Company company, String backendName, ExternalEntity<Long, T> externalEntity) {
        T entity = externalEntity.getValue();
        Long externalId = externalEntity.getExternalId();
        String externalIdStr = Long.toString(externalId);

        ExternalReference externalReference = findExternalReferenceOptional(company, backendName, externalIdStr, ExternalReferenceType.ATTRIBUTE_VALUE)
                .orElseGet(() -> createExternalReference(entity, company, backendName, externalIdStr));
        return externalReference;
    }

    private <T extends WithId> ExternalReference createExternalReference(T entity, Company company, String backendName, String externalIdStr) {
        entityManager.persist(entity);
        Long localId = entity.getId();
        ExternalReference externalReference = new ExternalReference(company, backendName, ExternalReferenceType.ATTRIBUTE_VALUE, externalIdStr, localId);
        entityManager.persist(externalReference);

        return externalReference;
    }

    private Optional<ExternalReference> findExternalReferenceOptional(Company company, String backendName, String externalIdStr, ExternalReferenceType externalReferenceType) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ExternalReference> query = criteriaBuilder.createQuery(ExternalReference.class);
        Root<ExternalReference> externalReferenceRoot = query.from(ExternalReference.class);

        Path<Company> companyPath = externalReferenceRoot.get(ExternalReference_.company);
        Predicate companyPredicate = criteriaBuilder.equal(companyPath, company);

        Path<String> backendNamePath = externalReferenceRoot.get(ExternalReference_.backendName);
        Predicate backendNamePredicate = criteriaBuilder.equal(backendNamePath, backendName);

        Path<ExternalReferenceType> typePath = externalReferenceRoot.get(ExternalReference_.type);
        Predicate typePredicate = criteriaBuilder.equal(typePath, externalReferenceType);

        Path<String> externalIdPath = externalReferenceRoot.get(ExternalReference_.externalId);
        Predicate externalIdPredicate = criteriaBuilder.equal(externalIdPath, externalIdStr);

        query.where(companyPredicate, backendNamePredicate, typePredicate, externalIdPredicate);

        TypedQuery<ExternalReference> typedQuery = entityManager.createQuery(query);
        try {
            ExternalReference externalReference = typedQuery.getSingleResult();
            return Optional.of(externalReference);
        } catch (NoResultException noResultException) {
            return Optional.empty();
        }
    }

}
