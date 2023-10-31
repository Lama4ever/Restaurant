import java.io.FileNotFoundException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        //create instances
        RestaurantManager manager = new RestaurantManager();
        CookBook cookBook = new CookBook();
        //1. load data
        try {
            cookBook.loadDishes(Settings.PATH + Settings.DISHESFILE);
            manager.loadOrders(Settings.PATH + Settings.ORDERSFILE, cookBook);
        } catch (RestaurantException | FileNotFoundException e) {
            System.err.print(e.getLocalizedMessage());
        }
        //2. add dishes
        try {
            cookBook.add(new Dish("Kuřecí řízek obalovaný 150g", 150, 30));
            cookBook.add(new Dish("Hranolky 150g", 75, 10));
            cookBook.add(new Dish("Pstruh na víně 200", 299, 45));
            cookBook.add(new Dish("Kofola 0,5l", 39, 5));
        } catch (RestaurantException e) {
            System.err.print(e.getLocalizedMessage());
        }
        //2. create orders
        manager.add(new Order(15, cookBook.get(0), 2, LocalDateTime.now().plusMinutes(3)));
        manager.add(new Order(15, cookBook.get(1), 2, LocalDateTime.now().plusMinutes(3)));
        manager.add(new Order(15, cookBook.get(3), 2, LocalDateTime.now()));

        manager.add(new Order(2, cookBook.get(2), 1, LocalDateTime.now()));
        manager.add(new Order(2, cookBook.get(3), 1, LocalDateTime.now()));

        //2. fulfill orders
        try {
            manager.get(2).setFulfilmentTime(LocalDateTime.now().plusMinutes(5));
        } catch (RestaurantException e) {
            System.err.print(e.getLocalizedMessage());
        }

        //3. total price for table 15
        System.out.println("Celková cena pro stůl 15. je: " + manager.getPriceForTable(15) + " Kč");
        //4. managment data
        System.out.println("Rozpracovaných objednávek je: " + manager.countNotFulfiled());
        System.out.println("Seznam objednávek dle času: " + manager.sortOrderedTime());
        System.out.println("Průměrná doba objednávky je: " + manager.getAverageOrderTime());
        System.out.println("Dnes byla objednána jídla: " + manager.getOrderedDishes());
        System.out.println(manager.getTableOrder(15));
        System.out.println(manager.getTableOrder(2));
        //5. save data
        try {
            cookBook.save();
        } catch (RestaurantException e) {
            System.err.print(e.getLocalizedMessage());
        }
        try {
            manager.save();
        } catch (RestaurantException e) {
            System.err.print(e.getLocalizedMessage());
        }
    }
}