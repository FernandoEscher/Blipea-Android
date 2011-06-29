package com.example.blipea;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Timeline extends Activity implements Runnable {

	static final String TAG = "BlipeaActivity";
	ArrayList<String> blipeaFeed = new ArrayList<String>();
    ListView lv;
    //We need this class for some events        
    final Timeline self = this;    
    BlipeaFeed bFeed;
    ArrayAdapter<String> blipAdapter;
    
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);                               
        setContentView(R.layout.main);
        
        lv = (ListView)this.findViewById(R.id.listView1);        
        //Creating the ArrayAdapter for the ListView to get populated
        blipAdapter = new ArrayAdapter<String>(this, 
        		R.layout.blip, 
        		blipeaFeed);                
        //Updating the list view with the new adapter               
        lv.setAdapter(blipAdapter);
    }
	
    public void onStart() {
    	super.onStart();
        //Getting the widgets form the view
        final EditText text = (EditText)this.findViewById(R.id.editText1);
        
        SharedPreferences settings = getSharedPreferences(Blipea.PREFS_NAME, 0);
        bFeed = new BlipeaFeed(settings.getString("username", null),
        					   settings.getString("password", null));
        
        
        Button b = (Button)this.findViewById(R.id.button1);
        b.setOnClickListener(new OnClickListener(){
        	public void onClick(View b){
        		final String t = text.getText().toString();
        		if(t.length() != 0){
        			bFeed.postBlip(t);        			
        			blipeaFeed.add(0, bFeed.getUsername()+": "+t);
        			blipAdapter.notifyDataSetChanged();  
        			text.setText("");
        		}else{
        			Toast.makeText(getApplicationContext(), 
        					  self.getResources().getText(R.string.add_text),
        			          Toast.LENGTH_SHORT).show();
        		}
        	}
        });
        
        Thread t = new Thread(this);
        t.start();
    }
    
        
    public void run(){
    	JSONArray j = bFeed.getUserFeed();    	    	
    	 
    	for(int i=0; i<j.length(); i++){    		    		
    		try {
    			String uname = j.getJSONObject(i).getJSONObject("user").getString("screen_name");
    			String text = j.getJSONObject(i).getString("text");
    			blipeaFeed.add(uname+": "+text);
			} catch (JSONException e) {
				e.printStackTrace();
			}
    	}    	
    	
    	handler.sendEmptyMessage(RESULT_OK);
    }
        
    private Handler handler = new Handler(){
    	@Override
        public void handleMessage(Message msg) {
    		blipAdapter = new ArrayAdapter<String>(self, R.layout.blip, blipeaFeed);    	            
    		lv.setAdapter(blipAdapter);                       
        }
    };

}