package is.symphony.test.javacro.stats.emitter.service.repository;

import is.symphony.test.javacro.stats.emitter.service.model.CompanyDB;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CompanyRepository extends ReactiveCassandraRepository<CompanyDB, UUID> {
    @Query("SELECT COUNT(*) FROM " + CompanyDB.TABLE_NAME + " WHERE "
            + CompanyDB.COUNTRY + "=?0 " +
            "AND " + CompanyDB.CITY + "=?1")
    Mono<Long> countByCountryAndCity(String country, String city);

}
