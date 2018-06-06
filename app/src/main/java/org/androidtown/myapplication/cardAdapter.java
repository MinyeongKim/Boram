package org.androidtown.myapplication;

/**
 * Created by sj971 on 2018-06-05.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class cardAdapter extends RecyclerView.Adapter<cardAdapter.ViewHolder> {

    Context context;
    List<item> items;
    int item_layout;

    public cardAdapter(Context context, List<item> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, null);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final item item = items.get(position);
        final int POSITION = position;
        Drawable drawable = ContextCompat.getDrawable(context, item.getPhoto());
        holder.image.setBackground(drawable);
        holder.title.setText(item.getName());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, item.getName(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, HistoryActivity.class);

<<<<<<< HEAD
                //String type = item.getWithwho();

                //i.putExtra("Check_type", type);
=======
                String type = item.getWithwho();
                String UserId = item.getUserID();

                Bundle bundle = new Bundle();
                bundle.putInt("INDEX", POSITION);
                bundle.putString("Check_type", type);
                bundle.putString("ID", UserId);

                i.putExtras(bundle);
>>>>>>> 9769a3b367235c76346eb58b5eae4005c888e5a6
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}