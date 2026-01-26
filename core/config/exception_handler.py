import logging

from core.exceptions.base_exceptions import DomainError, AuthError
from fastapi.responses import JSONResponse
from fastapi.requests import Request

async def domain_exception_handler(
    request: Request,
    exc: DomainError
):
    return JSONResponse(
        status_code=409,
        content={
            "detail": str(exc)
        }
    )

async def auth_exception_handler(
    request: Request,
    exc: AuthError
):
    return JSONResponse(
        status_code=401,
        content={
            "detail": str(exc)
        }
    )

