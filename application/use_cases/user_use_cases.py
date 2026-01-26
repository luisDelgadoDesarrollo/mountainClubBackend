import logging
from fastapi import HTTPException, BackgroundTasks
from sqlalchemy.orm import Session
from api.schemas.user_schemas import Token
from application.dtos.user_dtos import UserCreateDTO
from application.services.club_service import check_club_exists_service
from application.services.emails.user_emails import send_verify_user_email
from application.services.user_service import create_user_service, login_service
from core.exceptions.club_exceptions import ClubNotFoundException
from core.exceptions.user_exceptions import UserNotActivatedError, UserAlreadyExistError
from core.security.jwt import create_access_token


def login_use_case(username: str, password: str,  db: Session) -> Token:
    logging.info("login_use_case %s", username)
    login_service(username = username, password = password, db = db)
    token = create_access_token({"sub": username})
    return Token(access_token=token, token_type="bearer")



def create_user_use_case(user: UserCreateDTO, db: Session, background_tasks: BackgroundTasks) -> None:
    logging.info("create_user_use_case %s", user.email)
    if not check_club_exists_service(user.club_id):
        raise ClubNotFoundException(user.club_id)
    created = create_user_service(user=user, db=db)
    background_tasks.add_task(send_verify_user_email, user.email)
    if not created:
        raise UserNotActivatedError(user.email)




