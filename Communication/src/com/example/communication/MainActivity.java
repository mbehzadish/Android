package com.example.communication;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	boolean send_flg = false ; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final Button Call_but = (Button) findViewById(R.id.call_but);
        final Button Msg_but = (Button) findViewById(R.id.msg_but);
        
        final EditText Phone_num = (EditText) findViewById(R.id.phone_num);
        
        final Button Send_but = (Button) findViewById(R.id.send_but);
        Send_but.setVisibility(View.INVISIBLE);       
        final EditText Msg = (EditText) findViewById(R.id.msg_txt);
        Msg.setVisibility(View.INVISIBLE);
        
        Call_but.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String phoneNum = Phone_num.getText().toString();
				
				try{
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + phoneNum));
				startActivity(callIntent);
				}catch(Exception e){
					Log.e("TAG" , e.getMessage());				}
				
			}
		});
        
        Msg_but.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				send_flg = true ;
				Msg.setVisibility(View.VISIBLE);
				Send_but.setVisibility(View.VISIBLE);
			}
		});
        
        Send_but.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(send_flg){
					String msg_txt = Msg.getText().toString();
					String phoneNum = Phone_num.getText().toString();
					
					// tavasot e barname e messenger bara dial ba dialer ham bayad be ja dial call bezari
//					Intent smsIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+phoneNum));
//					smsIntent.putExtra("sms_body",msg_txt);
//					startActivity(smsIntent);
					SmsManager sManager = SmsManager.getDefault();
					sManager.sendTextMessage(phoneNum, null, msg_txt, null, null);
					Toast.makeText(getApplicationContext(), "Message Sent",
							Toast.LENGTH_LONG).show();
					Msg.setText(null);
					Msg.setVisibility(View.INVISIBLE);
					Send_but.setVisibility(View.INVISIBLE);
				}
			}
		});
    }
}
