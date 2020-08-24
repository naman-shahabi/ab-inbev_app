package com.abinbev.config.database;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(
        prefix = "abinbev.datasource"
)
public class DatasourceConfigProperties extends DataSourceProperties {
    private String defaultSchema;
    private String driverClassName;
    private Long connectionTimeout;
    private Integer minimumIdle;
    private Integer maximumPoolSize;
    private Long idleTimeout;
}
