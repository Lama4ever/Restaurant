import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class RestaurantManager extends ArrayList<Order> {
    public int countNotFulfiled() {
        int count = 0;
        for (Order order : this) {
            if (!order.isFullfiled()) count++;
        }
        return count;
    }

    public void loadOrders(String path, CookBook cookingBook) throws RestaurantException, FileNotFoundException {
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(path)))) {
            int lineNumber = 0;
            while (scanner.hasNext()) {
                String data = scanner.nextLine();
                String[] parsedData = data.split(Settings.DELIMITER);
                if (parsedData.length < 4)
                    throw new RestaurantException("Error reading Order on line : " + lineNumber + " " + data);
                int tableNumber;
                int dishNumber;
                int dishCount;
                LocalDateTime orderedTime;
                try {
                    tableNumber = Integer.parseInt(parsedData[0]);
                    dishNumber = Integer.parseInt(parsedData[1]);
                    dishCount = Integer.parseInt(parsedData[2]);
                    orderedTime = LocalDateTime.parse(parsedData[3]);
                } catch (DateTimeParseException | NumberFormatException e) {
                    throw new RestaurantException("Wrong format of data for Order on line: " + lineNumber + " " + data);
                }
                if (parsedData.length == 4) {
                    this.add(new Order(tableNumber, cookingBook.getDishById(dishNumber), dishCount, orderedTime));
                } else if (parsedData.length == 5) {
                    LocalDateTime fulfilledTime;
                    try {
                        fulfilledTime = LocalDateTime.parse(parsedData[4]);
                    } catch (DateTimeParseException e) {
                        throw new RestaurantException("Wrong format of data for Order on line: " + lineNumber + " " + data);
                    }
                    this.add(new Order(tableNumber, cookingBook.getDishById(dishNumber), dishCount, orderedTime, fulfilledTime));
                } else {
                    LocalDateTime fulfilledTime;
                    boolean paid;
                    try {
                        fulfilledTime = LocalDateTime.parse(parsedData[4]);
                        paid = Boolean.parseBoolean(parsedData[5]);
                    } catch (DateTimeParseException e) {
                        throw new RestaurantException("Wrong format of data for Order on line: " + lineNumber + " " + data);
                    }
                    this.add(new Order(tableNumber, cookingBook.getDishById(dishNumber), dishCount, orderedTime, fulfilledTime, paid));
                }
            }
        }
    }

    public void save() throws RestaurantException {
        File ordersFile = new File(Settings.PATH + Settings.ORDERSFILE);
        if (!ordersFile.exists()) {
            try {
                ordersFile.createNewFile();
            } catch (IOException e) {
                throw new RestaurantException("Cannot create file: " + Settings.PATH + Settings.ORDERSFILE);
            }
        }
        try (PrintWriter writter = new PrintWriter(new BufferedWriter(new FileWriter(ordersFile)))) {
            StringBuilder data = new StringBuilder();
            this.forEach(order -> data.append(order.getSaveData()));
            writter.print(data);
        } catch (IOException e) {
            throw new RestaurantException("Cannot write to file: " + Settings.PATH + Settings.ORDERSFILE);
        }
    }

    public String sortOrderedTime() {
        StringBuilder returnString = new StringBuilder();
        ArrayList<Order> sortedList = new ArrayList<>(this);
        sortedList.sort((o1, o2) -> {
            if (o1.getOrderedTime().isBefore(o2.getOrderedTime())) {
                return -1;
            }
            if (o1.getOrderedTime().isAfter(o2.getOrderedTime())) {
                return 1;
            }
            return 0;
        });
        for (Order order : sortedList) {
            returnString.append(order.toString()).append("\n");
        }
        return returnString.toString();
    }

    public double getAverageOrderTime() {
        long totalLength = 0;
        int count = 0;
        for (Order order : this) {
            if (order.isFullfiled()) {
                totalLength += order.getDuration();
                count++;
            }
        }
        if (count > 0) {
            return (double) totalLength / count;
        } else {
            return 0;
        }

    }

    public int getPriceForTable(int tableNumber) {
        int fullPrice = 0;
        for (Order order : this) {
            if (order.getTableNumber() == tableNumber) {
                fullPrice += order.getPrice();
            }
        }
        return fullPrice;
    }

    public String getOrderedDishes() {
        StringBuilder returnString = new StringBuilder();
        Set<Dish> orderedDishes = new HashSet<>();
        this.forEach(order -> orderedDishes.add(order.getDish()));
        orderedDishes.forEach(dish -> returnString.append(dish.toString()).append("\n"));
        return returnString.toString();
    }

    public String getTableOrder(int tableNumber) {
        StringBuilder returnString = new StringBuilder();
        returnString.append("** Objednávky pro stůl č. ").append(tableNumber).append(" **\n").append("****\n");
        final int[] id = {1};
        this.stream().filter(order -> order.getTableNumber() == tableNumber).forEach(filteredOrder -> {

            returnString.append(String.format("%s. %s %sx (%s Kc) :\t %s-%s %s\n",
                    id[0],
                    filteredOrder.getDish().getName(),
                    filteredOrder.getDishCount(),
                    filteredOrder.getPrice(),
                    filteredOrder.getOrderedTimeFormatted(),
                    filteredOrder.getFulfilmentTimeFormatted(),
                    filteredOrder.isPaidString()
            ));
            id[0]++;
        });
        returnString.append("******\n");
        return returnString.toString();
    }
}
