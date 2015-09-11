package ssui.rdonegan.schedulr;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class NewItem extends ActionBarActivity {

		String task;
		EditText taskName;
	
	Button submitTask;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState)  {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.add_item);
	        
	        //create activity for submit button
	        submitTask = (Button)findViewById(R.id.submitTaskBT);
	        
	        taskName = (EditText)findViewById(R.id.taskName);
	        
//	        submitTask.setOnClickListener(this);
	       
	    }
	 
	 
	 public void onClick(View v){
		task = taskName.getText().toString();
		Intent myintent = new Intent(this, MainActivity.class); //change to main menu screen
		myintent.putExtra("taskName",task);
		startActivity(myintent);
	 }
	
	
}
