package dto;

public class DtoUserResponse {

    public DtoUserData dtoUserData;
    public String accessToken;
    public String refreshToken;

    public DtoUserResponse(DtoUserData dtoUserData, String accessToken, String refreshToken) {
        this.dtoUserData = dtoUserData;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
