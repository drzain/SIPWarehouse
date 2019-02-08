package com.sip.warehouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.onesignal.OneSignal;

import java.util.HashMap;

import io.fabric.sdk.android.Fabric;

public class NewMainActivity extends AppCompatActivity {

    private CardView btnLogout;
    private SessionManager session;
    private SQLiteHandler db;
    private TextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        Fabric.with(this, new Crashlytics());

        txtName = (TextView) findViewById(R.id.textUser);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        txtName.setText(name);

        session = new SessionManager(getApplicationContext());

        btnLogout = (CardView) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    public void Logout(){
        session.setLogin(false);
        db.deleteUsers();
        Intent intent = new Intent(NewMainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
