from fastapi import APIRouter, Depends, BackgroundTasks, Request
from sqlalchemy.orm import Session
import logging
from api.mapper.user_mappers import user_create_schema_to_user_create_dto
from api.schemas.user_schemas import LoginRequest, Token, UserCreate
from application.use_cases.user_use_cases import login_use_case, create_user_use_case
from core.db.database import get_db

router = APIRouter()

@router.get("/login")
def login(login_request: LoginRequest, request: Request, db: Session = Depends(get_db)) -> Token:
    return login_use_case(login_request.username, login_request.password, db)

@router.post("/users")
def create_user(user: UserCreate, background_tasks: BackgroundTasks, request: Request, db: Session = Depends(get_db)) -> None:
    logging.info("create_user %s", user.email)
    return create_user_use_case(user_create_schema_to_user_create_dto(user), db, background_tasks)



