package model.dto;

public class CartDTO {
    private int id;
    private String name;
    private int price;
    private int quantity;
    private String img;

    public CartDTO(int id, String name, int price, int quantity, String img) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.img = img;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getImg() { return img; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
}
