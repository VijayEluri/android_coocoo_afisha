package ua.in.leopard.androidCoocooAfisha;

import com.google.analytics.tracking.android.EasyTracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class EditPreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {
   private DataProgressDialog backgroudUpdater;
   // Option names and default values
   private static final String OPT_CITY = "city_current_name";
   private static final String OPT_CITY_DEF = "Киев";
   private static final String OPT_CITY_ID = "city_id";
   private static final String OPT_CITY_ID_DEF = "1";
   private static final String OPT_AUTO_UPD = "auto_update";
   private static final Boolean OPT_AUTO_UPD_DEF = false;
   private static final String OPT_AUTO_UPD_TIME = "auto_update_every_time";
   private static final String OPT_AUTO_UPD_TIME_DEF = "1";
   private static final String OPT_CACHED_POSTER = "cache_poster";
   private static final Boolean OPT_CACHED_POSTER_DEF = true;
   private static final String OPT_THEATERS_FILTER = "theaters_is_filter";
   private static final Boolean OPT_THEATERS_FILTER_DEF = false;
   private static final String OPT_NO_POSTER = "no_poster";
   private static final Boolean OPT_NO_POSTER_DEF = false;
   private static final String OPT_GPS_ONOFF = "gps_onoff";
   private static final Boolean OPT_GPS_ONOFF_DEF = true;
   
   private static final String SECRET_TOKEN = SecretData.getSecretToken();
   private static final String THEATERS_URL_KEY = "theaters_url";
   private static final String THEATERS_URL = "http://coocoorooza.com/api/afisha_theaters/:city_id/:token.json";
   private static final String CINEMAS_URL_KEY = "cinemas_url";
   private static final String CINEMAS_URL = "http://coocoorooza.com/api/afisha_cinemas/:city_id/:token.json";
   
   
   private ListPreference cities_lp;
   private CheckBoxPreference checkbox_theaters_filter;
   private CheckBoxPreference checkbox_cached_poster;

   @SuppressWarnings("deprecation")
@Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.settings);
      
      cities_lp = (ListPreference)findPreference(OPT_CITY_ID);
      
      ListPreference lp = (ListPreference)findPreference(OPT_AUTO_UPD_TIME);
      if (getPreferenceScreen().getSharedPreferences().getBoolean(OPT_AUTO_UPD, OPT_AUTO_UPD_DEF)){
	   lp.setEnabled(true);
	   
      } else {
	   lp.setEnabled(false);
      }
      
      checkbox_cached_poster = (CheckBoxPreference)findPreference(OPT_CACHED_POSTER);
      if (EditPreferences.isNoPosters(this)){
		   checkbox_cached_poster.setEnabled(false);
	   } else {
		   checkbox_cached_poster.setEnabled(true);
	   }
      
      checkbox_theaters_filter = (CheckBoxPreference)findPreference(OPT_THEATERS_FILTER);
      if (EditPreferences.getTheaterUrl(this) == "" || EditPreferences.getCinemasUrl(this) == ""){
    	  checkbox_theaters_filter.setEnabled(false);
      } else {
    	  checkbox_theaters_filter.setEnabled(true);
      }
      
      restoreBackgroudUpdate();
   }
   
   @SuppressWarnings("deprecation")
private void restoreBackgroudUpdate(){
   	if (getLastNonConfigurationInstance()!=null) {
   		backgroudUpdater = (DataProgressDialog)getLastNonConfigurationInstance();
   		if(backgroudUpdater.getStatus() == AsyncTask.Status.RUNNING) {
   			backgroudUpdater.newView(this);
   		}
   	}
   }
   
   @Override
   public Object onRetainNonConfigurationInstance() {
	if(backgroudUpdater!= null && backgroudUpdater.getStatus() == AsyncTask.Status.RUNNING) {
		backgroudUpdater.closeView();
	}
   	return(backgroudUpdater);
   }
   
   @SuppressWarnings("deprecation")
@Override
   protected void onResume() {
       super.onResume();
       // Set up a listener whenever a key changes            
       getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
       if (new DatabaseHelper(this).isSetFilters()){
 		  checkbox_theaters_filter.setChecked(true);
 	   } else {
 		  checkbox_theaters_filter.setChecked(false);
 	   }
   }

   @SuppressWarnings("deprecation")
