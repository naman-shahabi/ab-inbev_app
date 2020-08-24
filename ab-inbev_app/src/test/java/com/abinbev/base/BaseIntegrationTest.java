package com.abinbev.base;

import com.abinbev.config.database.DatabaseConfiguration;
import com.abinbev.services.ProductServiceImpl;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@SpringBootTest(classes = {BaseIntegrationTest.IntegrationTestConfiguration.class})
@DatabaseTearDown("classpath:product/reset.xml")
@DbUnitConfiguration(databaseConnection = "databaseDataSourceConnectionFactoryBean")
@TestExecutionListeners(value = {
        TransactionalTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class
}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class BaseIntegrationTest extends BaseTest {

    @Autowired
    protected ProductServiceImpl productService;

    public static final PostgreSQLContainer postgreSQLContainer = AbInbevPostgresqlContainer.getInstance();

    @EnableJpaAuditing
    @Import(value = {
            DatabaseConfiguration.class,
    })
    @ComponentScan(basePackages = {
        "com.abinbev.services",
        "com.abinbev.controllers",
        "com.abinbev.exceptions"
    })
    public static class IntegrationTestConfiguration {
        @Bean
        public PostgresqlDataTypeFactory postgresqlDataTypeFactory() {
            return new PostgresqlDataTypeFactory();
        }

        @Bean
        public DatabaseConfigBean databaseConfigBean(PostgresqlDataTypeFactory postgresqlDataTypeFactory) {
            final DatabaseConfigBean databaseConfigBean = new DatabaseConfigBean();
            databaseConfigBean.setDatatypeFactory(postgresqlDataTypeFactory);
            return databaseConfigBean;
        }

        @Bean
        public DatabaseDataSourceConnectionFactoryBean databaseDataSourceConnectionFactoryBean(DatabaseConfigBean databaseConfigBean, @Qualifier(DatabaseConfiguration.DATA_SOURCE_BEAN) DataSource productDataSource) {
            final DatabaseDataSourceConnectionFactoryBean databaseDataSourceConnectionFactoryBean = new DatabaseDataSourceConnectionFactoryBean();
            databaseDataSourceConnectionFactoryBean.setDatabaseConfig(databaseConfigBean);
            databaseDataSourceConnectionFactoryBean.setDataSource(productDataSource);
            return databaseDataSourceConnectionFactoryBean;
        }
    }
}
