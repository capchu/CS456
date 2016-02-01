package com.cs456.chris.twitterclonewithfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
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
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase("https://luminous-torch-6850.firebaseio.com/chatty/");

        final EditText textEdit = (EditText) this.findViewById(R.id.text_edit);
        //final EditText textEditSender = (EditText) this.findViewById(R.id.text_edit_sender);
        Button sendButton = (Button) this.findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textEdit.getText().toString();
                //String sender = textEdit.getText().toString();
                ChatMessage message = new ChatMessage(text);
                message.setSender("CapTest");
                Long timeStampLong = (System.currentTimeMillis());
                message.setTimeStamp(formatter.format(new Date(timeStampLong)));
                message.setLatitude("59.329323");
                message.setLongitude("18.068581");
                //ArrayList<String> message = new ArrayList<String>();
                mFirebaseRef.push().setValue(message);
                textEdit.setText("");
            }
        });

        final ListView listView = (ListView) this.findViewById(android.R.id.list);
        mListAdapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                                      R.layout.message_display, mFirebaseRef) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                ((TextView)v.findViewById(R.id.sender_text)).setText(model.getSender());
                ((TextView)v.findViewById(R.id.message_text)).setText(model.getMessage());
                ((TextView)v.findViewById(R.id.timestamp_text)).setText(model.getTimeStamp());
                ((TextView)v.findViewById(R.id.latitude_text)).setText(model.getLatitude());
                ((TextView)v.findViewById(R.id.longitude_text)).setText(model.getLongitude());
            }
        };
        listView.setAdapter(mListAdapter);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mListAdapter.cleanup();
    }
}
