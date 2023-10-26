import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CookBook extends ArrayList<Dish>{
    public void loadDishes(String path) throws RestaurantException, FileNotFoundException {
        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader(path)))) {
            int lineNumber = 0;
            while(scanner.hasNext()){
                String data = scanner.nextLine();
                String[] parsedData = data.split(Settings.DELIMITER);
                if(parsedData.length < 3) throw new RestaurantException("Error reading Dish on line : " + lineNumber + " " + data);
                int price;
                int preparationTime;
                try {
                    price = Integer.parseInt(parsedData[1]);
                    preparationTime = Integer.parseInt(parsedData[2]);
                } catch (NumberFormatException e) {
                    throw new RestaurantException("Wrong id format for Dish on line: " + lineNumber + " " + data);
                }
                if(parsedData.length==3)
                {
                    this.add(new Dish(parsedData[0], price, preparationTime));
                } else if(parsedData.length==4)
                {
                    this.add(new Dish(parsedData[0], price, preparationTime, parsedData[3]));
                } else {
                    List<String> otherFotos = new ArrayList<>(Arrays.asList(parsedData).subList(4, parsedData.length));
                    this.add(new Dish(parsedData[0], price, preparationTime, parsedData[3], otherFotos));
                }
            }
        }
    }
    public Dish getDishById(int id) throws RestaurantException {
        for (Dish dish : this)
        {
            if(dish.getId()==id) {
                return dish;
            }
        }
        throw new RestaurantException("Cannot find dish under id: " + id);
    }
}
