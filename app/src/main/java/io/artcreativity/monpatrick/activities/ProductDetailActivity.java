package io.artcreativity.monpatrick.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import io.artcreativity.monpatrick.dao.DataBaseRoom;
import io.artcreativity.monpatrick.dao.ProductRoomDao;
import io.artcreativity.monpremierprojet.R;
import io.artcreativity.monpatrick.entities.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView designation;
    private TextView descriptionView;
    private TextView priceView;
    private TextView quantityView;
    private TextView alertquantityView;
    final static int EDIT_ACTIVITE=140;

    private ProductRoomDao productRoomDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Product product = (Product) getIntent().getSerializableExtra("PRODUCT");
        productRoomDao= DataBaseRoom.getInstance(getApplicationContext()).productRoomDao();

        designation=findViewById(R.id.Name);
        descriptionView=findViewById(R.id.Description);
        priceView=findViewById(R.id.Prix);
        quantityView=findViewById(R.id.Quantite);
        alertquantityView=findViewById(R.id.Alert_quantite);
        Log.e("TAG", "onCreate: " + product);
    }
}