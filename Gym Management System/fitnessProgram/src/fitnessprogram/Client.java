package fitnessprogram_alubi;

import static fitnessprogram_alubi.FitnessProgram_alubi.showError;
import static fitnessprogram_alubi.FitnessProgram_alubi.showMessage;
import static fitnessprogram_alubi.User.readUsers;
import static fitnessprogram_alubi.User.writeUsers;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Client extends User implements Serializable {

    private Trainer trainer;
    private double currentWeight;
    private Program program;
    private HashMap<LocalDateTime, Double> progress;
    private LocalDateTime endofSubscriptions;

    public Client(String username, String password, String name, int age, String email, Gender gender, double currentWeight, LocalDateTime endofSubscriptions) {
        super(username, password, name, age, email, gender);
        ArrayList<User> users = User.readUsers();
        ArrayList<Trainer> trainers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Trainer) {
                trainers.add((Trainer) user);
            }
        }
        Trainer assignedTrainer = null;
        if (!trainers.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(trainers.size());
            assignedTrainer = trainers.get(randomIndex);
        }
        this.program = null;
        this.trainer = assignedTrainer;
        this.currentWeight = currentWeight;
        this.progress = new HashMap<>();
        this.endofSubscriptions = endofSubscriptions;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public HashMap<LocalDateTime, Double> getProgress() {
        return progress;
    }

    public void setProgress(HashMap<LocalDateTime, Double> progress) {
        this.progress = progress;
    }

    public LocalDateTime getEndofSubscriptions() {
        return endofSubscriptions;
    }

    public void setEndofSubscriptions(LocalDateTime endofSubscriptions) {
        this.endofSubscriptions = endofSubscriptions;
    }

    public static boolean signup(String username, String password, String name, int age, String email, Gender gender, double currentWeight, LocalDateTime endofSubscriptions) {
        ArrayList<User> usersList = readUsers();
        for (User u : usersList) {
            if (u.getUsername().equals(username)) {
                return false;
            }
        }

        usersList.add(new Client(username, password, name, age, email, gender, currentWeight, endofSubscriptions));
        writeUsers(usersList);
        return true;
    }

    private GridPane viewProfile() {

        // Labels for Client's data
        Label idLabel = new Label("ID:");
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label nameLabel = new Label("Name:");
        Label ageLabel = new Label("Age:");
        Label emailLabel = new Label("Email:");
        Label genderLabel = new Label("Gender:");
        Label trainerLabel = new Label("Trainer:");
        Label weightLabel = new Label("Current Weight:");
        Label subscriptionLabel = new Label("End of Subscription:");
        Label progressLabel = new Label("Progress:");

        // Display data from the Client object
        Label idValue = new Label(String.valueOf(this.getId()));
        Label usernameValue = new Label(this.getUsername());
        Label passwordValue = new Label(this.getPassword());
        Label nameValue = new Label(this.getName());
        Label ageValue = new Label(String.valueOf(this.getAge()));
        Label emailValue = new Label(this.getEmail());
        Label genderValue = new Label(this.getGender().toString());
        Label trainerValue = new Label(this.getTrainer() != null ? this.getTrainer().getName() : "None");
        Label weightValue = new Label(String.format("%.2f kg", this.getCurrentWeight()));
        Label subscriptionValue = new Label(this.getEndofSubscriptions().toString());

        // Progress displayed in a table-like layout
        GridPane progressTable = new GridPane();
        progressTable.setHgap(10);
        progressTable.setVgap(5);
        progressTable.add(new Label("Date"), 0, 0);
        progressTable.add(new Label("Weight (kg)"), 1, 0);

        int row = 1;
        for (var entry : this.getProgress().entrySet()) {
            progressTable.add(new Label(entry.getKey().toString()), 0, row);
            progressTable.add(new Label(String.format("%.2f", entry.getValue())), 1, row);
            row++;
        }

        // Main GridPane for profile view
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        // Adding labels and values
        gridPane.add(idLabel, 0, 0);
        gridPane.add(idValue, 1, 0);
        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(usernameValue, 1, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordValue, 1, 2);
        gridPane.add(nameLabel, 0, 3);
        gridPane.add(nameValue, 1, 3);
        gridPane.add(ageLabel, 0, 4);
        gridPane.add(ageValue, 1, 4);
        gridPane.add(emailLabel, 0, 5);
        gridPane.add(emailValue, 1, 5);
        gridPane.add(genderLabel, 0, 6);
        gridPane.add(genderValue, 1, 6);
        gridPane.add(trainerLabel, 0, 7);
        gridPane.add(trainerValue, 1, 7);
        gridPane.add(weightLabel, 0, 8);
        gridPane.add(weightValue, 1, 8);
        gridPane.add(subscriptionLabel, 0, 9);
        gridPane.add(subscriptionValue, 1, 9);
        gridPane.add(progressLabel, 0, 10);
        gridPane.add(progressTable, 0, 11, 2, 1);

        return gridPane;
    }

    private GridPane myProgress() {
        // Labels and Fields
        Label currentWeightLabel = new Label("Current Weight:");
        Label newWeightLabel = new Label("Enter New Weight:");
        Label progressHistoryLabel = new Label("Progress History:");

        Label currentWeightValue = new Label(String.format("%.2f kg", this.getCurrentWeight()));
        TextField newWeightField = new TextField();

        Button updateButton = new Button("Update");

        // Progress table
        GridPane progressTable = new GridPane();
        progressTable.setHgap(10);
        progressTable.setVgap(5);
        progressTable.add(new Label("Date"), 0, 0);
        progressTable.add(new Label("Weight (kg)"), 1, 0);

        // Populate progress history
        updateProgressTable(progressTable, this.getProgress());

        // Action for Update Button
        updateButton.setOnAction(e -> {
            String newWeightText = newWeightField.getText();

            if (newWeightText == null || newWeightText.trim().isEmpty()) {
                showError("Weight cannot be empty");
                return;
            }

            try {
                double newWeight = Double.parseDouble(newWeightText);
                if (newWeight <= 0) {
                    showError("Weight must be a positive number");
                    return;
                }

                // Update current weight and progress
                this.setCurrentWeight(newWeight);
                this.getProgress().put(LocalDateTime.now(), newWeight);

                // Update UI
                currentWeightValue.setText(String.format("%.2f kg", newWeight));
                newWeightField.clear();
                updateProgressTable(progressTable, this.getProgress());

                showMessage("Weight updated successfully!");
            } catch (NumberFormatException ex) {
                showError("Please enter a valid number");
            }
        });

        // Main Layout
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(currentWeightLabel, 0, 0);
        gridPane.add(currentWeightValue, 1, 0);
        gridPane.add(newWeightLabel, 0, 1);
        gridPane.add(newWeightField, 1, 1);
        gridPane.add(updateButton, 1, 2);
        gridPane.add(progressHistoryLabel, 0, 3);
        gridPane.add(progressTable, 0, 4, 2, 1);

        return gridPane;
    }

