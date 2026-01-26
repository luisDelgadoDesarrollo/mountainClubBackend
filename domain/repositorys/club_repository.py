import logging

from sqlalchemy.orm import Session

from application.dtos.club_dtos import ClubDto, ClubCreateDto
from domain.mappers.club_mapper import club_create_dto_to_club_model, club_model_to_club_dto


def create_club_repository(club:ClubCreateDto, db:Session) -> ClubDto:
    logging.info("create_club_repository :%s", club.name)
    model = club_create_dto_to_club_model(club)
    db.add(model)
    db.flush()
    return club_model_to_club_dto(model)
