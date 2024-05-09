package fit.api.social_network.model.mapper;

import fit.api.social_network.model.entity.Comments;
import fit.api.social_network.model.request.comment.CreateCommentRequest;
import fit.api.social_network.model.response.comment.CommentResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {UserMapper.class, PostMapper.class, CommentMapper.class})
public interface CommentMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user", target = "user", qualifiedByName = "toResponse")
    @Mapping(source = "post", target = "post", qualifiedByName = "toResponse")
    @Mapping(source = "parent", target = "parent", qualifiedByName = "toParent")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "comment", target = "comment")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("toResponse")
    CommentResponse toResponse(Comments comments);


    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "comment", target = "comment")
    @BeanMapping(ignoreByDefault = true)
    @Named("createFromCreateRequest")
    Comments createFromCreateRequest(CreateCommentRequest createCommentRequest);


    @IterableMapping(elementTargetType = CommentResponse.class, qualifiedByName = "toResponse")
    @BeanMapping(ignoreByDefault = true)
    List<CommentResponse> toResponseList(List<Comments> list);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user", target = "user", qualifiedByName = "toResponse")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "comment", target = "comment")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("toParent")
    CommentResponse toParent(Comments comments);
}
