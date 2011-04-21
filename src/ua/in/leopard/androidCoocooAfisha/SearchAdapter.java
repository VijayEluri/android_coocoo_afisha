package ua.in.leopard.androidCoocooAfisha;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SearchAdapter extends BaseAdapter {
	private Context context;
	private List<SearchResDB> search_list;
	
	public SearchAdapter(Context context, List<SearchResDB> search_list){
		this.context = context;
		this.search_list = search_list;
	}
	
	@Override
	public int getCount() {
		return search_list.size();
	}

	@Override
	public Object getItem(int position) {
		return search_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SearchResDB entry = search_list.get(position);
		return new SearchAdapterView(context, entry);
	}
}
