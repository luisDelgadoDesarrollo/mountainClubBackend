from api.schemas.club_schemas import ClubCreateSchema
from application.dtos.club_dtos import  ClubCreateDto


def club_create_schema_to_club_create_dto(user: ClubCreateSchema) -> ClubCreateDto:
    return ClubCreateDto(**user.model_dump())