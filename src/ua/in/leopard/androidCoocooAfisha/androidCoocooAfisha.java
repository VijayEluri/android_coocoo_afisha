package ua.in.leopard.androidCoocooAfisha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class androidCoocooAfisha extends Activity implements OnClickListener {
	private TextView current_city;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
    	
        current_city=(TextView)findViewById(R.id.current_city);
        current_city.setText(getString(R.string.current_city_title) + EditPreferences.getCityId(this));
    }
    
    @Override
    protected void onResume() {
       super.onResume();
       current_city.setText(getString(R.string.current_city_title) + EditPreferences.getCityId(this));
    }

    @Override
    protected void onPause() {
       super.onPause();
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	      case R.id.about_button:
	         Intent i = new Intent(this, About.class);
	         startActivity(i);
	         break;
	      case R.id.exit_button:
	         finish();
	         break;	         
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
      }
      return false;
   }

}