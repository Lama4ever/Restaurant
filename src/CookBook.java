import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CookBook extends ArrayList<Dish> {
    public void loadDishes(String path) throws RestaurantException, FileNotFoundException {
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(path)))) {
            int lineNumber = 1;
            while (scanner.hasNext()) {
                String data = scanner.nextLine();
                this.add(Dish.parseDish(data, lineNumber));
                lineNumber++;
            }

        }
    }

    public Dish getDishById(int id) throws RestaurantException {
        for (Dish dish : this) {
            if (dish.getId() == id) {
                return dish;
            }
        }
        throw new RestaurantException("Cannot find dish under id: " + id);
    }

    public void save() throws RestaurantException {
        File dishesFile = new File(Settings.PATH + Settings.DISHESFILE);
        if (!dishesFile.exists()) {
            try {
                dishesFile.createNewFile();
            } catch (IOException e) {
                throw new RestaurantException("Cannot create file: " + Settings.PATH + Settings.DISHESFILE);
            }
        }
        try (PrintWriter writter = new PrintWriter(new BufferedWriter(new FileWriter(dishesFile)))) {
            StringBuilder data = new StringBuilder();
            this.forEach(dish -> data.append(dish.getSaveData()));
            writter.print(data);
        } catch (IOException e) {
            throw new RestaurantException("Cannot write to file: " + Settings.PATH + Settings.DISHESFILE);
        }
    }
}
