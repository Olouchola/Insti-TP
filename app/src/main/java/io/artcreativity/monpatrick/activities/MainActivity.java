package io.artcreativity.monpatrick.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import io.artcreativity.monpatrick.dao.DataBaseRoom;
import io.artcreativity.monpatrick.dao.ProductRoomDao;
import io.artcreativity.monpatrick.entities.Product;
import io.artcreativity.monpatrick.webservices.ProductWebService;
import io.artcreativity.monpremierprojet.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = MainActivity.class.getCanonicalName();

    private TextInputEditText designationEditText;
    private TextInputEditText descriptionEditText;
    private TextInputEditText priceEditText;
    private TextInputEditText quantityInStockEditText;
    private TextInputEditText alertQuantityEditText;

    private Button myButton;

    private boolean isname;
    private boolean isdescription;
    private boolean isprice;
    private boolean isquantite;
    private boolean isalertquantite;

    private boolean isupdate=false;
    final static int EDIT_PRODUCT=0;
    final static int INSERT_PRODUCT=1;

    private ProductRoomDao productRoomDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        designationEditText = findViewById(R.id.name);
        descriptionEditText = findViewById(R.id.description);
        priceEditText = findViewById(R.id.price);
        quantityInStockEditText = findViewById(R.id.quantity_in_stock);
        alertQuantityEditText = findViewById(R.id.alert_quantity);
        myButton=findViewById(R.id.my_btn);
//        findViewById(R.id.my_btn).setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                saveProduct(view);
//            }
//        });

        findViewById(R.id.my_btn).setOnClickListener(this);
        productRoomDao= DataBaseRoom.getInstance(getApplicationContext()).productRoomDao();
        SetModification();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void saveProduct(int value , Integer id) {
        Log.d(TAG, "saveProduct: ");
        if (isValide()) {
            Product product = new Product();
            product.name = designationEditText.getText().toString();
            product.description = descriptionEditText.getText().toString();
            product.price = Double.parseDouble(priceEditText.getText().toString());
            product.quantityInStock = Double.parseDouble(quantityInStockEditText.getText().toString());
            product.alertQuantity = Double.parseDouble(alertQuantityEditText.getText().toString());
            Log.e(TAG, "saveProduct: " + product);
            Toast.makeText(getApplicationContext(), "J'ai clique", Toast.LENGTH_SHORT).show();

//            new Thread(
//                    ()->{
//                        ProductWebService productWebService=new ProductWebService();
//                        Product save=productWebService.createProduct(product);
////                    System.out.println(save);
//                        System.out.println("save :: " + save);
//                        runOnUiThread(()->{
////                        Intent intent = getIntent();
////                        intent.putExtra("MY_PROD", save);
////                        setResult(Activity.RESULT_OK, intent);
////                        finish();
//                        });
//                    }
//            ).start();

            if (value==EDIT_PRODUCT){
                product.id=id;
                product.serverId=id;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ProductWebService productWebService=new ProductWebService();
                        Product save=productWebService.updateProduct(product);
                        System.out.println("save :: " + save);
                        productRoomDao.update(product);
                        runOnUiThread(()->{
                            Intent intent = getIntent();
                            intent.putExtra("MY_PROD_UPDATE",save);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        });
                    }
                }).start();


            }
            if (value==INSERT_PRODUCT){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ProductWebService productWebService=new ProductWebService();
                        Product save=productWebService.createProduct(product);
                        System.out.println("save :: " + save);
                        productRoomDao.insert(product);
                        runOnUiThread(()->{
                            Intent intent = getIntent();
                            intent.putExtra("MY_PROD", product);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        });
                    }
                }).start();


            }
        }
    }

    @Override
    public void onClick(View view) {

    }


    public boolean isValide(){
        if (designationEditText.getText().toString().isEmpty()){
            designationEditText.setError("designation is required");
            Toast.makeText(getApplicationContext(), "Remplissez le champ designation", Toast.LENGTH_SHORT).show();
            isname=false;
        }
        else {
            isname=true;
        }
        if (descriptionEditText.getText().toString().isEmpty()){
            descriptionEditText.setError("description is required");
            Toast.makeText(getApplicationContext(), "Remplissez le champ description", Toast.LENGTH_SHORT).show();
            isdescription=false;
        }
        else {
            isdescription=true;
        }
        if (priceEditText.getText().toString().isEmpty()){
            priceEditText.setError("price is required");
            Toast.makeText(getApplicationContext(), "Remplissez le champ price", Toast.LENGTH_SHORT).show();
            isprice=false;
        }
        else {
            isprice=true;
        }
        if (quantityInStockEditText.getText().toString().isEmpty()){
            quantityInStockEditText.setError("quantity in stock is rquired");
            Toast.makeText(getApplicationContext(), "Remplissez le champ quantité en stock", Toast.LENGTH_SHORT).show();
            isquantite=false;
        }
        else {
            isquantite=true;
        }
        if (alertQuantityEditText.getText().toString().isEmpty()){
            alertQuantityEditText.setError("alert quantity is required");
            Toast.makeText(getApplicationContext(), "Remplissez le champ alert quantité", Toast.LENGTH_SHORT).show();
            isalertquantite=false;

        }else {
            isalertquantite=true;
        }
        if (isname==true  && isdescription==true&& isprice==true  && isalertquantite==true){
            return true;
        }
        else {
            return false;
        }
    }

    private void SetModification() {
        Product product= (Product) getIntent().getSerializableExtra("Editer");
        if (product!=null){
            myButton.setText("Editer");
            designationEditText.setText(product.name);
            descriptionEditText.setText(product.description);
            priceEditText.setText(String.valueOf(product.price));
            quantityInStockEditText.setText(String.valueOf(product.quantityInStock));
            alertQuantityEditText.setText(String.valueOf(product.alertQuantity));
            myButton.setOnClickListener(v -> saveProduct(EDIT_PRODUCT,product.id));

        }
        else {
            myButton.setOnClickListener(v -> saveProduct(INSERT_PRODUCT,null));
        }
    }
}