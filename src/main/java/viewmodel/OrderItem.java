package viewmodel;

import java.io.Serializable;
import java.sql.Timestamp;

public class OrderItem implements Serializable {
    
    // DBのカラムとJSPの表示に合わせてフィールドを定義
    private int orderItemId;
    private int orderId;
    private String menuId;
    private String name;      // JSPが item.name を期待しているため goodsName ではなく name に
    private int price;
    private int quantity;
    private String image;     // 画像ファイル名用（menusテーブルから取得）
    
    // 注文情報
    private Timestamp orderTime;
    private String status;
    private int tableNo;

    public OrderItem() {}

    // --- Getter / Setter ---
    // JSPの EL式 ${item.name} は getName() を呼び出します

    public int getOrderItemId() { return orderItemId; }
    public void setOrderItemId(int orderItemId) { this.orderItemId = orderItemId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getMenuId() { return menuId; }
    public void setMenuId(String menuId) { this.menuId = menuId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Timestamp getOrderTime() { return orderTime; }
    public void setOrderTime(Timestamp orderTime) { this.orderTime = orderTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getTableNo() { return tableNo; }
    public void setTableNo(int tableNo) { this.tableNo = tableNo; }

    // 小計（価格 × 個数）
    public int getSubtotal() {
        return this.price * this.quantity;
    }
}