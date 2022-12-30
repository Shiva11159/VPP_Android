package com.application.vpp.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.application.vpp.Datasets.ClientlistData;
import com.application.vpp.Datasets.LeadDetailReportDataset;
import com.application.vpp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bpandey on 14-06-2018.
 */

public class MyLeadsdapter extends RecyclerView.Adapter<MyLeadsdapter.ViewHolder>  {
    ArrayList<LeadDetailReportDataset> leadListDatasetArrayList;
    Context context;
    ArrayList<LeadDetailReportDataset> detailsFiltered;
    public MyLeadsdapter(ArrayList<LeadDetailReportDataset> leadListDatasetArrayList, Context context){

        this.leadListDatasetArrayList = leadListDatasetArrayList;
        this.context = context;
        detailsFiltered = new ArrayList<>(leadListDatasetArrayList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_lead_list1,null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.txtName.invalidate();
        holder.txtLeadNumber.setText(String.valueOf(leadListDatasetArrayList.get(position).LeadNo));
        holder.txtName.setText(leadListDatasetArrayList.get(position).CustomerName);
        holder.txtProduct.setText(leadListDatasetArrayList.get(position).ProductName);
        holder.txtStatus.setText(leadListDatasetArrayList.get(position).Status);

        holder.txtStatus.invalidate();

    }

    @Override
    public int getItemCount() {
        return leadListDatasetArrayList.size();
    }

//    @Override
//    public Filter getFilter() {
//        return dataFilter;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtLeadNumber,txtProduct,txtName,txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            txtLeadNumber = (TextView)itemView.findViewById(R.id.txtLeadNumber);
            txtName = (TextView)itemView.findViewById(R.id.txtName);
            txtProduct = (TextView)itemView.findViewById(R.id.txtProduct);
            txtStatus = (TextView)itemView.findViewById(R.id.txtStatus);
        }
    }

//    private Filter dataFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<LeadDetailReportDataset> filterlist = new ArrayList<>();
//            if (constraint == null || constraint.length() == 0) {
//                filterlist.addAll(detailsFiltered);
//
//
//            } else {
//                String fillpatern = constraint.toString().toLowerCase().trim();
//                for (LeadDetailReportDataset item : detailsFiltered) {
//                    if (item.CustomerName.toLowerCase().contains(fillpatern) || item.LeadNo.toLowerCase().contains(fillpatern) ) {
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
//            leadListDatasetArrayList.clear();
//            leadListDatasetArrayList.addAll((List) filterResults.values);
//            notifyDataSetChanged();
//        }
//    };

    public void filterList(ArrayList<LeadDetailReportDataset> filteredList) {
        leadListDatasetArrayList = filteredList;
        notifyDataSetChanged();
    }

}
