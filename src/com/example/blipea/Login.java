package com.example.blipea;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {
	static final String TAG = "LoginActivity";
	final Login self=this;
	SharedPreferences settings;
	
	public void onCreate(Bundle savedInstanceState){
	    settings = getSharedPreferences(Blipea.PREFS_NAME, 0);
    	
	    super.onCreate(savedInstanceState);                               
        setContentView(R.layout.login);               
        
        if(settings.getString("username", null) != null &&
           settings.getString("password", null) != null	){
        	startActivity(new Intent(self, Timeline.class));
        }
        
        Button b = (Button)this.findViewById(R.id.loginButton);
        b.setOnClickListener(new OnClickListener(){
        	public void onClick(View b){
                String user = ((EditText)self.findViewById(R.id.usuario)).getText().toString();
                String password = ((EditText)self.findViewById(R.id.clave)).getText().toString();                                
                
                SharedPreferences.Editor editor = settings.edit();
               
                editor.putString("username", user);
                editor.putString("password", password);
                editor.commit();
                
                startActivity(new Intent(self, Timeline.class));
        	}
        }); 
	}

}
