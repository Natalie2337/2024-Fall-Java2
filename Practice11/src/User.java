public class User {
    @MinLength
    @CustomValidation(rule = Rule.ALL_LOWERCASE)
    private String username;
    @MinLength(min=8)
    @CustomValidation(rule = Rule.NO_USERNAME)
    @CustomValidation(rule = Rule.HAS_BOTH_DIGITS_AND_LETTERS)
    private String password;



    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
