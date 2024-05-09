package fit.api.social_network.model.mapper;

import fit.api.social_network.model.entity.MessageViews;
import fit.api.social_network.model.response.messageView.MessageViewResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {UserMapper.class, MessageMapper.class})
public interface MessageViewMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "message", target = "message",qualifiedByName = "toResponse")
    @Mapping(source = "viewer", target = "viewer", qualifiedByName = "toResponse")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("toResponse")
    MessageViewResponse toResponse(MessageViews entity);

    @IterableMapping(elementTargetType = MessageViewResponse.class, qualifiedByName = "toResponse")
    @BeanMapping(ignoreByDefault = true)
    List<MessageViewResponse> toResponseList(List<MessageViews> list);
}
