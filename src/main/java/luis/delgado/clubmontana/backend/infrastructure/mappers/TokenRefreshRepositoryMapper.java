package luis.delgado.clubmontana.backend.infrastructure.mappers;

import luis.delgado.clubmontana.backend.domain.model.TokenRefresh;
import luis.delgado.clubmontana.backend.infrastructure.entitys.TokenRefreshEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenRefreshRepositoryMapper {
  TokenRefresh tokenRefreshEntityToTOkenRefresh(TokenRefreshEntity tokenRefreshEntity);

  TokenRefreshEntity tokenRefreshToTokenRefreshEntity(TokenRefresh tokenRefresh);
}
