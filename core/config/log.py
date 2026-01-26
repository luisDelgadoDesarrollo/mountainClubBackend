import logging
import uuid
from contextvars import ContextVar
from fastapi import Request

request_id_ctx: ContextVar[str] = ContextVar(
    "request_id",
    default="-"
)
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - [%(request_id)s] - %(message)s",
)

class RequestIdFilter(logging.Filter):
    def filter(self, record):
        record.request_id = request_id_ctx.get()
        return True


def configure_logging():
    logging.basicConfig(
        level=logging.INFO,
        format="%(asctime)s - %(levelname)s - [%(request_id)s] - %(message)s",
    )

    logging.getLogger().addFilter(RequestIdFilter())



async def add_request_id_middleware(request: Request, call_next):
    request_id = request.headers.get("X-Request-ID", str(uuid.uuid4()))

    request_id_ctx.set(request_id)

    response = await call_next(request)
    response.headers["X-Request-ID"] = request_id
    return response

