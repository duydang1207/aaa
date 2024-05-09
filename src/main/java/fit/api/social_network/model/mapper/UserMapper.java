package fit.api.social_network.model.mapper;

import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.request.user.UpdateProfileRequest;
import fit.api.social_network.model.response.user.UserResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "bio", target = "bio")
    @Mapping(source = "avatar", target = "avatar")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "otp", target = "otp")
    @Mapping(source = "otpExpiredDate", target = "otpExpiredDate")
    @Mapping(source = "banedExpiredDate", target = "banedExpiredDate")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("toResponse")
    UserResponse toResponse(User user);
    @IterableMapping(elementTargetType = UserResponse.class, qualifiedByName = "toResponse")
    @BeanMapping(ignoreByDefault = true)
    @Named("toResponseList")
    List<UserResponse> toResponseList(List<User> list);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "bio", target = "bio")
    @Mapping(source = "avatar", target = "avatar")
    @BeanMapping(ignoreByDefault = true)
    @Named("updateProfile")
    void updateProfile(UpdateProfileRequest updateProfileRequest, @MappingTarget User user);
}
