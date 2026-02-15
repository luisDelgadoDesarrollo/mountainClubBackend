package luis.delgado.clubmontana.backend.domain.services;

import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface RequestPartUtils {
  <T> T parseAndValidate(String data, Class<T> clazz);

    Map<String, MultipartFile> toFileMap(List<MultipartFile> files);
}
