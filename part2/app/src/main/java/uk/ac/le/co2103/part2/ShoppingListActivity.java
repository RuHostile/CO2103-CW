package uk.ac.le.co2103.part2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.nio.BufferUnderflowException;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity implements ProductAdapter.HandleProductClick {
    private int shoppingListId;
    private ProductAdapter productAdapter;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button popupUpdate, popupDelete;
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        shoppingListId = getIntent().getIntExtra("shopping_list_id",0);
        String shoppingListName = getIntent().getStringExtra("shopping_list_name");

        FloatingActionButton fabAddProd = findViewById(R.id.fabAddProduct);
        fabAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingListActivity.this, AddProductActivity.class);
                intent.putExtra("shopping_list_id", shoppingListId);
                intent.putExtra("shopping_list_name", shoppingListName);
                startActivityForResult(intent,11);
            }
        });

        productRecyclerView();

        loadProductList();
    }

    private void productRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        productAdapter = new ProductAdapter(this,this);
        recyclerView.setAdapter(productAdapter);
    }

    private void loadProductList() {
        ShoppingListDB db = ShoppingListDB.getDbInstance(this.getApplicationContext());
        List<Product> allProducts = db.shoppingListDao().getAllProductsList(shoppingListId);
        productAdapter.setProductList(allProducts);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 11 || requestCode == 12) {
            loadProductList();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void productClick(Product product) {
        popUpDialog(product);
    }

    @Override
    public void productLongClick(Product product) {
        Toast.makeText(ShoppingListActivity.this, product.description, Toast.LENGTH_LONG).show();
    }

    public void popUpDialog(Product product){
        dialogBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.popup, null);

        popupUpdate = (Button) popupView.findViewById(R.id.popupUpdateProduct);
        popupUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
                Intent intent = new Intent(ShoppingListActivity.this, UpdateProductActivity.class);
                intent.putExtra("shopping_list_id", shoppingListId);
                intent.putExtra("product_id", product.productId);
                intent.putExtra("product_name", product.name);
                intent.putExtra("product_quantity", product.quantity);
                intent.putExtra("product_description", product.description);
                intent.putExtra("product_unit", product.unit);

                startActivityForResult(intent, 12);
            }
        });

        popupDelete = (Button) popupView.findViewById(R.id.popupDeleteProduct);
        popupDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ShoppingListActivity.this,"Deleted product", Toast.LENGTH_LONG).show();
                deleteProduct(product);
                dialog.hide();
                startActivity(getIntent());
            }
        });
        back = (TextView) popupView.findViewById(R.id.textViewBackFromPopup);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });

        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    private void deleteProduct(Product product){
        ShoppingListDB db = ShoppingListDB.getDbInstance(this.getApplicationContext());

        product.productShoppingListId = shoppingListId;
        db.shoppingListDao().deleteProduct(product);

    }
}