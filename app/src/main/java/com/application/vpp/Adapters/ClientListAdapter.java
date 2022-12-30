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
import com.application.vpp.Datasets.SubPatnerActivityModel;
import com.application.vpp.R;
import com.application.vpp.Utility.AlertDialogClass;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by bpandey on 15-06-2018.
 */

public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.ViewHolder>  {

    ArrayList<ClientlistData> clientlistDataArrayList;
    ArrayList<ClientlistData> detailsFiltered;
    Context context;

    public ClientListAdapter(ArrayList<ClientlistData> clientlistDataArrayList, Context context) {
        this.clientlistDataArrayList = clientlistDataArrayList;
        this.context = context;
        detailsFiltered = new ArrayList<>(clientlistDataArrayList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_client_list1, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Log.e("onLayout___", clientlistDataArrayList.get(position).AccountOpenedDate);

        try {
//            Collections.sort(clientlistDataArrayList, new Comparator<ClientlistData>() {
//                SimpleDateFormat f = new SimpleDateFormat("dd MMM yyyy");
//                @Override
//                public int compare(ClientlistData lhs, ClientlistData rhs) {
//                    try {
//                        return f.parse(lhs.getAccountOpenedDate()).compareTo(f.parse(rhs.getAccountOpenedDate()));
//                    } catch (ParseException e) {
//                        throw new IllegalArgumentException(e);
//                    }
//                }
//            });

            holder.txtClientCode.setText(clientlistDataArrayList.get(position).ClientCode);
            holder.txtClientName.setText(clientlistDataArrayList.get(position).ClientName);
            holder.txtClientPlan.setText(clientlistDataArrayList.get(position).ProductName);
            holder.txtClientRegDate.setText(clientlistDataArrayList.get(position).AccountOpenedDate);

            // Log.e( "AccountOpenedDate: ",clientlistDataArrayList.get(position).AccountOpenedDate);

            StringTokenizer stringTokenizer=new StringTokenizer(clientlistDataArrayList.get(position).AccountOpenedDate," ");
            holder.date.setText(stringTokenizer.nextToken());
            holder.month.setText(stringTokenizer.nextToken());
            holder.year.setText(stringTokenizer.nextToken());

            if ((position + 1) == clientlistDataArrayList.size()) {
                Log.e("onLayout", "process done 11");
            }
            Log.e("onLayout", clientlistDataArrayList.get(position).AccountOpenedDate);



        }catch (Exception e){
            AlertDialogClass.ShowMsg(context,e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return clientlistDataArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtClientCode, txtClientName, txtClientRegDate, txtClientPlan,date,month,year;

        public ViewHolder(View itemView) {
            super(itemView);
            try {
                txtClientCode = (TextView) itemView.findViewById(R.id.txtClientCode);
                txtClientName = (TextView) itemView.findViewById(R.id.txtClientName);
                txtClientPlan = (TextView) itemView.findViewById(R.id.txtProductClient);
                txtClientRegDate = (TextView) itemView.findViewById(R.id.txtClientRegDate);
                date = (TextView) itemView.findViewById(R.id.date);///
                month = (TextView) itemView.findViewById(R.id.month);
                year = (TextView) itemView.findViewById(R.id.year);
            }catch (Exception e){
                AlertDialogClass.ShowMsg(context,e.getMessage());
            }
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
//                    if (item.ClientName.toLowerCase().contains(fillpatern) || item.ClientCode.toLowerCase().contains(fillpatern)) {
//                        filterlist.add(item);
//                    }
//                }
//            }
//
//            FilterResults result = new FilterResults();
//            result.values = filterlist;
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