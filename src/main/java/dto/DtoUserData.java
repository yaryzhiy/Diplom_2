package dto;

public class DtoUserData {

    public boolean success;
    public User user;

    public DtoUserData(boolean success, User user) {
        this.success = success;
        this.user = user;
    }

    public class User {

        public String email;
        public String name;

        public User(String email, String name) {
            this.email = email;
            this.name = name;
        }
    }
}
