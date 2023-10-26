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
            cookBook.add(new Dish("Kuřecí řízek obalovaný 150g",150, 30));
            cookBook.add(new Dish("Hranolky 150g",75, 10));
            cookBook.add(new Dish("Pstruh na víně 200",299, 45));
            cookBook.add(new Dish("Kofola 0,5l",39, 5 ));
        } catch (RestaurantException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        //2. create orders
        manager.add(new Order(15, cookBook.get(0), 2, LocalDateTime.now()));
        manager.add(new Order(15, cookBook.get(1), 2, LocalDateTime.now()));
        manager.add(new Order(15, cookBook.get(3), 2, LocalDateTime.now()));

        manager.add(new Order(2, cookBook.get(2), 1, LocalDateTime.now()));
        manager.add(new Order(2, cookBook.get(3), 1, LocalDateTime.now()));

        //2. fulfill orders
        try {
            manager.get(2).setFulfilmentTime(LocalDateTime.now().plusMinutes(5));
        } catch (RestaurantException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        //3. total price for table 15
        System.out.println("Celkova cena pro stůl 15. je: " + manager.getPriceForTable(15)+ " Kč");
        //4. managment data
        System.out.println("Rozpracovanych objednavek je: " + manager.countNotFulfiled());
        System.out.println("Seznam objednavek dle casu: " + manager.sortOrderedTime());
        System.out.println("Prumerna doba objednavky je: " + manager.getAverageOrderTime());
        System.out.println("Dnes byla objednan jidla: " + manager.getOrderedDishes());
        System.out.println(manager.getTableOrder(15));

    }
}