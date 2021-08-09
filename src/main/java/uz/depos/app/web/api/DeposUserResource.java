package uz.depos.app.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.depos.app.repository.UserRepository;
import uz.depos.app.service.UserService;
import uz.depos.app.service.dto.DeposUserDTO;
import uz.depos.app.service.dto.DeposUserLoginDTO;
import uz.depos.app.service.dto.LoginDTO;
import uz.depos.app.web.rest.AccountResource;
import uz.depos.app.web.rest.UserResource;
import uz.depos.app.web.rest.errors.BadRequestAlertException;
import uz.depos.app.web.rest.errors.EmailAlreadyUsedException;
import uz.depos.app.web.rest.errors.InvalidPasswordException;
import uz.depos.app.web.rest.errors.LoginAlreadyUsedException;
import uz.depos.app.web.rest.vm.ManagedUserVM;

@RestController
@RequestMapping("/api/auth")
@Api(tags = "User")
public class DeposUserResource {

    private static class DeposUserResourceException extends RuntimeException {

        private DeposUserResourceException(String message) {
            super(message);
        }
    }

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
     * {@code POST  /auth/generate-login}  : Generate login.
     * <p>
     * 1. Checking is true @param isUzb
     * 2. Checking is not Empty @param INN, to already used INN and check is Empty login,
     * 3. Generate a new login from INN
     *
     * @param userLoginDTO the user to create.
     * @return the String login with status {@code 201 (Generated)} or with status {@code 400 (Bad Request)} if the login or INN is already in use.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the Uzb is false.
     */
    @PostMapping("/generate-login")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Generate login", notes = "This method is generate new login from present INN")
    public ResponseEntity<LoginDTO> generateLogin(@Valid @RequestBody DeposUserLoginDTO userLoginDTO) {
        log.debug("REST request to generate login from INN : {}", userLoginDTO);

        if (userLoginDTO.isUzb()) {
            LoginDTO loginDTO = userService.generateLogin(userLoginDTO);
            return ResponseEntity.ok(loginDTO);
        } else {
            throw new BadRequestAlertException("For generate new login isUzb will be true", "userManagement", "isUzbexists");
        }
    }

    /**
     * {@code POST  /auth/create} : register the Depositary user.
     *
     * @param deposUserDTO the managed user View Model.
     * @throws InvalidPasswordException  {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Create new user", notes = "This method creates a new user")
    public ResponseEntity<DeposUserDTO> createUser(@Valid @RequestBody DeposUserDTO deposUserDTO) throws URISyntaxException {
        log.debug("REST request to save Depos-User : {}", deposUserDTO);

        if (deposUserDTO.getId() != null && deposUserDTO.getId() > 0) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(deposUserDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(deposUserDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else if (isPasswordLengthInvalid(deposUserDTO.getPassword())) {
            throw new InvalidPasswordException();
        } else {
            DeposUserDTO deposUser = userService.createDeposUser(deposUserDTO);
            return ResponseEntity
                .created(new URI("/api/moder/users/" + deposUser.getLogin()))
                .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", deposUser.getLogin()))
                .body(deposUser);
        }
    }

    /**
     * Check password while create new user, and return the TRUE if ok.
     *
     * @param password password to check.
     * @return Boolean true/false.
     */
    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
            password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
            password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }

    /**
     * {@code GET /auth/users/:ID} : get the "login" user.
     *
     * @param id the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "ID" user, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{id}")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MODERATOR + "\")")
    @ApiOperation(value = "Get user", notes = "This method to get one user by ID")
    public ResponseEntity<DeposUserDTO> getUser(@PathVariable Long id) {
        log.debug("REST request to get User : {}", id);
        return ResponseUtil.wrapOrNotFound(userService.getUserWithAuthoritiesById(id).map(DeposUserDTO::new));
    }
}
