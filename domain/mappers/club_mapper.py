import logging
from dataclasses import asdict
from application.dtos.club_dtos import ClubDto, ClubCreateDto
from domain.models.club_models import ClubModel


def club_create_dto_to_club_model(club: ClubCreateDto) :
    logging.info("club_create_dto_to_club_model %s", club.name)
    return ClubModel(**asdict(club))

def club_model_to_club_dto(club_model: ClubModel) -> ClubDto:
    logging.info("club_model_to_club_dto %s", club_model.name)

    return ClubDto(club_id=club_model.club_id,
                   name=club_model.name,
                   nif=club_model.nif,
                   description=club_model.description,
                   logo=club_model.logo,
                   has_inicio=club_model.has_inicio,
                   has_secciones=club_model.has_secciones,
                   has_galeria=club_model.has_galeria,
                   has_enlaces=club_model.has_enlaces,
                   has_contacto=club_model.has_contacto,
                   has_federarse=club_model.has_federarse,
                   has_tienda=club_model.has_tienda,
                   has_calendario=club_model.has_calendario,
                   has_conocenos=club_model.has_conocenos,
                   has_noticias=club_model.has_noticias,
                   has_foro=club_model.has_foro,
                   has_estatutos=club_model.has_estatutos,
                   has_normas=club_model.has_normas,
                   has_hazte_socio=club_model.has_hazte_socio)