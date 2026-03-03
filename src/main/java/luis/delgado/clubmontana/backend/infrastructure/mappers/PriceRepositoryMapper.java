package luis.delgado.clubmontana.backend.infrastructure.mappers;

import java.util.List;
import luis.delgado.clubmontana.backend.domain.model.Price;
import luis.delgado.clubmontana.backend.infrastructure.entitys.PriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PriceRepositoryMapper {
  List<PriceEntity> listPriceToPriceEntityList(List<Price> prices);

  @Mapping(target = "club.clubId", source = "clubId")
  PriceEntity priceToPriceEntity(Price price);

  @Mapping(target = "clubId", source = "club.clubId")
  Price priceEntityToPrice(PriceEntity priceEntity);
}
