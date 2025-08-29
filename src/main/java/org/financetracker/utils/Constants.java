package org.financetracker.utils;

public class Constants {
    public class ValidationRegex {
        public static final String EMAIL_REGEX="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        public static final String PASSWORD_REGEX="/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/\n";
    }
}
