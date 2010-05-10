package qoodro.sg.todo;

import java.util.ArrayList;
import java.util.Collections;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ViewTasks extends ListActivity {
	/** Called when the activity is first created. */
	EditText editNewTask;
	Button buttonNewTask;
	ListView listTasks;
	ArrayList<String> arrayTasks;
	ArrayAdapter<String> arrayTaskAdapter;

	static final private int SORT_TASKS = Menu.FIRST;
	static final private int REMOVE_TASKS = Menu.FIRST+1;
	static final private int EDIT_TASK = Menu.FIRST+2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		arrayTasks = new ArrayList<String>();
		arrayTasks.add("UT2");
		arrayTasks.add("FYP");

		editNewTask = (EditText)findViewById(R.id.editNewTask);
		buttonNewTask = (Button)findViewById(R.id.buttonNewTask);
		listTasks = getListView();

		registerForContextMenu(listTasks);

		arrayTaskAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayTasks);
		listTasks.setAdapter(arrayTaskAdapter);

		buttonNewTask.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(editNewTask.getText().length() > 0 ) {
					addNewTask(editNewTask.getText().toString());
					editNewTask.setText("");
				} else {
					alertUser("Invalid input");
				}
			}
		});
		editNewTask.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN){
					if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
						if(editNewTask.getText().length() > 0 ) {
							addNewTask(editNewTask.getText().toString());
							editNewTask.setText("");
							
							return true;
						} else {
							alertUser("Invalid input");
						}
					}
				}
				return false;
			}
		});

	}


	private void addNewTask(String task) {
		arrayTasks.add(task);
		arrayTaskAdapter.notifyDataSetChanged();
		Toast.makeText(getBaseContext(), "Added new task",Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuItem itemSort = menu.add(0,SORT_TASKS,Menu.NONE,R.string.sortAll);	
		MenuItem itemRemove = menu.add(0,REMOVE_TASKS,Menu.NONE,R.string.removeAll);

		itemSort.setIcon(R.drawable.sort_btn);
		itemRemove.setIcon(R.drawable.remove_btn);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);
		switch(item.getItemId()) {
		case(SORT_TASKS):{
			Collections.sort(arrayTasks);
			arrayTaskAdapter.notifyDataSetChanged();
			return true;
		}
		case(REMOVE_TASKS):{
			promptUserRemoveAll("Are you sure you want to delete all of your tasks?");
			return true;
		}
		}

		return false;
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu,View v,ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Options");
		menu.add(0,REMOVE_TASKS,Menu.NONE,R.string.removeItem);
		menu.add(0,EDIT_TASK,Menu.NONE,R.string.editItem);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		AdapterView.AdapterContextMenuInfo menuInfo;
		menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		final int index = menuInfo.position;
		switch(item.getItemId()){
		case(REMOVE_TASKS):{
			promptUserRemoveOne("Are you sure you want to delete this task?",index);
			return true;
		}
		case(EDIT_TASK):{
			editNewTask.setText(arrayTasks.get(index));
			buttonNewTask.setText("Save");
			buttonNewTask.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					arrayTasks.remove(index);
					arrayTasks.add(index, editNewTask.getText().toString());
					arrayTaskAdapter.notifyDataSetChanged();
					Toast.makeText(getBaseContext(), "Saved",Toast.LENGTH_SHORT).show();
					buttonNewTask.setText("Add");
					editNewTask.setText("");
					buttonNewTask.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v) {
							if(editNewTask.getText().length() > 0 ) {
								addNewTask(editNewTask.getText().toString());
								editNewTask.setText("");
							} else {
								alertUser("Invalid input");
							}
						}

					});
				}		
			});
		}
		}
		return false;
	}

	private void alertUser(String message) {
		new AlertDialog.Builder(this).setTitle("Alert!").setMessage(message).setNeutralButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int sumthin) {
				// do nothing – it will close on its own
			}
		}).show();
	}

	private void promptUserRemoveAll(String message){
		new AlertDialog.Builder(this).setTitle("Alert!").setMessage(message).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int sumthin) {
				arrayTasks.clear();
				arrayTaskAdapter.notifyDataSetChanged();
				Toast.makeText(getBaseContext(), "Tasks removed",Toast.LENGTH_SHORT).show();
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int sumthin) {

			}}).show();
	}

	private void promptUserRemoveOne(String message,final int index){
		new AlertDialog.Builder(this).setTitle("Alert!").setMessage(message).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int sumthin) {

				arrayTasks.remove(index);
				arrayTaskAdapter.notifyDataSetChanged();
				Toast.makeText(getBaseContext(), "Task removed",Toast.LENGTH_SHORT).show();
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int sumthin) {

			}}).show();
	}

}