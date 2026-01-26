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


class PlanModel(Base):
    __tablename__ = "plan"

    plan_id = Column(Integer, primary_key=True, autoincrement=True)

    name = Column(String(100), nullable=False)
    description = Column(Text)

    # Comisión base (ej: 0.0500 = 5%)
    percentage = Column(Numeric(5, 4), nullable=False)

class ClubPlanModel(Base):
    __tablename__ = "club_plan"

    club_plan_id = Column(Integer, primary_key=True, autoincrement=True)

    club_id = Column(Integer, ForeignKey("club.club_id", ondelete="CASCADE"), nullable=False)
    plan_id = Column(Integer, ForeignKey("plan.plan_id", ondelete="RESTRICT"), nullable=False)

    # Coste extra por módulos adicionales
    extra_sections = Column(Numeric(10, 2), nullable=False, default=0.00)

    created_at = Column(DateTime, nullable=False, server_default=func.now())