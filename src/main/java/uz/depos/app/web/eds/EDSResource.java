package uz.depos.app.web.eds;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.Api;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.depos.app.domain.Authority;
import uz.depos.app.domain.User;
import uz.depos.app.domain.enums.UserAuthTypeEnum;
import uz.depos.app.domain.enums.UserGroupEnum;
import uz.depos.app.repository.UserRepository;
import uz.depos.app.security.jwt.JWTFilter;
import uz.depos.app.security.jwt.TokenProvider;
import uz.depos.app.service.MailService;
import uz.depos.app.service.UserService;
import uz.depos.app.service.dto.DeposUserDTO;
import uz.depos.app.service.mapper.UserMapper;

/**
 * Electronic digital signature (Elektron raqamli imzo)
 */
@RestController
@RequestMapping("/api/eds")
@Api(tags = "e-imzo")
public class EDSResource {

    private final Logger log = LoggerFactory.getLogger(EDSResource.class);

    final CommonServices commonService;
    final PkiManager pkiManager;
    final UserService userService;
    final UserRepository userRepository;
    final MailService mailService;
    final AuthenticationManagerBuilder authenticationManagerBuilder;
    final TokenProvider tokenProvider;
    final UserMapper userMapper;

    public EDSResource(
        CommonServices commonService,
        PkiManager pkiManager,
        UserService userService,
        UserRepository userRepository,
        MailService mailService,
        AuthenticationManagerBuilder authenticationManagerBuilder,
        TokenProvider tokenProvider,
        UserMapper userMapper
    ) {
        this.commonService = commonService;
        this.pkiManager = pkiManager;
        this.userService = userService;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
        this.userMapper = userMapper;
    }

    /**
     * "/signrequest" GET method to get random value for put signature of client.
     *
     * @param request
     * @param session
     * @return UUID String
     */
    @GetMapping("/signrequest")
    public ResponseEntity<JSONObject> signRequest(HttpServletRequest request, HttpSession session) {
        log.debug("SIGNREQUEST by POST method Information for Auth by EDS: {}", request.getParameter("serialNumber"));
        String serialNumber = request.getParameter("serialNumber");

        JSONObject json = new JSONObject();
        String hash = serialNumber + "$" + commonService.getCurrentTimeStamp() + "$" + commonService.getRandomInt();
        json.put("hash", hash);
        json.put("success", true);

        session.setAttribute("auth_with_ecp", hash);
        return ResponseEntity.ok(json);
    }

