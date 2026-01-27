package luis.delgado.clubmontana.backend.domain.model.enums;

import lombok.Getter;

public enum Sex {

    M(1L, "M"),
    H(2L, "H"),
    NS(3L, "NS");

    @Getter
    private final Long id;
    @Getter
    private final String code;

    Sex(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    public static Sex fromCode(String code) {
        for (Sex sex : values()) {
            if (sex.code.equalsIgnoreCase(code)) {
                return sex;
            }
        }
        throw new IllegalArgumentException("Invalid sex code: " + code);
    }
}
