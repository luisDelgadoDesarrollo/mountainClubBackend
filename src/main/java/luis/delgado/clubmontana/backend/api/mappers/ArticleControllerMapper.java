package luis.delgado.clubmontana.backend.api.mappers;

import luis.delgado.clubmontana.backend.api.dtos.CreateArticleDto;
import luis.delgado.clubmontana.backend.api.dtos.IdResponseDto;
import luis.delgado.clubmontana.backend.domain.model.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleControllerMapper {
  Article createArticleDtoToArticle(CreateArticleDto createArticleDto);

  @Mapping(target = "id", source = "articleId")
    IdResponseDto articleToIdResponseDto(Article article);
}
