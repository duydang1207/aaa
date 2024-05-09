package fit.api.social_network.model.mapper;

import fit.api.social_network.model.entity.Posts;
import fit.api.social_network.model.request.post.CreatePostRequest;
import fit.api.social_network.model.request.post.UpdatePostRequest;
import fit.api.social_network.model.response.post.PostResponse;
import fit.api.social_network.model.response.post.StoryResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {UserMapper.class})
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user", target = "user", qualifiedByName = "toResponse")
    @Mapping(target = "image_url" , expression = "java(stringToList(entity.getImage_url()))")
    @Mapping(source = "caption", target = "caption")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "commentedAmount", target = "commentedAmount")
    @Mapping(source = "likedAmount", target = "likedAmount")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("toResponse")
    PostResponse toResponse(Posts entity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user", target = "user", qualifiedByName = "toResponse")
    @Mapping(target = "image_url" , expression = "java(stringToList(entity.getImage_url()))")
    @Mapping(source = "caption", target = "caption")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "commentedAmount", target = "commentedAmount")
    @Mapping(source = "likedAmount", target = "likedAmount")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true, qualifiedByName = "afterMappingToResponseForMyLike")
    @Named("toResponseForMyLike")
    PostResponse toResponseForMyLike(Posts entity);

    @AfterMapping
    @Named("afterMappingToResponseForMyLike")
    default void afterMappingToResponseForMyLike(Posts post, @MappingTarget PostResponse postResponse){
        postResponse.setIsLiked(true);
    }

    @Mapping(source = "image_url", target = "image_url")
    @Mapping(source = "caption", target = "caption")
    @Mapping(source = "kind", target = "kind")
    @BeanMapping(ignoreByDefault = true)
    @Named("createFromRequest")
    Posts createFromRequest(CreatePostRequest createPostRequest);

    @Mapping(source = "image_url", target = "image_url")
    @Mapping(source = "caption", target = "caption")
    @BeanMapping(ignoreByDefault = true)
    @Named("createFromRequest")
    void updateFromUpdateRequest(UpdatePostRequest updatePostRequest, @MappingTarget Posts post);

    @IterableMapping(elementTargetType = PostResponse.class, qualifiedByName = "toResponse")
    @BeanMapping(ignoreByDefault = true)
    List<PostResponse> toResponseList(List<Posts> list);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "image_url", target = "image_url")
    @Mapping(source = "caption", target = "caption")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "commentedAmount", target = "commentedAmount")
    @Mapping(source = "likedAmount", target = "likedAmount")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "isLiked", target = "isLiked")
    @BeanMapping(ignoreByDefault = true)
    @Named("responseToResponse")
    PostResponse responseToResponse(PostResponse entity);

    default List<String> stringToList(String input){
        if(input==null || input.isEmpty())
            return null;
        return Arrays.asList(input.split(";"));
    }
}
