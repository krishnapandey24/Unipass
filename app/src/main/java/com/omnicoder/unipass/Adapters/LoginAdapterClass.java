package com.omnicoder.unipass.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.omnicoder.unipass.Activities.ViewLoginActivity;
import com.omnicoder.unipass.Database.DBHandler;
import com.omnicoder.unipass.Database.ModelClass;
import com.omnicoder.unipass.R;

import java.util.ArrayList;
import java.util.List;


public class LoginAdapterClass extends RecyclerView.Adapter<LoginAdapterClass.MyViewHolder> implements Filterable{
    List<ModelClass> dataHolder;
    List<ModelClass> dataHolderFilter;
    Context context;
    public LoginAdapterClass(Context context, ArrayList<ModelClass> dataHolder){
        this.dataHolder= dataHolder;
        dataHolderFilter= new ArrayList<>(dataHolder);
        this.context= context;
    }

    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.login_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String title= dataHolder.get(position).getpTitle();
        String userName = dataHolder.get(position).getUsername();
        holder.titleView.setText(title);
        holder.usernameView.setText(userName);
        int imageResource= new DBHandler(context).getImageResource(title);
        if(imageResource==R.drawable.ic_otherss___copy){
            ConstraintLayout.LayoutParams layoutParams= new ConstraintLayout.LayoutParams(pxFromDp(context,60),pxFromDp(context,60));
            holder.imageView.setLayoutParams(layoutParams);

        }
        holder.imageView.setImageResource(imageResource);


    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {



            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results= new FilterResults();
                List<ModelClass> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(dataHolderFilter);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    Log.d("Search Text","Constraint:"+filterPattern);
                    for (ModelClass modelClass: dataHolderFilter) {
                        if (modelClass.getUsername().toLowerCase().contains(filterPattern) || modelClass.getpTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(modelClass);
                        }
                    }
                }


                results.count= filteredList.size();
                results.values = filteredList;
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataHolder.clear();
                dataHolder.addAll((List) results.values);
                notifyDataSetChanged();
            }


        };
        return filter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView titleView,usernameView;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.logoImageView);
            titleView= itemView.findViewById(R.id.titleView);
            usernameView = itemView.findViewById(R.id.categoryView);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            ModelClass modelClass = dataHolder.get(position);
            int itemID = modelClass.getpID();
            Intent ViewIntent = new Intent(context, ViewLoginActivity.class);
            ViewIntent.putExtra("itemID", itemID);
            context.startActivity(ViewIntent);


        }



    }

    public static int pxFromDp(final Context context, final float dp) {
        return (int)(dp * context.getResources().getDisplayMetrics().density);
    }




}
