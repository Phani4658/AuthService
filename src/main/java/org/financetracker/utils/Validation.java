package org.financetracker.utils;

import org.financetracker.DTOs.request.UserInfoDto;

public class Validation {
    public static Boolean validateUserEmailAndPassword(UserInfoDto user) {
        Boolean isEmailValid = user.getEmail().matches(Constants.ValidationRegex.EMAIL_REGEX);
        Boolean isPasswordValid = user.getPassword().matches(Constants.ValidationRegex.PASSWORD_REGEX);
        return isEmailValid && isPasswordValid;
    }
}
