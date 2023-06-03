package space.obminyashka.items_exchange.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import space.obminyashka.items_exchange.dto.AdvertisementDisplayDto;
import space.obminyashka.items_exchange.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.dto.AdvertisementTitleDto;
import space.obminyashka.items_exchange.model.Advertisement;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ImageMapper.class, LocationMapper.class, SubcategoryMapper.class})
public interface AdvertisementMapper {

    @Mapping(target = "phone", ignore = true)
    @Mapping(source = "user.avatarImage", target = "ownerAvatar")
    @Mapping(source = "user.username", target = "ownerName")
    @Mapping(source = "subcategory.category", target = "category")
    @Mapping(source = "id", target = "advertisementId")
    @Mapping(source = "created", target = "createdDate")
    AdvertisementDisplayDto toDto(Advertisement model);

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "subcategory.id", target = "subcategoryId")
    AdvertisementModificationDto toModificationDto(Advertisement model);

    //Unmapped target properties: "updated, status, defaultPhoto, user".
    @Mapping(source = "advertisementId", target = "id")
    @Mapping(source = "createdDate", target = "created")
    Advertisement toModel(AdvertisementDisplayDto dto);

    //Unmapped target properties:updated,status, defaultPhoto, user, images
    @Mapping(source = "subcategoryId", target = "subcategory.id")
    @Mapping(source = "locationId", target = "location.id")
    Advertisement toModel(AdvertisementModificationDto dto);

    @Mapping(source = "id", target = "advertisementId")
    @Mapping(source = "topic", target = "title")
    @Mapping(source = "user.username", target = "ownerName")
    @Mapping(source = "user.avatarImage", target = "ownerAvatar")
    AdvertisementTitleDto toTitleDto(Advertisement advertisement);

    List<AdvertisementTitleDto> toTitleDtoList(Iterable<Advertisement> modelList);

    List<AdvertisementDisplayDto> toDtoList(List<Advertisement> modelList);

    List<Advertisement> toModelList(List<AdvertisementDisplayDto> dtoList);
}
