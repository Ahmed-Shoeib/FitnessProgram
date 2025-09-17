/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fitnessprogram_alubi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.Scene;

public abstract class User implements Serializable {

    private static int idCounter = 0;
    private int id;
    private String username;
    private String password;
    private String name;
    private int age;
    private String email;
    private Gender gender;

    public User(String username, String password, String name, int age, String email, Gender gender) {
        this.id = ++idCounter;
        this.username = username;
        this.password = password;
        this.name = name;
        this.age = age;
        this.email = email;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + ", age=" + age + ", email=" + email + ", gender=" + gender + '}';
    }

    public abstract Scene homePage();

    public static User login(String username, String password) {
        ArrayList<User> usersList = readUsers();
        for (User user : usersList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public static void writeUsers(ArrayList<User> list) {
        try {
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(new File("users.bin")));
            o.writeObject(list);
            o.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static ArrayList<User> readUsers() {
        ArrayList<User> list = new ArrayList<>();
        try {
            ObjectInputStream i = new ObjectInputStream(new FileInputStream(new File("users.bin")));
            list = (ArrayList<User>) i.readObject();
            i.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return list;
    }

}
