package ua.in.leopard.androidCoocooAfisha;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Theaters extends Activity implements OnClickListener, OnItemClickListener {
	private TheaterAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.theaters);
        
        ListView TheaterList = (ListView)findViewById(R.id.theaters_list);
        DatabaseHelper DatabaseHelperObject = new DatabaseHelper(this);
        List<TheaterDB> theaters = DatabaseHelperObject.getTheaters();
        adapter = new TheaterAdapter(this, theaters);
        TheaterList.setAdapter(adapter);
        TheaterList.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		TheaterDB theater_object = (TheaterDB)adapter.getItem(position);
		Intent intent = new Intent(this, Theater.class);
		Bundle bundle = new Bundle();
		bundle.putInt("theater_id", theater_object.getId());
		intent.putExtras(bundle);
		startActivity(intent);
	}


	@Override
	public void onClick(View v) {

	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
      super.onCreateOptionsMenu(menu);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.second_menu, menu);
      return true;
    }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      case R.id.select_city:
         startActivity(new Intent(this, EditPreferences.class));
         return true;
      }
      return false;
   }

}
