package uk.ac.le.co2103.part2;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Product {

    @PrimaryKey(autoGenerate = true)
    public int productId;

    @ColumnInfo(name = "productName")
    public String name;

    @ColumnInfo(name = "productQuantity")
    public String quantity;

    @ColumnInfo(name = "productUnit")
    public String unit;

    @ColumnInfo(name = "productDescription")
    public String description;

    @ColumnInfo(name = "productShoppingListId")
    public int productShoppingListId;
}
