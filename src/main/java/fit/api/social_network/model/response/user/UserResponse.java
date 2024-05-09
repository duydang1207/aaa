package fit.api.social_network.model.response.user;

import fit.api.social_network.model.enums.ROLES;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String bio;
    private String avatar;
    private String password;
    private Integer kind;
    private String otp;
    private Date otpExpiredDate;
    private Date banedExpiredDate;
    private ROLES role;
    private Date createdDate;
    private Date modifiedDate;
    private Integer status;
    private Boolean isFollowed = false;
}
