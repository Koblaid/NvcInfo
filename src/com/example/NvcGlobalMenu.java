package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class NvcGlobalMenu extends ListActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        
        String[] from = new String[] { "name" };
    	int[] to = new int[] { R.id.row1 };
    	
    	// Now create an array adapter and set it to display using our row
    	List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
    	
    	HashMap<String,String> m1 = new HashMap<String,String> ();
    	m1.put("name", "Bedürftnisse");
    	m1.put("table", "need");
    	data.add(m1);
    	
    	HashMap<String,String> m2 = new HashMap<String,String> ();
    	m2.put("name", "Gefühle");
    	m2.put("table", "feeling");
    	data.add(m2);
    	
    	HashMap<String,String> m3 = new HashMap<String,String> ();
    	m3.put("name", "Strategien");
    	m3.put("table", "strategy");
    	data.add(m3);

    	SimpleAdapter notes = new SimpleAdapter(this, data, R.layout.row, from, to);
    	setListAdapter(notes);  
    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Object o = l.getAdapter().getItem(position);
		HashMap<String,String> h = (HashMap<String,String>)o;
		
		String table = (String)h.get("table");
		
		Intent i = new Intent(this, NvcListView.class);
		i.putExtra("table", table);
		startActivity(i);
		
	}
}

