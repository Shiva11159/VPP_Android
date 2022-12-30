package com.application.vpp.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.vpp.Datasets.BrokerageListData;
import com.application.vpp.R;

import java.util.ArrayList;

/**
 * Created by bpandey on 15-06-2018.
 */

public class BrokerageListAdapter extends RecyclerView.Adapter<BrokerageListAdapter.ViewHolder> {

    ArrayList<BrokerageListData>brokerageListDataArrayList;
    Context context;

    public BrokerageListAdapter( ArrayList<BrokerageListData>brokerageListDataArrayList,Context context){
        this.brokerageListDataArrayList = brokerageListDataArrayList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_brokerage_list,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtmonthlyProspectName.setText(brokerageListDataArrayList.get(position).MonthName);
        holder.txtmonthlyProspectName.setText("₹ "+brokerageListDataArrayList.get(position).MonthlyBrokerage);

    }

    @Override
    public int getItemCount() {
       return brokerageListDataArrayList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtmonthlyProspectName,txtbrokerageProductName;
        public ViewHolder(View itemView) {
            super(itemView);

            txtmonthlyProspectName = (TextView)itemView.findViewById(R.id.txtmonthlyProspectName);
            txtbrokerageProductName = (TextView)itemView.findViewById(R.id.txtbrokerageProductName);

        }
    }
}
