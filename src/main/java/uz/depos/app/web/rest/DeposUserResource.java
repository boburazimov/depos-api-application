package uz.depos.app.web.rest;

import java.net.URISyntaxException;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.depos.app.domain.User;
import uz.depos.app.repository.UserRepository;
import uz.depos.app.service.UserService;
import uz.depos.app.service.dto.ApiResponse;
import uz.depos.app.service.dto.DeposUserDTO;
import uz.depos.app.service.dto.DeposUserLoginDTO;
import uz.depos.app.web.rest.errors.*;
import uz.depos.app.web.rest.vm.ManagedUserVM;

@RestController
@RequestMapping("/api/auth")
public class DeposUserResource {

    private static class DeposUserResourceException extends RuntimeException {

        private DeposUserResourceException(String message) {
            super(message);
        }
    }

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
     * @param isUzb        to check county.
     * @return the String login with status {@code 201 (Generated)} or with status {@code 400 (Bad Request)} if the login or INN is already in use.
     * @throws URISyntaxException       if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping("/generate-login")
    public ResponseEntity<ApiResponse> generateLogin(@Valid @RequestBody DeposUserLoginDTO userLoginDTO, Boolean isUzb)
        throws URISyntaxException {
        log.debug("REST request to generate login from INN : {}", userLoginDTO);

        if (isUzb) {
            ApiResponse response = userService.generateLogin(userLoginDTO);
            return ResponseEntity.status(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
        } else {
            throw new BadRequestAlertException("For generate new login isUzb will be true", "userManagement", "isUzbexists");
        }
    }

    /**
     * {@code POST  /register} : register the Depositary user.
     *
     * @param deposUserDTO the managed user View Model.
     * @throws InvalidPasswordException  {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody DeposUserDTO deposUserDTO) {
        log.debug("REST request to save Depos-User : {}", deposUserDTO);

        if (isPasswordLengthInvalid(deposUserDTO.getPassword())) {
            throw new InvalidPasswordException();
        } else if (deposUserDTO.getId() == null) {
            throw new IdNotEmptyException();
        }

        User user = userService.createDeposUser(deposUserDTO);
        return user;
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
}
