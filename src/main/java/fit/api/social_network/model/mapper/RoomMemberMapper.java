package fit.api.social_network.model.mapper;

import fit.api.social_network.model.entity.RoomMember;
import fit.api.social_network.model.response.roomMember.RoomMemberResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {UserMapper.class, RoomMapper.class})
public interface RoomMemberMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "room", target = "room",qualifiedByName = "toResponse")
    @Mapping(source = "user", target = "user",qualifiedByName = "toResponse")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("toResponse")
    RoomMemberResponse toResponse(RoomMember entity);


    @IterableMapping(elementTargetType = RoomMemberResponse.class, qualifiedByName = "toResponse")
    @BeanMapping(ignoreByDefault = true)
    List<RoomMemberResponse> toResponseList(List<RoomMember> list);
}
