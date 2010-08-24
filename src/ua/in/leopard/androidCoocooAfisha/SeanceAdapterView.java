package ua.in.leopard.androidCoocooAfisha;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SeanceAdapterView extends LinearLayout {

	public SeanceAdapterView(Context context, CinemaDB entry) {
		super(context);
		
		this.setOrientation(VERTICAL);
		this.setTag(entry);
		
		View v = inflate(context, R.layout.seance_row, null);
		
		ImageView cinemaPoster = (ImageView)v.findViewById(R.id.cinema_poster);
		Bitmap poster = entry.getPosterImg();
		if (poster != null){
			cinemaPoster.setImageBitmap(poster);
		} else {
			cinemaPoster.setImageResource(R.drawable.poster);
		}
		
		
		TextView cinemaTitle = (TextView)v.findViewById(R.id.cinema_title);
		cinemaTitle.setText(entry.getTitle());
		
		TextView origTitle = (TextView)v.findViewById(R.id.cinema_orig_title);
		origTitle.setText(Html.fromHtml(entry.getOrigTitle()));
		
		TextView zalTitle = (TextView)v.findViewById(R.id.cinema_zal_title);
		if (entry.getZalTitle().equals(null)){
			zalTitle.setText("");
		} else {
			zalTitle.setText(Html.fromHtml(entry.getZalTitle()));	
		}
		
		TextView cinemaTimes = (TextView)v.findViewById(R.id.cinema_times);
		if (entry.getTimes().equals(null)){
			cinemaTimes.setText("");
		} else {
			cinemaTimes.setText(entry.getTimes());
		}
		
		TextView cinemaPrices = (TextView)v.findViewById(R.id.cinema_prices);
		if (entry.getPrices().equals(null)){
			cinemaPrices.setText("");
		} else {
			cinemaPrices.setText(entry.getPrices());
		}
		
		
		addView(v);
	}

}
