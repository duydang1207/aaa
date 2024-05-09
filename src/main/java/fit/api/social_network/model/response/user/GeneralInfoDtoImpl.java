package fit.api.social_network.model.response.user;

import lombok.Data;

@Data
public class GeneralInfoDtoImpl implements GeneralInfoDto {
    private Long postAmount;
    private Long followerAmount;
    private Long followingAmount;
}