// Helper function to update the progress table
    private void updateProgressTable(GridPane progressTable, HashMap<LocalDateTime, Double> progress) {
        progressTable.getChildren().clear(); // Clear existing entries
        progressTable.add(new Label("Date"), 0, 0);
        progressTable.add(new Label("Weight (kg)"), 1, 0);

        int row = 1;
        for (var entry : progress.entrySet()) {
            progressTable.add(new Label(entry.getKey().toString()), 0, row);
            progressTable.add(new Label(String.format("%.2f", entry.getValue())), 1, row);
            row++;
        }
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

        Button viewProfileButton = new Button("My Profile");
        viewProfileButton.setOnAction((t) -> {
            borderPane.setCenter(viewProfile());
        });

        Button myProgressButton = new Button("My Progress");
        myProgressButton.setOnAction((t) -> {
            borderPane.setCenter(myProgress());

        });

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction((t) -> {
            fitnessprogram_alubi.FitnessProgram_alubi.stage.setScene(FitnessProgram_alubi.login());
        });

        buttonBox.getChildren().addAll(viewProfileButton, myProgressButton, logoutButton);
        borderPane.setLeft(buttonBox);

        Label centerLabel = new Label("Welcome back");
        centerLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
        borderPane.setCenter(centerLabel);

        Scene scene = new Scene(borderPane, 600, 400);
        return scene;
    }

}
