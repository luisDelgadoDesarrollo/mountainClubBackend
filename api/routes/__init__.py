from .users import router as users_router
from .clubs import router as bookings_router
from .health import router as health_router

routers = [
    users_router,
    bookings_router,
    health_router,
]
