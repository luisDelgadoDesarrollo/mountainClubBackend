from sqlalchemy import (
    Column,
    Integer,
    String,
    Date,
    Boolean,
    DateTime,
    func,
)

from domain.models.base import Base


class UserModel(Base):
    __tablename__ = "users"

    # PK
    user_id = Column(Integer, primary_key=True, index=True)

    # Auth
    email = Column(String(255), nullable=False, unique=True, index=True)
    password = Column(String(255), nullable=False)

    # Datos personales
    first_name = Column(String(100), nullable=False)
    last_name = Column(String(150), nullable=False)

    dni = Column(String(15), unique=True, nullable=True)
    birth_date = Column(Date, nullable=True)
    sex = Column(String(1), nullable=False, default="O")

    # Club
    club_id = Column(Integer, nullable=False, index=True)
    member_since = Column(Date, nullable=False)

    # Contacto
    phone = Column(String(30), nullable=True)
    address = Column(String(255), nullable=True)
    city = Column(String(100), nullable=True)
    postal_code = Column(String(20), nullable=True)
    country = Column(String(100), nullable=False, default="España")

    # Estado
    is_active = Column(Boolean, nullable=False, default=False)
    is_admin = Column(Boolean, nullable=False, default=False)
    email_verified = Column(Boolean, nullable=False, default=False)

    # Auditoría
    created_at = Column(
        DateTime,
        nullable=False,
        server_default=func.now(),
    )
    updated_at = Column(
        DateTime,
        nullable=False,
        server_default=func.now(),
        onupdate=func.now(),
    )