    @PostMapping("/auth")
    public ResponseEntity<?> loginEcp(
        @RequestBody EDSTemporaryDTO temporaryDTO,
        //        @RequestParam(name = "pkcs7") String pkcs7,
        //        @RequestParam(name = "pinfl") String pinfl,
        HttpServletRequest request,
        HttpServletResponse response,
        HttpSession session
    ) throws Exception {
        ResultDto result = new ResultDto();
        //        String pinfl = "31411860070045";

        String pinfl = temporaryDTO.getPinfl();

        //        String pkcs7 = commonService.getRequestValue(request, "pkcs7", "");
        //        String hash = request.getSession().getAttribute("auth_with_ecp").toString();

        // verify signature of xml64
        //        JsonVerifyPkcs7Response verifyPkcs7Response = pkiManager.verifyPkcs7(pkcs7);
        //        if (!verifyPkcs7Response.isSuccess()) {
        //            result.setSuccess(false);
        //            result.setMessage("Reason: " + verifyPkcs7Response.getReason());
        //            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        //        }
        //        if (verifyPkcs7Response.getPkcs7Info() == null || verifyPkcs7Response.getPkcs7Info().getSigners().size() != 1) {
        //            result.setSuccess(false);
        //            result.setMessage("Signature is incorrect");
        //            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        //        }
        //
        //        Pkcs7SignerInfoView signerInfo = verifyPkcs7Response.getPkcs7Info().getSigners().get(0);
        //        if (signerInfo.getException() != null) {
        //            result.setSuccess(false);
        //            result.setMessage("Exception dsvs: " + signerInfo.getException());
        //            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        //        }
        //        if (!signerInfo.isVerified()) {
        //            result.setSuccess(false);
        //            result.setMessage("Signature is invalid");
        //            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        //        }
        //        if (!signerInfo.isCertificateVerified()) {
        //            result.setSuccess(false);
        //            result.setMessage("CERTIFICATE_IS_NOT_VERIFIED");
        //            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        //        }
        //        if (!signerInfo.isCertificateValidAtSigningTime()) {
        //            result.setSuccess(false);
        //            result.setMessage("CERTIFICATE_IS_NOT_VALID_AT_SIGNING_TIME");
        //            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        //        }
        //
        //        if (signerInfo.getTimeStampInfo() == null) {
        //            result.setSuccess(false);
        //            result.setMessage("TIMESTAMP_INFO_IS_NULL");
        //            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        //        }
        //
        //        boolean allowed = false;
        //        String[] certificatePolicies = new String[]{
        //            "1.2.860.3.2.2.1.2.1",
        //            "1.2.860.3.2.2.1.2.2",
        //            "1.2.860.3.2.2.1.2.3",
        //            "1.2.860.3.2.2.1.2.4"
        //        };
        //        String[] policyIdentifiers = signerInfo.getPolicyIdentifiers();
        //        if (signerInfo.getCertificate()[0].getPublicKey().getKeyAlgName().equals("OZDST_1092_2009_1")) {
        //            allowed = true;
        //        } else {
        //            if (policyIdentifiers != null) {
        //                for (String pid : certificatePolicies) {
        //                    for (String cpid : policyIdentifiers) {
        //                        if (pid.equals(cpid)) {
        //                            allowed = true;
        //                            break;
        //                        }
        //                    }
        //                    if (allowed) {
        //                        break;
        //                    }
        //                }
        //            }
        //        }
        //        if (!allowed) {
        //            result.setSuccess(false);
        //            result.setMessage("CERTIFICATE_POLICY_IS_INVALID");
        //            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        //
        //        }
        //
        ////        if (!hash.equals(new String(Base64.decodeBase64(verifyPkcs7Response.getPkcs7Info().getDocumentBase64())))) {
        ////            result.setSuccess(false);
        ////            result.setMessage("SIGNATURE_IS_INVALID");
        ////            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        ////        }
        //        String cert_info = signerInfo.getCertificate()[0].getSubjectName();
        //
        //        String fullName;
        //        try {
        //            fullName = X500NameHelper.get("CN", cert_info);
        //        } catch (Throwable t) {
        //            result.setSuccess(false);
        //            result.setMessage("CANNOT FETCH NAME AND SURNAME FROM: " + cert_info);
        //            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        //        }
        //
        //        String yurTin = X500NameHelper.get(commonService.getYurTinOID(), cert_info);
        //        if (StringUtils.hasText(yurTin)) {
        //            result.setSuccess(false);
        //            result.setMessage("User must be physical. Juridicial tin: " + yurTin);
        //            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        //        }
        //        String fizTin = X500NameHelper.get("UID", cert_info);
        //        String organization = X500NameHelper.get("O", cert_info);
        //        String email = X500NameHelper.get("E", cert_info);
        //        String pinfl = X500NameHelper.get(commonService.getPinflOID(), cert_info);
        //
        ////        //get user type
        ////        UserTypeDto userTypeDto = usersDao.getTypeByTin(fizTin);
        ////        if (userTypeDto == null) {
        ////            result.setSuccess(false);
        ////            result.setMessage("Not access role. Tin: " + fizTin);
        ////            return result;
        ////        }
        //        result.setSuccess(true);
        //        session.invalidate();

        User user;
        // try to find user by PINFL
        Optional<User> optionalUser = userService.getUserWithAuthoritiesByPinfl(pinfl);
        Optional<User> optionalUser1 = userRepository.findOneByPinfl(pinfl);

        if (optionalUser.isPresent()) { // Check to present in DB current user, is it Old user.
            user = optionalUser.get();
        } else { // Create new user
            String generatedLogin = userService.generateLogin(pinfl);

            String password = RandomStringUtils.randomAlphanumeric(6); // Generate new random password.

            DeposUserDTO userDTO = new DeposUserDTO();
            userDTO.setLogin(generatedLogin);
            userDTO.setPassword(password);
            //            userDTO.setEmail("ard_admin@mail.ru");
            userDTO.setActivated(true);
            if (StringUtils.isNotEmpty(temporaryDTO.getFullName())) {
                String fn = temporaryDTO.getFullName();
                String collect = Arrays
                    .stream(fn.toLowerCase().split(" "))
                    .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                    .collect(Collectors.toList())
                    .toString()
                    .replace(",", "")
                    .replace("]", "")
                    .replace("[", "");
                userDTO.setFullName(collect);
            }
            userDTO.setPinfl(pinfl);
            userDTO.setGroupEnum(UserGroupEnum.INDIVIDUAL);
            userDTO.setAuthTypeEnum(UserAuthTypeEnum.ERI);
            userDTO.setResident(true);
            userDTO.setInn(temporaryDTO.getInn());
            user = userService.createDeposUser(userDTO);
            mailService.sendEDSUserCreatedEmail(user, password);
        }

        Collection<GrantedAuthority> grantedAuthorities = setUserAuthorities(user.getAuthorities());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            user.getLogin(),
            "",
            grantedAuthorities
        );
        //        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String jwt = tokenProvider.createToken(authenticationToken, false);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
        //        SecurityDto security = new SecurityDto();
        //        security.setUser_id(pinfl);
        //        security.setEmail(email);
        //        security.setIs_active(1);
        //        security.setNs10_code(0);
        //        security.setNs11_code(0);
        //        security.setFio(fullName);
        //        security.setUser_type(userTypeDto.getRole());
        //        cookie.set(response, security);
        //        result.setSuccess(true);
        //        session.invalidate();
        //        return result;
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }

    static Collection<GrantedAuthority> setUserAuthorities(Set<Authority> auths) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Authority auth : auths) {
            grantedAuthorities.add(new SimpleGrantedAuthority(auth.getName()));
        }
        return grantedAuthorities;
    }
}
