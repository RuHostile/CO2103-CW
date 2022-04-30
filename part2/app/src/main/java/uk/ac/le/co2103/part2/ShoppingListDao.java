package uk.ac.le.co2103.part2;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShoppingListDao {

    @Query("SELECT * FROM shoppingList")
    List<ShoppingList> getAllShoppingLists();

    @Insert
    void insertShoppingList(ShoppingList... shoppingLists);

    @Update
    void updateShoppingList(ShoppingList shoppingList);

    @Delete
    void deleteShoppingList(ShoppingList shoppingList);

    @Query("SELECT * FROM product WHERE productShoppingListId = :prodId")
    List<Product> getAllProductsList(int prodId);

    @Insert
    void insertProduct(Product... product);

    @Update
    void updateProduct(Product product);

    @Delete
    void deleteProduct(Product product);
}
