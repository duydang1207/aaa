package fit.api.social_network.model.mapper;

import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.request.user.RegisterRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterMapper {
    User toEntity(RegisterRequest request);
}
