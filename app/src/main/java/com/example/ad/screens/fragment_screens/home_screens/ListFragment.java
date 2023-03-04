package com.example.ad.screens.fragment_screens.home_screens;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ad.R;
import com.example.ad.adapter.rcv.ProductAdapter;
import com.example.ad.database.db.Realtime;
import com.example.ad.database.db_local.DatabaseLocal;
import com.example.ad.function.Const;
import com.example.ad.function.ShowDialog;
import com.example.ad.model.Product;
import com.example.ad.screens.activity_screens.DetailsProductActivity;
import com.example.ad.screens.activity_screens.UpdateProductActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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
    private EditText inputSearch;
    private ImageView imageSearch;
    private ProgressDialog progressDialog;

    private Dialog dialogConfirm;
    private TextView textWarning;
    private Button btnAccept, btnCancel;

    private List<Product> products;
    private ProductAdapter adapter;
    private Product productCurrent;

    public void setProductCurrent(Product productCurrent) {
        this.productCurrent = productCurrent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list, container, false);

        _init();
        _getDatabase(inputSearch.getText().toString().toLowerCase().trim());

        // set onclick
        _setOnclick();

        return view;
    }

    private void _init() {
        // view fragment
        rcv = view.findViewById(R.id.rcvProduct);
        inputSearch = view.findViewById(R.id.inputSearch);
        imageSearch = view.findViewById(R.id.imageSearch);
        progressDialog = new ProgressDialog(getActivity());

        // rcv
        products = new ArrayList<>();
        adapter = new ProductAdapter(getActivity(), products, new ProductAdapter.ClickItem() {
            @Override
            public void showDetail(Product product) {
                _handleShowDetails(product);
            }

            @Override
            public void remove(Product product) {
                _removeProduct(product);
            }

            @Override
            public void edit(Product product) {
                _editProduct(product);
            }
        });

        // dialog
        dialogConfirm = ShowDialog.getDialogWarning(getActivity());
        textWarning = dialogConfirm.findViewById(R.id.textWarningMessage);
        btnAccept = dialogConfirm.findViewById(R.id.btnYesWarningDialog);
        btnCancel = dialogConfirm.findViewById(R.id.btnNoWarningDialog);
    }

    private void _getDatabase(String key) {
        Realtime.getDatabaseProduct(key);
        progressDialog.show();
        new Handler().postDelayed(() -> {
            progressDialog.cancel();
            products = Realtime.getProducts();
            adapter.setProducts(products);
            rcv.setAdapter(adapter);
        }, 3000);
    }

    private void _setOnclick() {
        imageSearch.setOnClickListener(view -> _search());
        btnCancel.setOnClickListener(view -> dialogConfirm.cancel());
        btnAccept.setOnClickListener(view -> _handleRemoveProduct());
    }

    private void _search() {
        progressDialog.show();
        String key = inputSearch.getText().toString().toLowerCase().trim();
        _getDatabase(key);
        _displayKeyBoard();
    }

    private void _displayKeyBoard() {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void _handleShowDetails(final Product product) {
        // save object to db local => convert obj to view model
        DatabaseLocal.saveProductCurrent(getActivity(), product);

        _startActivityProduct(DetailsProductActivity.class, product, Const.flagKeyDetails);
    }

    private void _removeProduct(final Product product) {
        setProductCurrent(product);

        // init
        textWarning.setText("Bạn có muốn xóa "+productCurrent.getName()+"?");
        btnCancel.setText("Không");
        btnAccept.setText("Có");

        dialogConfirm.show();
    }

    private void _handleRemoveProduct() {
        dialogConfirm.cancel();
        progressDialog.show();
        Realtime.removeProduct(productCurrent);
        new Handler()
                .postDelayed(() -> {
                    progressDialog.cancel();
                    boolean isSuccess = Realtime.isFlagComplete();
                    if (isSuccess) {
                        ShowDialog.handleShowDialog(getActivity(), Const.flagSuccessDialog, "Xóa thành công!");
                        _getDatabase(inputSearch.getText().toString().trim());
                    } else {
                        ShowDialog.handleShowDialog(getActivity(), Const.flagSuccessDialog, "Xóa không thành công!");
                    }
                }, 3000);
    }

    private void _editProduct(final Product product) {
        _startActivityProduct(UpdateProductActivity.class, product, Const.flagKeyUpdate);
    }

    private void _startActivityProduct(Class classTo, final Product product, int flag) {
        /*
        *   flag == 1 => update
        *   flag == -1 => details
        * */

        String key = (flag == 1) ? Const.keyProductUpdate : Const.keyProductDetails;

        Intent intent = new Intent(getActivity(), classTo);
        intent.putExtra(key, product);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        _getDatabase(inputSearch.getText().toString().trim());
    }
}