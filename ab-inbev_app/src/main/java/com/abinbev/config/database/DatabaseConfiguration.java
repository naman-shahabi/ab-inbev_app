package com.abinbev.config.database;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
@EntityScan(basePackages = {"com.abinbev.model"})
@EnableJpaRepositories(basePackages = "com.abinbev.repositories")
@EnableConfigurationProperties({DatasourceConfigProperties.class})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class DatabaseConfiguration {
    public static final String DATA_SOURCE_BEAN = "product-datasource";

    @Bean(name = DATA_SOURCE_BEAN)
    public DataSource gopManagementDataSource(DatasourceConfigProperties datasourceConfigProperties) {
        final HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername(datasourceConfigProperties.getUsername());
        hikariDataSource.setPassword(datasourceConfigProperties.getPassword());
        hikariDataSource.setJdbcUrl(datasourceConfigProperties.getUrl());
        hikariDataSource.setSchema(datasourceConfigProperties.getDefaultSchema());
        hikariDataSource.setDriverClassName(datasourceConfigProperties.getDriverClassName());
        hikariDataSource.setConnectionTimeout(datasourceConfigProperties.getConnectionTimeout());
        hikariDataSource.setMinimumIdle(datasourceConfigProperties.getMinimumIdle());
        hikariDataSource.setMaximumPoolSize(datasourceConfigProperties.getMaximumPoolSize());
        hikariDataSource.setIdleTimeout(datasourceConfigProperties.getIdleTimeout());
        hikariDataSource.setPoolName("product_management");
        return hikariDataSource;
    }

    @Bean
    public SpringLiquibase springLiquibase(@Autowired DatasourceConfigProperties datasourceConfigProperties, @Qualifier(DatabaseConfiguration.DATA_SOURCE_BEAN) DataSource dataSource, @Value("${abinbev.liquibase.changelogPath}") String changelogPath) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (ResultSet resultSet = connection.prepareStatement(String.format("SELECT schema_name from information_schema.schemata WHERE schema_name = '%s'", datasourceConfigProperties.getDefaultSchema())).executeQuery()) {
                if (!resultSet.next()) {
                    try(PreparedStatement statement = connection.prepareStatement(String.format("CREATE SCHEMA %s;", datasourceConfigProperties.getDefaultSchema()))){
                        statement.execute();
                    }
                }
            }
        }
        final SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setChangeLog(changelogPath);
        springLiquibase.setDataSource(dataSource);
        return springLiquibase;
    }
}
