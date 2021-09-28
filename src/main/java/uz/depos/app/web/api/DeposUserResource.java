package uz.depos.app.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.undertow.util.BadRequestException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.depos.app.domain.User;
import uz.depos.app.domain.enums.UserSearchFieldEnum;
import uz.depos.app.repository.UserRepository;
import uz.depos.app.service.UserService;
import uz.depos.app.service.UsernameAlreadyUsedException;
import uz.depos.app.service.dto.ApiResponse;
import uz.depos.app.service.dto.DeposUserDTO;
import uz.depos.app.service.dto.DeposUserNameDTO;
import uz.depos.app.web.rest.AccountResource;
import uz.depos.app.web.rest.UserResource;
import uz.depos.app.web.rest.errors.*;

@RestController
@RequestMapping("/api/moder/user")
@Api(tags = "User")
public class DeposUserResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "fullName", "email", "phoneNumber", "groupEnum", "inn", "activated")
    );

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    final UserService userService;
    final AccountResource accountResource;
    final UserRepository userRepository;

    public DeposUserResource(UserService userService, AccountResource accountResource, UserRepository userRepository) {
        this.userService = userService;
        this.accountResource = accountResource;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /moder/generate-login}  : Generate login.
     * <p>
     * Checking is not Empty @param PINFL, to already used PINFL and check is Empty login,
     *
     * @param pinfl the user to create.
     * @return the String login with status {@code 201 (Generated)} or with status {@code 400 (Bad Request)} if the login or PINFL is already in use.
     * @throws PinflAlreadyUsedException {@code 400 (Bad Request)} if the PINFL is already in use.
     */
    @PostMapping("/generate-login/{pinfl:[a-zA-Z0-9]{14}}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Generate login", notes = "This method is generate new login from present PINFL.")
    public ResponseEntity<String> generateLogin(@ApiParam(value = "Length must be 14 characters!") @PathVariable String pinfl) {
        log.debug("REST request to generate login from PINFL : {}", pinfl);

        if (pinfl != null) {
            Optional<User> existingUserByPinfl = userRepository.findOneByPinfl(pinfl);
            boolean present = existingUserByPinfl.isPresent();
            if (present) throw new PinflAlreadyUsedException();
        } else throw new NullPointerException("PINFL must not be null!");

        String login = userService.generateLogin(pinfl);
        return ResponseEntity.ok(login);
    }

    /**
     * {@code POST  /moder/create} : register the Depositary user.
     *
     * @param deposUserDTO the managed user View Model.
     * @return DeposUserDTO with status {@code 201 (Created)}
     * @throws InvalidPasswordException     {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException    {@code 400 (Bad Request)} if the email is already used.
     * @throws UsernameAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Create user", notes = "This method creates a new user")
    public ResponseEntity<DeposUserDTO> createUser(@Valid @RequestBody DeposUserDTO deposUserDTO)
        throws URISyntaxException, BadRequestException {
        log.debug("REST request to save Depos-User : {}", deposUserDTO);

        DeposUserDTO deposUser = userService.createDeposUser(deposUserDTO);
        return ResponseEntity
            .created(new URI("/api/moder/users/" + deposUser.getLogin()))
            .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", deposUser.getLogin()))
            .body(deposUser);
    }

    /**
     * {@code GET /moder/users/:ID} : get the "login" user.
     *
     * @param id the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "ID" user, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Get user", notes = "This method to get one user by ID")
    public ResponseEntity<DeposUserDTO> getUser(@PathVariable Long id) {
        log.debug("REST request to get User : {}", id);
        return ResponseUtil.wrapOrNotFound(userService.getUserWithAuthoritiesById(id).map(DeposUserDTO::new));
    }

    /**
     * {@code GET /moder/user} : get all users with all the details - calling this are only allowed for the administrators.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Get users", notes = "This method to get all users in pageable")
    public ResponseEntity<List<DeposUserDTO>> getAllUsers(Pageable pageable) {
        log.debug("REST request to get all User for an admin");

        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<DeposUserDTO> page = userService.getAllManagedDeposUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET /moder/user/names} : get all user's name.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all user's name.
     */
    @GetMapping("/names")
    @ApiOperation(value = "Get name and pinfl", notes = "This method to get all user's name and pinfl for search fields")
    public ResponseEntity<List<DeposUserNameDTO>> getAllUsersNameAndPinfl() {
        log.debug("REST request to get all User's name and pinfl.");

        final List<DeposUserNameDTO> userNameDTOs = userService.getAllManagedDeposUsersName();
        return new ResponseEntity<>(userNameDTOs, HttpStatus.OK);
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    /**
     * {@code PUT /moder/user} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException       {@code 400 (Bad Request)} if the email is already in use.
     * @throws UsernameAlreadyUsedException    {@code 400 (Bad Request)} if the login is already in use.
     * @throws InnAlreadyUsedException         {@code 400 (Bad Request)} if the inn is already in use.
     * @throws PhoneNumberAlreadyUsedException {@code 400 (Bad Request)} if the phone-number is already in use.
     * @throws PassportAlreadyUsedException    {@code 400 (Bad Request)} if the passport is already in use.
     * @throws PinflAlreadyUsedException       {@code 400 (Bad Request)} if the pinfl is already in use.
     */
    @PutMapping
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Update user", notes = "This method to update user fields")
    public ResponseEntity<DeposUserDTO> editUser(@Valid @RequestBody DeposUserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);

        Optional<User> existingByEmail = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (
            StringUtils.isNotEmpty(userDTO.getEmail()) &&
            existingByEmail.isPresent() &&
            (!existingByEmail.get().getId().equals(userDTO.getId()))
        ) throw new EmailAlreadyUsedException();

        Optional<User> existingByLogin = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (
            StringUtils.isNotEmpty(userDTO.getLogin()) &&
            existingByLogin.isPresent() &&
            (!existingByLogin.get().getId().equals(userDTO.getId()))
        ) throw new LoginAlreadyUsedException();

        Optional<User> existingByInn = userRepository.findOneByInn(userDTO.getInn());
        if (
            StringUtils.isNotEmpty(userDTO.getInn()) && existingByInn.isPresent() && (!existingByInn.get().getId().equals(userDTO.getId()))
        ) throw new InnAlreadyUsedException();

        Optional<User> existingByPassport = userRepository.findOneByPassport(userDTO.getPassport());
        if (
            StringUtils.isNotEmpty(userDTO.getPassport()) &&
            existingByPassport.isPresent() &&
            (!existingByPassport.get().getId().equals(userDTO.getId()))
        ) throw new PassportAlreadyUsedException();

        Optional<User> oneByPinfl = userRepository.findOneByPinfl(userDTO.getPinfl());
        if (
            StringUtils.isNotEmpty(userDTO.getPinfl()) && oneByPinfl.isPresent() && (!oneByPinfl.get().getId().equals(userDTO.getId()))
        ) throw new PinflAlreadyUsedException();

        Optional<User> oneByPhoneNumber = userRepository.findOneByPhoneNumber(userDTO.getPhoneNumber());
        if (
            StringUtils.isNotEmpty(userDTO.getPhoneNumber()) &&
            oneByPhoneNumber.isPresent() &&
            (!oneByPhoneNumber.get().getId().equals(userDTO.getId()))
        ) throw new PhoneNumberAlreadyUsedException();

        Optional<DeposUserDTO> updatedUser = userService.editUser(userDTO);

        return ResponseUtil.wrapOrNotFound(
            updatedUser,
            HeaderUtil.createAlert(applicationName, "userManagement.edited", userDTO.getLogin())
        );
    }

    /**
     * {@code DELETE /moder/user/:id} : delete the "id" User.
     *
     * @param id the id of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 202 (ACCEPTED)}.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @ApiOperation(value = "Delete user", notes = "This method to delete user by id")
    public ResponseEntity<ApiResponse> removeUser(@PathVariable Long id) {
        log.debug("REST request to delete User: {}", id);
        ApiResponse response = userService.removeUser(id);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping("/search")
    @ApiOperation(value = "Search users", notes = "This method to search users by name or pinfl containing")
    public ResponseEntity<List<DeposUserNameDTO>> searchUsers(@RequestParam(required = false) String searchValue) {
        log.debug("REST request to search Users name of : {}", searchValue);
        try {
            List<DeposUserNameDTO> userNameDTOs = userService.searchUserByName(searchValue);
            return new ResponseEntity<>(userNameDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Filters for header table User.
     *
     * @param field    - Column in the table (entity).
     * @param value    - fragment of word to search by him.
     * @param pageable - params for pageable.
     * @return - List of DeposUserDTO.
     */
    @GetMapping("/filter")
    @ApiOperation(value = "Filter user", notes = "This method to filter users by field and search-value in pageable form.")
    public ResponseEntity<List<DeposUserDTO>> filterUser(
        @RequestParam UserSearchFieldEnum field,
        @RequestParam String value,
        Pageable pageable
    ) {
        log.debug("REST request to filter Users field : {}", field);

        final Page<DeposUserDTO> page = userService.filterUsers(field, value, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
