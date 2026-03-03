package luis.delgado.clubmontana.backend.application.useCases;

import java.util.List;
import luis.delgado.clubmontana.backend.core.annotations.NoAuthenticationNeeded;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.Price;
import luis.delgado.clubmontana.backend.domain.repository.PricesRepository;
import luis.delgado.clubmontana.backend.domain.userCases.PricesUseCases;

@UseCase
public class PricesUseCasesImpl implements PricesUseCases {

  private final PricesRepository pricesRepository;

  public PricesUseCasesImpl(PricesRepository pricesRepository) {
    this.pricesRepository = pricesRepository;
  }

  @Override
  public void put(Long clubId, List<Price> prices) {
    prices.forEach(price -> price.setClubId(clubId));
    pricesRepository.save(prices);
  }

  @NoAuthenticationNeeded
  @Override
  public List<Price> get(Long clubId) {
    return pricesRepository.get(clubId);
  }
}
