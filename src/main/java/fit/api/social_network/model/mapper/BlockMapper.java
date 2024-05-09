package fit.api.social_network.model.mapper;

import fit.api.social_network.model.entity.Block;
import fit.api.social_network.model.response.block.BlockResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {UserMapper.class})
public interface BlockMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "user", target = "user", qualifiedByName = "toResponse")
    @Mapping(source = "blockUser", target = "blockUser", qualifiedByName = "toResponse")
    @BeanMapping(ignoreByDefault = true)
    @Named("toResponse")
    BlockResponse toResponse(Block block);

    @IterableMapping(elementTargetType = BlockResponse.class, qualifiedByName = "toResponse")
    @BeanMapping(ignoreByDefault = true)
    List<BlockResponse> toResponseList(List<Block> list);
}
