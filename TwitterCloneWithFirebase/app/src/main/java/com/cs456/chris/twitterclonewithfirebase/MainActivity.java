package com.cs456.chris.twitterclonewithfirebase;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Firebase mFirebaseRef = null;
    FirebaseListAdapter<ChatMessage> mListAdapter;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyy HH:mm:ss", Locale.US);
    Location curLocation = null;
    String senderFilter = "";
    protected static final String TAG = "MainActivity";
    String curSender = "CapTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase("https://luminous-torch-6850.firebaseio.com/chatty/");

        //Posting a Message
        final EditText textEdit = (EditText) this.findViewById(R.id.text_edit);
        //final EditText textEditSender = (EditText) this.findViewById(R.id.text_edit_sender);
        Button sendButton = (Button) this.findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textEdit.getText().toString();
                //String sender = textEdit.getText().toString();
                ChatMessage message = new ChatMessage(text);
                message.setSender(curSender);
                Long timeStampLong = (System.currentTimeMillis());
                message.setTimeStamp(formatter.format(new Date(timeStampLong)));
                message.setLatitude(curLocation.getLatitude()+"");
                message.setLongitude(curLocation.getLongitude()+"");
                //ArrayList<String> message = new ArrayList<String>();
                mFirebaseRef.push().setValue(message);
                textEdit.setText("");
            }
        });


        //Displaying The Messages
        final ListView listView = (ListView) this.findViewById(android.R.id.list);
        mListAdapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                                      R.layout.message_display, mFirebaseRef) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                    ((TextView) v.findViewById(R.id.sender_text)).setText(model.getSender());
                    ((TextView) v.findViewById(R.id.message_text)).setText(model.getMessage());
                    ((TextView) v.findViewById(R.id.timestamp_text)).setText(model.getTimeStamp());
                    ((TextView) v.findViewById(R.id.latitude_text)).setText(model.getLatitude());
                    ((TextView) v.findViewById(R.id.longitude_text)).setText(model.getLongitude());
            }
        };
        listView.setAdapter(mListAdapter);


        //Getting the Location
        LocationManager locationManager =
                (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                curLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


        //Filter By Sender
        final EditText senderEdit = (EditText) this.findViewById(R.id.edit_user);
        //final EditText textEditSender = (EditText) this.findViewById(R.id.text_edit_sender);
        Button filterButton = (Button) this.findViewById(R.id.filter_button);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curSender = senderEdit.getText().toString();
                senderEdit.setText("");
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mListAdapter.cleanup();
    }
}
