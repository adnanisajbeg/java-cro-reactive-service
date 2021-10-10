package is.symphony.test.javacro.stats.emitter.service.controller;


import is.symphony.test.javacro.stats.emitter.service.service.CompanyStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
public class CompanyStatsController {
    private final CompanyStatsService companyStatsService;

    public CompanyStatsController(CompanyStatsService companyStatsService) {
        this.companyStatsService = companyStatsService;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/stats/company/count/all",
            produces = { "application/stream+json" }
    )
    public Flux<Long> getCompanyStatsPerCountryAndCity(@RequestParam(name = "country") String country,
                                                                             @RequestParam(name = "city") String city) {
        return companyStatsService.countByCountryAndCity(country, city);
    }
}
