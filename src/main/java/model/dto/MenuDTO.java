package model.dto;

public class MenuDTO {
    private int id;
    private String name;
    private int quantity;
    private int price;
    private String img;
    private String category;

    // ★ 引数なしコンストラクタ
    public MenuDTO() {}

    // ★ 6つ全部を受け取るコンストラクタ
    public MenuDTO(int id, String name, int quantity, int price, String img, String category) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.img = img;
        this.category = category;
    }

    // getter/setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
