import java.io.FileNotFoundException;
import java.time.LocalDateTime;

public class Main {

    public static RestaurantManager manager;
    public static CookBook cookBook;
    public static void main(String[] args) {
        //create instances
        manager = new RestaurantManager();
        cookBook = new CookBook();
        //1. load data
        loadData();
        //2. add dishes
        addDishes();
        //2. create orders
        createOrders();
        //2. fulfill orders
        fulfillOrders(2);
        //3. total price for table 15
        System.out.println("Celková cena pro stůl 15. je: " + manager.getPriceForTable(15) + " Kč");
        //4. managment data
        printManagmentData();
        //5. save data
        saveData();
    }

    public static void loadData(){
        try {
            cookBook.loadDishes(Settings.PATH + Settings.DISHESFILE);
            manager.loadOrders(Settings.PATH + Settings.ORDERSFILE, cookBook);
        } catch (RestaurantException | FileNotFoundException e) {
            System.err.print(e.getLocalizedMessage());
        }
    }

    public static void addDishes(){
        try {
            cookBook.add(new Dish("Kuřecí řízek obalovaný 150g", 150, 30));
            cookBook.add(new Dish("Hranolky 150g", 75, 10));
            cookBook.add(new Dish("Pstruh na víně 200", 299, 45));
            cookBook.add(new Dish("Kofola 0,5l", 39, 5));
        } catch (RestaurantException e) {
            System.err.print(e.getLocalizedMessage());
        }
    }

    public static void createOrders(){
        manager.add(new Order(15, cookBook.get(0), 2, LocalDateTime.now().plusMinutes(3)));
        manager.add(new Order(15, cookBook.get(1), 2, LocalDateTime.now().plusMinutes(3)));
        manager.add(new Order(15, cookBook.get(3), 2, LocalDateTime.now()));

        manager.add(new Order(2, cookBook.get(2), 1, LocalDateTime.now()));
        manager.add(new Order(2, cookBook.get(3), 1, LocalDateTime.now()));
    }

    public static void fulfillOrders(int orderIndex)
    {
        try {
            manager.get(orderIndex).setFulfilmentTime(LocalDateTime.now().plusMinutes(5));
        } catch (RestaurantException e) {
            System.err.print(e.getLocalizedMessage());
        } catch (IndexOutOfBoundsException e){
            System.err.print("No order with index: " + orderIndex );
        }
    }

    public static void printManagmentData(){
        System.out.println("Rozpracovaných objednávek je: " + manager.countNotFulfiled());
        System.out.println("Seznam objednávek dle času: " + manager.sortOrderedTime());
        System.out.println("Průměrná doba objednávky je: " + manager.getAverageOrderTime());
        System.out.println("Dnes byla objednána jídla: " + manager.getOrderedDishes());
        System.out.println(manager.getTableOrder(15));
        System.out.println(manager.getTableOrder(2));
    }

    public static void saveData(){
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