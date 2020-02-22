package io.github.victorhsr.ttxn.handler;

import io.github.victorhsr.ttxn.entitydomain.Person;
import io.github.victorhsr.ttxn.entitydomain.PersonRepository;
import io.github.victorhsr.ttxn.infraestructure.database.PostgresTestContainer;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.IllegalTransactionStateException;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Import(TenantTransactionBeansProvider.class)
@Sql(scripts = {"/database/postgres/init_schema.sql"})
public class PostgresqlChangeSchemaTtxnHandlerTest {

    @Autowired
    private PersonRepository personRepository;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();

    private static final String PUBLIC_SCHEMA = "public";
    private static final String MIDDLE_SCHOOL = "middle_school";
    private static final String HIGH_SCHOOL = "high_school";

    @Test
    public void simpleEMPersist() {
        final String personName = "Victor Hugo";
        final String schemaToPersist = MIDDLE_SCHOOL;

        this.personRepository.persistPerson(() -> schemaToPersist, new Person(personName));

        final Optional<Person> personFromWrongSchemaOpt = this.personRepository.findByName(() -> PUBLIC_SCHEMA, personName);
        Assert.assertFalse(personFromWrongSchemaOpt.isPresent());

        final Optional<Person> personFromWrong2SchemaOpt = this.personRepository.findByName(() -> HIGH_SCHOOL, personName);
        Assert.assertFalse(personFromWrong2SchemaOpt.isPresent());

        final Optional<Person> personOpt = this.personRepository.findByName(() -> schemaToPersist, personName);
        Assert.assertTrue(personOpt.isPresent());
    }

    @Test(expected = IllegalTransactionStateException.class)
    public void checkPropagation() {
        this.personRepository.runMandatoryPropagation(() -> PUBLIC_SCHEMA);
    }

}
