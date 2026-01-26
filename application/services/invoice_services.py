from sqlalchemy.orm import Session

from application.dtos.club_dtos import ClubCreateDto, ClubDto
from domain.repositorys.invoice_repository import create_plan_club_respository


def get_plan_service(club: ClubDto):
    ## conseguir el precio de la suma de todos los productos, segun cuanto se van a gastar, se calcula el plan, igual a mas gasto, se hace un porcentaje de descuento
    return 1


def create_plan_club_service(club: ClubDto, db: Session) -> None:
    create_plan_club_respository(club, get_plan_service(club), db)
