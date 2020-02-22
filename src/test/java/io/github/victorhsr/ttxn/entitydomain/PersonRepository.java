package io.github.victorhsr.ttxn.entitydomain;

import io.github.victorhsr.ttxn.TenantTransaction;
import io.github.victorhsr.ttxn.TenantWrapper;
import io.github.victorhsr.ttxn.TenantWrapperIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class PersonRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonRepository.class);

    @PersistenceContext
    private EntityManager entityManager;

    @TenantTransaction
    public void persistPerson(final @TenantWrapperIdentifier TenantWrapper tenantIdentifier, final Person person) {
        this.entityManager.persist(person);
        LOGGER.info("simpleEMPersist DONE");
    }

    @TenantTransaction(propagation = Propagation.MANDATORY)
    public void runMandatoryPropagation(final @TenantWrapperIdentifier TenantWrapper tenantIdentifier){
        LOGGER.info("runRequireNewIsolation DONE");
    }

    @TenantTransaction
    public Optional<Person> findByName(final @TenantWrapperIdentifier TenantWrapper tenantIdentifier, final String personName) {
        final String queryString = "SELECT person FROM Person AS person WHERE person.fullName = :name";
        final TypedQuery<Person> typedQuery = this.entityManager.createQuery(queryString, Person.class)
                .setParameter("name", personName);

        try{
            final Person person = typedQuery.getSingleResult();
            LOGGER.info("findByName retrieved {}", person);
            return Optional.of(person);
        }catch (NoResultException ex){
            LOGGER.info("findByName notFound {}", personName);
            return Optional.empty();
        }
    }
}
