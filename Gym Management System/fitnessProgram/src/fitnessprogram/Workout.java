package fitnessprogram_alubi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Workout implements Serializable{

    private String name;
    private WorkoutType type;

    public Workout(String name, WorkoutType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkoutType getType() {
        return type;
    }

    public void setType(WorkoutType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Workout{"
                + "name='" + name + '\''
                + ", type=" + type
                + '}';
    }

    public static void writeWorkouts(ArrayList<Workout> list) {
        try {
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(new File("workouts.bin")));
            o.writeObject(list);
            o.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static ArrayList<Workout> readWorkouts() {
        ArrayList<Workout> list = new ArrayList<>();
        try {
            ObjectInputStream i = new ObjectInputStream(new FileInputStream(new File("workouts.bin")));
            list = (ArrayList<Workout>) i.readObject();
            i.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return list;
    }
}
