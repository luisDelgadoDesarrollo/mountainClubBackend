from dataclasses import dataclass
from typing import Optional

from application.dtos.user_dtos import UserCreateDTO

@dataclass
class ClubCreateDto:
    name: str
    nif: str
    description: Optional[str]
    logo: Optional[str]
    has_inicio: bool
    has_secciones: bool
    has_galeria: bool
    has_enlaces: bool
    has_contacto: bool
    has_federarse: bool
    has_tienda: bool
    has_calendario: bool
    has_conocenos: bool
    has_noticias: bool
    has_foro: bool
    has_estatutos: bool
    has_normas: bool
    has_hazte_socio: bool
    plan: int
    user: UserCreateDTO

@dataclass
class ClubDto:
    club_id: int
    name: str
    nif: str
    description: Optional[str]
    logo: Optional[str]
    has_inicio: bool
    has_secciones: bool
    has_galeria: bool
    has_enlaces: bool
    has_contacto: bool
    has_federarse: bool
    has_tienda: bool
    has_calendario: bool
    has_conocenos: bool
    has_noticias: bool
    has_foro: bool
    has_estatutos: bool
    has_normas: bool
    has_hazte_socio: bool