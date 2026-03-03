package luis.delgado.clubmontana.backend.domain.repository;

import java.util.List;
import luis.delgado.clubmontana.backend.domain.model.Price;

public interface PricesRepository {
    void save(List<Price> prices);
    List<Price> get(Long clubId);
}
