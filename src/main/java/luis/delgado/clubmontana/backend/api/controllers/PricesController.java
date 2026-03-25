package luis.delgado.clubmontana.backend.api.controllers;

import java.util.List;
import luis.delgado.clubmontana.backend.api.dtos.PriceDto;
import luis.delgado.clubmontana.backend.api.mappers.PricesControllerMapper;
import luis.delgado.clubmontana.backend.core.annotations.ClubId;
import luis.delgado.clubmontana.backend.domain.useCases.PricesUseCases;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubs/{club}/prices")
public class PricesController {

  private final PricesControllerMapper pricesControllerMapper;
  private final PricesUseCases pricesUseCases;

  public PricesController(
      PricesControllerMapper pricesControllerMapper, PricesUseCases pricesUseCases) {
    this.pricesControllerMapper = pricesControllerMapper;
    this.pricesUseCases = pricesUseCases;
  }

  @PutMapping
  public ResponseEntity<Void> put(@ClubId Long clubId, @RequestBody List<PriceDto> prices) {
    pricesUseCases.put(clubId, pricesControllerMapper.priceDtoListToPriceList(prices));
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping
  public ResponseEntity<List<PriceDto>> get(@ClubId Long clubId) {
    return ResponseEntity.ok(
        pricesControllerMapper.priceDtoListToPriceDtoList(pricesUseCases.get(clubId)));
  }
}
