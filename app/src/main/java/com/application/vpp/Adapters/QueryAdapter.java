package com.application.vpp.Adapters;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.application.vpp.Datasets.QueryStatusData;
import com.application.vpp.Interfaces.OnclickQuery;
import com.application.vpp.R;
import com.application.vpp.Views.Views;

import java.util.ArrayList;
import java.util.List;

public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.ViewHolder> {

    List<QueryStatusData> itemList1;
    private Context context;
    boolean dropdown = false;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    OnclickQuery onclickQuery;

    public QueryAdapter(List<QueryStatusData> itemList, Context context,OnclickQuery onclickQuery) {

        this.itemList1 = itemList;
        this.onclickQuery = onclickQuery;
        this.context = context;
        for (int i = 0; i < itemList.size(); i++) {
            expandState.append(i, false);
        }
        this.itemList1 = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public QueryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_query_status3, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QueryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        final boolean isExpanded = expandState.get(holder.getAdapterPosition());
        holder.booleantxt.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.txt_token.setText(itemList1.get(position).getSr_no());

        if (itemList1.get(position).getStatus().trim().toUpperCase().equals("OPEN")) {
            holder.txtstatusopen.setVisibility(View.VISIBLE);
            holder.txtstatusclose.setVisibility(View.GONE);
        } else {
            holder.txtstatusopen.setVisibility(View.GONE);
            holder.txtstatusclose.setVisibility(View.VISIBLE);
        }

        holder.txtDate.setText(itemList1.get(position).getDate());
//        holder.txtQuery.setText(itemList1.get(position).getQuery());



        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onclickQuery.OnclickQuery(itemList1.get(position).getQuery(),itemList1.get(position).getRemark());

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtstatusopen, txtstatusclose, txtDate, txt_token;
        CardView card_view;
        ImageButton imageButtonDropdown;
        TextView booleantxt;
        TextView txtseehide;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageButtonDropdown = itemView.findViewById(R.id.imageButtonDropdown);
            txt_token = itemView.findViewById(R.id.txttoken);
            txtstatusopen = itemView.findViewById(R.id.txtstatusopen);
            txtstatusclose = itemView.findViewById(R.id.txtstatusclose);
            txtDate = itemView.findViewById(R.id.txtDate);
            booleantxt = itemView.findViewById(R.id.booleantxt);
            txtseehide = itemView.findViewById(R.id.txtseehide);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }



    public void ANim(TextView expandableLayout) {
        TranslateAnimation animation = new TranslateAnimation(100.0f, 0.0f, 100.0f, 0.0f);
        animation.setDuration(1000);  // animation duration
        animation.setRepeatCount(0);  // animation repeat count
//                    animation.setRepeatMode(1);   // repeat animation (left to right, right to left )
        expandableLayout.startAnimation(animation);
    }

}
