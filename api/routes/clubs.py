import logging

from fastapi import APIRouter, Depends, BackgroundTasks, Request
from sqlalchemy.orm import Session

from api.mapper.club_mapper import club_create_schema_to_club_create_dto
from api.schemas.club_schemas import ClubCreate
from application.use_cases.club_use_cases import create_club_use_case
from core.db.database import get_db

router = APIRouter()

@router.post("/clubs")
def create_club(create_club: ClubCreate, background_tasks: BackgroundTasks, request: Request, db: Session = Depends(get_db)):
    logging.info("create_club: %s ",{create_club.name})
    return create_club_use_case(club_create_schema_to_club_create_dto(create_club), background_tasks, db)
