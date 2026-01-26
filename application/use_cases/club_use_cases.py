import logging

from sqlalchemy.orm import Session

from application.dtos.club_dtos import ClubCreateDto
from application.services.club_service import create_club_service
from application.services.emails.club_email import send_created_club_email
from application.services.emails.user_emails import send_verify_user_email
from application.services.user_service import create_user_service
from fastapi import  BackgroundTasks

from core.exceptions.user_exceptions import UserNotActivatedError


def create_club_use_case(club_create_dto: ClubCreateDto, background_tasks: BackgroundTasks, db: Session):
    logging.info("create_club_use_case: %s ",{club_create_dto.name})
    user = club_create_dto.user
    clubDto = create_club_service(club_create_dto, db)
    background_tasks.add_task(send_created_club_email, user.email, club_create_dto.name)
    user.is_admin = True
    user.club_id = clubDto.club_id

    created = create_user_service(user=user, db=db)
    background_tasks.add_task(send_verify_user_email, user.email)
    if not created:
        raise UserNotActivatedError(user.email)