package com.application.vpp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.application.vpp.R;

public class NotificationDetails extends NavigationDrawer {
    TextView title,deatails;
    String Title,Details;
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        getIntent().getStringExtra("AnotherActivity");
        getLayoutInflater().inflate(R.layout.notification_details,mDrawerLayout);
        toolbar.setTitle("NOTIFICATION");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        title=findViewById(R.id.title);
        deatails=findViewById(R.id.details);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null){
            Title=bd.getString("Title");
            Details=bd.getString("Details");
            Spanned Detail = Html.fromHtml(Details);
            title.setText(Title);
            deatails.setText(Detail);
            deatails.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
    public void onBackPressed() {

        finish();
        Intent intent = new Intent(NotificationDetails.this, Notifications.class);

        startActivity(intent);
    }


}