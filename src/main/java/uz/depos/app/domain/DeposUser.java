//package uz.depos.app.domain;
//
//import javax.persistence.*;
//import javax.validation.constraints.Size;
//import lombok.Getter;
//import lombok.Setter;
//import uz.depos.app.domain.enums.UserAuthTypeEnum;
//import uz.depos.app.domain.enums.UserGroupEnum;
//
//@Getter
//@Setter
//public class DeposUser extends User {
//
//    @Size(max = 50)
//    @Column(name = "middle_name", length = 50)
//    private String middleName;
//
//    // Require to LowerCase
//    @Size(max = 50)
//    @Column(length = 9)
//    private String passport;
//
//    @Size(max = 50)
//    @Column(length = 14, unique = true)
//    private String pinfl;
//
//    @Enumerated(EnumType.STRING)
//    private UserGroupEnum groupEnum;
//
//    // Способ регистрации/авторизации
//    @Enumerated(EnumType.STRING)
//    private UserAuthTypeEnum authTypeEnum;
//
//    // Страна (будет условия по Узб)
//    @Column(length = 64)
//    private String country;
//
//    // Номер ИНН
//    @Column(length = 9, unique = true)
//    private Integer inn;
//
//    // Номер телефона
//    @Column(unique = true, length = 13, name = "phone_number")
//    private String phoneNumber;
//
//    // Доп инфо
//    @Column(length = 128, name = "extra_info")
//    private String extraInfo;
//}
