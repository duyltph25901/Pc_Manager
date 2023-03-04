package com.example.ad.screens.fragment_screens.home_screens;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ad.R;
import com.example.ad.function.Const;
import com.example.ad.function.CustomTextColor;
import com.example.ad.function.ShowDialog;
import com.example.ad.function.Validate;
import com.example.ad.model.Product;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
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
    private EditText inputProductName, inputProductIntroduce, inputProductPrice, inputProductDiscount, inputProductSum;
    private ImageView imageProduct;
    private Button buttonAdd;
    private ProgressDialog progressDialog;

    private Bitmap path;
    private Uri uri;

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setPath(Bitmap path) {
        this.path = path;
    }

    public Bitmap getPath() {
        return path;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add, container, false);
        _init();
        _setOnclick();
        return view;
    }

    private void _init() {
        TextView textTitle = view.findViewById(R.id.textTitle);
        inputProductName = view.findViewById(R.id.inputProductName);
        inputProductIntroduce = view.findViewById(R.id.inputIntroduce);
        inputProductPrice = view.findViewById(R.id.inputPrice);
        inputProductDiscount = view.findViewById(R.id.inputDiscount);
        inputProductSum = view.findViewById(R.id.inputProductSum);
        imageProduct = view.findViewById(R.id.imageChooseProductImage);
        buttonAdd = view.findViewById(R.id.btnAddProduct);

        progressDialog = new ProgressDialog(getActivity());

        // custom text color
        CustomTextColor.customTextColor(textTitle,
                ContextCompat.getColor(requireContext(), R.color.color_text_start),
                ContextCompat.getColor(requireContext(), R.color.color_text_end));
    }

    private void _setOnclick() {
        imageProduct.setOnClickListener(view -> _openGallery());
        buttonAdd.setOnClickListener(view -> _add());
    }

    private void _openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        activityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    final private ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent i = result.getData();
                    if (i == null) return;
                    Uri uri = i.getData();
                    try {
                        setPath(
                                MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri)
                        );
                        Glide.with(AddFragment.this) // show image after chosen
                                .load(uri)
                                .error(R.drawable.icon_cam)
                                .into(imageProduct);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

    private void _add() {
        String name = inputProductName.getText().toString().trim();
        String introduce = inputProductIntroduce.getText().toString().trim();
        String price = inputProductPrice.getText().toString().trim();
        String discount = inputProductDiscount.getText().toString().trim();
        String sum = inputProductSum.getText().toString().trim();

        boolean isNull = Validate.isNull(name, introduce, price, sum) || path == null;
        boolean isPrice = Validate.isPrice(price);
        boolean isDiscount = Validate.isDiscount(discount);
        boolean isSum = Validate.isSum(sum);
        boolean isBreaking = Validate.isBreakingAdd(getActivity(), isNull, isPrice, isDiscount, isSum);
        if (isBreaking) return;

        progressDialog.show();
        _progressAdd(name, introduce, price, discount, sum);
    }

    private void _progressAdd(String... input) {
        // get attribute
        String id = _autoGenId();
        String name = input[0];
        String introduce = input[1];
        double price = Double.parseDouble(input[2]);
        int discount = input[3].isEmpty()
                ? 0 : Integer.parseInt(input[3]);
        int sum = Integer.parseInt(input[4]);

        // handle add
        // init storage
        StorageReference stRef = FirebaseStorage.getInstance().getReference();
        StorageReference lapTopRef = stRef.child(Const.productTableName).child(path.toString());
        // Get the data from an ImageView as bytes
        imageProduct.setDrawingCacheEnabled(true);
        imageProduct.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageProduct.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        // upload image from device
        UploadTask uploadTask = lapTopRef.putBytes(data);
        uploadTask
                .addOnFailureListener(e -> {
                    progressDialog.cancel();
                    ShowDialog.handleShowDialog(getActivity(), Const.flagErrorDialog, e.getMessage());
                })
                .addOnSuccessListener(taskSnapshot -> {
                    // get Uri img on firebase
                    Task<Uri> getDownloadUriTask = uploadTask.continueWithTask(task -> {
                        if(!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }
                        return lapTopRef.getDownloadUrl();
                    });
                    getDownloadUriTask.addOnCompleteListener(task -> {
                        progressDialog.cancel();
                        if (task.isSuccessful()) {
                            setUri(task.getResult());

                            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

                            // init object
                            Product product = new Product(id, name, getUri().toString(), getPath().toString(), introduce, price, discount, sum, 0);
                            // handle insert new object
                            dbRef.child(Const.productTableName).child(id).setValue(product);
                            ShowDialog.handleShowDialog(getActivity(), Const.flagSuccessDialog, "New added success!");
                            _clear();
                        } else {
                            ShowDialog.handleShowDialog(getActivity(), Const.flagErrorDialog, "Add new failure. Please try again!!!");
                        }
                    });
                });
    }

    private String _autoGenId() {
        return FirebaseDatabase.getInstance().getReference().push().getKey();
    }

    private void _clear() {
        inputProductName.setText("");
        inputProductPrice.setText("");
        inputProductIntroduce.setText("");
        inputProductDiscount.setText("");
        inputProductSum.setText("");
        setPath(null);
        setUri(null);
        imageProduct.setImageResource(R.drawable.icon_cam);
    }
}