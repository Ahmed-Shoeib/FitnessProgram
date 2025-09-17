package fitnessprogram_alubi;

import static fitnessprogram_alubi.FitnessProgram_alubi.showError;
import static fitnessprogram_alubi.FitnessProgram_alubi.showMessage;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

class Admin extends User implements Serializable {

    public Admin(String username, String password, String name, int age, String email) {
        super(username, password, name, age, email, null);
    }

    private boolean addUser(String username, String password, String name, int age, String email, Gender gender, double currentWeight, LocalDateTime endofSubscriptions, String userType) {
        ArrayList<User> usersList = readUsers();
        for (User u : usersList) {
            if (u.getUsername().equals(username)) {
                return false;
            }
        }
        User u = null;
        if (userType.equals("Admin")) {
            u = new Admin(username, password, name, age, email);
        } else if (userType.equals("Client")) {
            u = new Client(username, password, name, age, email, gender, currentWeight, endofSubscriptions);
        } else if (userType.equals("Trainer")) {
            u = new Trainer(username, password, name, age, email, gender);
        } else if (userType.equals("Nutritionist")) {
            u = new Nutritionist(username, password, name, age, email, gender);
        }

        usersList.add(u);
        writeUsers(usersList);
        return true;
    }

    private boolean removeUser(String username) {
        ArrayList<User> usersList = readUsers();
        for (User user : usersList) {
            if (user.getUsername().equals(username)) {
                usersList.remove(user);
                writeUsers(usersList);  // Save the updated list
                return true;
            }
        }
        return false;
    }

    private User SearchUser(String username) {
        ArrayList<User> usersList = readUsers();
        for (User user : usersList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private GridPane addUser() {

        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label nameLabel = new Label("Name:");
        Label ageLabel = new Label("Age:");
        Label emailLabel = new Label("Email:");
        Label genderLabel = new Label("Gender:");
        Label userTypeLabel = new Label("User Type:");
        Label weightLabel = new Label("Current Weight (kg):");
        Label subscriptionLabel = new Label("Subscription Duration:");

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField nameField = new TextField();
        TextField ageField = new TextField();
        TextField emailField = new TextField();
        TextField weightField = new TextField();

        ComboBox<Gender> genderComboBox = new ComboBox<>();
        genderComboBox.getItems().setAll(Gender.values());

        ComboBox<String> userTypeComboBox = new ComboBox<>();
        userTypeComboBox.getItems().addAll("Admin", "Client", "Trainer", "Nutritionist");

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

        VBox subscriptionBox = new VBox(5, oneMonth, threeMonths, sixMonths, nineMonths, twelveMonths);

        Button addUserButton = new Button("Add User");

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

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
        gridPane.add(userTypeLabel, 0, 7);
        gridPane.add(userTypeComboBox, 1, 7);

        // Current Weight and Subscription (hidden by default)
        gridPane.add(weightLabel, 0, 8);
        gridPane.add(weightField, 1, 8);
        gridPane.add(subscriptionLabel, 0, 9);
        gridPane.add(subscriptionBox, 1, 9);

        gridPane.add(addUserButton, 0, 10);

        weightLabel.setVisible(false);
        weightField.setVisible(false);
        subscriptionLabel.setVisible(false);
        subscriptionBox.setVisible(false);

        // Toggle visibility when user type changes
        userTypeComboBox.setOnAction(e -> {
            String selectedType = userTypeComboBox.getValue();
            boolean isClient = "Client".equals(selectedType);

            weightLabel.setVisible(isClient);
            weightField.setVisible(isClient);
            subscriptionLabel.setVisible(isClient);
            subscriptionBox.setVisible(isClient);
        });

        addUserButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String name = nameField.getText();
            String ageText = ageField.getText();
            String email = emailField.getText();
            Gender gender = genderComboBox.getValue();
            String userType = userTypeComboBox.getValue();

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

            if (userType == null) {
                showError("Please select a user type");
                return;
            }

            double currentWeight = 0;
            LocalDateTime endOfSubscription = LocalDateTime.now();

            if ("Client".equals(userType)) {
                String weightText = weightField.getText();
                RadioButton selectedRadioButton = (RadioButton) subscriptionGroup.getSelectedToggle();

                if (weightText == null || weightText.trim().isEmpty()) {
                    showError("Current weight cannot be empty");
                    return;
                }
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
                endOfSubscription = LocalDateTime.now().plusMonths(months);
            }
            if (addUser(username, password, name, age, email, gender, currentWeight, endOfSubscription, userType)) {
                showMessage(username + " created successfully as " + userType);
            } else {
                showError("Username is taken");
            }

        });

        return gridPane;
    }

    private HBox removeUser() {
        Label usernameLabel = new Label("Enter Username to Remove:");

        TextField usernameField = new TextField();

        Button removeButton = new Button("Remove User");

        removeButton.setOnAction(e -> {
            String username = usernameField.getText();
            if (username == null || username.trim().isEmpty()) {
                showError("Username cannot be empty");
                return;
            }

            if (removeUser(username)) {
                showMessage("User " + username + " removed successfully");
            } else {
                showError("User " + username + " not found");
            }
        });

        HBox hbox = new HBox(10, usernameLabel, usernameField, removeButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-padding: 10px;");

        return hbox;
    }

    private TableView<User> listUsers() {
        TableView<User> tableView = new TableView<>();

        TableColumn<User, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> genderColumn = new TableColumn<>("Gender");
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

        tableView.getColumns().addAll(idColumn, nameColumn, usernameColumn, passwordColumn, ageColumn, emailColumn, genderColumn
        );

        ArrayList<User> usersList = readUsers();
        tableView.getItems().addAll(usersList);

        return tableView;
    }

    private HBox SearchUser() {
        Label usernameLabel = new Label("Enter Username to Search:");

        TextField usernameField = new TextField();

        Button searchButton = new Button("Search User");

        searchButton.setOnAction(e -> {
            String username = usernameField.getText();
            if (username == null || username.trim().isEmpty()) {
                showError("Username cannot be empty");
                return;
            }
            User u = SearchUser(username);
            if (u == null) {
                showError("User " + username + " not found");
            } else {
                showMessage(u.toString());
            }
        });

        HBox hbox = new HBox(10, usernameLabel, usernameField, searchButton);
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

        Button addButton = new Button("Add User");
        addButton.setOnAction((t) -> {
            borderPane.setCenter(addUser());
        });

        Button searchButton = new Button("Search Users");
        searchButton.setOnAction((t) -> {
            borderPane.setCenter(SearchUser());

        });

        Button listButton = new Button("List Users");
        listButton.setOnAction((t) -> {
            borderPane.setCenter(new VBox(listUsers(), removeUser()));

        });

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction((t) -> {
            fitnessprogram_alubi.FitnessProgram_alubi.stage.setScene(FitnessProgram_alubi.login());
        });

        buttonBox.getChildren().addAll(addButton, searchButton, listButton, logoutButton);
        borderPane.setLeft(buttonBox);

        Label centerLabel = new Label("Welcome back");
        centerLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
        borderPane.setCenter(centerLabel);

        Scene scene = new Scene(borderPane, 600, 400);
        return scene;
    }

}
