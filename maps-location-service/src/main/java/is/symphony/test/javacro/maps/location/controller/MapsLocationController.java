package is.symphony.test.javacro.maps.location.controller;

import io.swagger.annotations.ApiParam;
import is.symphony.test.javacro.maps.location.generated.api.ApiUtil;
import is.symphony.test.javacro.maps.location.generated.api.MapLocationApi;
import is.symphony.test.javacro.maps.location.generated.api.MapLocationListApi;
import is.symphony.test.javacro.maps.location.generated.api.model.MapsLocationData;
import is.symphony.test.javacro.maps.location.service.MapsLocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
public class MapsLocationController implements MapLocationApi, MapLocationListApi {
    private final MapsLocationService mapsLocationService;

    public MapsLocationController(MapsLocationService mapsLocationService) {
        this.mapsLocationService = mapsLocationService;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/maps/get/image",
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    public Mono<ResponseEntity<byte[]>> getMap(@ApiParam(value = "Map data" ,required=true )
                                                    @Valid @RequestBody Mono<MapsLocationData> mapsLocationData,
                                                @ApiIgnore final ServerWebExchange exchange) {
        return mapsLocationService.getLocation(mapsLocationData)
                .map(ResponseEntity::ok);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/maps/get/image/list",
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    public Mono<ResponseEntity<Flux<byte[]>>> getMapLocationList(@ApiParam(value = "Map location data list" ,required=true )
                                                                               @Valid @RequestBody Flux<MapsLocationData> mapsLocationData,
                                                                           @ApiIgnore final ServerWebExchange exchange) {
        return Mono.just(
                    ResponseEntity.ok(mapsLocationService.getLocationList(mapsLocationData)
                )
        );

    }
}
