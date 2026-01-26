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