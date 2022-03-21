package utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.DtoUserResponse;

import java.io.IOException;

public class Utils {

    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";

    public static DtoUserResponse mapper(Object response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DtoUserResponse dtoUserResponse = mapper.readValue((JsonParser) response, DtoUserResponse.class);
        return dtoUserResponse;
    }
}
