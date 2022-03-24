package dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DtoIngredientsResponse {

    @JsonProperty("success")
    public boolean success;

    @JsonProperty("data")
    public ArrayList<DtoIngredient> data;

    public static class DtoIngredient {

        @JsonProperty("_id")
        public String id;

        @JsonProperty("name")
        public String name;

        @JsonProperty("type")
        public String type;

        @JsonProperty("proteins")
        public int proteins;

        @JsonProperty("fat")
        public int fat;

        @JsonProperty("carbohydrates")
        public int carbohydrates;

        @JsonProperty("calories")
        public int calories;

        @JsonProperty("price")
        public int price;

        @JsonProperty("image")
        public String image;

        @JsonProperty("image_mobile")
        public String imageMobile;

        @JsonProperty("image_large")
        public String imageLarge;

        @JsonProperty("__v")
        public int v;
    }
}