from sqlalchemy import (
    Column,
    Integer,
    String,
    Boolean,
    Text,
    DateTime, Numeric, ForeignKey
)
from sqlalchemy.sql import func
from domain.models.base import Base


class ClubModel(Base):
    __tablename__ = "club"

    club_id = Column(Integer, primary_key=True, autoincrement=True)

    name = Column(String(255), nullable=False)
    nif = Column(String(50), nullable=False, unique=True)

    description = Column(Text)
    logo = Column(String(255))

    created_at = Column(DateTime, nullable=False, server_default=func.now())
    created_by = Column(Integer)

    # Features / secciones
    has_inicio = Column(Boolean, nullable=False, default=False)
    has_secciones = Column(Boolean, nullable=False, default=False)
    has_galeria = Column(Boolean, nullable=False, default=False)
    has_enlaces = Column(Boolean, nullable=False, default=False)
    has_contacto = Column(Boolean, nullable=False, default=False)
    has_federarse = Column(Boolean, nullable=False, default=False)
    has_tienda = Column(Boolean, nullable=False, default=False)
    has_calendario = Column(Boolean, nullable=False, default=False)
    has_conocenos = Column(Boolean, nullable=False, default=False)
    has_noticias = Column(Boolean, nullable=False, default=False)
    has_foro = Column(Boolean, nullable=False, default=False)
    has_estatutos = Column(Boolean, nullable=False, default=False)
    has_normas = Column(Boolean, nullable=False, default=False)
    has_hazte_socio = Column(Boolean, nullable=False, default=False)


