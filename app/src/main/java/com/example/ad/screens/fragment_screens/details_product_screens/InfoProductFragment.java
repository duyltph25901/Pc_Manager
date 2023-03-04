package com.example.ad.screens.fragment_screens.details_product_screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ad.R;
import com.example.ad.database.db_local.DatabaseLocal;
import com.example.ad.model.Product;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InfoProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoProductFragment newInstance(String param1, String param2) {
        InfoProductFragment fragment = new InfoProductFragment();
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
    private TextView textId, textPrice, textDiscount, textSum, textPurchases;

    private Product mProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_info_product, container, false);

        _getProductCurrent();
        _init();
        _setDefault();

        return view;
    }

    private void _getProductCurrent() {
        mProduct = DatabaseLocal.getProductCurrent(getActivity());
    }

    private void _init() {
        textId = view.findViewById(R.id.textId);
        textPrice = view.findViewById(R.id.textPrice);
        textDiscount = view.findViewById(R.id.textDiscount);
        textSum = view.findViewById(R.id.textSum);
        textPurchases = view.findViewById(R.id.textPurchases);
    }

    private void _setDefault() {
        textId.setText(
                "Mã sản phẩm: " + mProduct.getId()
        );
        textPrice.setText(
                "Giá tiền: " + mProduct.getPrice() + "$"
        );
        textSum.setText(
                "Tổng kho: " + mProduct.getSum()
        );
        textDiscount.setText(
                "Triết khấu: " + mProduct.getDiscount() +"%"
        );
        textPurchases.setText(
                "Lượt mua: " + mProduct.getPurchases()
        );
    }
}