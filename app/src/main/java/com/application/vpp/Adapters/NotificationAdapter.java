package com.application.vpp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.application.vpp.Datasets.Notification_data;
import com.application.vpp.R;
import com.application.vpp.other.MySingleton;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Context context;
    ArrayList<Notification_data> notifiactionDataList;
    List<CharSequence> lines = new ArrayList<>();


    public NotificationAdapter(Context context, ArrayList<Notification_data> notifiactionDataList) {
        this.context = context;
        this.notifiactionDataList = notifiactionDataList;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification, null);

        NotificationAdapter.ViewHolder viewHolder = new NotificationAdapter.ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationAdapter.ViewHolder holder, final int position) {
        holder.txtTitle.setText(notifiactionDataList.get(position).title);
        String details = notifiactionDataList.get(position).message;
        Spanned Detail = Html.fromHtml(details);
        holder.txtMsg.setText(Detail);
        holder.txtMsg.setMovementMethod(LinkMovementMethod.getInstance());


        /*if(holder.txtMsg.getMaxLines() == 2){
            holder.txtMsg.setTypeface(null, Typeface.BOLD);
            holder.txtMsg.setText("VIEW MORE....");
            holder.txtMsg.setTextColor(Color.BLUE);
        }*/

        String imgurl = notifiactionDataList.get(position).imgurl;

        if(!imgurl.isEmpty()){

            holder.image.setVisibility(View.VISIBLE);


        ImageRequest imageRequest = new ImageRequest(imgurl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.image.setImageBitmap(response);
            }
        }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(context).addToRequestQueue(imageRequest);

        }



        /*holder.layoutNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NotificationDetails.class);
                intent.putExtra("Title",notifiactionDataList.get(position).title);
                intent.putExtra("Details",notifiactionDataList.get(position).message);
                view.getContext().startActivity(intent);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return notifiactionDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView txtTitle, txtMsg;
        ImageView image;
        RelativeLayout layoutNotification;

        public ViewHolder(View itemView) {
            super(itemView);
            layoutNotification = (RelativeLayout)itemView.findViewById(R.id.layoutNotification);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtMsg = (TextView) itemView.findViewById(R.id.txt_msg);
            image = (ImageView)itemView.findViewById(R.id.imgview);


        }

    }
}

