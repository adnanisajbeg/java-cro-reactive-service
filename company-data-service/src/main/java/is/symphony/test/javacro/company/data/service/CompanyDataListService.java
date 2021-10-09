package is.symphony.test.javacro.company.data.service;

import is.symphony.test.javacro.company.data.generated.api.model.Company;
import is.symphony.test.javacro.company.data.model.CompanyDB;
import is.symphony.test.javacro.company.data.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class CompanyDataListService {

    private final CompanyRepository companyRepository;

    public CompanyDataListService(final CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Flux<Company> getAllCompanies() {
        return companyRepository.findAll()
                .map(CompanyDB::toDTO);
    }

    public Mono<Void> saveAll(Flux<Company> newCompanies) {
        return companyRepository.saveAll(newCompanies.map(CompanyDB::new))
                .then();
    }
}
