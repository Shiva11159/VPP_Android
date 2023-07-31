package com.application.vpp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.application.vpp.Datasets.CallbackModel;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Methods;
import com.application.vpp.Views.Views;
import com.github.vipulasri.timelineview.TimelineView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import org.w3c.dom.Text;

/**
 * Created by bpandey on 15-06-2018.
 */

public class CallBackAdapter extends RecyclerView.Adapter<CallBackAdapter.ViewHolder>  {
    boolean animation = true;
    ArrayList<CallbackModel> clientlistDataArrayList;
    ArrayList<CallbackModel> detailsFiltered;
    Context context;
    private LayoutInflater mLayoutInflater;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    boolean dropdown = false;
    public CallBackAdapter(ArrayList<CallbackModel> clientlistDataArrayList, Context context) {
        this.clientlistDataArrayList = clientlistDataArrayList;
        this.context = context;
        detailsFiltered = new ArrayList<>(clientlistDataArrayList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_call_back, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        String status = clientlistDataArrayList.get(holder.getAdapterPosition()).getStatus();

        Log.e("status",  clientlistDataArrayList.get(holder.getAdapterPosition()).getStatus());
        holder.txtStatus.setText(String.valueOf(clientlistDataArrayList.get(holder.getAdapterPosition()).getStatus()));
//        holder.txtDate.setText("Date :" + queryDataList.get(position).date);
        final boolean isExpanded = expandState.get(holder.getAdapterPosition());

        holder.booleantxt.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        if (clientlistDataArrayList.get(holder.getAdapterPosition()).getContact_number().length() > 19) {
            holder.txtcontact_number.setText(clientlistDataArrayList.get(holder.getAdapterPosition()).getContact_number().substring(0, 20));
        } else {
            holder.txtcontact_number.setText(clientlistDataArrayList.get(holder.getAdapterPosition()).getContact_number());
        }
        String name = clientlistDataArrayList.get(holder.getAdapterPosition()).getName();
        String date = clientlistDataArrayList.get(holder.getAdapterPosition()).getCreated_date();
        holder.txtNAME.setText("" + name);
        holder.txtdate.setText("" + date);

        if (clientlistDataArrayList.get(holder.getAdapterPosition()).getRemark().length() > 14) {
            holder.txt_remark.setText(clientlistDataArrayList.get(holder.getAdapterPosition()).getRemark().substring(0, 15));
            holder.txtseehide.setVisibility(VISIBLE);

        } else {

            if (clientlistDataArrayList.get(holder.getAdapterPosition()).getRemark().equalsIgnoreCase("")){
                holder.txt_remark.setText("-");

            }else
            holder.txt_remark.setText(clientlistDataArrayList.get(holder.getAdapterPosition()).getRemark());
//                holder.txtseehide.setVisibility(View.GONE);
        }

        if (status.equals("OPEN")) {
//            holder.txtremark.setVisibility(View.VISIBLE);
//            holder.mTimelineView.setMarker(mContext.getResources().getDrawable(R.drawable.close));

//            holder.openclose.setBackground(context.getResources().getDrawable(R.drawable.opne));
            holder.openclose.setText("open");
//            holder.txt_remark.setVisibility(View.GONE);
            holder.roundedLinear.setBackground(context.getResources().getDrawable(R.drawable.round));

        } else {
            holder.openclose.setText("close");

            holder.openclose.setBackground(context.getResources().getDrawable(R.drawable.close));
            holder.roundedLinear.setBackground(context.getResources().getDrawable(R.drawable.roundclose));

//            holder.mTimelineView.setMarker(mContext.getResources().getDrawable(R.drawable.open));

        }

//        holder.card_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onClickButton(holder.booleantxt,isExpanded,holder.txt_remark, holder.txtremark, holder.imageButtonDropdown, holder.getAdapterPosition(), holder.txtcontact_number, holder.txtseehide);
//
//            }
//        });
//        holder.txtStatus.invalidate();

//        try {
//
//            StringTokenizer stringTokenizer = new StringTokenizer(clientlistDataArrayList.get(holder.getAdapterPosition()).getCreated_date(), " ");
//            String date = stringTokenizer.nextToken();
//            String time = stringTokenizer.nextToken();
//
//            Date date1 = new SimpleDateFormat("dd-MMM-yy").parse(date);
//            holder.week.setText(Methods.getday(date1));
//            holder.date.setText(Methods.getdate(date1));
//            holder.monthyr.setText(Methods.getmonth(date1) + " " + Methods.getyear(date1));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public int getItemCount() {
        return clientlistDataArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtStatus, txtDate, txtcontact_number, txt_remark, txtNAME,txtdate;
        TimelineView mTimelineView;
        TextView week, date, monthyr, txtseehide, txtremark,booleantxt;
        LinearLayout roundedLinear;
        ImageButton imageButtonDropdown;

        TextView openclose;
        CardView card_view;

        public ViewHolder(View itemView) {
            super(itemView);

//            txtDate = (TextView) itemView.findViewById(R.id.txtdate);
            booleantxt = itemView.findViewById(R.id.booleantxt);
            txtremark = itemView.findViewById(R.id.txtremark);
            txtseehide = itemView.findViewById(R.id.txtseehide);
            roundedLinear = itemView.findViewById(R.id.roundedLinear);
            txtcontact_number = (TextView) itemView.findViewById(R.id.txtcontact_number);
            txt_remark = (TextView) itemView.findViewById(R.id.txtqueryremark);
            txtNAME = (TextView) itemView.findViewById(R.id.txtNAME);
            txtdate = (TextView) itemView.findViewById(R.id.txtdate);
            mTimelineView = (TimelineView) itemView.findViewById(R.id.time_marker);
            txtStatus = (TextView) itemView.findViewById(R.id.txtstatus);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            imageButtonDropdown = (ImageButton) itemView.findViewById(R.id.imageButtonDropdown);

            week = (TextView) itemView.findViewById(R.id.week);
            date = (TextView) itemView.findViewById(R.id.date);
            monthyr = (TextView) itemView.findViewById(R.id.mon_yr);
            openclose = itemView.findViewById(R.id.openclose);

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

    public void filterList(ArrayList<CallbackModel> filteredList) {
        clientlistDataArrayList = filteredList;
        notifyDataSetChanged();
    }
    private void setAnimation(View viewToAnimate) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (animation) {
            animation = false;
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.left_to_right);
            viewToAnimate.startAnimation(animation);
        } else {
            animation = true;
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.down_to_up);
            viewToAnimate.startAnimation(animation);
        }
    }
    private void onClickButton(TextView booleantxt,boolean isExpanded,final TextView txt_remark, final TextView txtremark, final ImageButton buttonLayout, final int i, TextView txtcontact_number, TextView txtseehide) {
        //Simply set View to Gone if not expanded
        //Not necessary but I put simple rotation on button layout

//        if (dataList.get(i).equals(i)){
        if (booleantxt.getVisibility()==VISIBLE) {
            booleantxt.setVisibility(GONE);
            if (!clientlistDataArrayList.get(i).getRemark().equalsIgnoreCase("")) {
                txtremark.setVisibility(VISIBLE);
                if (clientlistDataArrayList.get(i).getRemark().length() > 14) {
                    txt_remark.setText(Html.fromHtml(clientlistDataArrayList.get(i).getRemark().substring(0, 15)));
                    txtseehide.setVisibility(VISIBLE);

                } else {
                    txt_remark.setText(Html.fromHtml(clientlistDataArrayList.get(i).getRemark()));
//                    txtseehide.setVisibility(GONE);

                }
            } else {
                txt_remark.setText("");
//                txtremark.setVisibility(View.GONE);
            }

            if (clientlistDataArrayList.get(i).getContact_number().length() > 19) {
                txtcontact_number.setText(clientlistDataArrayList.get(i).getContact_number().substring(0, 20));

            } else {
                txtcontact_number.setText(clientlistDataArrayList.get(i).getContact_number());
            }

            Views.createRotateAnimator(buttonLayout, 180f, 0f).start();
            txt_remark.setVisibility(VISIBLE);
            dropdown = false;
            expandState.put(i, false);
            txt_remark.clearAnimation();
//            txtseehide.setText("see more");


        } else {
            dropdown = true;
            txt_remark.setText(clientlistDataArrayList.get(i).getRemark());
            txtcontact_number.setText(clientlistDataArrayList.get(i).getContact_number());
            Views.createRotateAnimator(buttonLayout, 0f, 180f).start();
            txt_remark.setVisibility(VISIBLE);
//            ANim(txt_remark);
            expandState.put(i, true);
            booleantxt.setVisibility(VISIBLE);
//            txtseehide.setText("Hide");

        }

//        }else {
//            expandableLayout.setVisibility(View.GONE);
//            expandState.put(i, false);
//
//
//        }


    }

}