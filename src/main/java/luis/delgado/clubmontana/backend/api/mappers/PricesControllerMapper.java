package luis.delgado.clubmontana.backend.api.mappers;

import java.util.List;
import luis.delgado.clubmontana.backend.api.dtos.PriceDto;
import luis.delgado.clubmontana.backend.domain.model.Price;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PricesControllerMapper {
  List<PriceDto> priceDtoListToPriceDtoList(List<Price> prices);

  List<Price> priceDtoListToPriceList(List<PriceDto> priceDtoList);
}
