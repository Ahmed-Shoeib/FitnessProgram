package fitnessprogram_alubi;

import static fitnessprogram_alubi.FitnessProgram_alubi.showError;
import static fitnessprogram_alubi.FitnessProgram_alubi.showMessage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Trainer extends User implements Serializable {

    public Trainer(String username, String password, String name, int age, String email, Gender gender) {
        super(username, password, name, age, email, gender);
    }

    private boolean addWorkout(String name, WorkoutType type) {
        ArrayList<Workout> workoutsList = Workout.readWorkouts();
        for (Workout w : workoutsList) {
            if (w.getName().equals(name)) {
                return false;
            }
        }
        Workout workout = new Workout(name, type);
        workoutsList.add(workout);
        Workout.writeWorkouts(workoutsList);
        return true;
    }

    private boolean removeWorkout(String name) {
        ArrayList<Workout> workoutsList = Workout.readWorkouts();
        for (Workout w : workoutsList) {
            if (w.getName().equals(name)) {
                workoutsList.remove(w);
                Workout.writeWorkouts(workoutsList);
                return true;
            }
        }
        return false;
    }

    private Workout searchWorkout(String name) {
        ArrayList<Workout> workoutsList = Workout.readWorkouts();
        for (Workout w : workoutsList) {
            if (w.getName().equals(name)) {
                return w;
            }
        }
        return null;
    }

    private TableView<Workout> listWorkouts() {
        TableView<Workout> tableView = new TableView<>();

        TableColumn<Workout, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Workout, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        tableView.getColumns().addAll(nameColumn, typeColumn);

        ArrayList<Workout> workoutList = Workout.readWorkouts();
        tableView.getItems().addAll(workoutList);

        return tableView;
    }

    private GridPane addWorkout() {
        Label nameLabel = new Label("Workout Name:");
        TextField nameField = new TextField();

        Label typeLabel = new Label("Workout Type:");

        ComboBox<WorkoutType> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().setAll(WorkoutType.values());

        Button addWorkoutButton = new Button("Add Workout");

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(nameLabel, 0, 1);
        gridPane.add(nameField, 1, 1);
        gridPane.add(typeLabel, 0, 2);
        gridPane.add(typeComboBox, 1, 2);
        gridPane.add(addWorkoutButton, 0, 3);

        addWorkoutButton.setOnAction(e -> {
            String name = nameField.getText();
            WorkoutType type = typeComboBox.getValue();

            if (name == null || name.trim().isEmpty()) {
                showError("Workout name cannot be empty");
                return;
            }

            if (type == null) {
                showError("Please select a workout type");
                return;
            }

            if (addWorkout(name, type)) {
                showMessage("Workout created successfully");
            } else {
                showError("Workout with this name already exists");
            }
        });

        return gridPane;
    }

    private HBox removeWorkout() {
        Label nameLabel = new Label("Enter Workout Name to Remove:");

        TextField nameField = new TextField();

        Button removeButton = new Button("Remove Workout");

        removeButton.setOnAction(e -> {
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                showError("Workout name cannot be empty");
                return;
            }

            if (removeWorkout(name)) {
                showMessage("Workout " + name + " removed successfully");
            } else {
                showError("Workout " + name + " not found");
            }
        });

        HBox hbox = new HBox(10, nameLabel, nameField, removeButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-padding: 10px;");

        return hbox;
    }

    private HBox searchWorkout() {
        Label nameLabel = new Label("Enter Workout Name to Search:");

        TextField nameField = new TextField();

        Button searchButton = new Button("Search Workout");

        searchButton.setOnAction(e -> {
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                showError("Workout name cannot be empty");
                return;
            }
            Workout w = searchWorkout(name);
            if (w == null) {
                showError("Workout " + name + " not found");
            } else {
                showMessage(w.toString());
            }
        });

        HBox hbox = new HBox(10, nameLabel, nameField, searchButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-padding: 10px;");

        return hbox;
    }

    private boolean addProgram(String name, ArrayList<Workout> workouts, ArrayList<Meal> meals) {
        ArrayList<Program> programsList = Program.readPrograms();
        for (Program p : programsList) {
            if (p.getName().equals(name)) {
                return false;
            }
        }
        Program program = new Program(name, workouts, meals);
        programsList.add(program);
        Program.writePrograms(programsList);
        return true;
    }

    private boolean removeProgram(String name) {
        ArrayList<Program> programsList = Program.readPrograms();
        for (Program p : programsList) {
            if (p.getName().equals(name)) {
                programsList.remove(p);
                Program.writePrograms(programsList);
                return true;
            }
        }
        return false;
    }

    private Program searchProgram(String name) {
        ArrayList<Program> programsList = Program.readPrograms();
        for (Program p : programsList) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    private GridPane addProgram() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        Label nameLabel = new Label("Program Name:");
        TextField nameField = new TextField();

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameField, 1, 0);

        Label workoutsLabel = new Label("Select Workouts:");
        gridPane.add(workoutsLabel, 0, 1);

        VBox workoutsBox = new VBox();
        workoutsBox.setSpacing(5);
        ArrayList<Workout> workouts = Workout.readWorkouts();
        ArrayList<CheckBox> workoutCheckBoxes = new ArrayList<>();

        for (Workout workout : workouts) {
            CheckBox checkBox = new CheckBox(workout.getName());
            workoutCheckBoxes.add(checkBox);
            workoutsBox.getChildren().add(checkBox);
        }

        gridPane.add(workoutsBox, 1, 1);

        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        gridPane.add(separator, 0, 2, 2, 1);

        Label mealsLabel = new Label("Select Meals:");
        gridPane.add(mealsLabel, 0, 3);

        VBox mealsBox = new VBox();
        mealsBox.setSpacing(5);
        ArrayList<Meal> meals = Meal.readMeals();
        ArrayList<CheckBox> mealCheckBoxes = new ArrayList<>();

        for (Meal meal : meals) {
            CheckBox checkBox = new CheckBox(meal.getName());
            mealCheckBoxes.add(checkBox);
            mealsBox.getChildren().add(checkBox);
        }

        gridPane.add(mealsBox, 1, 3);

        Button submitButton = new Button("Add Program");
        gridPane.add(submitButton, 1, 4);

        submitButton.setOnAction(e -> {
            String programName = nameField.getText();
            if (programName == null || programName.trim().isEmpty()) {
                showError("Program name cannot be empty");
                return;
            }
            ArrayList<Workout> selectedWorkouts = new ArrayList<>();
            ArrayList<Meal> selectedMeals = new ArrayList<>();

            for (int i = 0; i < workoutCheckBoxes.size(); i++) {
                if (workoutCheckBoxes.get(i).isSelected()) {
                    selectedWorkouts.add(workouts.get(i));
                }
            }

            for (int i = 0; i < mealCheckBoxes.size(); i++) {
                if (mealCheckBoxes.get(i).isSelected()) {
                    selectedMeals.add(meals.get(i));
                }
            }

            if (addProgram(programName, selectedWorkouts, selectedMeals)) {
                showMessage("Program created successfully");
            } else {
                showError("Program with this name already exists");
            }

        });

        return gridPane;
    }

    private HBox removeProgram() {
        Label nameLabel = new Label("Enter Program Name to Remove:");

        TextField nameField = new TextField();

        Button removeButton = new Button("Remove Program");

        removeButton.setOnAction(e -> {
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                showError("Program name cannot be empty");
                return;
            }

            if (removeProgram(name)) {
                showMessage("Program " + name + " removed successfully");
            } else {
                showError("Program " + name + " not found");
            }
        });

        HBox hbox = new HBox(10, nameLabel, nameField, removeButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-padding: 10px;");

        return hbox;
    }

    private HBox searchProgram() {
        Label nameLabel = new Label("Enter Program Name to Search:");

        TextField nameField = new TextField();

        Button searchButton = new Button("Search Program");

        searchButton.setOnAction(e -> {
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                showError("Program name cannot be empty");
                return;
            }
            Program p = searchProgram(name);
            if (p == null) {
                showError("Program " + name + " not found");
            } else {
                showMessage(p.toString());
            }
        });

        HBox hbox = new HBox(10, nameLabel, nameField, searchButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-padding: 10px;");

        return hbox;
    }

    private TableView<Program> listPrograms() {
        TableView<Program> tableView = new TableView<>();

        TableColumn<Program, String> nameColumn = new TableColumn<>("Program Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Program, String> workoutsColumn = new TableColumn<>("Workouts");
        workoutsColumn.setCellValueFactory(cellData -> {
            Program program = cellData.getValue();
            String workoutsNames = program.getWorkouts().stream()
                    .map(Workout::getName) // Assuming Workout has a getName() method
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(workoutsNames);
        });

        TableColumn<Program, String> mealsColumn = new TableColumn<>("Meals");
        mealsColumn.setCellValueFactory(cellData -> {
            Program program = cellData.getValue();
            String mealsNames = program.getMeals().stream()
                    .map(Meal::getName) // Assuming Meal has a getName() method
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(mealsNames);
        });

        tableView.getColumns().addAll(nameColumn, workoutsColumn, mealsColumn);

        ArrayList<Program> programList = Program.readPrograms(); // Assuming a method to fetch programs
        tableView.getItems().addAll(programList);

        return tableView;
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

        VBox buttonBoxLeft = new VBox(10);
        buttonBoxLeft.setStyle("-fx-padding: 10px; -fx-background-color: #f0f0f0;");

        Button addButton = new Button("Add Workout");
        addButton.setOnAction((t) -> {
            borderPane.setCenter(addWorkout());
        });

        Button searchButton = new Button("Search Workout");
        searchButton.setOnAction((t) -> {
            borderPane.setCenter(searchWorkout());
        });
        Button listButton = new Button("List Workouts");
        listButton.setOnAction((t) -> {
            borderPane.setCenter(new VBox(listWorkouts(), removeWorkout()));
        });
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction((t) -> {
            fitnessprogram_alubi.FitnessProgram_alubi.stage.setScene(FitnessProgram_alubi.login());
        });

        buttonBoxLeft.getChildren().addAll(addButton, searchButton, listButton, logoutButton);
        borderPane.setLeft(buttonBoxLeft);

        // Right side buttons for program
        VBox buttonBoxRight = new VBox(10);
        buttonBoxRight.setStyle("-fx-padding: 10px; -fx-background-color: #f0f0f0;");

        Button addProgramButton = new Button("Add Program");
        addProgramButton.setOnAction((t) -> {
            borderPane.setCenter(addProgram());
        });

        Button searchProgramButton = new Button("Search Program");
        searchProgramButton.setOnAction((t) -> {
            borderPane.setCenter(searchProgram());
        });
        Button listProgramButton = new Button("List Programs");
        listProgramButton.setOnAction((t) -> {
            borderPane.setCenter(new VBox(listPrograms(), removeProgram()));
        });

        buttonBoxRight.getChildren().addAll(addProgramButton, searchProgramButton, listProgramButton);
        borderPane.setRight(buttonBoxRight);

        Label centerLabel = new Label("Welcome back");
        centerLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
        borderPane.setCenter(centerLabel);

        Scene scene = new Scene(borderPane, 800, 400);
        return scene;
    }
}
