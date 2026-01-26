import logging

from sqlalchemy.orm import Session
from application.dtos.user_dtos import UserCreateDTO, UserDTO, UserAuthDTO
from domain.mappers.user_mappers import user_create_dto_to_user_model, user_model_to_user_dto, \
    user_model_to_user_auth_dto
from domain.models.user_models import UserModel


def create_user_repository(user: UserCreateDTO, db: Session) -> None:
    logging.info("create_user_repository email=%s", user.email)
    db.add(user_create_dto_to_user_model(user))

def get_user_by_email_repository(email: str, db: Session) -> UserDTO:
    logging.info("get_user_by_email_repository email=%s", email)
    return user_model_to_user_dto(db.query(UserModel).filter(UserModel.email == email).first())

def get_user_auth_by_email(email: str,  db: Session) -> UserAuthDTO:
    logging.info("get_user_auth_by_email email=%s", email)
    userModel = db.query(UserModel).filter(UserModel.email == email).first()
    return user_model_to_user_auth_dto(userModel) if userModel is not None else None

