package uk.ac.le.co2103.part2;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private HandleProductClick clickListener;

    public ProductAdapter(Context context, HandleProductClick clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        holder.tvProdName.setText(this.productList.get(position).name);
        holder.tvProdQuantity.setText(this.productList.get(position).quantity);
        holder.tvProdUnit.setText(this.productList.get(position).unit);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.productClick(productList.get(position));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clickListener.productLongClick(productList.get(position));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView tvProdName;
        TextView tvProdQuantity;
        TextView tvProdUnit;

        public ProductViewHolder(@NonNull View view) {
            super(view);
            tvProdName = view.findViewById(R.id.textViewProductName1);
            tvProdQuantity = view.findViewById(R.id.textViewProductQuantity2);
            tvProdUnit = view.findViewById(R.id.textViewProductUnit3);

        }
    }

    public interface HandleProductClick {
        void productClick(Product product);
        void productLongClick(Product product);
    }
}
