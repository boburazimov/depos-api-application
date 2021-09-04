package uz.depos.app.service;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;
import uz.depos.app.config.Constants;
import uz.depos.app.domain.Authority;
import uz.depos.app.domain.Company;
import uz.depos.app.domain.User;
import uz.depos.app.domain.enums.UserGroupEnum;
import uz.depos.app.domain.enums.UserSearchFieldEnum;
import uz.depos.app.repository.AuthorityRepository;
import uz.depos.app.repository.UserRepository;
import uz.depos.app.security.AuthoritiesConstants;
import uz.depos.app.security.SecurityUtils;
import uz.depos.app.service.dto.*;
import uz.depos.app.service.mapper.UserMapper;
import uz.depos.app.web.rest.errors.*;
import uz.depos.app.web.rest.errors.InnAlreadyUsedException;
import uz.depos.app.web.rest.errors.PassportAlreadyUsedException;
import uz.depos.app.web.rest.errors.PhoneNumberAlreadyUsedException;
import uz.depos.app.web.rest.errors.PinflAlreadyUsedException;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final CacheManager cacheManager;
    private final UserMapper userMapper;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AuthorityRepository authorityRepository,
        CacheManager cacheManager,
        UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.userMapper = userMapper;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .map(
                user -> {
                    // activate given user for the registration key.
                    user.setActivated(true);
                    user.setActivationKey(null);
                    this.clearUserCaches(user);
                    log.debug("Activated user: {}", user);
                    return user;
                }
            );
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(
                user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    user.setResetDate(null);
                    this.clearUserCaches(user);
                    return user;
                }
            );
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(
                user -> {
                    user.setResetKey(RandomUtil.generateResetKey());
                    user.setResetDate(Instant.now());
                    this.clearUserCaches(user);
                    return user;
                }
            );
    }

    public Boolean isEmailAlreadyUsed(String email) {
        boolean present = userRepository.findOneByEmailIgnoreCase(email).isPresent();
        if (!present) {
            return true;
        } else throw new EmailAlreadyUsedException();
    }

    public String generateLogin(String pinfl) {
        // Generate new login from @PINFL by adding to begin bit-word "uz-"
        String bitWord = "uz-";
        StringBuilder login = new StringBuilder();
        login.insert(0, bitWord);
        login.insert(3, pinfl);
        userRepository
            .findOneByLogin(login.toString().toLowerCase())
            .ifPresent(
                user -> {
                    throw new UsernameAlreadyUsedException();
                }
            );
        log.debug("Generated Login from PINFL Information for User: {}", login);
        return login.toString();
    }

    public DeposUserDTO createDeposUser(DeposUserDTO userDTO) {
        // Create new User object
        User newUser = new User();

        // Checking field @Login, @Passport, @PhoneNumber and @INN to already used
        userRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .ifPresent(
                user -> {
                    throw new UsernameAlreadyUsedException();
                }
            );
        //        userRepository
        //            .findOneByInn(userDTO.getInn())
        //            .ifPresent(
        //                user -> {
        //                    throw new InnAlreadyUsedException();
        //                }
        //            );
        if (userDTO.getPassport() != null) userRepository
            .findOneByPassport(userDTO.getPassword().toUpperCase())
            .ifPresent(
                user -> {
                    throw new PassportAlreadyUsedException();
                }
            );
        if (userDTO.getPhoneNumber() != null) userRepository
            .findOneByPhoneNumber(userDTO.getPhoneNumber())
            .ifPresent(
                user -> {
                    throw new PhoneNumberAlreadyUsedException();
                }
            );
        if (userDTO.getPinfl() != null) userRepository
            .findOneByPinfl(userDTO.getPinfl())
            .ifPresent(
                user -> {
                    throw new PinflAlreadyUsedException();
                }
            );

        if (StringUtils.isNoneBlank(userDTO.getLogin())) newUser.setLogin(userDTO.getLogin().toLowerCase());

        // Checks and get or generate password if none of the CharSequences are empty (""), null or whitespace only.
        String password = StringUtils.isNoneBlank(userDTO.getPassword())
            ? RandomStringUtils.randomAlphanumeric(6) // Generate new random password.
            : userDTO.getPassword();

        // Encode password which to set User table in DB
        String encryptedPassword = passwordEncoder.encode(password);

        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);

        // Encode by Base64 generated password for sending to Front-End side.
        String saltedPassword = Base64.getEncoder().encodeToString(password.concat(Constants.PASSWORD_KEY).getBytes());
        String decode = Arrays.toString(Base64.getDecoder().decode(saltedPassword));

        newUser.setFullName(userDTO.getFullName());
        newUser.setActivated(userDTO.isActivated());
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());

        Set<Authority> authorities;
        if (userDTO.getAuthorities() != null) {
            authorities =
                userDTO
                    .getAuthorities()
                    .stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
        } else {
            // by default new user's authority @USER
            authorities = new HashSet<>();
            authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        }
        newUser.setAuthorities(authorities);
        if (StringUtils.isNoneBlank(userDTO.getEmail())) newUser.setEmail(userDTO.getEmail().toLowerCase());
        if (StringUtils.isNoneBlank(userDTO.getPassport())) newUser.setPassport(userDTO.getPassport().toUpperCase());
        if (StringUtils.isNoneBlank(userDTO.getPinfl())) newUser.setPinfl(userDTO.getPinfl());
        newUser.setGroupEnum(userDTO.getGroupEnum());
        newUser.setAuthTypeEnum(userDTO.getAuthTypeEnum());
        newUser.setResident(userDTO.isResident());
        if (userDTO.getInn() != null) newUser.setInn(userDTO.getInn());
        if (userDTO.getPhoneNumber() != null) newUser.setPhoneNumber(userDTO.getPhoneNumber());
        User savedUser = userRepository.save(newUser);
        DeposUserDTO deposUserDTO = userMapper.userToDeposUserDTO(savedUser);
        deposUserDTO.setPassword(saltedPassword);
        this.clearUserCaches(savedUser);
        log.debug("Created Information for User: {}", deposUserDTO);
        return deposUserDTO;
    }

    public User registerUser(AdminUserDTO userDTO, String password) {
        userRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .ifPresent(
                existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new UsernameAlreadyUsedException();
                    }
                }
            );
        userRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .ifPresent(
                existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new EmailAlreadyUsedException();
                    }
                }
            );
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(AdminUserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO
                .getAuthorities()
                .stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional
            .of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                user -> {
                    this.clearUserCaches(user);
                    user.setLogin(userDTO.getLogin().toLowerCase());
                    user.setFirstName(userDTO.getFirstName());
                    user.setLastName(userDTO.getLastName());
                    if (userDTO.getEmail() != null) {
                        user.setEmail(userDTO.getEmail().toLowerCase());
                    }
                    user.setImageUrl(userDTO.getImageUrl());
                    user.setActivated(userDTO.isActivated());
                    user.setLangKey(userDTO.getLangKey());
                    Set<Authority> managedAuthorities = user.getAuthorities();
                    managedAuthorities.clear();
                    userDTO
                        .getAuthorities()
                        .stream()
                        .map(authorityRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(managedAuthorities::add);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                }
            )
            .map(AdminUserDTO::new);
    }

    /**
     * Edit all information for a specific user, and return the modified user.
     *
     * @param userDTO user to edit.
     * @return edited user.
     */
    public Optional<DeposUserDTO> editUser(DeposUserDTO userDTO) {
        return Optional
            .of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                user -> {
                    this.clearUserCaches(user);

                    // Login
                    if (StringUtils.isNoneBlank(userDTO.getLogin())) user.setLogin(userDTO.getLogin().toLowerCase());
                    // Email
                    if (StringUtils.isNoneBlank(userDTO.getEmail())) user.setEmail(userDTO.getEmail().toLowerCase());
                    // Password - Encryption @password if not null
                    if (StringUtils.isNoneBlank(userDTO.getPassword())) {
                        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
                        user.setPassword(encryptedPassword);
                    }
                    // Active
                    user.setActivated(userDTO.isActivated());
                    // Authorities
                    Set<Authority> managedAuthorities = user.getAuthorities();
                    managedAuthorities.clear();
                    userDTO
                        .getAuthorities()
                        .stream()
                        .map(authorityRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(managedAuthorities::add);
                    // FullName
                    if (StringUtils.isNoneBlank(userDTO.getFullName())) user.setFullName(userDTO.getFullName());
                    // Passport
                    if (StringUtils.isNoneBlank(userDTO.getPassport().toUpperCase())) user.setPassport(userDTO.getPassport().toUpperCase());
                    if (StringUtils.isNoneBlank(userDTO.getPinfl())) user.setPinfl(userDTO.getPinfl());
                    user.setGroupEnum(userDTO.getGroupEnum());
                    user.setAuthTypeEnum(userDTO.getAuthTypeEnum());
                    user.setResident(userDTO.isResident());
                    if (ObjectUtils.isNotEmpty(userDTO.getInn())) user.setInn(userDTO.getInn());
                    if (StringUtils.isNoneBlank(userDTO.getPhoneNumber())) user.setPhoneNumber(userDTO.getPhoneNumber());
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                }
            )
            .map(DeposUserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository
            .findOneByLogin(login)
            .ifPresent(
                user -> {
                    userRepository.delete(user);
                    this.clearUserCaches(user);
                    log.debug("Deleted User: {}", user);
                }
            );
    }

    public ApiResponse removeUser(Long id) {
        try {
            userRepository
                .findById(id)
                .ifPresent(
                    user -> {
                        userRepository.delete(user);
                        this.clearUserCaches(user);
                        log.debug("Deleted User: {}", user);
                    }
                );
            return new ApiResponse("User deleted!", true);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), false);
        }
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                user -> {
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    if (email != null) {
                        user.setEmail(email.toLowerCase());
                    }
                    user.setLangKey(langKey);
                    user.setImageUrl(imageUrl);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                }
            );
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                user -> {
                    String currentEncryptedPassword = user.getPassword();
                    if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                        throw new InvalidPasswordException();
                    }
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    this.clearUserCaches(user);
                    log.debug("Changed password for User: {}", user);
                }
            );
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<DeposUserDTO> getAllManagedDeposUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(DeposUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesById(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(
                user -> {
                    log.debug("Deleting not activated user {}", user.getLogin());
                    userRepository.delete(user);
                    this.clearUserCaches(user);
                }
            );
    }

    /**
     * Gets a list of all the authorities.
     *
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    public List<DeposUserNameDTO> searchUserByName(String name) {
        List<User> users = new ArrayList<>();
        if (name == null) users.addAll(userRepository.findAll()); else userRepository
            .findByFullNameIgnoreCaseContaining(name)
            .ifPresent(users::addAll);
        if (users.isEmpty()) {
            return null;
        }
        log.debug("Found searched Information for User by name: {}", name);
        return userMapper.usersToDeposUserDTO(users);
    }

    public Page<DeposUserDTO> filterUsers(UserSearchFieldEnum field, String value, Pageable pageable) {
        User user = new User();
        switch (field) {
            case FULL_NAME:
                user.setFullName(value);
                break;
            case EMAIL:
                user.setEmail(value);
                break;
            case PHONE_NUMBER:
                user.setPhoneNumber(value);
                break;
            case GROUP:
                user.setGroupEnum(UserGroupEnum.valueOf(value.toUpperCase()));
                break;
            case PINFL:
                user.setPinfl(value);
                break;
            default:
                break;
        }

        ExampleMatcher matcher = ExampleMatcher
            .matching()
            .withMatcher("fullName", contains().ignoreCase())
            .withMatcher("email", contains().ignoreCase())
            .withMatcher("phoneNumber", contains().ignoreCase())
            .withMatcher("groupEnum", contains().ignoreCase())
            .withMatcher("pinfl", contains().ignoreCase())
            .withIgnorePaths("createdDate", "lastModifiedDate", "authorities", "activated", "isResident");

        if (value == null) {
            return userRepository.findAll(pageable).map(DeposUserDTO::new);
        } else {
            log.debug("Filtered Information for User by filed: {}", field);
            return userRepository.findAll(Example.of(user, matcher), pageable).map(DeposUserDTO::new);
        }
    }
}
