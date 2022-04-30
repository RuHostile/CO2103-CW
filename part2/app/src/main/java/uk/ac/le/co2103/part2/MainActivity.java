package uk.ac.le.co2103.part2;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ShoppingListAdapter.HandleShoppingListClick{
    private ShoppingListAdapter shoppingListAdapter;
    private static final String TAG = MainActivity.class.getSimpleName();

    private Button confirmYes, confirmNo;
    private TextView shoppingListName;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_main);

        final FloatingActionButton button = findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, CreateListActivity.class),1);
            }
        });

        shoppingListRecyclerView();

        loadShoppingLists();
    }

    private void shoppingListRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        shoppingListAdapter = new ShoppingListAdapter(this,this);
        recyclerView.setAdapter(shoppingListAdapter);
    }

    private void loadShoppingLists() {
        ShoppingListDB db = ShoppingListDB.getDbInstance(this.getApplicationContext());
        List<ShoppingList> allShoppingLists = db.shoppingListDao().getAllShoppingLists();
        shoppingListAdapter.setShoppingList(allShoppingLists);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1) {
            loadShoppingLists();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void shoppingListClick(ShoppingList shoppingList) {
        Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
        intent.putExtra("shopping_list_id", shoppingList.listId);
        intent.putExtra("shopping_list_name",shoppingList.name);

        startActivity(intent);
    }

    public void shoppingListLongClick(ShoppingList shoppingList) {
        popUpConfirm(shoppingList);
    }

    public void popUpConfirm(ShoppingList shoppingList){
        dialogBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.popup_main, null);

        shoppingListName = (TextView) popupView.findViewById(R.id.textView5);
        shoppingListName.setText(shoppingList.name);
        confirmYes = (Button) popupView.findViewById(R.id.btn_main_yes);
        confirmYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Deleted product", Toast.LENGTH_LONG).show();
                String sLname = "You Deleted : "+ shoppingList.name;
                Toast.makeText(MainActivity.this, sLname, Toast.LENGTH_SHORT).show();
                deleteShoppingList(shoppingList);
                finish();
                startActivity(getIntent());
                dialog.hide();

            }
        });

        confirmNo = (Button) popupView.findViewById(R.id.btn_main_no);
        confirmNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });

        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    private void deleteShoppingList(ShoppingList shoppingList) {
        ShoppingListDB db = ShoppingListDB.getDbInstance(this.getApplicationContext());
        List productList = db.shoppingListDao().getAllProductsList(shoppingList.listId);
        db.shoppingListDao().deleteShoppingList(shoppingList);

        for(int i = 0; i < productList.size(); i++) {
            System.out.println(productList.get(i));
            db.shoppingListDao().deleteProduct((Product) productList.get(i));

        }
    }

}