package fit.api.social_network.model.mapper;

import fit.api.social_network.model.entity.Followers;
import fit.api.social_network.model.response.follower.FollowerResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {UserMapper.class})
public interface FollowerMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user", target = "user",qualifiedByName = "toResponse")
    @Mapping(source = "followingUser", target = "followingUser", qualifiedByName = "toResponse")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("toResponse")
    FollowerResponse toResponse(Followers followers);


    @IterableMapping(elementTargetType = FollowerResponse.class, qualifiedByName = "toResponse")
    @BeanMapping(ignoreByDefault = true)
    List<FollowerResponse> toResponseList(List<Followers> list);
}
