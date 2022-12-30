package com.application.vpp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.application.vpp.Datasets.ClientlistData;
import com.application.vpp.Datasets.SubPatnerActivityModel;
import com.application.vpp.Interfaces.CallBack;
import com.application.vpp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by bpandey on 28-06-2018.
 */

public class SubPartnerAdapter extends RecyclerView.Adapter<SubPartnerAdapter.ViewHolder>  {

    ArrayList<SubPatnerActivityModel>arrayList;
    Context context;
    AlertDialog alertDialog;
    CallBack callBack;

    ArrayList<SubPatnerActivityModel> detailsFiltered;
    public SubPartnerAdapter(ArrayList<SubPatnerActivityModel>arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
        detailsFiltered = new ArrayList<>();
    }
    @NonNull
    @Override
    public SubPartnerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_not_subpartner,null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SubPartnerAdapter.ViewHolder holder, final int position) {

        holder.txtClientCode.setText(arrayList.get(position).getClientCode());
        holder.txtClientName.setText(arrayList.get(position).getClientName());
        holder.txtProductName.setText(arrayList.get(position).getProductName());

//        String string = "January 2, 2010";

        try {
            DateFormat format = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.ENGLISH);
//            }else {
//                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//            }

            format = new SimpleDateFormat("dd-MMM-yy HH:mm:ss", Locale.ENGLISH);
            Date date = null;
            date = format.parse(arrayList.get(position).getAccountOpenedDate());
            Log.e("date", arrayList.get(position).getAccountOpenedDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        StringTokenizer stringTokenizer=new StringTokenizer(arrayList.get(position).getAccountOpenedDate()," ");
        String datestr=stringTokenizer.nextToken();
        String timestr=stringTokenizer.nextToken();
        holder.txtDate.setText(datestr);

//        holder.txtleadDate.setText(clientlistDataArrayList.get(position).LeadDate);

     //   holder.notInterestedReason.setText(clientlistDataArrayList.get(position).notInterestedreason);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

//    @Override
//    public Filter getFilter() {
//        return dataFilter;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtClientCode,txtProductName,txtClientName,txtDate;
        public ViewHolder(View itemView) {
            super(itemView);

            txtDate = (TextView)itemView.findViewById(R.id.txtDate);
            txtClientCode = (TextView)itemView.findViewById(R.id.txtClientCode);
            txtProductName = (TextView)itemView.findViewById(R.id.txtProductName);
//            txtleadDate = (TextView)itemView.findViewById(R.id.txtleadDate);
            txtClientName = (TextView)itemView.findViewById(R.id.txtClientName);

        }

    }

//    private Filter dataFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<SubPatnerActivityModel> filterlist = new ArrayList<>();
//            if (constraint == null || constraint.length() == 0) {
//                filterlist.addAll(detailsFiltered);
//
//
//            } else {
//                String fillpatern = constraint.toString().toLowerCase().trim();
//                for (SubPatnerActivityModel item : detailsFiltered) {
//                    if (item.getClientName().toLowerCase().contains(fillpatern) || item.getProductName().toLowerCase().contains(fillpatern)|| item.getClientCode().toLowerCase().contains(fillpatern)) {
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
//            arrayList.clear();
//            arrayList.addAll((List) filterResults.values);
//            notifyDataSetChanged();
//        }
//    };

    public void filterList(ArrayList<SubPatnerActivityModel> filteredList) {
        arrayList = filteredList;
        notifyDataSetChanged();
    }
}
