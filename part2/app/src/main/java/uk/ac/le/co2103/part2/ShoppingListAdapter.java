package uk.ac.le.co2103.part2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>{
    private Context context;
    private List<ShoppingList> shoppingListList;
    private HandleShoppingListClick clickListener;

    public ShoppingListAdapter(Context context, HandleShoppingListClick clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setShoppingList(List<ShoppingList> shoppingListList) {
        this.shoppingListList = shoppingListList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShoppingListAdapter.ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false);

        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListAdapter.ShoppingListViewHolder holder, int position) {
        holder.tvName.setText(this.shoppingListList.get(position).name);
        //holder.tvImage.setText(this.shoppingListList.get(position).shoppingListImage);
        String strBitmap = this.shoppingListList.get(position).image;
        Bitmap bitmap = StringToBitMap(strBitmap);
        holder.imageViewImage.setImageBitmap(bitmap);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.shoppingListClick(shoppingListList.get(position));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clickListener.shoppingListLongClick(shoppingListList.get(position));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.shoppingListList.size();
    }

    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvImage;
        ImageView imageViewImage;
        public ShoppingListViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.textViewShoppingListName);
            tvImage = view.findViewById(R.id.textViewShoppingListImage);
            imageViewImage = view.findViewById(R.id.imageViewShoppingList);
        }
    }
    public interface HandleShoppingListClick {
        void shoppingListClick(ShoppingList shoppingList);

        void shoppingListLongClick(ShoppingList shoppingList);
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
