package com.example.blipea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

public class BlipeaFeed implements Runnable{
	String user;
	String password;
	String message;
	
	public BlipeaFeed(String user, String password){
		this.user = user;
		this.password = password;
	}
	
	public JSONArray getPublicFeed(){
		BufferedReader in = null;
		
		try{
			URL url = new URL("http://blipea.com/api/public.json");
			in = new BufferedReader(new InputStreamReader(url.openStream()));			
			String str;
			String js = "";
			while((str = in.readLine()) != null){
				js+=str;
			}							
            in.close();                                    
            return new JSONArray(js);
            
		}catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
         
            		
		
		return null;
	}
	
	public JSONArray getUserFeed(){
		BufferedReader in = null;
		
		try{
			Authenticator.setDefault(new Authenticator(){
				protected PasswordAuthentication getPasswordAuthentication(){
					return new PasswordAuthentication(user, password.toCharArray());
				}
			});
			URL url = new URL("http://blipea.com/api/statuses/home_timeline.json");
			in = new BufferedReader(new InputStreamReader(url.openStream()));			
			String str;
			String js = "";
			while((str = in.readLine()) != null){
				js+=str;
			}							
            in.close();                                    
            return new JSONArray(js);
            
		}catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
         
            		
		
		return null;
	}
	
	public boolean postBlip(String message){
		this.message = message;
		Thread thread = new Thread(this);
		thread.start();
		return true;
	}
	
	public void run(){	
	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    httpclient.getCredentialsProvider().setCredentials(new AuthScope("blipea.com", 80), 
	    										new UsernamePasswordCredentials(user, password));
	    HttpPost httppost = new HttpPost("http://blipea.com/blips/update");	    

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("blip", message));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));            
            // Execute HTTP Post Request
            httpclient.execute(httppost);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

	}
	
	public String getUsername(){
		return user;
	}


		
}