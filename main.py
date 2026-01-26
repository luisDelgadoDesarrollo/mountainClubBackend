from fastapi import FastAPI
from fastapi.responses import JSONResponse
from fastapi.requests import Request
from api.routes import routers
from core.config.exception_handler import domain_exception_handler, auth_exception_handler
from core.config.log import add_request_id_middleware, configure_logging
from core.exceptions.base_exceptions import DomainError, AuthError

configure_logging()

app = FastAPI()

for router in routers:
    app.include_router(router)

app.middleware("http")(add_request_id_middleware)
app.add_exception_handler(AuthError, auth_exception_handler)
app.add_exception_handler(DomainError, domain_exception_handler)

