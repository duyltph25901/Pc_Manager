package com.example.ad.database.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ad.function.Const;
import com.example.ad.model.Cart;
import com.example.ad.model.Order;
import com.example.ad.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Realtime {
    private static List<Product> products = new ArrayList<>();
    private static List<Order> orders = new ArrayList<>();
    private static List<Cart> carts = new ArrayList<>();
    private static final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private static boolean flagComplete = false;

    public static void setFlagComplete(boolean flagComplete) {
        Realtime.flagComplete = flagComplete;
    }

    public static boolean isFlagComplete() {
        return flagComplete;
    }

    public static void setProducts(List<Product> products) {
        Realtime.products = products;
    }

    public static List<Product> getProducts() {
        return products;
    }

    public static void setOrders(List<Order> orders) {
        Realtime.orders = orders;
    }

    public static List<Order> getOrders() {
        return orders;
    }

    public static void setCarts(List<Cart> carts) {
        Realtime.carts = carts;
    }

    public static List<Cart> getCarts() {
        return carts;
    }

    public static void getDatabaseProduct(String key) {
        dbRef.child(Const.productTableName).orderByChild("id")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        products.clear();
                        if (key.isEmpty()) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                Product product = data.getValue(Product.class);
                                products.add(product);
                            }

                            setProducts(products);
                        } else {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                Product product = data.getValue(Product.class);
                                assert product != null;
                                if (product.getName().toLowerCase().contains(key)) {
                                    products.add(product);
                                }
                            }

                            Collections.reverse(products);
                            setProducts(products);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public static void removeProduct(final Product product) {
        dbRef.child(Const.productTableName).child(product.getId()).removeValue()
                .addOnCompleteListener(task -> setFlagComplete(task.isSuccessful()));
    }

    public static void getDatabaseOrder() {
        dbRef.child(Const.orderTableName).orderByChild("createdAt")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orders.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Order order = data.getValue(Order.class);
                            orders.add(order);
                        }

                        Collections.reverse(orders);
                        setOrders(orders);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Error get database order", error.getMessage());
                    }
                });
    }

    public static boolean handleAcceptedCheckOut(final Order order) {
        try {
            order.setStatus(true);
            dbRef.child(Const.orderTableName)
                    .child(order.getId()).setValue(order);
        } catch (DatabaseException e) {
            return false;
        }

        return true;
    }

    public static Order foundOrderById(String id) {
        for (Order order : orders) {
            if (order.getId().matches(id)) {
                return order;
            }
        }

        return null;
    }

    public static void getDatabaseCart() {
        dbRef.child(Const.cartTableName_BackEnd).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carts.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Cart cart = data.getValue(Cart.class);
                    carts.add(cart);
                }

                setCarts(carts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error get database cart", error.getMessage());
            }
        });
    }

    public static Cart foundCartById(String id) {
        for (Cart cart : carts) {
            if (cart.getId().matches(id)) return cart;
        }

        return null;
    }

    public static Product foundProductById(String id) {
        for (Product product : products) {
            if (product.getId().matches(id)) return product;
        }

        return null;
    }

    public static void updateProduct (final Product product) {
        dbRef.child(Const.productTableName).child(product.getId()).setValue(product)
                .addOnCompleteListener(task -> setFlagComplete(true));
    }
}
