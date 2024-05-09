package fit.api.social_network.model.mapper;

import fit.api.social_network.model.dto.MessageDto;
import fit.api.social_network.model.entity.Message;
import fit.api.social_network.model.response.message.MessageResponse;
import fit.api.social_network.model.response.message.MessageResponseCustom;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {UserMapper.class, RoomMapper.class})
public interface MessageMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "room", target = "room", qualifiedByName = "toResponse")
    @Mapping(source = "sender", target = "sender", qualifiedByName = "toResponse")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("toResponse")
    MessageResponse toResponse(Message entity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "room", target = "room", qualifiedByName = "toResponse")
    @Mapping(source = "sender", target = "sender", qualifiedByName = "toResponse")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "users", target = "users", qualifiedByName = "toResponseList")
    @BeanMapping(ignoreByDefault = true)
    @Named("toResponseCustom")
    MessageResponseCustom toResponseCustom(MessageDto entity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "room", target = "room")
    @Mapping(source = "sender", target = "sender")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "status", target = "status")
    MessageDto toDto(Message entity);

    @IterableMapping(elementTargetType = MessageResponse.class, qualifiedByName = "toResponse")
    @BeanMapping(ignoreByDefault = true)
    List<MessageResponse> toResponseList(List<Message> list);
}
