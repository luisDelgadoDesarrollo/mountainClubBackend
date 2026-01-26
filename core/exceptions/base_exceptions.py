class DomainError(Exception):
    """Base class for domain exceptions"""
    pass

class AuthError(DomainError):
    """Errores de autenticación"""
    pass

