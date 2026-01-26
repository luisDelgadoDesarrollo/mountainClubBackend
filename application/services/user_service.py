import logging

from sqlalchemy.orm import Session
from fastapi import BackgroundTasks

from application.dtos.user_dtos import UserCreateDTO, UserDTO
from core.exceptions.user_exceptions import UserNotActivatedError, UserPasswordIncorrect, UserNotFoundError, \
    UserAlreadyExistError
from core.security.password import hash_password, verify_password
from domain.repositorys.user_repository import create_user_repository, get_user_by_email_repository, \
    get_user_auth_by_email


def create_user_service(user: UserCreateDTO, db: Session) -> bool:
    logging.info("create_user_service %s", user.email)
    user.password = hash_password(user.password)
    userDto = get_user_by_email_service(email=user.email, db=db)
    if userDto is None:
        create_user_repository(user=user, db=db)
        return True
    if not userDto.email_verified or not userDto.is_active:
        logging.info("user exists but not active or not verified isActive=%s emailVerified=%s", userDto.is_active, userDto.email_verified)
        return False
    logging.info("user exists %s", userDto.email)
    raise UserAlreadyExistError(user.email)

def get_user_by_email_service(email: str, db: Session) -> UserDTO:
    logging.info("get_user_by_email_service %s", email)
    return get_user_by_email_repository(email = email, db = db)


def login_service(username: str, password: str, db: Session) -> None:
    logging.info("login_service %s", username)
    user_auth_dto = get_user_auth_by_email(email=username, db=db)
    if not user_auth_dto:
        logging.info("user not found %s", username)
        raise UserNotFoundError(username)
    if not user_auth_dto.is_active or not user_auth_dto.email_verified:
        logging.info("user dont activate or email dont verified isActive=%s, emailVerified=%s", user_auth_dto.is_active, user_auth_dto.email_verified)
        raise UserNotActivatedError(username)
    if not verify_password(password, user_auth_dto.password):
        logging.info("password incorrect %s", user_auth_dto.email)
        raise UserPasswordIncorrect()

