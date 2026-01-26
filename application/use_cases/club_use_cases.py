import logging

from sqlalchemy.orm import Session

from application.dtos.club_dtos import ClubCreateDto
from application.dtos.user_dtos import UserCreateDTO
from application.services.club_service import create_club_service
from application.services.emails.club_email import send_created_club_email
from application.services.emails.user_emails import send_verify_user_email
from application.services.invoice_services import  create_plan_club_service
from application.services.user_service import create_user_if_needed_and_allow_email
from fastapi import  BackgroundTasks


def create_club_use_case(club_create_dto: ClubCreateDto, background_tasks: BackgroundTasks, db: Session):
    logging.info("create_club_use_case: %s ",{club_create_dto.name})
    user: UserCreateDTO = club_create_dto.user
    clubDto = create_club_service(club_create_dto, db)
    create_plan_club_service(clubDto, db)
    background_tasks.add_task(send_created_club_email, user.email, clubDto.name)
    user.is_admin = True
    user.club_id = clubDto.club_id

    send_email = create_user_if_needed_and_allow_email(user=user, db=db)
    if send_email:
        background_tasks.add_task(send_verify_user_email, user.email)