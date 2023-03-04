package com.example.ad.screens.fragment_screens.detail_ordered_screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ad.R;
import com.example.ad.adapter.rcv.ProductBoughtAdapter;
import com.example.ad.database.db.Realtime;
import com.example.ad.database.db_local.DatabaseLocal;
import com.example.ad.function.Const;
import com.example.ad.model.Bill;
import com.example.ad.model.Cart;
import com.example.ad.model.Order;
import com.example.ad.model.Product;
import com.example.ad.screens.activity_screens.DetailsProductActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListProductOrderedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListProductOrderedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListProductOrderedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListProductOrderedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListProductOrderedFragment newInstance(String param1, String param2) {
        ListProductOrderedFragment fragment = new ListProductOrderedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private View view;
    private RecyclerView rcv;
    private ProgressDialog progressDialog;

    private Bill bill;
    private Order order;
    private List<Product> products;
    private ProductBoughtAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_product_ordered, container, false);
        _init();
        _getData();

        return view;
    }

    private void _init() {
        rcv = view.findViewById(R.id.rcvProductBought);
        products = new ArrayList<>();

        adapter = new ProductBoughtAdapter(getActivity(), products, product -> {
            // save data local
            DatabaseLocal.saveProductCurrent(getActivity(), product);

            Intent intent = new Intent(getActivity(), DetailsProductActivity.class);
            intent.putExtra(Const.keyProductDetails, product);
            startActivity(intent);
        });
    }

    private void _getData() {
        bill = DatabaseLocal.getBillCurrent(getActivity());
        order = Realtime.foundOrderById(bill.getIdOrder());
        Log.d("CheckOrderFound", (order == null)+"");
        if (order == null) return;
        for (String idCart : order.getIdCart()) {
            Cart cart = Realtime.foundCartById(idCart);
            Log.d("CHECK_CART_FOUND", (cart == null) + "");
            if (cart == null) return;
            Product product = Realtime.foundProductById(cart.getIdProduct());
            Log.d("CHECK_PRODUCT_FOUND", (product == null)+"");

            products.add(product);
        }

        Log.d("CheckSizeProducts", (products.size())+"");

        adapter.setProducts(products);
        rcv.setAdapter(adapter);
    }
}