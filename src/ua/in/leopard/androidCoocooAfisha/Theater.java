package ua.in.leopard.androidCoocooAfisha;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Theater extends Activity implements OnItemClickListener {
	private SeanceAdapter adapter_today, adapter_tomorrow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.theater);
        
        TabHost tabs=(TabHost)findViewById(R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec=tabs.newTabSpec("afisha_today_tag");
        spec.setContent(R.id.afisha_today_list);
        spec.setIndicator(getString(R.string.afisha_today), getResources().getDrawable(R.drawable.today_icon));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("afisha_tomorrow_tag");
        spec.setContent(R.id.afisha_tomorrow_list);
        spec.setIndicator(getString(R.string.afisha_tomorrow), getResources().getDrawable(R.drawable.tomorrow_icon));
        tabs.addTab(spec);
        tabs.setCurrentTab(0);
        
        Bundle extras = getIntent().getExtras();
        int theater_id = 0;
        if(extras != null) {
        	theater_id = extras.getInt("theater_id", 0);
        }
        
        if (theater_id != 0){
        	DatabaseHelper DatabaseHelperObject = new DatabaseHelper(this);
        	TheaterDB theater_main = DatabaseHelperObject.getTheater(theater_id);
        	if (theater_main != null){
        		TextView theater_title = (TextView)findViewById(R.id.theater_title);
        		theater_title.setText(theater_main.getTitle());
        		TextView theater_address = (TextView)findViewById(R.id.theater_address);
        		theater_address.setText(Html.fromHtml(theater_main.getAddress()));
        		TextView theater_phone = (TextView)findViewById(R.id.theater_phone);
        		theater_phone.setText(Html.fromHtml(theater_main.getPhone()));
        		
        		ListView seanceTodayList = (ListView)findViewById(R.id.afisha_today_list);
        		List<CinemaDB> cinemas_today = DatabaseHelperObject.getTodayOrTomorrowByTheater(theater_main, true);
        		adapter_today = new SeanceAdapter(this, cinemas_today);
        		seanceTodayList.setAdapter(adapter_today);
        		seanceTodayList.setOnItemClickListener(this);
        		
        		ListView seanceTomorrowList = (ListView)findViewById(R.id.afisha_tomorrow_list);
        		List<CinemaDB> cinemas_tomorrow = DatabaseHelperObject.getTodayOrTomorrowByTheater(theater_main, false);
        		adapter_tomorrow = new SeanceAdapter(this, cinemas_tomorrow);
        		seanceTomorrowList.setAdapter(adapter_tomorrow);
        		seanceTomorrowList.setOnItemClickListener(this);
        	}
        }
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		SeanceAdapter s_adapter = (SeanceAdapter)parent.getAdapter();
		CinemaDB cinema_obj = (CinemaDB)s_adapter.getItem(position);
		Intent intent = new Intent(this, Cinema.class);
		Bundle bundle = new Bundle();
		bundle.putInt("cinema_id", cinema_obj.getId());
		intent.putExtras(bundle);
		startActivity(intent);
	}
}