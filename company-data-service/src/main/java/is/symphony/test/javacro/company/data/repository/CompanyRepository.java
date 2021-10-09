package is.symphony.test.javacro.company.data.repository;

import is.symphony.test.javacro.company.data.model.CompanyDB;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface CompanyRepository extends ReactiveCassandraRepository<CompanyDB, UUID> {
    @Query("SELECT * FROM " + CompanyDB.TABLE_NAME + " WHERE "
            + CompanyDB.COUNTRY + "=?0 " +
            "AND " + CompanyDB.CITY + "=?1")
    Flux<CompanyDB> findAllByCity(String country, String city);
}
