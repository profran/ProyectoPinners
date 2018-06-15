package Classes;

/**
 *
 * @author user2
 */
public class user {

    private String userName;
    private String userPassword;

    public user(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

}
