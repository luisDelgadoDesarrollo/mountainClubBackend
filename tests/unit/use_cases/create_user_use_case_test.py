from unittest.mock import Mock
import pytest

from application.dtos.user_dtos import UserCreateDTO
from application.use_cases.user_use_cases import create_user_use_case
from core.exceptions.base_exceptions import DomainError
from core.exceptions.user_exceptions import UserNotActivatedError


def test_create_user_use_case_created_sends_email(mocker):
    # Arrange
    db = Mock()
    background_tasks = Mock()

    user = UserCreateDTO(
        email="user@tests.com",
        password="password",
        first_name="Test",
        last_name="User",
        club_id=1
    )
    mocker.patch(
        "application.use_cases.user_use_cases.check_club_exists_service",
        return_value=True
    )
    mocker.patch(
        "application.use_cases.user_use_cases.create_user_service",
        return_value=True
    )
    mocker.patch(
        "application.use_cases.user_use_cases.send_verify_user_email"
    )

    create_user_use_case(
        user=user,
        db=db,
        background_tasks=background_tasks
    )

    # Assert
    background_tasks.add_task.assert_called_once()


def test_create_user_use_case_not_activated_sends_email_and_raises(mocker):
    # Arrange
    db = Mock()
    background_tasks = Mock()

    user = UserCreateDTO(
        email="user@tests.com",
        password="password",
        first_name="Test",
        last_name="User",
        club_id=1
    )
    mocker.patch(
        "application.use_cases.user_use_cases.check_club_exists_service",
        return_value=True
    )
    mocker.patch(
        "application.use_cases.user_use_cases.create_user_service",
        return_value=False
    )
    mocker.patch(
        "application.use_cases.user_use_cases.send_verify_user_email"
    )

    with pytest.raises(UserNotActivatedError):
        create_user_use_case(
            user=user,
            db=db,
            background_tasks=background_tasks
        )
    background_tasks.add_task.assert_called_once()


def test_create_user_use_case_propagates_service_exception(mocker):
    db = Mock()
    background_tasks = Mock()

    user = UserCreateDTO(
        email="user@tests.com",
        password="password",
        first_name="Test",
        last_name="User",
        club_id=1
    )
    mocker.patch(
        "application.use_cases.user_use_cases.check_club_exists_service",
        return_value=True
    )
    mocker.patch(
        "application.use_cases.user_use_cases.create_user_service",
        side_effect=DomainError("boom")
    )

    with pytest.raises(DomainError):
        create_user_use_case(
            user=user,
            db=db,
            background_tasks=background_tasks
        )
    background_tasks.add_task.assert_not_called()

def test_create_user_use_case_raise_club_not_found(mocker):
    db = Mock()
    background_tasks = Mock()

    user = UserCreateDTO(
        email="user@tests.com",
        password="password",
        first_name="Test",
        last_name="User",
        club_id=1
    )
    mocker.patch(
        "application.use_cases.user_use_cases.check_club_exists_service",
        return_value=False
    )

    with pytest.raises(DomainError):
        create_user_use_case(
            user=user,
            db=db,
            background_tasks=background_tasks
        )
    background_tasks.add_task.assert_not_called()

