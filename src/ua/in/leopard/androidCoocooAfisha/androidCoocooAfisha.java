package ua.in.leopard.androidCoocooAfisha;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class androidCoocooAfisha extends Activity implements OnClickListener {
	private TextView main_title;
	private DataProgressDialog backgroudUpdater;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        main_title = (TextView)findViewById(R.id.main_title);
        
        View cinemasButton = findViewById(R.id.cinemas_button);
        cinemasButton.setOnClickListener(this);
        View theatersButton = findViewById(R.id.theaters_button);
        theatersButton.setOnClickListener(this);
        View theatersMapButton = findViewById(R.id.theaters_map_button);
        theatersMapButton.setOnClickListener(this);
        View updateButton = findViewById(R.id.update_button);
        updateButton.setOnClickListener(this);
        
        restoreBackgroudUpdate();
        
        if (EditPreferences.getTheaterUrl(this) == "" || EditPreferences.getCinemasUrl(this) == ""){
        	startActivity(new Intent(this, EditPreferences.class));
        	Toast.makeText(this, getString(R.string.select_city_dialog), Toast.LENGTH_LONG).show();
        } else {

        	if (EditPreferences.getAutoUpdate(this)){
        		if (backgroudUpdater == null){
        			backgroudUpdater = new DataProgressDialog(this);
        		}
        		if(backgroudUpdater.getStatus() == AsyncTask.Status.PENDING) {
        			backgroudUpdater.execute();
        		}
	        	if (Integer.parseInt(EditPreferences.getAutoUpdateTime(this)) != 0){
	        		startService(new Intent(this, DataUpdateService.class));
	        	}
	        }
        }
    }
    
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
    	if(backgroudUpdater != null && backgroudUpdater.getStatus() == AsyncTask.Status.RUNNING) {
    		backgroudUpdater.closeView();
    	}
    	return(backgroudUpdater);
    }
  
    @Override
    protected void onResume() {
       super.onResume();
       main_title.setText(Html.fromHtml(getString(R.string.current_city_title) + " <b>" + EditPreferences.getCity(this) + "</b>"));
    }

    @Override
    protected void onPause() {
       super.onPause();
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	if (EditPreferences.getAutoUpdate(this) && 
    		Integer.parseInt(EditPreferences.getAutoUpdateTime(this)) != 0){
    		stopService(new Intent(this, DataUpdateService.class));
    	}    	
	}

	@Override
	public void onClick(View v) {
		if (EditPreferences.getTheaterUrl(this) == "" || EditPreferences.getCinemasUrl(this) == ""){
        	startActivity(new Intent(this, EditPreferences.class));
        	Toast.makeText(this, getString(R.string.select_city_dialog), Toast.LENGTH_LONG).show();
        } else {
			switch (v.getId()) {
			  case R.id.cinemas_button:
				 startActivity(new Intent(this, Cinemas.class));
		         break;
			  case R.id.theaters_button:
				 startActivity(new Intent(this, Theaters.class));
		         break;
			  case R.id.theaters_map_button:
				 startActivity(new Intent(this, TheatersMap.class));
		         break;
			  case R.id.update_button:
				 backgroudUpdater = new DataProgressDialog(this);
				 if(backgroudUpdater.getStatus() == AsyncTask.Status.PENDING) {
					 backgroudUpdater.execute();
				 }
				 /* Update widgets */
				 this.sendBroadcast(new Intent(AfishaWidgetProvider.FORCE_WIDGET_UPDATE));
		         break;         
		      }
        }
	}
	
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      super.onCreateOptionsMenu(menu);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      	case R.id.settings:
         startActivity(new Intent(this, EditPreferences.class));
         return true;
      	case R.id.about_button:
      	 startActivity(new Intent(this, About.class));
      	return true;
      	default:
	     return super.onOptionsItemSelected(item);
      }
   }

}