package luis.delgado.clubmontana.backend.domain.model;

import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Club {
    private Long clubId;
    private String name;
    private String nif;
    private String description;
    private String logo;
    private String url;
    private String contactEmail;
    private LocalDateTime createdAt;
    private Long createdBy;
    private boolean hasInicio;
    private boolean hasSecciones;
    private boolean hasGaleria;
    private boolean hasEnlaces;
    private boolean hasContacto;
    private boolean hasFederarse;
    private boolean hasTienda;
    private boolean hasCalendario;
    private boolean hasConocenos;
    private boolean hasNoticias;
    private boolean hasForo;
    private boolean hasEstatutos;
    private boolean hasNormas;
    private boolean hasHazteSocio;
}
