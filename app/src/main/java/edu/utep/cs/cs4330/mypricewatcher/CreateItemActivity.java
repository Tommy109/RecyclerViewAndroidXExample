package edu.utep.cs.cs4330.mypricewatcher;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CreateItemActivity extends AppCompatActivity {

    private EditText urlView;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_create);

        setTitle("Create Item");

        urlView = findViewById(R.id.url_view);
        createButton = findViewById(R.id.create_button);

        String action = getIntent().getAction();
        String type = getIntent().getType();

        String url;

        //If another app is sharing text, use that as the url for item...
        if(Intent.ACTION_SEND.equalsIgnoreCase(action) &&
                type != null && ("text/plain".equals(type))){
            url = getIntent().getStringExtra(Intent.EXTRA_TEXT);

            urlView.setText(url);
        }
        else{
            url = urlView.getText().toString();
        }



        createButton.setOnClickListener(v ->{
            Intent resultIntent;
            Intent intent = getIntent();

            /** If the this activity was started via sharing a URL, send url
             * to main activity.
             */
            if(intent.getAction() == Intent.ACTION_SEND){
                resultIntent = new Intent(this,MainActivity.class);
                resultIntent.putExtra("url",url);

                startActivity(resultIntent);
            }
            else {
                resultIntent = new Intent();
                resultIntent.putExtra("url", urlView.getText().toString());
                /** CHECK IF URL IS MALFORMED ON MAIN ACTIVITY **/

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });


    }
}
