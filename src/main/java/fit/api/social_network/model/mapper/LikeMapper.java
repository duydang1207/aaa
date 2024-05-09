package fit.api.social_network.model.mapper;

import fit.api.social_network.model.entity.Likes;
import fit.api.social_network.model.response.like.LikeResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {UserMapper.class, PostMapper.class, CommentMapper.class})
public interface LikeMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user", target = "user", qualifiedByName = "toResponse")
    @Mapping(source = "post", target = "post", qualifiedByName = "toResponseForMyLike")
    @Mapping(source = "comment", target = "comment", qualifiedByName = "toResponse")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("toResponse")
    LikeResponse toResponse(Likes likes);


    @IterableMapping(elementTargetType = LikeResponse.class, qualifiedByName = "toResponse")
    @BeanMapping(ignoreByDefault = true)
    List<LikeResponse> toResponseList(List<Likes> list);
}
