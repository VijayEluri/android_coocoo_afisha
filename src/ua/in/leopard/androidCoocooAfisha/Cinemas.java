package ua.in.leopard.androidCoocooAfisha;

import java.util.List;

import com.google.analytics.tracking.android.EasyTracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Cinemas extends MainActivity implements OnClickListener, OnItemClickListener {
	private CinemaAdapter adapter;
	private DataProgressDialog backgroudUpdater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.cinemas);
        
        String title = getString(R.string.cinemas_title) + " - " + EditPreferences.getCity(this);
        if (EditPreferences.isTheatersIsFilter(this)){
        	title = title + " (" + getString(R.string.theaters_is_filter_on) + ")";
        }
        setTitle(title);
        
        updateDataInView();
        
        View updateButton = findViewById(R.id.no_data_update_button);
        updateButton.setOnClickListener(this);
        
        restoreBackgroudUpdate();
	}
	
	private void restoreBackgroudUpdate(){
    	if (getLastNonConfigurationInstance() != null) {
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
    
    
    public void updateDataInView(){
    	ListView CinemasList = (ListView)findViewById(R.id.cinemas_list);
    	LinearLayout noDataText = (LinearLayout)findViewById(R.id.no_data_block);
    	
        DatabaseHelper DatabaseHelperObject = new DatabaseHelper(this);
        List<CinemaDB> cinemas = DatabaseHelperObject.getTodayCinemas();
        adapter = new CinemaAdapter(this, cinemas);
        CinemasList.setAdapter(adapter);
        CinemasList.setOnItemClickListener(this);
        if (cinemas.size() == 0){
        	CinemasList.setVisibility(View.GONE);
        	noDataText.setVisibility(View.VISIBLE);
        } else {
        	CinemasList.setVisibility(View.VISIBLE);
        	noDataText.setVisibility(View.GONE);
        }
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		CinemaDB cinema_object = (CinemaDB)adapter.getItem(position);
		//work
		Intent intent = new Intent(this, Cinema.class);
		Bundle bundle = new Bundle();
		bundle.putInt("cinema_id", cinema_object.getId());
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		  case R.id.no_data_update_button:
			 backgroudUpdater = new DataProgressDialog(this);
			 backgroudUpdater.addUpdateView(this);
			 if(backgroudUpdater.getStatus() == AsyncTask.Status.PENDING) {
				 backgroudUpdater.execute();
			 }
	         break;         
	      }
		
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
