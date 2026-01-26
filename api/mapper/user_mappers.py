from api.schemas.user_schemas import UserCreate
from application.dtos.user_dtos import UserCreateDTO


def user_create_schema_to_user_create_dto(user: UserCreate) -> UserCreateDTO:
    return UserCreateDTO(**user.model_dump())