package is.symphony.test.javacro.stats.emitter.service.service;

import is.symphony.test.javacro.stats.emitter.service.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class CompanyStatsService {
    private final CompanyRepository companyRepository;

    public CompanyStatsService(final CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Flux<Long> countAll() {
   /*     return Flux.<Long>generate(sink -> sink.next(1L))
                .delayElements(Duration.ofSeconds(5L))
                .flatMap(e -> companyRepository.count())
                .log(); */

        return Flux.interval(Duration.ofSeconds(5L))
                .flatMap(e -> companyRepository.count())
                .log();
    }

    public Flux<Long> countByCountryAndCity(String country, String city) {
        return Flux.interval(Duration.ofSeconds(5L))
                .flatMap(e -> companyRepository.countByCountryAndCity(country, city))
                .log();
    }
}
