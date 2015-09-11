package ssui.rdonegan.schedulr;

import java.util.ArrayList;
import android.app.ListActivity;

//import edu.stanford.nick.DetailActivity;
//import edu.stanford.nick.R;
//import edu.stanford.nick.TodoDB;



import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MainActivity extends ListActivity implements OnClickListener, OnKeyListener {
	private TodoDB mDB;  // Our connection to the database.
	private SimpleCursorAdapter mCursorAdapter;
	
	
	//initialize interactor values

	Button btnAdd;
	ListView listItems;
	String newTask;
	
	ArrayList<String> toDoItems;
	ArrayAdapter<String> aa;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
      
        
        
        
        //initialize each variable to its value in interactor

//        btnAdd = (Button)findViewById(R.id.btnAdd);
//        listItems = (ListView)findViewById(R.id.list);
        
        
//        btnAdd.setOnClickListener(this);
        btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//startDetail is its own method which starts the new interaction
				startDetail(0, true);  // true = create new
			}
		});
        
     // Start up DB connection (closed in onDestroy).
        mDB = new TodoDB(this);
        mDB.open();
        
     // Get the "all rows" cursor. startManagingCursor() is built in for the common case,
        // takes care of closing etc. the cursor.
		Cursor cursor = mDB.queryAll();
		startManagingCursor(cursor);
		
		// Adapter: maps cursor keys, to R.id.XXX fields in the row layout.
		String[] from = new String[] { TodoDB.KEY_TITLE, TodoDB.KEY_STATE };
		int[] to = new int[] { R.id.rowtext, R.id.rowtext2 };
		mCursorAdapter = new SimpleCursorAdapter(this, R.layout.row2, cursor, from, to);
		
		// Map "state" int to text in the row -- intercept the setup of each row view,
		// fiddle with the data for the state column.
		mCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if (columnIndex == TodoDB.INDEX_STATE) {
					TextView textView = (TextView) view;
					if (cursor.getInt(TodoDB.INDEX_STATE) > 0) {
						textView.setText(" (done) ");
					}
					else {
						textView.setText("");
					}
					return true;  // i.e. we handled it
			    }
			    return false;  // i.e. the system should handle it
			}
			});
				
		setListAdapter(mCursorAdapter);
		registerForContextMenu(getListView());

        
//        toDoItems = new ArrayList<String>();
//        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, toDoItems);
//        listItems.setAdapter(aa);
//        
//
//        
//        //retrieve new task to display, if any
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//        	newTask = extras.getString("taskName");
//            System.out.println("**** value: " + newTask);
////            this.addItem(newTask);
////            toDoItems.add(newTask);
//            addItem(newTask);
//            
//        }
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		mDB.close();
	}
    
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	MenuItem item;
    	item = menu.add("money2");
    	item.setIcon(R.drawable.money2);
//    	item = menu.add("newspapers");
//    	item.setIcon(R.drawable.newspapers);
//    	item = menu.add("open28");
//    	item.setIcon(R.drawable.open28);
//    	item = menu.add("ring6");
//    	item.setIcon(R.drawable.ring6);
//    	item = menu.add("service1");
//    	item.setIcon(R.drawable.service1);
//    	item = menu.add("technical2");
//    	item.setIcon(R.drawable.technical2);
    	return true;
    }

    //adds item to the todo list, and updates the list item
    private void addItem(String item){
    	if (item.length() > 0){
    		this.toDoItems.add(item);
    		this.aa.notifyDataSetChanged();

    	}
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
    
    // Create context menu for click-hold in list. DO THIS LATER *****
//   	@Override
//   	public void onCreateContextMenu(ContextMenu menu, View v,
//   	                                ContextMenuInfo menuInfo) {
//   		super.onCreateContextMenu(menu, v, menuInfo);
//   		MenuInflater inflater = getMenuInflater();
//   		inflater.inflate(R.menu.list_menu, menu);
//   	}
    
 // Removes the given rowId from the database, updates the UI.
    public void remove(long rowId) {
		mDB.deleteRow(rowId);
		//mCursorAdapter.notifyDataSetChanged();  // confusingly, this does not work
		mCursorAdapter.getCursor().requery();  // need this
    }
    
public static final String EXTRA_ROWID = "rowid";
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long rowId) {
		super.onListItemClick(l, v, position, rowId);
		startDetail(rowId, false);
	}
	
	// Starts the detail activity, either edit existing or create new.
	public void startDetail(long rowId, boolean create) {
		Intent intent = new Intent(this, NewItem.class);
		// Our convention: add rowId to edit existing. To create add nothing.
		if (!create) {
			intent.putExtra(EXTRA_ROWID, rowId);
		}
		startActivity(intent);
		// Easy bug: remember to add to add a manifest entry for the detail activity
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
//			this.addItem(this.txtItem.getText().toString());
			return true;
		}
		return false;
	}


	@Override
	public void onClick(View v) {
//		if (v == this.btnAdd){
////			this.addItem(this.txtItem.getText().toString());
//			Intent myintent = new Intent(this, NewItem.class); //change to new item screen
//			startActivity(myintent);
//		}
//		
//		System.out.println("click worked!");		
		Intent myintent = new Intent(this, NewItem.class); //change to new item screen
		startActivity(myintent);
		
	}
}
