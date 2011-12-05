package com.example;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class NvcDetailView extends Activity {
	private NvcDbAdapter mDbHelper;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        
        Bundle extras = getIntent().getExtras();
        long id = extras.getLong("id");
        String table = extras.getString("table");
    	
        mDbHelper = new NvcDbAdapter(this);
        mDbHelper.open();
        
        Cursor c = mDbHelper.fetchDetails(table, id);
    	startManagingCursor(c);

    	c.moveToFirst();
    	
    	TextView headingView = (TextView) findViewById(R.id.heading);
    	TextView descView = (TextView) findViewById(R.id.description);

    	headingView.setText(c.getString(c.getColumnIndexOrThrow("name")));
    	descView.setText(c.getString(c.getColumnIndexOrThrow("description")));        
	}
}