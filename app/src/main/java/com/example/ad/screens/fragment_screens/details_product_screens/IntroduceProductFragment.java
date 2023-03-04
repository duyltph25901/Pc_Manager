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
 * Use the {@link IntroduceProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntroduceProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IntroduceProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IntroduceProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IntroduceProductFragment newInstance(String param1, String param2) {
        IntroduceProductFragment fragment = new IntroduceProductFragment();
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
    private TextView textIntroProduct;

    private Product mProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_introduce_product, container, false);
        _getProductCurrent();
        _init();
        _setDefault();

        return view;
    }

    private void _getProductCurrent() {
        mProduct = DatabaseLocal.getProductCurrent(getActivity());
    }

    private void _init() {
        textIntroProduct = view.findViewById(R.id.textIntroProduct);
    }

    private void _setDefault() {
        textIntroProduct.setText(mProduct.getIntro());
    }
}