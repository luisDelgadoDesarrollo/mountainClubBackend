from pydantic import BaseModel, EmailStr, Field
from datetime import date
from typing import Optional



class LoginRequest(BaseModel):
    username: str
    password: str

class Token(BaseModel):
    access_token: str
    token_type: str

class UserCreate(BaseModel):
    email: EmailStr = Field(
        ...,
        json_schema_extra={"example": "socio@clubmontana.es"}
    )
    password: str = Field(
        ...,
        min_length=8,
        json_schema_extra={"example": "passwordSeguro123"}
    )

    first_name: str = Field(
        ...,
        json_schema_extra={"example": "Juan"}
    )
    last_name: str = Field(
        ...,
        json_schema_extra={"example": "Pérez"}
    )

    dni: Optional[str] = Field(
        None,
        json_schema_extra={"example": "12345678Z"}
    )
    birth_date: Optional[date] = Field(
        None,
        json_schema_extra={"example": "1990-05-12"}
    )
    sex: Optional[str] = Field(
        "O",
        json_schema_extra={"example": "M"}
    )

    club_id: int = Field(
        ...,
        json_schema_extra={"example": 1}
    )
    member_since: date = Field(
        ...,
        json_schema_extra={"example": "2024-01-01"}
    )

    phone: Optional[str] = Field(
        None,
        json_schema_extra={"example": "+34 600 123 456"}
    )
    address: Optional[str] = Field(
        None,
        json_schema_extra={"example": "Calle Mayor 10"}
    )
    city: Optional[str] = Field(
        None,
        json_schema_extra={"example": "Zaragoza"}
    )
    postal_code: Optional[str] = Field(
        None,
        json_schema_extra={"example": "50001"}
    )
    country: Optional[str] = Field(
        "España",
        json_schema_extra={"example": "España"}
    )
