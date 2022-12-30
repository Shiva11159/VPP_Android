package com.application.vpp.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.application.vpp.Datasets.BranchLocatorDetails;
import com.application.vpp.Interfaces.CallBack;
import com.application.vpp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bpandey on 14-06-2018.
 */

public class BranchLocatorAdapter extends RecyclerView.Adapter<BranchLocatorAdapter.ViewHolder> implements Filterable  {
    ArrayList<BranchLocatorDetails> branchListDatasetArrayList;
    Context context;
    ArrayList<BranchLocatorDetails> detailsFiltered;
    CallBack callBack;
    Context ctx;
    public BranchLocatorAdapter(Context ctx,ArrayList<BranchLocatorDetails> branchListDatasetArrayList, CallBack callBack){
        this.ctx = ctx;
        this.branchListDatasetArrayList = branchListDatasetArrayList;
        this.callBack = callBack;
        detailsFiltered = new ArrayList<>(branchListDatasetArrayList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.branch_name_item_layout,null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {


        //holder.txtName.invalidate();
//        holder.txtLeadNumber.setText(String.valueOf(branchListDatasetArrayList.get(position).sr_no));
        holder.txtName.setText(branchListDatasetArrayList.get(holder.getAdapterPosition()).branch_name);

        holder.relative_post_ur_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ContactUs contactUs = new ContactUs();
                // branchLocatorFragment.textview_contact_person.setText(branchListDatasetArrayList.get(position).branch_name);

                callBack.getDetails(branchListDatasetArrayList.get(holder.getAdapterPosition()).branch_name,
                        branchListDatasetArrayList.get(holder.getAdapterPosition()).contact_person,branchListDatasetArrayList.get(holder.getAdapterPosition()).email_id,
                        branchListDatasetArrayList.get(holder.getAdapterPosition()).mobile_no);

            }
        });

    }

    @Override
    public int getItemCount() {
        return branchListDatasetArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return dataFilter;
    }

    public BranchLocatorAdapter() {
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName/*,txtLeadNumber*/;
        RelativeLayout relative_post_ur_query;
        public ViewHolder(View itemView) {
            super(itemView);

//            txtLeadNumber = (TextView)itemView.findViewById(R.id.txt_branch_sr_no);
            txtName = (TextView)itemView.findViewById(R.id.txt_branch_name);
            relative_post_ur_query = (RelativeLayout) itemView.findViewById(R.id.relative_post_ur_query);

        }
    }

    private Filter dataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<BranchLocatorDetails> filterlist = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filterlist.addAll(detailsFiltered);


            } else {
                String fillpatern = constraint.toString().toLowerCase().trim();
                for (BranchLocatorDetails item : detailsFiltered) {
                    if (item.branch_name.toLowerCase().contains(fillpatern)) {
                        filterlist.add(item);
                    }
                }
            }


            FilterResults result = new FilterResults();
            result.values = filterlist;


            return result;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            branchListDatasetArrayList.clear();
            branchListDatasetArrayList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
