import logging

from application.services.emails.email import send_email


def send_created_club_email(to: str, club_name:str) -> None:
    content = "Mensaje para avisar de que el club ha sido creado"
    subject = "Club %s creado con exito!", club_name
    logging.info("send_created_club_email: %s", to)
    send_email(to = to, content = content, subject = subject)