package io.github.victorhsr.ttxn.infraestructure.database;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

    private static final String IMAGE_VERSION = "postgres:12.1";
    private static PostgresTestContainer container;

    private PostgresTestContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgresTestContainer getInstance() {
        if (container == null) {
            container = new PostgresTestContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();

        System.setProperty("DB_URL", this.getJdbcUrl());
        System.setProperty("DB_USERNAME", this.getUsername());
        System.setProperty("DB_PASSWORD", this.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}