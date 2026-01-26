import logging

from sqlalchemy.orm import Session

from application.dtos.club_dtos import ClubCreateDto, ClubDto
from domain.repositorys.club_repository import create_club_repository


def check_club_exists_service(club_id: int) -> bool:
    return True

def create_club_service(club_create_dto: ClubCreateDto, db:Session) -> ClubDto:
    logging.info("create_club_service: %s ",{club_create_dto.name})
    return create_club_repository(club_create_dto, db)