package luis.delgado.clubmontana.backend.domain.useCases;

import java.util.List;
import luis.delgado.clubmontana.backend.domain.model.Price;

public interface PricesUseCases {
  void put(Long clubId, List<Price> prices);

  List<Price> get(Long clubId);
}
