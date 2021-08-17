package uz.depos.app.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.depos.app.repository.CityRepository;
import uz.depos.app.service.CityService;
import uz.depos.app.service.dto.ApiResponse;
import uz.depos.app.service.dto.CityDTO;

@RestController
@Api(tags = "City")
@RequestMapping(path = "/api/city")
public class CityResource {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(CityResource.class);

    final CityService cityService;
    final CityRepository cityRepository;

    public CityResource(CityService cityService, CityRepository cityRepository) {
        this.cityService = cityService;
        this.cityRepository = cityRepository;
    }

    /**
     * {@code POST  /city/create} : Create city.
     *
     * @param cityDTO the managed city View Model.
     * @return CityDTO with status {@code 201 (Created)}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Create city", notes = "This method creates a new city")
    public ResponseEntity<CityDTO> createCity(@RequestBody CityDTO cityDTO) throws URISyntaxException {
        log.debug("REST request to create City : {}", cityDTO);

        CityDTO city = cityService.createCity(cityDTO);
        return ResponseEntity
            .created(new URI("/api/moder/city/"))
            .headers(HeaderUtil.createAlert(applicationName, "cityManagement.created", cityDTO.getNameUz()))
            .body(city);
    }

    /**
     * {@code PUT /city/update} : Updates an existing City.
     *
     * @param cityDTO the city to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated city.
     */
    @PutMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Update city", notes = "This method to update city fields")
    public ResponseEntity<CityDTO> updateCity(@Valid @RequestBody CityDTO cityDTO) {
        log.debug("REST request to update City : {}", cityDTO);

        Optional<CityDTO> updateCity = cityService.updateCity(cityDTO);
        return ResponseUtil.wrapOrNotFound(
            updateCity,
            HeaderUtil.createAlert(applicationName, "cityManagement.updated", cityDTO.getNameUz())
        );
    }

    /**
     * {@code GET /city/:id} : get the "id" city.
     *
     * @param id the id of the city to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the city, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Get city", notes = "This method to get one of city by ID")
    public HttpEntity<CityDTO> getCity(@PathVariable Integer id) {
        log.debug("REST request to get City : {}", id);

        return ResponseUtil.wrapOrNotFound(cityService.getCityById(id).map(CityDTO::new));
    }

    /**
     * {@code GET /city} : get all cities with all the details - calling this are only allowed for the administrators.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all cities.
     */
    @GetMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Get cities", notes = "This method to get cities of pageable")
    public ResponseEntity<List<CityDTO>> getAllCities(Pageable pageable) {
        log.debug("REST request to get all City for an admin");

        final Page<CityDTO> page = cityService.getAllManagedCities(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code DELETE /city/:id} : delete the "id" city.
     *
     * @param id the id of the city to delete.
     * @return the {@link ApiResponse} with status {@code 200 (OK)} and success
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete city", notes = "This method to delete one of city by ID")
    public HttpEntity<ApiResponse> deleteCity(@PathVariable Integer id) {
        ApiResponse response = cityService.deleteCity(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);
    }
}
