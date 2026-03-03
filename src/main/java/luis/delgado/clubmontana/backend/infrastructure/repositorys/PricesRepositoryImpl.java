package luis.delgado.clubmontana.backend.infrastructure.repositorys;

import java.util.List;
import luis.delgado.clubmontana.backend.domain.model.Price;
import luis.delgado.clubmontana.backend.domain.repository.PricesRepository;
import luis.delgado.clubmontana.backend.infrastructure.jpa.PriceEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.mappers.PriceRepositoryMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PricesRepositoryImpl implements PricesRepository {
  private final PriceEntityJpa priceEntityJpa;
  private final PriceRepositoryMapper priceRepositoryMapper;

  public PricesRepositoryImpl(
      PriceEntityJpa priceEntityJpa, PriceRepositoryMapper priceRepositoryMapper) {
    this.priceEntityJpa = priceEntityJpa;
    this.priceRepositoryMapper = priceRepositoryMapper;
  }

  @Override
  @Transactional
  public void save(List<Price> prices) {
    if (prices == null || prices.isEmpty()) return;
    priceEntityJpa.deleteByClub_ClubId(prices.getFirst().getClubId());
    priceEntityJpa.saveAll(priceRepositoryMapper.listPriceToPriceEntityList(prices));
  }

  @Override
  public List<Price> get(Long clubId) {
    return priceEntityJpa.findByClub_ClubId(clubId).stream()
        .map(priceRepositoryMapper::priceEntityToPrice)
        .toList();
  }
}
