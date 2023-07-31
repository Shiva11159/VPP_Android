package com.application.vpp.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.vpp.Datasets.ClientlistData;
import com.application.vpp.Datasets.InProcessDataset;
import com.application.vpp.Datasets.NotInterestedData;
import com.application.vpp.Interfaces.CallBack;
import com.application.vpp.R;
import com.application.vpp.Utility.AlertDialogClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by bpandey on 28-06-2018.
 */

public class NotInterestedAdapter extends RecyclerView.Adapter<NotInterestedAdapter.ViewHolder> {

    ArrayList<NotInterestedData> clientlistDataArrayList;
    Context context;
    AlertDialog alertDialog;
    CallBack callBack;

    ArrayList<NotInterestedData> detailsFiltered;

    public NotInterestedAdapter(ArrayList<NotInterestedData> clientlistDataArrayList, Context context, CallBack callBack) {
        this.clientlistDataArrayList = clientlistDataArrayList;
        this.context = context;
        this.callBack = callBack;
        detailsFiltered = new ArrayList<>(clientlistDataArrayList);
    }

    @NonNull
    @Override
    public NotInterestedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_not_interested1, null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NotInterestedAdapter.ViewHolder holder, final int i) {

//        holder.txtLeadNumber.setText(clientlistDataArrayList.get(holder.getAdapterPosition()).getLeadNo());

        String name = capitizeString(clientlistDataArrayList.get(holder.getAdapterPosition()).getCustomerName()).toLowerCase(Locale.ROOT);
        holder.txtName.setText(clientlistDataArrayList.get(i).getCustomerName());
        holder.txtDate.setText(clientlistDataArrayList.get(i).getLeadDate());
        holder.txtProduct.setText(clientlistDataArrayList.get(i).getProductName());
        holder.txtMobileNo.setText(clientlistDataArrayList.get(i).getMobileNo());


        holder.txtMobileNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+clientlistDataArrayList.get(i).getMobileNo().trim()));
//                startActivity(intent);
                context.startActivity(intent);
            }
        });




//        holder.txtleadDate.setText(clientlistDataArrayList.get(position).LeadDate);

//        Log.e("onBindViewHolder: ", clientlistDataArrayList.get(holder.getAdapterPosition()).getLeadDate());

//        StringTokenizer stringTokenizer = new StringTokenizer(clientlistDataArrayList.get(holder.getAdapterPosition()).getLeadDate(), " ");
//        holder.date.setText(stringTokenizer.nextToken());
//        holder.mon_yr.setText(stringTokenizer.nextToken() + " " + stringTokenizer.nextToken());
//        holder.time.setText(stringTokenizer.nextToken());
//        //   holder.notInterestedReason.setText(clientlistDataArrayList.get(position).notInterestedreason);


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.getReason(clientlistDataArrayList.get(holder.getAdapterPosition()).getNotInterestedreason(), clientlistDataArrayList.get(holder.getAdapterPosition()).getCustomerName(), clientlistDataArrayList.get(holder.getAdapterPosition()).getLeadNo(),clientlistDataArrayList.get(holder.getAdapterPosition()).getLeadUpdateDate());
            }
        });

        AlertDialogClass.PopupWindowDismiss();

    }

    @Override
    public int getItemCount() {
        return clientlistDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtName, txtMobileNo, txtProduct;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            txtMobileNo = (TextView) itemView.findViewById(R.id.txtMobileNo);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtProduct = (TextView) itemView.findViewById(R.id.txtProduct);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
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
    public void filterList(ArrayList<NotInterestedData> filteredList) {
        clientlistDataArrayList = filteredList;
        notifyDataSetChanged();
    }

    private String capitizeString(String name) {
        String captilizedString = "";
        if (!name.trim().equals("")) {
            captilizedString = name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return captilizedString;
    }

}
