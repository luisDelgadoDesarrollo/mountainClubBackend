import pytest
from unittest.mock import Mock

from application.dtos.user_dtos import UserCreateDTO
from application.use_cases.club_use_cases import create_club_use_case
from core.exceptions.base_exceptions import DomainError
from core.exceptions.user_exceptions import UserNotActivatedError
def test_create_club_use_case_creates_club_and_admin_and_sends_emails(mocker):
    db = Mock()
    background_tasks = Mock()

    user = UserCreateDTO(
        email="admin@club.com",
        password="password",
        first_name="Admin",
        last_name="User"
    )

    club = Mock()
    club.name = "Club Test"
    club.user = user

    club_dto = Mock()
    club_dto.club_id = 42

    mocker.patch(
        "application.use_cases.club_use_cases.create_club_service",
        return_value=club_dto
    )

    mocker.patch(
        "application.use_cases.club_use_cases.create_user_service",
        return_value=True
    )

    send_created_mock = mocker.patch(
        "application.use_cases.club_use_cases.send_created_club_email"
    )

    send_verify_mock = mocker.patch(
        "application.use_cases.club_use_cases.send_verify_user_email"
    )

    create_club_use_case(
        club_create_dto=club,
        background_tasks=background_tasks,
        db=db
    )

    assert user.is_admin is True
    assert user.club_id == 42

    background_tasks.add_task.assert_any_call(
        send_created_mock, user.email, club.name
    )
    background_tasks.add_task.assert_any_call(
        send_verify_mock, user.email
    )


def test_create_club_use_case_user_not_activated_raises(mocker):
    db = Mock()
    background_tasks = Mock()

    user = UserCreateDTO(
        email="admin@club.com",
        password="password",
        first_name="Admin",
        last_name="User"
    )

    club = Mock()
    club.name = "Club Test"
    club.user = user

    club_dto = Mock()
    club_dto.club_id = 10

    mocker.patch(
        "application.use_cases.club_use_cases.create_club_service",
        return_value=club_dto
    )

    mocker.patch(
        "application.use_cases.club_use_cases.create_user_service",
        return_value=False
    )

    send_verify_mock = mocker.patch(
        "application.use_cases.club_use_cases.send_verify_user_email"
    )

    with pytest.raises(UserNotActivatedError):
        create_club_use_case(
            club_create_dto=club,
            background_tasks=background_tasks,
            db=db
        )


def test_create_club_use_case_propagates_service_exception(mocker):
    db = Mock()
    background_tasks = Mock()

    user = UserCreateDTO(
        email="admin@club.com",
        password="password",
        first_name="Admin",
        last_name="User"
    )

    club = Mock()
    club.name = "Club Test"
    club.user = user

    mocker.patch(
        "application.use_cases.club_use_cases.create_club_service",
        side_effect=DomainError("boom")
    )

    with pytest.raises(DomainError):
        create_club_use_case(
            club_create_dto=club,
            background_tasks=background_tasks,
            db=db
        )

    background_tasks.add_task.assert_not_called()
