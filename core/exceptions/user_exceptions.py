from core.exceptions.base_exceptions import DomainError, AuthError


class UserNotActivatedError(DomainError):
    def __init__(self, email: str):
        super().__init__(
            "Usuario creado, falta activación. "
            f"Hemos reenviado un email a {email}"
        )

class UserAlreadyExistError(DomainError):
    def __init__(self, email: str):
        super().__init__(
            f"Usuario ya creado con este mismo email, {email} "
        )

class UserPasswordIncorrect(AuthError):
    def __init__(self):
        super().__init__(
            f"Contraseña incorrecta"
        )

class UserNotFoundError(AuthError):
    def __init__(self, email:str):
        super().__init__(
            f"Usuario no encontrado, {email} "
        )