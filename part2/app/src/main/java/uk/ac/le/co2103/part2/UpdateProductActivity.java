package uk.ac.le.co2103.part2;

import androidx.appcompat.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class UpdateProductActivity extends AppCompatActivity {
    private int shoppingListId, productId;
    int count;

    EditText updProdName, updProdDescription;
    TextView updProdQuantity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        updProdName= findViewById(R.id.updateProductName);
        updProdName.setInputType(InputType.TYPE_CLASS_TEXT);
        updProdQuantity= (TextView) findViewById(R.id.updateProductQuantity);
        updProdQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        updProdDescription = (EditText) findViewById(R.id.updateProductDescription);
        updProdDescription.setInputType(InputType.TYPE_CLASS_TEXT);

        Spinner updProdUnit = findViewById(R.id.updateProductUnit);
        String data[] = {"Unit", "Kg", "Liter"};

        count = Integer.parseInt(getIntent().getStringExtra("product_quantity"));

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,data);
        updProdUnit.setAdapter(spinnerAdapter);
        updProdUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Button btmUpdateProduct = findViewById(R.id.buttonUpdateProduct);
        shoppingListId = getIntent().getIntExtra("shopping_list_id",1);
        productId = getIntent().getIntExtra("product_id",1);
        updProdName.setText(getIntent().getStringExtra("product_name"));
        updProdQuantity.setText(getIntent().getStringExtra("product_quantity"));
        updProdDescription.setText(getIntent().getStringExtra("product_description"));

        btmUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = updProdName.getText().toString().trim();
                String description = updProdDescription.getText().toString().trim();
                String quantity = updProdQuantity.getText().toString().trim();
                String unit = updProdUnit.getSelectedItem().toString();
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(quantity) ||TextUtils.isEmpty(unit) ) {
                    Toast.makeText(UpdateProductActivity.this, "Fill all fields.", Toast.LENGTH_LONG).show();
                    return;
                }
                updateProduct(name, quantity, unit, description);
            }
        });

    }

    public void increment(View v) {
        count++;
        updProdQuantity.setText("" + count);

    }

    public void decrement(View v) {
        if(count <= 1) count =1;
        else count--;
        updProdQuantity.setText("" + count);

    }

    private void updateProduct(String name, String quantity, String unit, String description){
        ShoppingListDB db = ShoppingListDB.getDbInstance(this.getApplicationContext());

        Product product = new Product();
        product.productId = productId;
        product.name = name;
        product.quantity = quantity;
        product.description = description;
        product.unit = unit;
        product.productShoppingListId = shoppingListId;

        //if (checkIfProductExists(name, quantity, unit).equals(true)) {
        //    Toast.makeText(UpdateProductActivity.this, "Product already exists.", Toast.LENGTH_LONG).show();
        //    return;
        //}

        db.shoppingListDao().updateProduct(product);

        finish();
    }

    private Boolean checkIfProductExists(String productName, String quantity, String unit) {
        ShoppingListDB db = ShoppingListDB.getDbInstance(this.getApplicationContext());
        List<Product> productList = db.shoppingListDao().getAllProductsList(shoppingListId);
        for(int i = 0; i < productList.size(); i++) {

            if((productList.get(i).name).toLowerCase().trim().equals(productName.toLowerCase().trim())) {
                if(!(productList.get(i).unit).equals(unit)) {
                    return false;
                }
                if(!(productList.get(i).quantity).equals(quantity)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }
}