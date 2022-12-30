package com.application.vpp.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.application.vpp.Datasets.ClientlistData;
import com.application.vpp.Datasets.InProcessDataset;
import com.application.vpp.Interfaces.CallBack;
import com.application.vpp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by bpandey on 28-06-2018.
 */

public class NotInterestedAdapter extends RecyclerView.Adapter<NotInterestedAdapter.ViewHolder>{

    ArrayList<ClientlistData>clientlistDataArrayList;
    Context context;
    AlertDialog alertDialog;
    CallBack callBack;

    ArrayList<ClientlistData> detailsFiltered;
    public NotInterestedAdapter(ArrayList<ClientlistData>clientlistDataArrayList, Context context,CallBack callBack){
        this.clientlistDataArrayList = clientlistDataArrayList;
        this.context = context;
        this.callBack = callBack;
        detailsFiltered = new ArrayList<>(clientlistDataArrayList);
    }
    @NonNull
    @Override
    public NotInterestedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_not_interested1,null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NotInterestedAdapter.ViewHolder holder, final int i) {

        holder.txtLeadNumber.setText(clientlistDataArrayList.get(holder.getAdapterPosition()).LeadNo);
        holder.txtLeadName.setText(clientlistDataArrayList.get(holder.getAdapterPosition()).CustomerName);
//        holder.txtleadDate.setText(clientlistDataArrayList.get(position).LeadDate);

        Log.e( "onBindViewHolder: ", clientlistDataArrayList.get(holder.getAdapterPosition()).LeadDate);

        StringTokenizer stringTokenizer = new StringTokenizer(clientlistDataArrayList.get(holder.getAdapterPosition()).LeadDate, " ");
        holder.date.setText(stringTokenizer.nextToken());
        holder.mon_yr.setText(stringTokenizer.nextToken() + " " + stringTokenizer.nextToken());
        holder.time.setText(stringTokenizer.nextToken());
     //   holder.notInterestedReason.setText(clientlistDataArrayList.get(position).notInterestedreason);
        holder.notInterestedReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.getReason(clientlistDataArrayList.get(holder.getAdapterPosition()).notInterestedreason,clientlistDataArrayList.get(holder.getAdapterPosition()).CustomerName,clientlistDataArrayList.get(holder.getAdapterPosition()).LeadNo);
              //  PopupMenu popup = new PopupMenu(holder.notInterestedReason.getContext(), itemView);
//                AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());
//                LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());
//                final View dialogView = inflater.inflate(R.layout.reason_popup, null);
//                builder.setView(dialogView);
//                TextView txtreason=dialogView.findViewById(R.id.txt_reason);
//                txtreason.setText(clientlistDataArrayList.get(position).notInterestedreason);
//                FancyButton btn_positive = dialogView.findViewById(R.id.btnIsRegYes);
//                btn_positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//
//
//
//                    }
//                });
//                builder.setCancelable(true);
//
//
//                alertDialog = builder.show();
//
//
//                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
}
       });

    }

    @Override
    public int getItemCount() {
        return clientlistDataArrayList.size();
    }

//    @Override
//    public Filter getFilter() {
//        return dataFilter;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtLeadNumber,txtLeadName,txtleadDate,notInterestedReason,date, mon_yr, time;;
        public ViewHolder(View itemView) {
            super(itemView);

            txtLeadNumber = (TextView)itemView.findViewById(R.id.txtLeadNumber);
            txtLeadName = (TextView)itemView.findViewById(R.id.txtLeadName);
//            txtleadDate = (TextView)itemView.findViewById(R.id.txtleadDate);
            notInterestedReason = (TextView)itemView.findViewById(R.id.reason);
            date = (TextView) itemView.findViewById(R.id.date);
            mon_yr = (TextView) itemView.findViewById(R.id.mon_yr);
            time = (TextView) itemView.findViewById(R.id.time);
        }

    }

//    private Filter dataFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<ClientlistData> filterlist = new ArrayList<>();
//            if (constraint == null || constraint.length() == 0) {
//                filterlist.addAll(detailsFiltered);
//
//
//            } else {
//                String fillpatern = constraint.toString().toLowerCase().trim();
//                for (ClientlistData item : detailsFiltered) {
//                    if (item.CustomerName.toLowerCase().contains(fillpatern) || item.LeadNo.toLowerCase().contains(fillpatern)) {
//                        filterlist.add(item);
//                    }
//                }
//            }
//
//
//            FilterResults result = new FilterResults();
//            result.values = filterlist;
//
//
//            return result;
//        }
//
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//            clientlistDataArrayList.clear();
//            clientlistDataArrayList.addAll((List) filterResults.values);
//            notifyDataSetChanged();
//        }
//    };
    public void filterList(ArrayList<ClientlistData> filteredList) {
        clientlistDataArrayList = filteredList;
        notifyDataSetChanged();
    }

}
