package fitnessprogram_alubi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Program implements Serializable {

    private String name;
    private ArrayList<Workout> workouts;
    private ArrayList<Meal> meals;

    public Program(String name, ArrayList<Workout> workouts, ArrayList<Meal> meals) {
        this.name = name;
        this.workouts = workouts;
        this.meals = meals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(ArrayList<Workout> workouts) {
        this.workouts = workouts;
    }

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    public void setMeals(ArrayList<Meal> meals) {
        this.meals = meals;
    }

    public static void writePrograms(ArrayList<Program> list) {
        try {
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(new File("programs.bin")));
            o.writeObject(list);
            o.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static ArrayList<Program> readPrograms() {
        ArrayList<Program> list = new ArrayList<>();
        try {
            ObjectInputStream i = new ObjectInputStream(new FileInputStream(new File("programs.bin")));
            list = (ArrayList<Program>) i.readObject();
            i.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return list;
    }

    @Override
    public String toString() {
        return "Program{" + "name=" + name + ", workouts=" + workouts + ", meals=" + meals + '}';
    }

}
