package dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DtoOrder {

    public ArrayList<String> ingredients;

    public DtoOrder() {
        this.ingredients = new ArrayList<>();
    }

    public DtoOrder(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setIngredient(String ingredient) {
        ingredients.add(ingredient);
    }
}
