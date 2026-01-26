from sqlalchemy.orm import Session

from application.dtos.club_dtos import ClubCreateDto, ClubDto
from domain.models.invoice_models import PlanModel, ClubPlanModel


def create_plan_club_respository(club: ClubDto, plan : int, db: Session) -> None:
    db.add(ClubPlanModel(club_id = club.club_id, plan = plan, extra_sections = 0.0))