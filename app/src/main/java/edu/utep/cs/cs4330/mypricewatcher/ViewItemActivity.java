package edu.utep.cs.cs4330.mypricewatcher;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class ViewItemActivity extends AppCompatActivity {
    private TextView nameView,priceView,urlView,currentPriceView,percentView;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_info);

        setTitle("Item Info");

        nameView = findViewById(R.id.name_view);
        priceView = findViewById(R.id.price_view);
        urlView = findViewById(R.id.url_view);
        currentPriceView = findViewById(R.id.currentPrice);
        percentView = findViewById(R.id.percentChange);
        imageView = findViewById(R.id.image_view);

        Intent intent = getIntent();
        String name,url,initPrice,percent,currPrice;

        name = intent.getStringExtra("name");
        url = intent.getStringExtra("url");
        currPrice = intent.getStringExtra("price");
        Bitmap image = intent.getParcelableExtra("bitmap");

        nameView.setText(name);
        urlView.setText(url);
        currentPriceView.setText(currPrice);
        imageView.setImageBitmap(image);


    }
}