@Override
   protected void onPause() {
       super.onPause();
       // Unregister the listener whenever a key changes            
       getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);    
   }

   
   public static String getCity(Context context) {
	   return PreferenceManager.getDefaultSharedPreferences(context).getString(OPT_CITY, OPT_CITY_DEF);
   }
   
   public static Boolean isCachedPosters(Context context) {
	   return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_CACHED_POSTER, OPT_CACHED_POSTER_DEF);
   }
   
   public static Boolean isTheatersIsFilter(Context context) {
	   return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_THEATERS_FILTER, OPT_THEATERS_FILTER_DEF);
   }
   
   public static Boolean isNoPosters(Context context) {
	   return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_NO_POSTER, OPT_NO_POSTER_DEF);
   }
   
   /** Get the current value of the cities option */
   public static String getCityId(Context context) {
      return PreferenceManager.getDefaultSharedPreferences(context).getString(OPT_CITY_ID, OPT_CITY_ID_DEF);
   }
   
   public static String getTheaterUrl(Context context) {
	   return PreferenceManager.getDefaultSharedPreferences(context).getString(THEATERS_URL_KEY, "");
   }
   
   public static String getCinemasUrl(Context context) {
	   return PreferenceManager.getDefaultSharedPreferences(context).getString(CINEMAS_URL_KEY, "");
   }
   
   public static Boolean getAutoUpdate(Context context) {
	   return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_AUTO_UPD, OPT_AUTO_UPD_DEF);
   }
   
   public static String getAutoUpdateTime(Context context) {
	   return PreferenceManager.getDefaultSharedPreferences(context).getString(OPT_AUTO_UPD_TIME, OPT_AUTO_UPD_TIME_DEF);
   }
   
   public static Boolean getGpsStatus(Context context) {
	   return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_GPS_ONOFF, OPT_GPS_ONOFF_DEF);
   }
   
   public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	   if (key.equals(OPT_CITY_ID)) {
		   SharedPreferences.Editor editor = sharedPreferences.edit();
		   
		   editor.putString(OPT_CITY, cities_lp.getEntry().toString());
		   editor.commit();
		   
		   setDefUrls(sharedPreferences);
		   
		   backgroudUpdater = new DataProgressDialog(this);
		   if(backgroudUpdater.getStatus() == AsyncTask.Status.PENDING) {
			   backgroudUpdater.execute();
		   }
		   
		   checkbox_theaters_filter.setEnabled(true);
		   checkbox_theaters_filter.setChecked(false);
	   }
	   /* auto updater */
	   if (key.equals(OPT_AUTO_UPD)) {
		   @SuppressWarnings("deprecation")
		ListPreference lp = (ListPreference)findPreference(OPT_AUTO_UPD_TIME);
		   stopService(new Intent(getApplicationContext(), DataUpdateService.class));
		   if (sharedPreferences.getBoolean(OPT_AUTO_UPD, OPT_AUTO_UPD_DEF)){
			   lp.setEnabled(true);
			   if (Integer.parseInt(sharedPreferences.getString(OPT_AUTO_UPD_TIME, OPT_AUTO_UPD_TIME_DEF)) != 0){
				   startService(new Intent(getApplicationContext(), DataUpdateService.class));
			   }
		   } else {
			   lp.setEnabled(false);
		   }
	   }
	   if (key.equals(OPT_AUTO_UPD_TIME)) {
		   stopService(new Intent(this, DataUpdateService.class));
		   if (Integer.parseInt(sharedPreferences.getString(OPT_AUTO_UPD_TIME, OPT_AUTO_UPD_TIME_DEF)) != 0){
			   startService(new Intent(getApplicationContext(), DataUpdateService.class));
		   }
	   }
	   /* cache posters */
	   if (key.equals(OPT_CACHED_POSTER)) {
		   if (sharedPreferences.getBoolean(OPT_CACHED_POSTER, OPT_CACHED_POSTER_DEF)){
			   backgroudUpdater = new DataProgressDialog(this);
			   if(backgroudUpdater.getStatus() == AsyncTask.Status.PENDING) {
				   backgroudUpdater.execute();
			   }
		   }
	   }
	   /* cinemas filter */
	   if (key.equals(OPT_THEATERS_FILTER)) {
		   if (sharedPreferences.getBoolean(OPT_THEATERS_FILTER, OPT_THEATERS_FILTER_DEF)){
			   startActivity(new Intent(this, TheatersFilterDialog.class));
		   } else {
			   new DatabaseHelper(this).clearFilterTheaters();
		   }
	   }
	   /* no posters */
	   if (key.equals(OPT_NO_POSTER)) {
		   if (sharedPreferences.getBoolean(OPT_NO_POSTER, OPT_NO_POSTER_DEF)){
			   checkbox_cached_poster.setEnabled(false);
		   } else {
			   checkbox_cached_poster.setEnabled(true);
		   }
	   }
   }
   
   private void setDefUrls(SharedPreferences sharedPreferences){
	   SharedPreferences.Editor editor = sharedPreferences.edit();
	   String theaters_url = THEATERS_URL;
	   theaters_url = theaters_url.replace(":city_id", cities_lp.getValue());
	   theaters_url = theaters_url.replace(":token", SECRET_TOKEN);
	   editor.putString(THEATERS_URL_KEY, theaters_url);
	   String cinemas_url = CINEMAS_URL;
	   cinemas_url = cinemas_url.replace(":city_id", cities_lp.getValue());
	   cinemas_url = cinemas_url.replace(":token", SECRET_TOKEN);
	   editor.putString(CINEMAS_URL_KEY, cinemas_url);
	   
	   editor.commit();
   }
   
	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this); // Add this method.
	}
	  
	@Override
	public void onStop() {
		super.onStop();
	    EasyTracker.getInstance().activityStop(this); // Add this method.
	}
   
}
