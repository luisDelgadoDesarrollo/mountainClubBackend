package luis.delgado.clubmontana.backend.unit.useCases;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import luis.delgado.clubmontana.backend.application.useCases.BylawsUseCaseImpl;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.services.FileStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class ByLawsUseCaseTest {

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private BylawsUseCaseImpl bylawsUseCase;

    @Test
    void save_happyPath() {
        Long clubId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        bylawsUseCase.save(clubId, file);

        verify(fileStorageService).savePdf(
                clubId,
                file,
                ImageType.BY_LAWS,
                "pylaws"
        );
    }

    @Test
    void getBylaws_happyPath() {
        Long clubId = 1L;
        Resource resource = mock(Resource.class);

        when(fileStorageService.getPdf(clubId, ImageType.BY_LAWS, "pylaws"))
                .thenReturn(resource);

        Resource result = bylawsUseCase.getBylaws(clubId);

        assertThat(result).isEqualTo(resource);
    }

}
