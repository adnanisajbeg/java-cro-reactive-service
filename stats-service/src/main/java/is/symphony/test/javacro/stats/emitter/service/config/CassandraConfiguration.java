package is.symphony.test.javacro.stats.emitter.service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DataCenterReplication;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CassandraConfiguration extends AbstractCassandraConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraConfiguration.class);

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keySpace;

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    protected String getKeyspaceName() {
        return keySpace;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        LOGGER.info("setup keyspace specification...");
        return Arrays.asList(
                CreateKeyspaceSpecification.createKeyspace(keySpace)
                        .ifNotExists()
                        .withNetworkReplication(DataCenterReplication.of("datacenter1", 1L)));
    }
}
