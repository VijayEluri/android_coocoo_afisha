package ua.in.leopard.androidCoocooAfisha;


import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

public class AfishaWidgetProvider extends AppWidgetProvider {
	public static String FORCE_WIDGET_UPDATE = "ua.in.leopard.androidCoocooAfisha.FORCE_WIDGET_UPDATE";
	private List<CinemaDB> cinemas_list = null;
	
	private Hashtable<Integer, AppWidgetManager> app_widget_managers = new Hashtable<Integer, AppWidgetManager>();
	private Hashtable<Integer, RemoteViews> main_views = new Hashtable<Integer, RemoteViews>();
	private Hashtable<Integer, Timer> timers = new Hashtable<Integer, Timer>();
	private Hashtable<Integer, Integer> cinemas_iterators = new Hashtable<Integer, Integer>();
	
	public class WidgetTimerTask extends TimerTask{
		private int id;
		
		public WidgetTimerTask(int id){
			this.id = id;
		}
		
		@Override
		public void run() {
			updatePoster(this.id);
		}
		
	}
	
	
	
	@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] app_widget_ids) {
		
		DatabaseHelper DatabaseHelperObject = new DatabaseHelper(context);
	    List<CinemaDB> cinemas = DatabaseHelperObject.getTodayCinemas();
		
		final int count = app_widget_ids.length;
        for (int i=0; i< count; i++) {
            updateAppWidget(context, appWidgetManager, app_widget_ids[i], cinemas);
        }
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if (FORCE_WIDGET_UPDATE.equals(intent.getAction())) {
			// TODO Update widget UI.
		}
	}
	
    @Override
    public void onEnabled(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName("ua.in.leopard.androidCoocooAfisha", ".AfishaWidgetBroadcastReceiver"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
    @Override
    public void onDisabled(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName("ua.in.leopard.androidCoocooAfisha", ".AfishaWidgetBroadcastReceiver"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
    @Override
    public void onDeleted(Context context, int[] app_widget_ids) {
    	for (int i=0; i< app_widget_ids.length; i++) {
    		Timer timer = timers.get(i);
    		if (timer != null){
    			timer.cancel();
    		}
        }
    }
    
    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
            int app_widget_id, List<CinemaDB> cinemas) {
    	this.cinemas_list = cinemas;
    	
    	timers.put(app_widget_id, new Timer());
    	cinemas_iterators.put(app_widget_id, 0);
    	app_widget_managers.put(app_widget_id, appWidgetManager);
    	main_views.put(app_widget_id, new RemoteViews(context.getPackageName(), R.layout.afisha_widget_provider));
    	
    	RemoteViews view = main_views.get(app_widget_id);
    	view.setImageViewResource(R.id.cinema_poster, R.drawable.poster);
    	
        startTimer(app_widget_id);
        
        //Intent form = new Intent(context, HelloAndroid.class);
        //PendingIntent main = PendingIntent.getActivity(context, 0, form, 0);
        //views.setOnClickPendingIntent(R.id.start_program, main);

        // Tell the widget manager
        appWidgetManager.updateAppWidget(app_widget_id, view);
    }
    
    private void startTimer(int app_widget_id){
    	Timer timer = timers.get(app_widget_id);
    	timer.scheduleAtFixedRate(
		new WidgetTimerTask(app_widget_id), 
		0, 1000 * 5);
	}
    
    private void updatePoster(int id){
        if (cinemas_list.size() > 0 && main_views.get(id) != null){
        	RemoteViews view = main_views.get(id);
        	int cinemas_iterator = cinemas_iterators.get(id);
        	if (cinemas_list.size() <= cinemas_iterator){
        		cinemas_iterator = 0;
    		}
        	
        	Bitmap poster = cinemas_list.get(cinemas_iterator).getPosterImg();
    		if (poster != null){
    			view.setImageViewBitmap(R.id.cinema_poster, poster);
    		}
    		
    		cinemas_iterator++;
    		if (cinemas_list.size() <= cinemas_iterator){
    			cinemas_iterator = 0;
    		}
    		cinemas_iterators.put(id, cinemas_iterator);
    		AppWidgetManager appWidgetManager = app_widget_managers.get(id);
    		appWidgetManager.updateAppWidget(id, view);
        }
    }
}
