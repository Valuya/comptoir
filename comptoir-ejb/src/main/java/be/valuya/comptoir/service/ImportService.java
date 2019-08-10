package be.valuya.comptoir.service;

import be.valuya.comptoir.model.commercial.ExternalEntity;
import be.valuya.comptoir.model.commercial.ExternalReference;
import be.valuya.comptoir.model.commercial.ExternalReferenceType;
import be.valuya.comptoir.model.commercial.ExternalReference_;
import be.valuya.comptoir.model.common.WithId;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.persistence.util.PrestashopImportUtil;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ImportService {

    @Inject
    @ConfigProperty(name = "be.valuya.comptoir.import.timeout.minute", defaultValue = "10")
    private int timeoutMinnute;

    @PersistenceContext
    private EntityManager entityManager;
    @Resource
    private UserTransaction userTransaction;

    public ImportSummary doImport(Company company, String backendName, PrestashopImportParams prestashopImportParams) {
        int timoutSeconds = (int) Duration.ofMinutes(this.timeoutMinnute)
                .get(ChronoUnit.SECONDS);
        try {
            userTransaction.setTransactionTimeout(timoutSeconds);
            userTransaction.begin();

            ImportSummary importSummary = doImportItems(company, backendName, prestashopImportParams);
            userTransaction.commit();
            return importSummary;
        } catch (NotSupportedException | SystemException e) {
            throw new RuntimeException(e);
        } catch (HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
            throw new RuntimeException(e);
        }
    }

    private ImportSummary doImportItems(Company company, String backendName, PrestashopImportParams prestashopImportParams) {
        PrestashopImportUtil prestashopImportUtil = new PrestashopImportUtil(company, prestashopImportParams);
        prestashopImportUtil.importAll();

        long attributeDefinitionCount = prestashopImportUtil.getAttributeDefinitionStore()
                .stream()
                .flatMap(externalEntity -> this.steamImportedEntity(company, backendName, externalEntity, ExternalReferenceType.ATTRIBUTE_DEFINITION))
                .count();
        entityManager.flush();
        long attributeValueCount = prestashopImportUtil.getAttributeValueStore()
                .stream()
                .flatMap(externalEntity -> this.steamImportedEntity(company, backendName, externalEntity, ExternalReferenceType.ATTRIBUTE_VALUE))
                .count();
        entityManager.flush();
        long itemVariantCount = prestashopImportUtil.getItemVariantStore()
                .stream()
                .flatMap(externalEntity -> this.steamImportedEntity(company, backendName, externalEntity, ExternalReferenceType.ITEM_VARIANT))
                .count();
        entityManager.flush();
        long itemCount = prestashopImportUtil.getItemStore()
                .stream()
                .flatMap(externalEntity -> this.steamImportedEntity(company, backendName, externalEntity, ExternalReferenceType.ITEM))
                .count();
        entityManager.flush();

        long defaultItemVariantCount = prestashopImportUtil.getDefaultItemVariants()
                .stream()
                .flatMap(e -> Stream.of(entityManager.merge(e)))
                .count();
        entityManager.flush();

        ImportSummary importSummary = new ImportSummary();
        importSummary.setAttributeDefinitionCount(attributeDefinitionCount);
        importSummary.setAttributeValueCount(attributeValueCount);
        importSummary.setItemCount(itemCount);
        importSummary.setItemVariantCount(itemVariantCount);
        importSummary.setDefaultItemVariantCount(defaultItemVariantCount);
        return importSummary;
    }

    private <T extends WithId> Stream<T> steamImportedEntity(Company company, String backendName, ExternalEntity<Long, T> externalEntity, ExternalReferenceType externalReferenceType) {
        T entity = externalEntity.getValue();

        ExternalReference externalReference = loadExternalReference(company, backendName, externalEntity, externalReferenceType);

        Long localId = externalReference.getLocalId();
        entity.setId(localId);
        T managedEntity = entityManager.merge(entity);
        Long id = managedEntity.getId();
        externalReference.setId(id);

        return Stream.of(managedEntity);
    }

    private <T extends WithId> ExternalReference loadExternalReference(Company company, String backendName, ExternalEntity<Long, T> externalEntity, ExternalReferenceType externalReferenceType) {
        T entity = externalEntity.getValue();
        Long externalId = externalEntity.getExternalId();
        String externalIdStr = Long.toString(externalId);

        ExternalReference externalReference = findExternalReferenceOptional(company, backendName, externalIdStr, externalReferenceType)
                .orElseGet(() -> createExternalReference(entity, company, backendName, externalReferenceType, externalIdStr));
        return externalReference;
    }

    private <T extends WithId> ExternalReference createExternalReference(T entity, Company company, String backendName, ExternalReferenceType externalReferenceType, String externalIdStr) {
        entityManager.persist(entity);
        Long localId = entity.getId();
        ExternalReference externalReference = new ExternalReference(company, backendName, externalReferenceType, externalIdStr, localId);
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
