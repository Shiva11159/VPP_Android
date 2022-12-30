package com.application.vpp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.vpp.Datasets.PhoneDirectoryData;
import com.application.vpp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneDirectoryAdapter extends RecyclerView.Adapter<PhoneDirectoryAdapter.ViewHolder> {

    Context context;
    ArrayList<PhoneDirectoryData> phoneDirectoryDataArrayList;
    int arrayListCount = 0;

    Activity activity;


    public PhoneDirectoryAdapter(Context context, ArrayList<PhoneDirectoryData>phoneDirectoryDataArrayList){

        this.context = context;
        this.phoneDirectoryDataArrayList = phoneDirectoryDataArrayList;
        arrayListCount = phoneDirectoryDataArrayList.size();
        activity = (Activity)context;


    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_directory_list_contacts,null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,  int i) {

        holder.txtcontactName.setText(phoneDirectoryDataArrayList.get(holder.getAdapterPosition()).getName());
        holder.txtcontactNumber.setText(phoneDirectoryDataArrayList.get(holder.getAdapterPosition()).getNumber());
        if(phoneDirectoryDataArrayList!=null){

        }

        holder.contact_directory_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent();
                intent.putExtra("number",phoneDirectoryDataArrayList.get(holder.getAdapterPosition()).getNumber());
                activity.setResult(Activity.RESULT_OK,intent);
                activity.finish();

            }
        });


    }


    @Override
    public int getItemCount() {
        return phoneDirectoryDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.contactName)
        TextView txtcontactName;
        @BindView(R.id.contactNumber) TextView txtcontactNumber;
        @BindView(R.id.contact_directory_list)
        LinearLayout contact_directory_list;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
