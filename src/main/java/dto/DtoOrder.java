package dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DtoOrder {

    public ArrayList<String> ingredients;

    public DtoOrder(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> setIngredient(String ingredient) {
        ingredients.add(ingredient);
        return ingredients;
    }
}
