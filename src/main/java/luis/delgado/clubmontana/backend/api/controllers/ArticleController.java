package luis.delgado.clubmontana.backend.api.controllers;

import jakarta.validation.Valid;
import java.util.List;
import luis.delgado.clubmontana.backend.api.dtos.ArticleDto;
import luis.delgado.clubmontana.backend.api.dtos.CreateArticleDto;
import luis.delgado.clubmontana.backend.api.dtos.ResponseDto;
import luis.delgado.clubmontana.backend.api.mappers.ArticleControllerMapper;
import luis.delgado.clubmontana.backend.core.annotations.ArticleId;
import luis.delgado.clubmontana.backend.core.annotations.ClubId;
import luis.delgado.clubmontana.backend.domain.model.Article;
import luis.delgado.clubmontana.backend.domain.services.RequestPartUtils;
import luis.delgado.clubmontana.backend.domain.userCases.ArticleUseCases;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/clubs/{club}/articles")
public class ArticleController {

  private final ArticleControllerMapper articleControllerMapper;
  private final ArticleUseCases articleUseCases;
  private final RequestPartUtils requestPartUtils;

  public ArticleController(
      ArticleControllerMapper articleControllerMapper,
      ArticleUseCases articleUseCases,
      RequestPartUtils requestPartUtils) {
    this.articleControllerMapper = articleControllerMapper;
    this.articleUseCases = articleUseCases;
    this.requestPartUtils = requestPartUtils;
  }

  @PostMapping
  public ResponseEntity<ResponseDto> post(
      @ClubId Long clubId,
      @RequestPart("article") @Valid CreateArticleDto createArticleDto,
      @RequestParam(value = "files", required = false) List<MultipartFile> files) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            articleControllerMapper.articleToIdResponseDto(
                articleUseCases.create(
                    clubId,
                    articleControllerMapper.createArticleDtoToArticle(createArticleDto),
                    requestPartUtils.toFileMap(files))));
  }

  @PutMapping("/{article}")
  public ResponseEntity<ResponseDto> put(
      @ClubId Long clubId,
      @ArticleId Long articleId,
      @RequestPart("article") @Valid CreateArticleDto createArticleDto,
      @RequestParam(value = "files", required = false) List<MultipartFile> files) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            articleControllerMapper.articleToIdResponseDto(
                articleUseCases.update(
                    clubId,
                    articleId,
                    articleControllerMapper.createArticleDtoToArticle(createArticleDto),
                    requestPartUtils.toFileMap(files))));
  }

  @DeleteMapping("/{article}")
  public ResponseEntity<Void> delete(@ClubId Long clubId, @ArticleId Long articleId) {
    articleUseCases.delete(clubId, articleId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping("/{article}")
  public ResponseEntity<ArticleDto> get(@ClubId Long clubId, @ArticleId Long articleId) {
    Pair<Article, List<String>> articleListPair = articleUseCases.get(clubId, articleId);
    return ResponseEntity.ok(articleControllerMapper.articleToArticleDto(articleListPair));
  }

  @GetMapping
  public ResponseEntity<Page<ArticleDto>> getAll(@ClubId Long clubId, Pageable pageable) {
    return ResponseEntity.ok(
        articleUseCases.getAll(clubId, pageable).map(articleControllerMapper::articleToArticleDto));
  }
}
