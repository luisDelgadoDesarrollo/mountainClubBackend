from unittest.mock import Mock

import pytest
from api.schemas.user_schemas import Token
from application.use_cases.user_use_cases import login_use_case
from core.exceptions.base_exceptions import DomainError


def test_login_use_case_returns_token(mocker):
    db = Mock()
    mocker.patch(
        "application.use_cases.user_use_cases.login_service",
        return_value=None
    )
    mocker.patch(
        "application.use_cases.user_use_cases.create_access_token",
        return_value="fake-jwt-token"
    )
    token = login_use_case(
        username="user@tests.com",
        password="password",
        db=db
    )
    assert isinstance(token, Token)
    assert token.access_token == "fake-jwt-token"
    assert token.token_type == "bearer"

def test_login_use_case_does_not_continue_if_service_raises(mocker):
    db = Mock()
    mocker.patch(
        "application.use_cases.user_use_cases.login_service",
        side_effect=DomainError("boom")
    )
    create_token_mock = mocker.patch(
        "application.use_cases.user_use_cases.create_access_token"
    )
    with pytest.raises(Exception):
        login_use_case(
            username="user@tests.com",
            password="password",
            db=db
        )
    create_token_mock.assert_not_called()