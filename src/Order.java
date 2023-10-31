import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Order {
    private int tableNumber;
    private Dish dish;
    private int dishCount;
    private LocalDateTime orderedTime;
    private LocalDateTime fulfilmentTime;
    private boolean paid;

    public Order(int tableNumber, Dish dish, int dishCount, LocalDateTime orderedTime) {
        this.tableNumber = tableNumber;
        this.dish = dish;
        this.dishCount=dishCount;
        this.orderedTime = orderedTime;
        this.paid = false;
    }
    public Order(int tableNumber, Dish dish, int dishCount, LocalDateTime orderedTime, LocalDateTime fulfilmentTime) {
        this.tableNumber = tableNumber;
        this.dish = dish;
        this.dishCount = dishCount;
        this.orderedTime = orderedTime;
        this.fulfilmentTime = fulfilmentTime;
        this.paid = false;
    }

    public Order(int tableNumber, Dish dish, int dishCount, LocalDateTime orderedTime, LocalDateTime fulfilmentTime, boolean paid) {
        this.tableNumber = tableNumber;
        this.dish = dish;
        this.dishCount = dishCount;
        this.orderedTime = orderedTime;
        this.fulfilmentTime = fulfilmentTime;
        this.paid = paid;
    }

    public void setFulfilmentTime(LocalDateTime fulfilmentTime) throws RestaurantException {
        if (fulfilmentTime.isBefore(orderedTime))
            throw new RestaurantException("Order fulfilment time has to be after ordered time.");
        this.fulfilmentTime = fulfilmentTime;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isFullfiled() {
        return fulfilmentTime != null;
    }

    public boolean isPaid() {
        return paid;
    }

    public String isPaidString() {
        if(paid) return "zaplaceno";
        return "";
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public String getOrderedTimeFormatted() {
        return Settings.DATE_FORMAT_CZ.format(orderedTime);
    }

    public LocalDateTime getFulfilmentTime() {
        return fulfilmentTime;
    }

    public String getFulfilmentTimeFormatted() {
        if(fulfilmentTime!=null) return Settings.DATE_FORMAT_CZ.format(fulfilmentTime);
        else return "";
    }

    public String getSaveData()
    {
        StringBuilder saveData = new StringBuilder();
        saveData.append(tableNumber).append(Settings.DELIMITER)
                .append(getDish().getId()).append(Settings.DELIMITER)
                .append(dishCount).append(Settings.DELIMITER)
                .append(getOrderedTime()).append(Settings.DELIMITER);
        if(getFulfilmentTime() != null) saveData.append(getFulfilmentTime()).append(Settings.DELIMITER);
        if(isPaid()) saveData.append(isPaid()).append(Settings.DELIMITER);
        saveData.append("\n");
        return saveData.toString();
    }

    public long getDuration()
    {
        return ChronoUnit.MINUTES.between(orderedTime, fulfilmentTime);
    }

    public Dish getDish() {
        return dish;
    }

    public int getDishCount() {
        return dishCount;
    }

    public int getPrice(){
        return getDish().getPrice()*getDishCount();
    }
    @Override
    public String toString() {
        return "Order{" +
                "tableNumber=" + tableNumber +
                ", dish=" + dish +
                ", orderedTime=" + orderedTime +
                ", fulfilmentTime=" + fulfilmentTime +
                ", paid=" + paid +
                '}';
    }
}
