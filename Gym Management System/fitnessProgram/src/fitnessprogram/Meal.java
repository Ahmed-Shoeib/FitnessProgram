package fitnessprogram_alubi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Meal implements Serializable{

    private String name;
    private int calories;
    private MealType type;
    private ArrayList<String> ingredients;

    public Meal(String name, int calories, MealType type, ArrayList<String> ingredients) {
        this.name = name;
        this.calories = calories;
        this.type = type;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public MealType getType() {
        return type;
    }

    public void setType(MealType type) {
        this.type = type;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Meal{" + "name=" + name + ", calories=" + calories + ", type=" + type + ", ingredients=" + ingredients + '}';
    }

    public static void writeMeals(ArrayList<Meal> list) {
        try {
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(new File("meals.bin")));
            o.writeObject(list);
            o.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static ArrayList<Meal> readMeals() {
        ArrayList<Meal> list = new ArrayList<>();
        try {
            ObjectInputStream i = new ObjectInputStream(new FileInputStream(new File("meals.bin")));
            list = (ArrayList<Meal>) i.readObject();
            i.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return list;
    }
}
