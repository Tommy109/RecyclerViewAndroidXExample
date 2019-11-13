package edu.utep.cs.cs4330.mypricewatcher;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//@Entity(tableName = "item_table")
public class Item {

    //@ColumnInfo(name = "name")
    private String name;

    //@ColumnInfo(name = "price")
    private Double price;

    //@PrimaryKey
    //@NonNull
    //@ColumnInfo(name = "url")
    private String url;


    private Bitmap image;

    public String getName(){return name;}
    public Double getPrice(){return price;}
    public String getUrl(){return url;}
    public Bitmap getImage(){return image;}

    public void setName(String name){this.name = name;}

    public void setImage(Bitmap image){
        this.image = image;
    }


    public Item(String name, Double price, String url){
        this.name = name;
        this.price = price;
        this.url = url;
    }


    public Item(){
        this(null,100.0,"boo");
    }
    /**
     * For testing purposes
     * @return
     */
    public final static List<Item> generateItems(){

        ArrayList<Item> list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {

            list.add(new Item("Item " + i,
                    new Random().nextDouble() * 100,
                    "https://www.google.com"));
        }

        return list;
    }
}
