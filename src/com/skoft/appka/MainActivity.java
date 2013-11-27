package com.skoft.appka;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.skoft.appka.MESSAGE";
	private final ArrayList<String> autocompletes = new ArrayList<String>();
	private AutoCompleteTextView editText;
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    	ArrayAdapter<String> autocomplete_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, autocompletes);
        
    	editText = (AutoCompleteTextView) findViewById(R.id.nameTextView);
        editText.setAdapter(autocomplete_adapter);
        
        final ListView listview = (ListView) findViewById(R.id.songsList);
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
            "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
            "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
            "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
            "Android", "iPhone", "WindowsMobile" };

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
          list.add(values[i]);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
            android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

          @Override
          public void onItemClick(AdapterView<?> parent, final View view,
              int position, long id) {
            final String item = (String) parent.getItemAtPosition(position);
                    list.remove(item);
                    adapter.notifyDataSetChanged();
          }
        });
    }
    
    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
            List<String> objects) {
          super(context, textViewResourceId, objects);
          for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
          }
        }

        @Override
        public long getItemId(int position) {
          String item = getItem(position);
          return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
          return true;
        }

      }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        return true;
    }
    
    private String httpGet(String address, String parameters)
    {
    	try
    	{
         String response = null;
         URL url = new URL(address);

         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
         connection.setDoOutput(true);
         connection.setRequestProperty("Content-Type",
                 "application/x-www-form-urlencoded");

         connection.setRequestMethod("GET");
         //get the output stream from the connection you created
         OutputStreamWriter request =
        		 new OutputStreamWriter(connection.getOutputStream());

         request.write(parameters);
         request.flush();
         request.close();
         String line = "";

         InputStreamReader isr = new InputStreamReader(
                 connection.getInputStream());
         //read in the data from input stream, this can be done a variety of ways
         BufferedReader reader = new BufferedReader(isr);
         StringBuilder sb = new StringBuilder();
         while ((line = reader.readLine()) != null) {
             sb.append(line + "\n");
         }
         //get the string version of the response data
         response = sb.toString();
         //do what you want with the data now

         //always remember to close your input and output streams 
         isr.close();
         reader.close();
         
         return response;
     } catch (IOException e) {
         Log.e("HTTP GET:", e.toString());
         return "";
     }
    }
    
    public void searchClick(View view) {
//    	http://suggestqueries.google.com/complete/search?client=firefox&q=YOURQUERY

    	String search = editText.getText().toString();
    	
    	final String address =
    			"http://suggestqueries.google.com/complete/search";
    	
    	String parameters = "client=firefox&hl=pl&q=chomikuj " + search;

    	String json = httpGet(address, parameters);
    	
    	autocompletes.add("abc");
    	autocompletes.add(search);
    	autocompletes.add("def");
    	
    	editText.getAdapter().notifyAll();
    	
    	
    	//    	Intent intent = new Intent(this, DisplayMessageActivity.class);
    	//new AlertDialog.Builder(this).setMessage(message).create().show();
    	//intent.putExtra(EXTRA_MESSAGE, message);
    	
    	//startActivity(intent);
    }
}
