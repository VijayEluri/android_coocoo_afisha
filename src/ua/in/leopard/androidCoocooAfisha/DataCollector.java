package ua.in.leopard.androidCoocooAfisha;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class DataCollector {
	private final Context myContext;
	private Boolean getInetError = false;
	
	public DataCollector(Context context) {
		this.myContext = context;
		this.getInetError = false;
	}
	
	public Boolean getInetError(){
		return getInetError;
	}
	
	public void getTheatersData(){
		this.getInetError = false;
		this.getTheatersDataFromJSON(JsonClient.getData(EditPreferences.getTheaterUrl(this.myContext)));
	}
	
	public void getCinemasData(){
		this.getInetError = false;
		this.getCinemasDataFromJSON(JsonClient.getData(EditPreferences.getCinemasUrl(this.myContext)));
	}
	
	public void getTheatersDataFromJSON(JSONObject js_obj){
		try {
			DatabaseHelper DatabaseHelperObject = new DatabaseHelper(this.myContext);
			JSONArray theaters_array = js_obj.getJSONArray("theaters");
			if (theaters_array != null){
				List<TheaterDB> theater_data = new ArrayList<TheaterDB>();
				int city_id = Integer.parseInt(EditPreferences.getCityId(this.myContext));
				int count = theaters_array.length();
				for (int i = 0; i < count; ++i) {
				    JSONObject row = theaters_array.getJSONObject(i);
				    if (city_id == row.getInt("city_id")){
				    	String phone = "";
				    	String address = "";
				    	String latitude = "";
				    	String longitude = "";
				    	String call_phone = "";
				    	if (!row.isNull("phone")){
				    		phone = row.getString("phone");
				    	}
				    	if (!row.isNull("address")){
				    		address = row.getString("address");
				    	}
				    	if (!row.isNull("latitude")){
				    		latitude = row.getString("latitude");
				    	}
				    	if (!row.isNull("longitude")){
				    		longitude = row.getString("longitude");
				    	}
				    	if (!row.isNull("call_phone")){
				    		call_phone = row.getString("call_phone");
				    	}
				    	theater_data.add(new TheaterDB(
				    			row.getInt("id"), city_id, 
				    			row.getString("title"), 
				    			row.getString("link"), 
				    			address, 
				    			phone,
				    			latitude,
				    			longitude,
				    			call_phone,
				    			0)
				    	);
				    }
				}
				DatabaseHelperObject.setTheaterTransaction(theater_data);
			}
		} catch (JSONException e) {
			//Log.i("dataCollector", "error data");
			//e.printStackTrace();
		} catch (Exception e) { 
        	//e.printStackTrace();
			this.getInetError = true;
        	//Log.v("dataCollector","Exception");
        }
	}
	
	public void getCinemasDataFromJSON(JSONObject js_obj){
		try {
			DatabaseHelper DatabaseHelperObject = new DatabaseHelper(this.myContext);
			JSONArray cinemas_array = js_obj.getJSONArray("cinemas");
			if (cinemas_array != null){
				List<CinemaDB> cinema_data = new ArrayList<CinemaDB>();
				int count = cinemas_array.length();
				for (int i = 0; i < count; ++i) {
				    JSONObject row = cinemas_array.getJSONObject(i);
				    if (row != null){
				    	String poster = null;
				    	String casts = "";
				    	if (!row.isNull("poster")){
				    		poster = row.getString("poster");
				    	}
				    	if (!row.isNull("casts")){
				    		casts = row.getString("casts");
				    	}
				    	cinema_data.add(new CinemaDB(
				    			row.getInt("id"), 
				    			row.getString("title"),
				    			row.getString("orig_title"), 
				    			row.getString("year"), 
				    			poster, 
				    			row.getString("description"),
				    			casts));
				    }
				}
				DatabaseHelperObject.setCinemaTransaction(cinema_data);
			}
			
			JSONArray afisha_array = js_obj.getJSONArray("afisha");
			if (afisha_array != null){
				List<AfishaDB> afisha_data = new ArrayList<AfishaDB>();
				int count = afisha_array.length();
				for (int i = 0; i < count; ++i) {
				    JSONObject row = afisha_array.getJSONObject(i);
				    if (row != null){
				    	String zal = null;
				    	String times = null;
				    	String prices = null;
				    	if (!row.isNull("zal_title")){
				    		zal = row.getString("zal_title");
				    	}
				    	if (!row.isNull("times")){
				    		times = row.getString("times");
				    	}
				    	if (!row.isNull("prices")){
				    		prices = row.getString("prices");
				    	}
				    	afisha_data.add(new AfishaDB(
				    			row.getInt("id"), 
				    			row.getInt("cinema_id"), 
				    			row.getInt("theater_id"), 
				    			zal,
				    			row.getString("date_begin"), 
				    			row.getString("date_end"), 
				    			times, 
				    			prices)
				    	);
				    }
				}
				DatabaseHelperObject.setAfishaTransaction(afisha_data);
			}
		} catch (JSONException e) {
			//Log.i("dataCollector", "error data");
			//e.printStackTrace();
		} catch (Exception e) { 
        	e.printStackTrace();
			this.getInetError = true;
        	//Log.v("dataCollector","Exception");
        }
	}
	
	public void clearOldData(){
		DatabaseHelper DatabaseHelperObject = new DatabaseHelper(this.myContext);
		DatabaseHelperObject.clearOldData();
	}
	
	public void indexTables(){
		DatabaseHelper DatabaseHelperObject = new DatabaseHelper(this.myContext);
		DatabaseHelperObject.indexCinemaTable();
	}
}
