package uk.ac.le.co2103.part2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class CreateListActivity extends AppCompatActivity {
    Uri imageUri = null;
    private static final int PICK_IMAGE = 1;
    ImageView image;
    Bitmap bitmap;
    String bitmapToString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        EditText shoppingListName = findViewById(R.id.editTextShoppingListName);
        image = findViewById(R.id.img_list_image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "Select A Picture."),PICK_IMAGE);
            }
        });
        Button saveButton = findViewById(R.id.buttonCreateList);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String name = shoppingListName.getText().toString();
                if(TextUtils.isEmpty(name)) {
                    Toast.makeText(CreateListActivity.this, "Enter a Shopping List name",Toast.LENGTH_LONG).show();
                    return;
                }
                if(checkIfShoppingListExists(name).equals(true)) {
                    Toast.makeText(CreateListActivity.this, "Shopping List already exists.", Toast.LENGTH_LONG).show();
                    return;
                }
                saveNewShoppingList(name);
            }
        });
    }

    private void saveNewShoppingList(String name) {
        ShoppingListDB db = ShoppingListDB.getDbInstance(this.getApplicationContext());

        ShoppingList shoppingList = new ShoppingList();
        shoppingList.name = name;
        shoppingList.image = bitmapToString;
        db.shoppingListDao().insertShoppingList(shoppingList);

        finish();
    }

    private Boolean checkIfShoppingListExists(String shoppingListName) {
        ShoppingListDB db = ShoppingListDB.getDbInstance(this.getApplicationContext());
        List<ShoppingList> shoppingListList = db.shoppingListDao().getAllShoppingLists();
        for(int i = 0; i < shoppingListList.size(); i++) {

            if((shoppingListList.get(i).name).toLowerCase().trim().equals(shoppingListName.toLowerCase().trim())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            imageUri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                image.setImageBitmap(bitmap);
                bitmapToString = BitMapToString(bitmap);

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}