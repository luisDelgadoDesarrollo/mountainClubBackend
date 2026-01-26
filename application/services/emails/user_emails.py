import logging

from application.dtos.user_dtos import UserCreateDTO
from application.services.emails.email import send_email


def send_verify_user_email(email: str) -> None:
    content = "Mensaje a enviar para indicar que el usuario tiene que verificar correo"
    subject = "Verificar usuario"
    logging.info("send_verify_user_email: %s", email)
    send_email(to = email, content = content, subject = subject)