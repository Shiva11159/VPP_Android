package com.application.vpp.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.vpp.Datasets.PayoutListData;
import com.application.vpp.R;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by bpandey on 25-09-2018.
 */

public class PayoutList extends RecyclerView.Adapter<PayoutList.ViewHolder> {
    Context context;
    ArrayList<PayoutListData> payoutListDataArrayList;

    public PayoutList(Context context, ArrayList<PayoutListData> payoutListDataArrayList) {

        this.context = context;
        this.payoutListDataArrayList = payoutListDataArrayList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_payout1, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtMonth.setText(payoutListDataArrayList.get(position).month);
        holder.txtPayout.setText(payoutListDataArrayList.get(position).payout);
        holder.txtProduct.setText(payoutListDataArrayList.get(position).product);

        StringTokenizer stringTokenizer = new StringTokenizer(payoutListDataArrayList.get(position).month, " ");

        holder.txtmonth.setText(stringTokenizer.nextToken());
        holder.txtyear.setText(stringTokenizer.nextToken());

    }

    @Override
    public int getItemCount() {
        return payoutListDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMonth, txtPayout, txtBrokerage, txtProduct, txtmonth, txtyear;

        public ViewHolder(View itemView) {
            super(itemView);

            txtyear = (TextView) itemView.findViewById(R.id.year);
            txtmonth = (TextView) itemView.findViewById(R.id.month);
            txtMonth = (TextView) itemView.findViewById(R.id.txtPayoutMonth);
            txtPayout = (TextView) itemView.findViewById(R.id.txtPayoutAmount);
            txtProduct = (TextView) itemView.findViewById(R.id.txtPayoutProduct);


        }
    }
}
