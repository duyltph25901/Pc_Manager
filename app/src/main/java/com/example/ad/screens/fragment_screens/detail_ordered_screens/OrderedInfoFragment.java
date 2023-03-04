package com.example.ad.screens.fragment_screens.detail_ordered_screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ad.R;
import com.example.ad.database.db.Realtime;
import com.example.ad.database.db_local.DatabaseLocal;
import com.example.ad.model.Bill;
import com.example.ad.model.Order;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderedInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderedInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderedInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderedInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderedInfoFragment newInstance(String param1, String param2) {
        OrderedInfoFragment fragment = new OrderedInfoFragment();
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

    private TextView textCreatedAt, textPriceSum, textStatus;
    private View view;

    private Bill bill;
    private Order order;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_info, container, false);

        _init();
        _getData();
        _showDataView();

        return view;
    }

    private void _init() {
        textCreatedAt = view.findViewById(R.id.textCreatedAt);
        textPriceSum = view.findViewById(R.id.textPriceSum);
        textStatus = view.findViewById(R.id.textOrderStatus);
    }

    private void _getData() {
        bill = DatabaseLocal.getBillCurrent(getActivity());
        order = Realtime.foundOrderById(bill.getIdOrder());
    }

    private void _showDataView() {
        if (order == null) return;
        textCreatedAt.setText("Đặt hàng lúc: " + order.getCreatedAd());
        textPriceSum.setText("Tổng tiền: " + order.getSumPrice() + "$");
        textStatus.setText("Trạng thái: " +
                (order.isStatus() ? "Giao hàng thành công" : "Đang chờ")
        );
    }
}