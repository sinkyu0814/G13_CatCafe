package viewmodel;

import java.io.Serializable;

public class CartItem implements Serializable {

    private int goodsId;        // menu_id
    private String goodsName;   // 商品名
    private int price;          // 単価
    private int quantity;       // 数量

    public CartItem() {}

    public CartItem(int goodsId, String goodsName, int price, int quantity) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /** 小計（price × quantity） */
    public int getTotalPrice() {
        return price * quantity;
    }
}