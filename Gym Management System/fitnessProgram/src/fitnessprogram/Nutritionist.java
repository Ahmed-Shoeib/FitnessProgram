package fitnessprogram_alubi;

import static fitnessprogram_alubi.FitnessProgram_alubi.showError;
import static fitnessprogram_alubi.FitnessProgram_alubi.showMessage;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class Nutritionist extends User implements Serializable {

    public Nutritionist(String username, String password, String name, int age, String email, Gender gender) {
        super(username, password, name, age, email, gender);
    }

    private boolean addMeal(String name, int calories, MealType type, ArrayList<String> ingredients) {
        ArrayList<Meal> mealsList = Meal.readMeals();
        for (Meal m : mealsList) {
            if (m.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }
        mealsList.add(new Meal(name, calories, type, ingredients));
        Meal.writeMeals(mealsList);
        return true;
    }

    private boolean removeMeal(String name) {
        ArrayList<Meal> mealsList = Meal.readMeals();
        for (Meal m : mealsList) {
            if (m.getName().equalsIgnoreCase(name)) {
                mealsList.remove(m);
                Meal.writeMeals(mealsList);
                return true;
            }
        }
        return false;
    }

    private Meal searchMeal(String name) {
        ArrayList<Meal> mealsList = Meal.readMeals();
        for (Meal m : mealsList) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    private TableView<Meal> listMeals() {
        TableView<Meal> tableView = new TableView<>();

        TableColumn<Meal, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Meal, Integer> caloriesColumn = new TableColumn<>("Calories");
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));

        TableColumn<Meal, MealType> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Meal, String> ingredientsColumn = new TableColumn<>("Ingredients");
        ingredientsColumn.setCellValueFactory(new PropertyValueFactory<>("ingredients"));

        tableView.getColumns().addAll(nameColumn, caloriesColumn, typeColumn, ingredientsColumn);

        ArrayList<Meal> mealsList = Meal.readMeals();
        tableView.getItems().addAll(mealsList);

        return tableView;
    }

    private GridPane addMealPane() {
        Label nameLabel = new Label("Name:");
        Label caloriesLabel = new Label("Calories:");
        Label typeLabel = new Label("Type:");
        Label ingredientsLabel = new Label("Ingredients (comma-separated):");

        TextField nameField = new TextField();
        TextField caloriesField = new TextField();
        ComboBox<MealType> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().setAll(MealType.values());
        TextField ingredientsField = new TextField();

        Button addMealButton = new Button("Add Meal");

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameField, 1, 0);
        gridPane.add(caloriesLabel, 0, 1);
        gridPane.add(caloriesField, 1, 1);
        gridPane.add(typeLabel, 0, 2);
        gridPane.add(typeComboBox, 1, 2);
        gridPane.add(ingredientsLabel, 0, 3);
        gridPane.add(ingredientsField, 1, 3);
        gridPane.add(addMealButton, 0, 4);

        addMealButton.setOnAction(e -> {
            String name = nameField.getText();
            String caloriesText = caloriesField.getText();
            MealType type = typeComboBox.getValue();
            String ingredientsText = ingredientsField.getText();

            if (name == null || name.trim().isEmpty()) {
                showError("Name cannot be empty");
                return;
            }

            int calories;
            try {
                calories = Integer.parseInt(caloriesText);
                if (calories <= 0) {
                    showError("Calories must be a positive number");
                    return;
                }
            } catch (NumberFormatException ex) {
                showError("Calories must be a valid number");
                return;
            }

            if (type == null) {
                showError("Please select a meal type");
                return;
            }

            ArrayList<String> ingredients = new ArrayList<>();
            if (ingredientsText != null && !ingredientsText.trim().isEmpty()) {
                String[] splitIngredients = ingredientsText.split(",");
                for (String ingredient : splitIngredients) {
                    ingredients.add(ingredient.trim());
                }
            }

            if (addMeal(name, calories, type, ingredients)) {
                showMessage(name + " added successfully");
            } else {
                showError("Meal with this name already exists");
            }
        });

        return gridPane;
    }

    private HBox removeMealPane() {
        Label nameLabel = new Label("Enter name to Remove:");

        TextField nameField = new TextField();

        Button removeButton = new Button("Remove Meal");

        removeButton.setOnAction(e -> {
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                showError("Name cannot be empty");
                return;
            }

            if (removeMeal(name)) {
                showMessage("Meal " + name + " removed successfully");
            } else {
                showError("Meal " + name + " not found");
            }
        });

        HBox hbox = new HBox(10, nameLabel, nameField, removeButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-padding: 10px;");

        return hbox;
    }

    private HBox searchMealPane() {
        Label nameLabel = new Label("Enter name to Search:");

        TextField nameField = new TextField();

        Button searchButton = new Button("Search Meal");

        searchButton.setOnAction(e -> {
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                showError("Name cannot be empty");
                return;
            }
            Meal m = searchMeal(name);
            if (m == null) {
                showError("Meal " + name + " not found");
            } else {
                // display user data
            }
        });

        HBox hbox = new HBox(10, nameLabel, nameField, searchButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-padding: 10px;");

        return hbox;
    }

    @Override
    public Scene homePage() {
        BorderPane borderPane = new BorderPane();

        Label welcomeLabel = new Label("Welcome, " + getName());
        welcomeLabel.setStyle("-fx-font-size: 30px; -fx-padding: 10px;");

        StackPane topPane = new StackPane(welcomeLabel);
        topPane.setAlignment(Pos.CENTER);
        topPane.setStyle("-fx-padding: 10px; -fx-background-color: #e0e0e0;");
        borderPane.setTop(topPane);

        VBox buttonBox = new VBox(10);
        buttonBox.setStyle("-fx-padding: 10px; -fx-background-color: #f0f0f0;");

        Button addButton = new Button("Add Meal");
        addButton.setOnAction((t) -> {
            borderPane.setCenter(addMealPane());
        });

        Button saerchButton = new Button("Search Meals");
        saerchButton.setOnAction((t) -> {
            borderPane.setCenter(searchMealPane());

        });

        Button listButton = new Button("List Meals");
        listButton.setOnAction((t) -> {
            borderPane.setCenter(new VBox(listMeals(), removeMealPane()));
        });

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction((t) -> {
            fitnessprogram_alubi.FitnessProgram_alubi.stage.setScene(FitnessProgram_alubi.login());
        });

        buttonBox.getChildren().addAll(addButton, saerchButton, listButton, logoutButton);
        borderPane.setLeft(buttonBox);

        Label centerLabel = new Label("Welcome to the Nutritionist Panel");
        centerLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
        borderPane.setCenter(centerLabel);

        return new Scene(borderPane, 600, 400);
    }

}
