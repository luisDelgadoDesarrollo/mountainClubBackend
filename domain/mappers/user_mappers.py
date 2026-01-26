import logging
from dataclasses import asdict

from application.dtos.user_dtos import UserCreateDTO, UserDTO, UserAuthDTO
from domain.models.user_models import UserModel


def user_create_dto_to_user_model(
    user: UserCreateDTO
) -> UserModel:
    logging.info("user_create_dto_to_user_create_model %s", user.email)
    return UserModel(**asdict(user))

def user_model_to_user_dto(
    user: UserModel
) -> UserDTO:
    logging.info("user_create_dto_to_user_create_model %s", user)
    return UserDTO(email=user.email,
                   first_name=user.first_name,
                   last_name=user.last_name,
                   dni=user.dni,
                   birth_date=user.birth_date,
                   sex=user.sex,
                   club_id=user.club_id,
                   member_since=user.member_since,
                   phone=user.phone,
                   address=user.address,
                   city=user.city,
                   postal_code=user.postal_code,
                   country=user.country,
                   is_active=user.is_active,
                   is_admin=user.is_admin,
                   email_verified=user.email_verified)

def user_model_to_user_auth_dto(user_model: UserModel) -> UserAuthDTO:
    logging.info("user_model_to_user_auth_dto %s", user_model.email)
    return UserAuthDTO(email=user_model.email,
                       email_verified=user_model.email_verified,
                       password=user_model.password,
                       is_active=user_model.is_active)
