package uk.ac.le.co2103.part2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class AddProductActivity extends AppCompatActivity {
    private int shoppingListId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        EditText productName = findViewById(R.id.editTextName);
        productName.setInputType(InputType.TYPE_CLASS_TEXT);
        EditText productDescription = findViewById(R.id.editTextProductDescription);
        productDescription.setInputType(InputType.TYPE_CLASS_TEXT);
        EditText productQuantity = findViewById(R.id.editTextQuantity);
        productQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);

        Spinner productUnit = findViewById(R.id.spinner);
        String data[] = {"Unit", "Kg", "Liter"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,data);
        productUnit.setAdapter(spinnerAdapter);
        productUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Button addButton = findViewById(R.id.buttonAddProduct);
        shoppingListId = getIntent().getIntExtra("shopping_list_id",1);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = productName.getText().toString().trim();
                String quantity = productQuantity.getText().toString().trim();
                String description = productDescription.getText().toString().trim();
                String unit = productUnit.getSelectedItem().toString();
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(quantity) || TextUtils.isEmpty(unit)) {
                    Toast.makeText(AddProductActivity.this, "Fill all fields.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(checkIfProductExists(name).equals(true)) {
                    Toast.makeText(AddProductActivity.this, "Product already exists.", Toast.LENGTH_LONG).show();
                    return;
                }

                saveNewProduct(name, quantity, unit, description);
            }
        });

    }
    private void saveNewProduct(String name, String quantity, String unit, String description){
        ShoppingListDB db = ShoppingListDB.getDbInstance(this.getApplicationContext());

        Product product = new Product();
        product.name = name;
        product.quantity = quantity;
        product.unit = unit;
        product.description = description;
        product.productShoppingListId = shoppingListId;
        db.shoppingListDao().insertProduct(product);

        finish();
    }

    private Boolean checkIfProductExists(String productName) {
        ShoppingListDB db = ShoppingListDB.getDbInstance(this.getApplicationContext());
        List<Product> productList = db.shoppingListDao().getAllProductsList(shoppingListId);
        for(int i = 0; i < productList.size(); i++) {

            if((productList.get(i).name).toLowerCase().trim().equals(productName.toLowerCase().trim())) {
                return true;
            }
        }
        return false;
    }
}