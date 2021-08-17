package uz.depos.app.service;

import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.depos.app.domain.City;
import uz.depos.app.domain.Company;
import uz.depos.app.repository.CityRepository;
import uz.depos.app.repository.CompanyRepository;
import uz.depos.app.service.dto.ApiResponse;
import uz.depos.app.service.dto.CityDTO;
import uz.depos.app.service.dto.CompanyDTO;
import uz.depos.app.service.mapper.CompanyMapper;

@Service
@Transactional
public class CityService {

    private final Logger log = LoggerFactory.getLogger(CityService.class);

    private final CityRepository cityRepository;
    private final CompanyMapper companyMapper;

    public CityService(CityRepository cityRepository, CompanyMapper companyMapper) {
        this.cityRepository = cityRepository;
        this.companyMapper = companyMapper;
    }

    /**
     * Create new city, and return it.
     *
     * @param cityDTO city to create.
     * @return created city.
     */
    public CityDTO createCity(CityDTO cityDTO) {
        City city = cityDTO.getId() == null || cityDTO.getId() == 0
            ? new City()
            : cityRepository.findById(cityDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("getCity"));

        if (StringUtils.isNoneBlank(cityDTO.getNameUz())) city.setNameUz(cityDTO.getNameUz());
        if (StringUtils.isNoneBlank(cityDTO.getNameRu())) city.setNameRu(cityDTO.getNameRu());
        if (StringUtils.isNoneBlank(cityDTO.getNameEn())) city.setNameEn(cityDTO.getNameEn());
        city.setExtraInfo(cityDTO.getExtraInfo());

        City savedCity = cityRepository.save(city);
        CityDTO toCityDTO = companyMapper.cityToCityDTO(savedCity);
        log.debug("Created Information for City: {}", toCityDTO);
        return toCityDTO;
    }

    /**
     * Edit all information for a specific city, and return the modified city.
     *
     * @param cityDTO city to edit.
     * @return edited city.
     */
    public Optional<CityDTO> updateCity(CityDTO cityDTO) {
        return Optional
            .of(cityRepository.findById(cityDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                city -> {
                    // NameUz
                    if (StringUtils.isNoneBlank(cityDTO.getNameUz())) city.setNameUz(cityDTO.getNameUz().toLowerCase());
                    // NameRu
                    if (StringUtils.isNoneBlank(cityDTO.getNameRu())) city.setNameRu(cityDTO.getNameRu().toLowerCase());
                    // NameEn
                    if (StringUtils.isNoneBlank(cityDTO.getNameEn())) city.setNameEn(cityDTO.getNameEn().toLowerCase());
                    city.setExtraInfo(cityDTO.getExtraInfo());

                    log.debug("Changed Information for City: {}", city);
                    return city;
                }
            )
            .map(CityDTO::new);
    }

    /**
     * Get one city by the id, and return it.
     *
     * @param id city to get.
     * @return city.
     */
    @Transactional(readOnly = true)
    public Optional<City> getCityById(Integer id) {
        return cityRepository.findById(id);
    }

    /**
     * Get all cities in pageable form for the moderator.
     *
     * @param pageable cities to get all.
     * @return pageable cities.
     */
    @Transactional(readOnly = true)
    public Page<CityDTO> getAllManagedCities(Pageable pageable) {
        return cityRepository.findAll(pageable).map(CityDTO::new);
    }

    /**
     * Delete city from data base.
     *
     * @param id city to delete.
     * @return ApiResponse.
     */
    public ApiResponse deleteCity(Integer id) {
        City city = cityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getCity"));
        try {
            cityRepository.delete(city);
            return new ApiResponse(city.getNameUz() + " Удален!", true);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), false);
        }
    }
}
