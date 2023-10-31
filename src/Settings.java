import java.text.DateFormat;
import java.time.format.DateTimeFormatter;

public class Settings {
    public static final String PATH = "./resources/";
    public static final String DISHESFILE = "dishes.txt";
    public static final String ORDERSFILE = "orders.txt";

    public static final String DELIMITER = "\t";

    public static final DateTimeFormatter DATE_FORMAT_CZ = DateTimeFormatter.ofPattern("HH:mm");
}
