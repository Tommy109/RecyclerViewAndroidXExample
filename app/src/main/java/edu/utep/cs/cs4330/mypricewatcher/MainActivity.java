/**
 * Tomas Chagoya
 */

package edu.utep.cs.cs4330.mypricewatcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static edu.utep.cs.cs4330.mypricewatcher.ItemAdapter.ItemViewHolder.REMOVE;
import static edu.utep.cs.cs4330.mypricewatcher.ItemAdapter.ItemViewHolder.UPDATE;

public class MainActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener, ItemFetching {

    static final int NEW_ENTRY = 1;
    static final int EDIT_ENTRY = 2;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter itemAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton fab;
    private Button saveButton;

    ArrayList<Item> itemList = new ArrayList<>();//Item.generateItems();

    @Override
    public void onItemClicked(View view, int position) {
        Intent intent = new Intent(MainActivity.this, ViewItemActivity.class);

        Item item = itemList.get(position);

        intent.putExtra("name", item.getName());
        intent.putExtra("url", item.getUrl());
        intent.putExtra("price", item.getPrice().toString());
        startActivity(intent);
    }

    @Override
    public void onDataFetched(Item item) {
        if(item == null){
            makeToast("Malformed or invalid URL");
        }
        else {
            updateAdapater(item);
            displayMessage("Item added");
        }
    }

    private void makeToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** finish later **/
        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(l ->{
            displayMessage("Saved");
        });

        createRecycleView();
        createFloatingActionButton();
        setUpAdapterListener();


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String url = extras.getString("url");

            /**
             * 1) Create base class for all Parsers later
             * 2) Create a parser selector that picks the correct asynctask parser to use
             * (Change in future)
             */

            if(url.contains("zumiez"))
                new ZumiezParser(this).execute(url);
            else if(url.contains("barnesandnoble"))
                new BarnesAndNoblesParser(this).execute(url);
        }

        /** tester **/
        String url = "https://www.barnesandnoble.com/w/alchemist-paulo-coelho/1100248293?ean=9780062315007#/";
        new BarnesAndNoblesParser(this).execute(url);


    }





    private void createFloatingActionButton() {
        fab = findViewById(R.id.floating_action_button);

        fab.setOnClickListener((View view) -> {
            Intent intent = new Intent(MainActivity.this, CreateItemActivity.class);
            startActivityForResult(intent, NEW_ENTRY);
        });
    }

    private void createRecycleView() {
        recyclerView = findViewById(R.id.recycler_view);


        /**
         * Hide the floating action button when scrolling down to avoid obstructing RecyclerView
         * items. When scrolling up button reappears.
         */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });


        //recycler view layout does not change; this is used to improve performance
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void setUpAdapterListener() {
        ItemAdapter.OnItemClickListener listener = (view, position) -> {

            Intent intent = new Intent(MainActivity.this, ViewItemActivity.class);

            Item item = itemList.get(position);
            intent.putExtra("name", item.getName());
            intent.putExtra("url", item.getUrl());
            intent.putExtra("price", Double.toString(item.getPrice()));
            intent.putExtra("bitmap",item.getImage());

            startActivity(intent);
        };

        itemAdapter = new ItemAdapter(itemList, listener);
        recyclerView.setAdapter(itemAdapter);
        registerForContextMenu(recyclerView);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int position = item.getGroupId();

        switch (item.getItemId()) {
            case REMOVE:
                removeItem(position);
                displayMessage("Item removed");
                return true;
            case UPDATE:
                displayMessage("Item Updated");
                return true;
        }

        return super.onContextItemSelected(item);
    }

    private void displayMessage(String message) {
        Snackbar.make(findViewById(R.id.root_view), message, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Remove an item from recycler view
     * Removes from model list and notifies the adapter so
     * it can update the views.
     *
     * @param position
     */
    public void removeItem(int position) {
        itemList.remove(position);
        itemAdapter.notifyItemRemoved(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_ENTRY) {
                String url = data.getStringExtra("url");

                try {
                    new BarnesAndNoblesParser(this).execute(url);
                }catch(IllegalArgumentException e){
                    e.printStackTrace();
                }


            }

            if (requestCode == EDIT_ENTRY) {

            }
        }
    }

    public void updateAdapater(Item item) {
        itemList.add(0, item);
        itemAdapter.notifyItemInserted(0);
    }




}
