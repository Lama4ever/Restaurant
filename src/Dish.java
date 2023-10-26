import java.util.ArrayList;
import java.util.List;

public class Dish {

    private static int nextId = 0;

    private int id;
    private String name;

    private int price;
    private int preparationTime;
    private String foto;

    private List<String> otherFoto;
    private static int getNextId()
    {
        return ++nextId;
    }
    public Dish(String name, int price, int preparationTime) throws RestaurantException {
        this(name, price, preparationTime,  "blank", new ArrayList<>());
    }

    public Dish(String name, int price, int preparationTime, String foto) throws RestaurantException {
        this(name, price, preparationTime, foto, new ArrayList<>());

    }

    public Dish(String name, int price, int preparationTime,String foto, List<String> otherFoto) throws RestaurantException {
        this.name = name;
        this.id = getNextId();
        this.setPrice(price);
        this.setPreparationTime(preparationTime);
        this.foto = foto;
        this.otherFoto = otherFoto;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPreparationTime(int preparationTime) throws RestaurantException {
        if(preparationTime>0) {
            this.preparationTime = preparationTime;
        } else {
            throw new RestaurantException("Preparation time have to be greater then 0.");
        }
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) throws RestaurantException {
        if(price>=0) {
            this.price = price;
        } else {
            throw new RestaurantException("Price cannot be lower then 0.");
        }
    }


    public void setFoto(String foto) {
        this.foto = foto;
    }


    public void setOtherFoto(List<String> otherFoto) {
        this.otherFoto = otherFoto;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name +
                ", price=" + price +
                ", preparationTime=" + preparationTime +
                ", foto='" + foto +
                ", otherFoto=" + otherFoto +
                '}';
    }
}
