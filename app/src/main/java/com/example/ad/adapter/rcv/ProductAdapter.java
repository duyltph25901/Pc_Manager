package com.example.ad.adapter.rcv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ad.R;
import com.example.ad.model.Product;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> products;
    private ClickItem clickItem;

    public ProductAdapter(Context context, List<Product> products, ClickItem clickItem) {
        this.context = context;
        this.products = products;
        this.clickItem = clickItem;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        if (product == null) return;

        holder.onBindViewHolder(product);
        holder.txtShowDetails.setOnClickListener(view -> clickItem.showDetail(product));
        holder.layoutEdit.setOnClickListener(view -> clickItem.edit(product));
        holder.layoutRemove.setOnClickListener(view -> clickItem.remove(product));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imgProduct;
        private TextView txtProductName, txtPrice, txtShowDetails;
        private TextView textProductSum;
        private LinearLayout layoutRemove, layoutEdit;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            _init(itemView);
        }

        private void _init(View view) {
            imgProduct = view.findViewById(R.id.imgProduct);
            txtProductName = view.findViewById(R.id.txtProductName);
            txtPrice = view.findViewById(R.id.txtPriceProduct);
            txtShowDetails = view.findViewById(R.id.textViewShowDetails);
            textProductSum = view.findViewById(R.id.textProductSum);
            layoutRemove = view.findViewById(R.id.layoutRemove);
            layoutEdit = view.findViewById(R.id.layoutEdit);
        }

        public void onBindViewHolder(final Product product) {
            Glide.with(context)
                    .load(product.getImage())
                    .error(R.mipmap.ic_launcher_round)
                    .into(imgProduct);
            txtPrice.setText(String.valueOf(product.getTax()));

            String name = product.getName();
            name = (name.length() >= 11)
                    ? name.substring(0, 11).toUpperCase() + "..."
                    : name.toUpperCase();
            txtProductName.setText(name);
            textProductSum.setText(product.getSum() + "");
        }
    }

    public interface ClickItem {
        void showDetail(Product product);
        void remove(Product product);
        void edit(Product product);
    }
}
