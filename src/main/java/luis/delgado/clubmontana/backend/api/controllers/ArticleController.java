package luis.delgado.clubmontana.backend.api.controllers;

import java.util.Map;
import luis.delgado.clubmontana.backend.api.dtos.CreateArticleDto;
import luis.delgado.clubmontana.backend.api.dtos.IdResponseDto;
import luis.delgado.clubmontana.backend.api.mappers.ArticleControllerMapper;
import luis.delgado.clubmontana.backend.domain.userCases.ArticleUseCases;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/clubs/{clubId}/articles")
public class ArticleController {

  private final ArticleControllerMapper articleControllerMapper;
  private final ArticleUseCases articleUseCases;

  public ArticleController(
      ArticleControllerMapper articleControllerMapper, ArticleUseCases articleUseCases) {
    this.articleControllerMapper = articleControllerMapper;
    this.articleUseCases = articleUseCases;
  }

  @PostMapping
  public ResponseEntity<IdResponseDto> post(
      @PathVariable Long clubId,
      @RequestPart("article") CreateArticleDto createArticleDto,
      @RequestParam Map<String, MultipartFile> files) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            articleControllerMapper.articleToIdResponseDto(
                articleUseCases.create(
                    clubId,
                    articleControllerMapper.createArticleDtoToArticle(createArticleDto),
                    files)));
  }
}
