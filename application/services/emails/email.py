import logging


def send_email(to, subject, content) -> None:
    logging.info(
        "sending email | to=%s | subject=%s | content=%s",
        to,
        subject,
        content,
    )