package com.application.vpp.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
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
import com.application.vpp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by bpandey on 28-06-2018.
 */

public class InProcessAdapter extends RecyclerView.Adapter<InProcessAdapter.ViewHolder>  {

    ArrayList<InProcessDataset> inProcessDatasetArrayList;
    Context context;
    ArrayList<InProcessDataset> detailsFiltered;
    String s;
    View view;

    public InProcessAdapter(ArrayList<InProcessDataset> inProcessDatasetArrayList, Context context, String s) {

        this.context = context;
        this.inProcessDatasetArrayList = inProcessDatasetArrayList;
        detailsFiltered = new ArrayList<>(inProcessDatasetArrayList);
        this.s = s;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (s.equalsIgnoreCase("rejected")) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_brokerage_list1, null);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_brokerage_list, null);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Log.e("kk", inProcessDatasetArrayList.get(position).ProspectName);

        if (!s.equalsIgnoreCase("")) {  //rejected
            holder.txtbrokerageProductName.setVisibility(View.VISIBLE);
            holder.txtbrokerageProductName.setText(inProcessDatasetArrayList.get(position).ProductName);

            StringTokenizer stringTokenizer=new StringTokenizer(inProcessDatasetArrayList.get(position).LeadDate);
            String one=stringTokenizer.nextToken();
            String two=stringTokenizer.nextToken();
            String three=stringTokenizer.nextToken();
            String four=stringTokenizer.nextToken();
            holder.txtdate.setText(one);
            holder.txtmon_yr.setText(two +" "+ three);
            holder.txttime.setText(four);

        } else {
            holder.txtbrokerageProductName.setVisibility(View.GONE);
        }
        holder.txtmonthlyProspectName.setText(inProcessDatasetArrayList.get(position).ProspectName);
        holder.txtbrokerageDate.setText(inProcessDatasetArrayList.get(position).LeadDate);

    }

    @Override
    public int getItemCount() {
        return inProcessDatasetArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtmonthlyProspectName, txtbrokerageDate, txtbrokerageProductName,txtdate,txtmon_yr,txttime;

        public ViewHolder(View itemView) {
            super(itemView);

            txtbrokerageProductName = (TextView) itemView.findViewById(R.id.txtbrokerageProductName);
            txtmonthlyProspectName = (TextView) itemView.findViewById(R.id.txtmonthlyProspectName);
            txtbrokerageDate = (TextView) itemView.findViewById(R.id.txtbrokerageDate);
            txtdate = (TextView) itemView.findViewById(R.id.date);
            txtmon_yr = (TextView) itemView.findViewById(R.id.mon_yr);
            txttime = (TextView) itemView.findViewById(R.id.time);

        }
    }

//    private Filter dataFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<InProcessDataset> filterlist = new ArrayList<>();
//            if (constraint == null || constraint.length() == 0) {
//                filterlist.addAll(detailsFiltered);
//
//
//            } else {
//                String fillpatern = constraint.toString().toLowerCase().trim();
//                for (InProcessDataset item : detailsFiltered) {
//                    if (item.ProspectName.toLowerCase().contains(fillpatern)) {
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
//            inProcessDatasetArrayList.clear();
//            inProcessDatasetArrayList.addAll((List) filterResults.values);
//            notifyDataSetChanged();
//        }
//    };
public void filterList(ArrayList<InProcessDataset> filteredList) {
    inProcessDatasetArrayList = filteredList;
    notifyDataSetChanged();
}

}
