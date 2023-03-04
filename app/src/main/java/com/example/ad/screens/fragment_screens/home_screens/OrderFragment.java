package com.example.ad.screens.fragment_screens.home_screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ad.R;
import com.example.ad.adapter.rcv.OrderAdapter;
import com.example.ad.database.db.Realtime;
import com.example.ad.database.db_local.DatabaseLocal;
import com.example.ad.function.Const;
import com.example.ad.function.ShowDialog;
import com.example.ad.model.Bill;
import com.example.ad.model.Order;
import com.example.ad.screens.activity_screens.DetailsOrderActivity;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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

    private List<Order> orders;
    private OrderAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order, container, false);
        _init();
        _getDatabase();

        return view;
    }

    private void _init() {
        rcv = view.findViewById(R.id.rcvOrder);
        progressDialog = new ProgressDialog(getActivity());

        orders = new ArrayList<>();
        adapter = new OrderAdapter(getActivity(), orders, new OrderAdapter.ClickItem() {
            @Override
            public void showDetails(Order order) {
                _handleShowDetails(order);
            }

            @Override
            public void acceptedCheckOut(Order order) {
                _handleAcceptedCheckOut(order);
            }
        });
    }

    private void _getDatabase() {
        progressDialog.show();
        Realtime.getDatabaseOrder();
        new Handler().postDelayed(() -> {
            progressDialog.cancel();
            orders = Realtime.getOrders();
            adapter.setOrders(orders);
            rcv.setAdapter(adapter);
        }, 3000);
    }

    private void _handleShowDetails(final Order order) {
        // save bill
        DatabaseLocal.saveBillCurrent(getActivity(), new Bill(order.getId()));

        Intent intent = new Intent(getActivity(), DetailsOrderActivity.class);
        intent.putExtra(Const.keyOrderDetails, order);
        startActivity(intent);
    }

    private void _handleAcceptedCheckOut(final Order order) {
        boolean isSuccess = Realtime.handleAcceptedCheckOut(order);
        if (isSuccess) {
            _getDatabase();
            new Handler()
                    .postDelayed(() -> ShowDialog.handleShowDialog(getActivity(), Const.flagSuccessDialog, "Xác nhận hóa đơn thành công!"), 3000);
        } else {
            ShowDialog.handleShowDialog(getActivity(), Const.flagErrorDialog, "Xác nhận hóa đơn thất bại!");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        _getDatabase();
    }
}