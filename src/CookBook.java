import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CookBook extends ArrayList<Dish> {
    public void loadDishes(String path) throws RestaurantException, FileNotFoundException {
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(path)))) {
            int lineNumber = 0;
            while (scanner.hasNext()) {
                String data = scanner.nextLine();
                String[] parsedData = data.split(Settings.DELIMITER);
                if (parsedData.length < 3)
                    throw new RestaurantException("Error reading Dish on line : " + lineNumber + " " + data);
                int price;
                int preparationTime;
                try {
                    price = Integer.parseInt(parsedData[1]);
                    preparationTime = Integer.parseInt(parsedData[2]);
                } catch (NumberFormatException e) {
                    throw new RestaurantException("Wrong id format for Dish on line: " + lineNumber + " " + data);
                }
                if (parsedData.length == 3) {
                    this.add(new Dish(parsedData[0], price, preparationTime));
                } else if (parsedData.length == 4) {
                    this.add(new Dish(parsedData[0], price, preparationTime, parsedData[3]));
                } else {
                    List<String> otherFotos = new ArrayList<>(Arrays.asList(parsedData).subList(4, parsedData.length));
                    this.add(new Dish(parsedData[0], price, preparationTime, parsedData[3], otherFotos));
                }
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
