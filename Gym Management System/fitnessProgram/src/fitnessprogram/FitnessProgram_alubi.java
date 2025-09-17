package fitnessprogram_alubi;

import java.time.LocalDateTime;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FitnessProgram_alubi extends Application {
    
    public static Stage stage;
    
    public static void main(String[] args) {        
        if (User.login("admin", "admin") == null) {
            ArrayList<User> users = User.readUsers();
            users.add(new Admin("admin", "admin", "admin", 0, "admin"));
            User.writeUsers(users);
        }
        launch(args);
    }
    
    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static Scene login() {
        
        Label loginLabel = new Label("Login");
        loginLabel.setStyle("-fx-font-size: 30px;");
        
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        
        Button loginButton = new Button("Login");
        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction((t) -> {
            stage.setScene(signup());
        });
        
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        
        gridPane.add(loginLabel, 0, 0, 2, 1);
        GridPane.setHalignment(loginLabel, javafx.geometry.HPos.CENTER);
        
        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(usernameField, 1, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(loginButton, 0, 3);
        gridPane.add(signUpButton, 1, 3);
        
        Scene scene = new Scene(gridPane, 500, 500);
        
        loginButton.setOnAction((t) -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            if (username == null || username.trim().isEmpty()) {
                showError("Username cannot be empty");
                return;
            }
            
            if (password == null || password.trim().isEmpty()) {
                showError("Password cannot be empty");
                return;
            }
            
            User u = User.login(username, password);
            if (u == null) {
                showError("Wrong username or password");
            } else {
                stage.setScene(u.homePage());
            }
        });
        
        return scene;
    }
    
    public static Scene signup() {
        Label signupLabel = new Label("Sign Up");
        signupLabel.setStyle("-fx-font-size: 30px;");

        // Labels
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label nameLabel = new Label("Name:");
        Label ageLabel = new Label("Age:");
        Label emailLabel = new Label("Email:");
        Label genderLabel = new Label("Gender:");
        Label weightLabel = new Label("Current Weight (kg):");
        Label subscriptionLabel = new Label("Subscription Duration:");

        // Input fields
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField nameField = new TextField();
        TextField ageField = new TextField();
        TextField emailField = new TextField();
        TextField weightField = new TextField();
        
        ComboBox<Gender> genderComboBox = new ComboBox<>();
        genderComboBox.getItems().setAll(Gender.values());

        // Subscription Radio Buttons
        ToggleGroup subscriptionGroup = new ToggleGroup();
        RadioButton oneMonth = new RadioButton("1 month");
        RadioButton threeMonths = new RadioButton("3 months");
        RadioButton sixMonths = new RadioButton("6 months");
        RadioButton nineMonths = new RadioButton("9 months");
        RadioButton twelveMonths = new RadioButton("12 months");
        
        oneMonth.setToggleGroup(subscriptionGroup);
        threeMonths.setToggleGroup(subscriptionGroup);
        sixMonths.setToggleGroup(subscriptionGroup);
        nineMonths.setToggleGroup(subscriptionGroup);
        twelveMonths.setToggleGroup(subscriptionGroup);

        // Buttons
        Button signUpButton = new Button("Sign Up");
        Button loginButton = new Button("Login");
        loginButton.setOnAction(t -> stage.setScene(login()));

        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        
        gridPane.add(signupLabel, 0, 0, 2, 1);
        GridPane.setHalignment(signupLabel, HPos.CENTER);
        
        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(usernameField, 1, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(nameLabel, 0, 3);
        gridPane.add(nameField, 1, 3);
        gridPane.add(ageLabel, 0, 4);
        gridPane.add(ageField, 1, 4);
        gridPane.add(emailLabel, 0, 5);
        gridPane.add(emailField, 1, 5);
        gridPane.add(genderLabel, 0, 6);
        gridPane.add(genderComboBox, 1, 6);
        gridPane.add(weightLabel, 0, 7);
        gridPane.add(weightField, 1, 7);
        
        gridPane.add(subscriptionLabel, 0, 8);
        VBox subscriptionBox = new VBox(5, oneMonth, threeMonths, sixMonths, nineMonths, twelveMonths);
        gridPane.add(subscriptionBox, 1, 8);
        
        gridPane.add(signUpButton, 0, 9);
        gridPane.add(loginButton, 1, 9);

        // Scene
        Scene scene = new Scene(gridPane, 500, 600);

        // Sign Up Button Action
        signUpButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String name = nameField.getText();
            String ageText = ageField.getText();
            String email = emailField.getText();
            String weightText = weightField.getText();
            Gender gender = genderComboBox.getValue();
            RadioButton selectedRadioButton = (RadioButton) subscriptionGroup.getSelectedToggle();

            // Validation
            if (username == null || username.trim().isEmpty()) {
                showError("Username cannot be empty");
                return;
            }
            
            if (password == null || password.trim().isEmpty()) {
                showError("Password cannot be empty");
                return;
            }
            
            if (name == null || name.trim().isEmpty()) {
                showError("Name cannot be empty");
                return;
            }
            
            if (ageText == null || ageText.trim().isEmpty()) {
                showError("Age cannot be empty");
                return;
            }
            int age = 0;
            try {
                age = Integer.parseInt(ageText);
                if (age <= 0) {
                    showError("Age must be a positive number");
                    return;
                }
            } catch (NumberFormatException ex) {
                showError("Age must be a valid number");
                return;
            }
            
            if (email == null || email.trim().isEmpty() || !email.contains("@")) {
                showError("Please provide a valid email");
                return;
            }
            
            if (gender == null) {
                showError("Please select a gender");
                return;
            }
            
            double currentWeight;
            try {
                currentWeight = Double.parseDouble(weightText);
                if (currentWeight <= 0) {
                    showError("Weight must be a positive number");
                    return;
                }
            } catch (NumberFormatException ex) {
                showError("Weight must be a valid number");
                return;
            }
            
            if (selectedRadioButton == null) {
                showError("Please select a subscription duration");
                return;
            }
            
            int months = Integer.parseInt(selectedRadioButton.getText().split(" ")[0]);
            LocalDateTime endOfSubscription = LocalDateTime.now().plusMonths(months);
            
            if (Client.signup(username, password, name, age, email, gender, currentWeight, endOfSubscription)) {
                showMessage(username + " created successfully");
            } else {
                showError("Username is taken");
            }
        });
        
        return scene;
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        
        Button loginButton = new Button("Login");
        loginButton.setOnAction((t) -> {
            stage.setScene(login());
        });
        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction((t) -> {
            stage.setScene(signup());
        });
        
        HBox hbox = new HBox(loginButton, signUpButton);
        hbox.setSpacing(10);
        
        Scene scene = new Scene(hbox, 500, 500);
        
        stage.setTitle("Fitness Program");
        stage.setScene(scene);
        stage.show();
    }
}
