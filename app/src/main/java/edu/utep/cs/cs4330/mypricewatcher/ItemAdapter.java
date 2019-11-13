package edu.utep.cs.cs4330.mypricewatcher;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    public interface OnItemClickListener{
        void onItemClicked(View view, int position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder
    implements View.OnCreateContextMenuListener{

        static final int REMOVE = 812;
        static final int EDIT = 813;
        static final int VIEW = 900;
        static final int UPDATE = 901;

        private TextView nameView;
        private TextView priceView;
        private ImageView imageView;

        public ItemViewHolder(View itemView){
            super(itemView);

            nameView = itemView.findViewById(R.id.name_view);
            priceView = itemView.findViewById(R.id.price_view);


            imageView = itemView.findViewById(R.id.image_view);

            /** Longer than a tap click **/
            itemView.setOnCreateContextMenuListener(this);
        }



        /** Display Context menu for item selected **/
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            int position = getAdapterPosition();

            contextMenu.add(position, EDIT,0, "Edit");
            contextMenu.add(position, REMOVE, 1, "Remove");
            contextMenu.add(position, UPDATE,2,"Update");
        }



    }

    public List<Item> itemList;
    private OnItemClickListener listener;



    public ItemAdapter(List<Item> itemList, OnItemClickListener listener){
        this.listener = listener;
        this.itemList = itemList;
    }

    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(itemView);
        itemView.setOnClickListener(v ->{
            listener.onItemClicked(v, itemViewHolder.getAdapterPosition()); });

        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            ItemViewHolder viewHolder = holder;

            Item item = itemList.get(position);

            viewHolder.nameView.setText(item.getName());
            viewHolder.priceView.setText(Double.toString(item.getPrice()));


            if(item.getImage() != null )
                viewHolder.imageView.setImageBitmap(item.getImage());
            else
                viewHolder.imageView.setImageResource(R.drawable.no_image_available);

            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
