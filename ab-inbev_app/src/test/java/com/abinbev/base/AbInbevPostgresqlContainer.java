package com.abinbev.base;

import org.testcontainers.containers.PostgreSQLContainer;

public class AbInbevPostgresqlContainer extends PostgreSQLContainer<AbInbevPostgresqlContainer> {
    private static final String IMAGE_VERSION = "postgres:12.0";
    private static AbInbevPostgresqlContainer container;

    private AbInbevPostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static AbInbevPostgresqlContainer getInstance() {
        if (container == null) {
            container = new AbInbevPostgresqlContainer();
        }
        container.start();
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DATABASE_URL", container.getJdbcUrl());
        System.setProperty("DATABASE_USERNAME", container.getUsername());
        System.setProperty("DATABASE_PASSWORD", container.getPassword());
        System.setProperty("DATABASE_DRIVER_CLASS_NAME", container.getDriverClassName());
    }
}
