package dto;

import lombok.Data;

@Data
public class DtoUser {

    public String email;
    public String password;
    public String name;

    public DtoUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
