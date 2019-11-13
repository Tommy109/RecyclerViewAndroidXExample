package edu.utep.cs.cs4330.mypricewatcher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class BarnesAndNoblesParser extends AsyncTask<String, Void, Item> {

    final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";

    private ItemFetching itemFetching;


    public BarnesAndNoblesParser(ItemFetching itemFetching) {
        this.itemFetching = itemFetching;
    }

    @Override
    protected Item doInBackground(String... param) {

        Item item = null;

        try {

            String url = param[0];
            final Document doc = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .get();


            String name = doc.select("h1.pdp-header-title").text();
            String price = doc.select("span#pdp-cur-price").text();
            String imageUrl = doc.select("img#pdpMainImage").attr("src");
            imageUrl = "http:"+imageUrl;
            price = price.substring(1);

            item = new Item(name, Double.parseDouble(price), url);


            InputStream is = new URL(imageUrl).openStream();
            Bitmap logo = BitmapFactory.decodeStream(is);
            item.setImage(logo);


        } catch (IOException e) {
            e.printStackTrace();
        }catch(IllegalArgumentException e){
            return null;
        }

        return item;
    }

    @Override
    protected void onPostExecute(Item item) {
        super.onPostExecute(item);
        if (itemFetching != null) {
            itemFetching.onDataFetched(item);
        }
    }
}