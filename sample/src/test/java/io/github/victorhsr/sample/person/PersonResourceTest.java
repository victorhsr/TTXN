package io.github.victorhsr.sample.person;

import io.github.victorhsr.sample.SchemaData;
import io.github.victorhsr.sample.database.PostgresTestContainer;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

@RunWith(SpringRunner.class)
@Sql(scripts = {"/database/init_schema.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonResourceTest {

    @LocalServerPort
    private int localPort;

    @Autowired
    private PersonRepository personRepository;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonResourceTest.class);

    private final ThreadPoolTaskExecutor taskExecutor;
    private final Random random = new Random();
    private final String[] schemas = new String[]{SchemaData.PUBLIC_SCHEMA, SchemaData.MIDDLE_SCHOOL, SchemaData.HIGH_SCHOOL};

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private long publicScCount = 0;
    private long middleSchoolScCount = 0;
    private long highSchoolScCount = 0;

    public PersonResourceTest(){
        this.taskExecutor = new ThreadPoolTaskExecutor();
        this.taskExecutor.setCorePoolSize(100);
        this.taskExecutor.initialize();
    }

    @Test
    public void runMassivetest() throws ExecutionException, InterruptedException {
        LOGGER.info("Starting persistence {}", LocalDateTime.now());

        final List<CompletableFuture> futures = this.buildFutures(5000);
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[1])).get();

        LOGGER.info("Persistence done at {}", LocalDateTime.now());

        LOGGER.info("publicScCount {}", publicScCount);
        Assert.assertEquals(Long.valueOf(publicScCount), this.personRepository.getTotal(() -> SchemaData.PUBLIC_SCHEMA));

        LOGGER.info("middleSchoolScCount {}", middleSchoolScCount);
        Assert.assertEquals(Long.valueOf(middleSchoolScCount), this.personRepository.getTotal(() -> SchemaData.MIDDLE_SCHOOL));

        LOGGER.info("highSchoolScCount {}", highSchoolScCount);
        Assert.assertEquals(Long.valueOf(highSchoolScCount), this.personRepository.getTotal(() -> SchemaData.HIGH_SCHOOL));
    }

    private List<CompletableFuture> buildFutures(final int personQuantity){

        final List<CompletableFuture> completableFutures = new ArrayList<>();

        final Function<List<RegisterPersonDTO>, CompletableFuture> buildCompletableFuture = persons -> {
            final CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                persons.forEach(this::doPersistPerson);
            }, this.taskExecutor);
            return future;
        };

        final List<RegisterPersonDTO> personDTOS = this.generatePersons(personQuantity);

        int personsRemaning = personQuantity;
        final int splitQuantity = 100;

        while(personsRemaning > 0){

            final int quantityToSplit = personsRemaning > splitQuantity ? splitQuantity : personsRemaning;

            final List<RegisterPersonDTO> splitedPersons = personDTOS.subList(0, quantityToSplit);
            completableFutures.add(buildCompletableFuture.apply(splitedPersons));
            personsRemaning -= quantityToSplit;
        }

        return completableFutures;
    }

    private void doPersistPerson(final RegisterPersonDTO registerPersonDTO) {

        final String uri = String.format("http://localhost:%d/person", this.localPort);

        final HttpEntity<RegisterPersonDTO > request = new HttpEntity<>(registerPersonDTO);
        this.restTemplate.postForObject(uri, request, Void.class,new HashMap<>());
        this.countSchema(registerPersonDTO.getTenant());
    }

    private List<RegisterPersonDTO> generatePersons(final int quantity) {

        final List<RegisterPersonDTO> dtos = new ArrayList<>();

        for (int i = 0; i <= quantity; i++) {
            final RegisterPersonDTO registerPersonDTO = RegisterPersonDTO
                    .builder()
                    .tenant(this.getRandomSchema())
                    .fullName(String.format("Person Nº%d", i))
                    .build();
            dtos.add(registerPersonDTO);
        }
        return dtos;
    }

    private synchronized void countSchema(final String schema) {
        switch (schema) {
            case SchemaData.PUBLIC_SCHEMA: {
                this.publicScCount++;
            }
            break;
            case SchemaData.MIDDLE_SCHOOL: {
                this.middleSchoolScCount++;
            }
            break;
            case SchemaData.HIGH_SCHOOL: {
                this.highSchoolScCount++;
            }
        }
    }

    private String getRandomSchema() {
        return this.schemas[this.random.nextInt(this.schemas.length)];
    }

}
