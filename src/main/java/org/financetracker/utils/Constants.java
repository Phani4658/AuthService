package org.financetracker.utils;

public class Constants {
    public class ValidationRegex {
        public static final String EMAIL_REGEX="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        public static final String PASSWORD_REGEX="/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/\n";
    }

    public class Routes {
        public static final String SIGN_UP="/api/auth/signup";
        public static final String SIGN_IN="/api/auth/login";
        public static final String REFRESH_TOKEN="/api/auth/refresh-token";
    }
}
