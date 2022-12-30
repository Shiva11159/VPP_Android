package com.application.vpp.Adapters;

import android.content.Context;
import android.media.Image;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.application.vpp.Adapters.lib_recyclerViewHeader.StickyHeaderAdapter;
import com.application.vpp.Datasets.LeadDetailReportDataset;
import com.application.vpp.Datasets.QueryStatusData;
import com.application.vpp.R;
import com.application.vpp.ReusableLogics.Methods;
import com.application.vpp.Views.Views;
import com.github.vipulasri.timelineview.TimelineView;

import org.w3c.dom.Text;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class QueryStatusAdapter extends RecyclerView.Adapter<QueryStatusAdapter.ViewHolder> implements StickyHeaderAdapter<QueryStatusAdapter.HeaderHolder> {
    ArrayList<QueryStatusData> queryDataList;
    Context context;
    ArrayList<LeadDetailReportDataset> detailsFiltered;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    boolean animation = true;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    boolean dropdown = false;

    public QueryStatusAdapter(ArrayList<QueryStatusData> queryDataList, Context context) {

        this.queryDataList = queryDataList;
        this.context = context;
        for (int i = 0; i < queryDataList.size(); i++) {
            expandState.append(i, false);
        }
//         Remark="<b>" + "Remark : " + "</b> ";

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        mLayoutInflater = LayoutInflater.from(mContext);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_query_status2, null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    public long getHeaderId(int position) {
        try {
            String gID = queryDataList.get(position).date;
            StringBuilder sb = new StringBuilder();
            for (char c : gID.toCharArray())
                sb.append((int) c);
            BigInteger mInt = new BigInteger(sb.toString());
            return Long.valueOf(mInt.toString());
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        return 000;
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = mLayoutInflater.inflate(R.layout.header_time_line, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder viewholder, int position) {
        try {
            viewholder.header.setText(queryDataList.get(position).date);
            viewholder.header.setBackgroundResource(R.drawable.text_header_bg);//"@drawable/text_header_bg");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (animation) {
            animation = false;
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
            viewholder.itemView.startAnimation(animation);
        } else {
            animation = true;
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_);
            viewholder.itemView.startAnimation(animation);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        String status = queryDataList.get(holder.getAdapterPosition()).status;
        holder.txtStatus.setText(String.valueOf(queryDataList.get(holder.getAdapterPosition()).status));
//        holder.txtDate.setText("Date :" + queryDataList.get(position).date);
        final boolean isExpanded = expandState.get(holder.getAdapterPosition());

        holder.booleantxt.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        if (queryDataList.get(holder.getAdapterPosition()).query.length() > 19) {
            holder.txtQuery.setText(queryDataList.get(holder.getAdapterPosition()).query.substring(0, 20));
        } else {
            holder.txtQuery.setText(queryDataList.get(holder.getAdapterPosition()).query);
        }
        int x = queryDataList.get(holder.getAdapterPosition()).sr_no;
        holder.txt_token.setText("" + x);

        if (queryDataList.get(holder.getAdapterPosition()).remark.length() > 14) {
            holder.txt_remark.setText(queryDataList.get(holder.getAdapterPosition()).remark.substring(0, 15));
            holder.txtseehide.setVisibility(VISIBLE);

        } else {
            holder.txt_remark.setText(queryDataList.get(holder.getAdapterPosition()).remark);
//                holder.txtseehide.setVisibility(View.GONE);
        }

        if (status.equals("Close")) {
//            holder.txtremark.setVisibility(View.VISIBLE);
//            holder.mTimelineView.setMarker(mContext.getResources().getDrawable(R.drawable.close));
            holder.openclose.setBackground(mContext.getResources().getDrawable(R.drawable.close));
            holder.roundedLinear.setBackground(mContext.getResources().getDrawable(R.drawable.roundclose));
        } else {

//            holder.mTimelineView.setMarker(mContext.getResources().getDrawable(R.drawable.open));
            holder.openclose.setBackground(mContext.getResources().getDrawable(R.drawable.opne));
//            holder.txt_remark.setVisibility(View.GONE);
            holder.roundedLinear.setBackground(mContext.getResources().getDrawable(R.drawable.round));

        }

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButton(holder.booleantxt,isExpanded,holder.txt_remark, holder.txtremark, holder.imageButtonDropdown, holder.getAdapterPosition(), holder.txtQuery, holder.txtseehide);

            }
        });
//        holder.txtStatus.invalidate();

        try {

            StringTokenizer stringTokenizer = new StringTokenizer(queryDataList.get(holder.getAdapterPosition()).date, " ");
            String date = stringTokenizer.nextToken();
            String time = stringTokenizer.nextToken();

            Date date1 = new SimpleDateFormat("dd-MMM-yy").parse(date);
            holder.week.setText(Methods.getday(date1));
            holder.date.setText(Methods.getdate(date1));
            holder.monthyr.setText(Methods.getmonth(date1) + " " + Methods.getyear(date1));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return queryDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtStatus, txtDate, txtQuery, txt_remark, txt_token;
        TimelineView mTimelineView;
        TextView week, date, monthyr, txtseehide, txtremark,booleantxt;
        LinearLayout roundedLinear;
        ImageButton imageButtonDropdown;

        ImageView openclose;
        CardView card_view;

        public ViewHolder(View itemView) {
            super(itemView);

//            txtDate = (TextView) itemView.findViewById(R.id.txtdate);
            booleantxt = itemView.findViewById(R.id.booleantxt);
            txtremark = itemView.findViewById(R.id.txtremark);
            txtseehide = itemView.findViewById(R.id.txtseehide);
            roundedLinear = itemView.findViewById(R.id.roundedLinear);
            txtQuery = (TextView) itemView.findViewById(R.id.txtquery);
            txt_remark = (TextView) itemView.findViewById(R.id.txtqueryremark);
            txt_token = (TextView) itemView.findViewById(R.id.txttoken);
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

    class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView header;

        public HeaderHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.txtHader);
        }
    }

    private void onClickButton(TextView booleantxt,boolean isExpanded,final TextView txt_remark, final TextView txtremark, final ImageButton buttonLayout, final int i, TextView txtQuery, TextView txtseehide) {
        //Simply set View to Gone if not expanded
        //Not necessary but I put simple rotation on button layout

//        if (dataList.get(i).equals(i)){
        if (booleantxt.getVisibility()==VISIBLE) {
            booleantxt.setVisibility(GONE);
            if (!queryDataList.get(i).remark.equalsIgnoreCase("")) {
                txtremark.setVisibility(VISIBLE);
                if (queryDataList.get(i).remark.length() > 14) {
                    txt_remark.setText(Html.fromHtml(queryDataList.get(i).remark.substring(0, 15)));
                    txtseehide.setVisibility(VISIBLE);

                } else {
                    txt_remark.setText(Html.fromHtml(queryDataList.get(i).remark));
//                    txtseehide.setVisibility(GONE);

                }
            } else {
                txt_remark.setText("");
//                txtremark.setVisibility(View.GONE);
            }

            if (queryDataList.get(i).query.length() > 19) {
                txtQuery.setText(queryDataList.get(i).query.substring(0, 20));

            } else {
                txtQuery.setText(queryDataList.get(i).query);
            }

            Views.createRotateAnimator(buttonLayout, 180f, 0f).start();
            txt_remark.setVisibility(VISIBLE);
            dropdown = false;
            expandState.put(i, false);
            txt_remark.clearAnimation();
            txtseehide.setText("see more");


        } else {
            dropdown = true;
            txt_remark.setText(queryDataList.get(i).remark);
            txtQuery.setText(queryDataList.get(i).query);
            Views.createRotateAnimator(buttonLayout, 0f, 180f).start();
            txt_remark.setVisibility(VISIBLE);
            ANim(txt_remark);
            expandState.put(i, true);
            booleantxt.setVisibility(VISIBLE);
            txtseehide.setText("Hide");

        }

//        }else {
//            expandableLayout.setVisibility(View.GONE);
//            expandState.put(i, false);
//
//
//        }


    }

    public void ANim(TextView expandableLayout) {
        TranslateAnimation animation = new TranslateAnimation(100.0f, 0.0f, 100.0f, 0.0f);
        animation.setDuration(1000);  // animation duration
        animation.setRepeatCount(0);  // animation repeat count
//                    animation.setRepeatMode(1);   // repeat animation (left to right, right to left )
        expandableLayout.startAnimation(animation);
    }

}
