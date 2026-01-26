from core.exceptions.base_exceptions import DomainError


class ClubNotFoundException(DomainError):
    def __init__(self, club_id:int):
        super().__init__(
            f"Club no encontrado, {club_id} "
        )