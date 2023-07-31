package com.application.vpp.Adapters;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import com.application.vpp.Datasets.ClientlistData;
import com.application.vpp.Datasets.LeadDetailReportDataset;
import com.application.vpp.R;
import com.application.vpp.Utility.AlertDialogClass;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_lead_list3,null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.txtName.invalidate();
//        holder.txtLeadNumber.setText(String.valueOf(leadListDatasetArrayList.get(position).LeadNo));
        String name = capitizeString(leadListDatasetArrayList.get(position).CustomerName).toLowerCase(Locale.ROOT);
//        Log.e("name", leadListDatasetArrayList.get(position).CustomerName);
        holder.txtName.setText(name);
//        holder.txtProduct.setText(leadListDatasetArrayList.get(position).ProductName);

        if (leadListDatasetArrayList.get(position).Status.toUpperCase().contains("CALL BACK")){
            holder.txtStatus.setTextColor(context.getResources().getColor(R.color.colorAccent));

        }else if (leadListDatasetArrayList.get(position).Status.toUpperCase().contains("NEW LEAD")){
            holder.txtStatus.setTextColor(context.getResources().getColor(R.color.black));

        }else if (leadListDatasetArrayList.get(position).Status.toUpperCase().contains("DEAD LEAD")){
            holder.txtStatus.setTextColor(context.getResources().getColor(R.color.red));

        }else {
            holder.txtStatus.setTextColor(context.getResources().getColor(R.color.btn_active));

        }
        holder.txtStatus.setText(leadListDatasetArrayList.get(position).Status);
        holder.txtMobileNo.setText(leadListDatasetArrayList.get(position).MobileNo);



        holder.txtMobileNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+leadListDatasetArrayList.get(position).MobileNo.trim()));
//                startActivity(intent);
                context.startActivity(intent);
            }
        });




//        holder.txtMobileNo.setTypeface(EasyFonts.caviarDreamsBold(context)); //androidNation
//        holder.txtStatus.setTypeface(EasyFonts.caviarDreamsBold(context));
//        holder.txtName.setTypeface(EasyFonts.caviarDreamsBold(context));
//        holder.txtDate.setTypeface(EasyFonts.caviarDreamsBold(context));

//        holder.txtDate.setText(leadListDatasetArrayList.get(position).LeadDate);

        // created date , mobile no , search by mobile and name ..

        StringTokenizer stringTokenizer = new StringTokenizer(leadListDatasetArrayList.get(holder.getAdapterPosition()).getLeadDate(), " ");
        String date = stringTokenizer.nextToken();
        String month =stringTokenizer.nextToken();
        String yr =stringTokenizer.nextToken();
        String time =stringTokenizer.nextToken();
        holder.txtDate.setText(date+" "+month+" "+yr);
        holder.txtStatus.invalidate();

        AlertDialogClass.PopupWindowDismiss();

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
//        TextView txtLeadNumber,txtProduct,txtMobileNo,txtName,txtStatus,date,mon_yr,time;
        TextView txtDate,txtMobileNo,txtName,txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);

//            txtLeadNumber = (TextView)itemView.findViewById(R.id.txtLeadNumber);
            txtName = (TextView)itemView.findViewById(R.id.txtName);
//            txtProduct = (TextView)itemView.findViewById(R.id.txtProduct);
            txtMobileNo = (TextView)itemView.findViewById(R.id.txtMobileNo);
            txtStatus = (TextView)itemView.findViewById(R.id.txtStatus);
            txtDate = (TextView)itemView.findViewById(R.id.txtDate);
//            date = (TextView) itemView.findViewById(R.id.date);
//            mon_yr = (TextView) itemView.findViewById(R.id.mon_yr);
//            time = (TextView) itemView.findViewById(R.id.time);
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

    private String capitizeString(String name) {
        String captilizedString = "";
        if (!name.trim().equals("")) {
            captilizedString = name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return captilizedString;
    }

}
