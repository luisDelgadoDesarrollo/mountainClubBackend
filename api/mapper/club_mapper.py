from api.mapper.user_mappers import user_create_schema_to_user_create_dto
from api.schemas.club_schemas import ClubCreate
from application.dtos.club_dtos import  ClubCreateDto


def club_create_schema_to_club_create_dto(schema: ClubCreate) -> ClubCreateDto:
    return ClubCreateDto(
        name=schema.name,
        nif=schema.nif,
        description=schema.description,
        logo=schema.logo,
        has_inicio=schema.has_inicio,
        has_secciones=schema.has_secciones,
        has_galeria=schema.has_galeria,
        has_enlaces=schema.has_enlaces,
        has_contacto=schema.has_contacto,
        has_federarse=schema.has_federarse,
        has_tienda=schema.has_tienda,
        has_calendario=schema.has_calendario,
        has_conocenos=schema.has_conocenos,
        has_noticias=schema.has_noticias,
        has_foro=schema.has_foro,
        has_estatutos=schema.has_estatutos,
        has_normas=schema.has_normas,
        has_hazte_socio=schema.has_hazte_socio,
        user=user_create_schema_to_user_create_dto(schema.user)
    )