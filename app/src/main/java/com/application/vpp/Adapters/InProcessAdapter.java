package com.application.vpp.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
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
import com.application.vpp.Interfaces.CallBack;
import com.application.vpp.R;
import com.application.vpp.Utility.AlertDialogClass;

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

    CallBack callBack;
    public InProcessAdapter(ArrayList<InProcessDataset> inProcessDatasetArrayList, Context context, String s,CallBack callback) {

        this.callBack=callback;
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
//            holder.txtbrokerageProductName.setVisibility(View.VISIBLE);
            holder.txtProduct.setText(inProcessDatasetArrayList.get(position).ProductName);
            holder.txtMobileNo.setText(inProcessDatasetArrayList.get(position).MobileNo);


//            holder.txtdate.setText(one);
//            holder.txtmon_yr.setText(two +" "+ three);
//            holder.txttime.setText(four);

        } else {
            holder.txtProduct.setText("-");
        }


        holder.txtMobileNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+inProcessDatasetArrayList.get(position).getMobileNo().trim()));
//                startActivity(intent);
                context.startActivity(intent);
            }
        });

        StringTokenizer stringTokenizer=new StringTokenizer(inProcessDatasetArrayList.get(position).LeadDate);
        String one=stringTokenizer.nextToken();
        String two=stringTokenizer.nextToken();
        String three=stringTokenizer.nextToken();
        String four=stringTokenizer.nextToken();
        holder.txtName.setText(inProcessDatasetArrayList.get(position).ProspectName);
        holder.txtDate.setText(one +" "+two+" "+three);


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // callBack.getReason(inProcessDatasetArrayList.get(holder.getAdapterPosition()).getReason(), inProcessDatasetArrayList.get(holder.getAdapterPosition()).getProspectName(),"",inProcessDatasetArrayList.get(holder.getAdapterPosition()).getRejectionDate());
            }
        });

        AlertDialogClass.PopupWindowDismiss();

    }

    @Override
    public int getItemCount() {
        return inProcessDatasetArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView txtmonthlyProspectName, txtbrokerageDate, txtbrokerageProductName,txtdate,txtmon_yr,txttime;
        TextView txtDate, txtName, txtMobileNo,txtProduct;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtMobileNo = (TextView) itemView.findViewById(R.id.txtMobileNo);
            txtProduct = (TextView) itemView.findViewById(R.id.txtProduct);

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
