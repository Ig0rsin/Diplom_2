package constants;

public class ErrorMessages {
    public static final String CREATE_USER_ALREADY_EXISTS = "User already exists";
    public static final String CREATE_USER_NOT_ENOUGH_DATA = "Email, password and name are required fields";
    public static final String LOGIN_USER_INCORRECT_CREDENTIALS = "email or password are incorrect";
    public static final String USER_NOT_AUTHORIZED = "You should be authorised";
    public static final String CREATE_ORDER_WITHOUT_ID = "Ingredient ids must be provided";
    public static final String CREATE_ORDER_WRONG_HASH = "One or more ids provided are incorrect";
}
