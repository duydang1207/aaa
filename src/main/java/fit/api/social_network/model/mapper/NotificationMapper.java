package fit.api.social_network.model.mapper;

import fit.api.social_network.model.entity.Notifications;
import fit.api.social_network.model.response.notification.NotificationResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {UserMapper.class})

public interface NotificationMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user", target = "user", qualifiedByName = "toResponse")
    @Mapping(source = "notification", target = "notification")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("toResponse")
    NotificationResponse toResponse(Notifications entity);


    @IterableMapping(elementTargetType = NotificationResponse.class, qualifiedByName = "toResponse")
    @BeanMapping(ignoreByDefault = true)
    List<NotificationResponse> toResponseList(List<Notifications> list);
}
