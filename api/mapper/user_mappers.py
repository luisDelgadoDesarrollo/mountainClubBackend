from api.schemas.user_schemas import UserCreate
from application.dtos.user_dtos import UserCreateDTO


def user_create_schema_to_user_create_dto(schema: UserCreate) -> UserCreateDTO:
    return UserCreateDTO(
        email=schema.email,
        password=schema.password,
        first_name=schema.first_name,
        last_name=schema.last_name,
        dni=schema.dni,
        birth_date=schema.birth_date,
        sex=schema.sex,
        club_id=schema.club_id,
        member_since=schema.member_since,
        phone=schema.phone,
        address=schema.address,
        city=schema.city,
        postal_code=schema.postal_code,
        country=schema.country,
    )
