package dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DtoUser {

    public String email;
    public String password;
    public String name;

    public DtoUser() {
    }

    public DtoUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
