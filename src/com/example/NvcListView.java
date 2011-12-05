package com.example;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class NvcListView extends ListActivity {
	
	private NvcDbAdapter mDbHelper;
	private String mTable; 
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        
        Bundle extras = getIntent().getExtras();
        this.mTable = extras.getString("table");
    	
        mDbHelper = new NvcDbAdapter(this);
        mDbHelper.open();
        
        Cursor c = mDbHelper.fetch(this.mTable);
    	startManagingCursor(c);
    	
    	String[] from = new String[] { "name" };
    	int[] to = new int[] { R.id.row1 };
    	
    	SimpleCursorAdapter notes = 
    			new SimpleCursorAdapter(this, R.layout.row, c, from, to);
    	setListAdapter(notes);  
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Intent i = new Intent(this, NvcDetailView.class);
		i.putExtra("id", id);
		i.putExtra("table", this.mTable);
		startActivity(i);
	}
}
