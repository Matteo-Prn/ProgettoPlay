package com.play.exceptions.service.user;

import com.play.exceptions.ApplicationException;
import com.play.exceptions.code.user.UserServiceErrorCode;

public class UserServiceException extends ApplicationException {

  public UserServiceException(UserServiceErrorCode userServiceErrorCode) {
    super(userServiceErrorCode);
  }

  public UserServiceException(UserServiceErrorCode userServiceErrorCode, Throwable cause) {
    super(userServiceErrorCode, cause);
  }

  public UserServiceException(String message, UserServiceErrorCode userServiceErrorCode) {
    super(message, userServiceErrorCode);
  }
}
