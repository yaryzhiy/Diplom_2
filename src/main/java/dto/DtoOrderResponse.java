package dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DtoOrderResponse {

    @JsonProperty("success")
    public boolean success;

    @JsonProperty("name")
    public String name;

    @JsonProperty("order")
    public OrderInfo order;

    public static class OrderInfo {

        @JsonProperty("ingredients")
        public ArrayList<DtoIngredientsResponse.DtoIngredient> ingredients;

        @JsonProperty("_id")
        public String id;

        @JsonProperty("owner")
        public Owner owner;

        @JsonProperty("status")
        public String status;

        @JsonProperty("name")
        public String name;

        @JsonProperty("createdAt")
        public String createdAt;

        @JsonProperty("updatedAt")
        public String updatedAt;

        @JsonProperty("number")
        public int number;

        @JsonProperty("price")
        public int price;
    }

    public static class Owner {

        @JsonProperty("name")
        public String name;

        @JsonProperty("email")
        public String email;

        @JsonProperty("createdAt")
        public String createdAt;

        @JsonProperty("updatedAt")
        public String updatedAt;
    }
}
