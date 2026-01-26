from fastapi import APIRouter, Depends
from typing import Annotated
from api.dependencies.auth import get_current_user

router = APIRouter()

@router.get("/testToken")
def test_token(user: Annotated[dict, Depends(get_current_user)]):
    return {"token": user}

@router.get("/")
def test_route():
    return {"status": "ok"}