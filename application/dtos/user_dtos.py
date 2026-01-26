from dataclasses import dataclass
from datetime import date
from typing import Optional


@dataclass
class UserCreateDTO:
    email: str
    password: str
    first_name: str
    last_name: str
    dni: Optional[str] = None
    birth_date: Optional[date] = None
    sex: str = "O"
    club_id: int = 0
    member_since: date | None = None
    phone: Optional[str] = None
    address: Optional[str] = None
    city: Optional[str] = None
    postal_code: Optional[str] = None
    country: str = "España"
    is_admin: bool = False

@dataclass
class UserDTO:
    email: str
    first_name: str
    last_name: str
    dni: Optional[str] = None
    birth_date: Optional[date] = None
    sex: str = "O"
    club_id: int = 0
    member_since: date | None = None
    phone: Optional[str] = None
    address: Optional[str] = None
    city: Optional[str] = None
    postal_code: Optional[str] = None
    country: str = "España"
    is_active: bool = False
    is_admin: bool = False
    email_verified:  bool = False

@dataclass
class UserAuthDTO:
    email: str
    password: str
    is_active: bool
    email_verified: bool