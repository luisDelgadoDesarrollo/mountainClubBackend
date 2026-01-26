from typing import Optional

from pydantic import BaseModel, Field


class UserCreateSchema:
    pass


class ClubCreateSchema(BaseModel):
    name: str = Field(..., min_length=2, max_length=255)
    nif: str = Field(..., min_length=5, max_length=50)
    description: Optional[str] = None
    logo: Optional[str] = None

    has_inicio: bool = False
    has_secciones: bool = False
    has_galeria: bool = False
    has_enlaces: bool = False
    has_contacto: bool = False
    has_federarse: bool = False
    has_tienda: bool = False
    has_calendario: bool = False
    has_conocenos: bool = False
    has_noticias: bool = False
    has_foro: bool = False
    has_estatutos: bool = False
    has_normas: bool = False
    has_hazte_socio: bool = False

    # Campo extra (plan seleccionado)
    plan: int = Field(..., ge=1, le=3)

    # Usuario creador del club
    user: UserCreateSchema