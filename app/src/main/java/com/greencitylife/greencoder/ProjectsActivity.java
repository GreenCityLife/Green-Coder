package com.greencitylife.greencoder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;
import java.text.*;
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.AdapterView;
import android.view.View;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class ProjectsActivity extends AppCompatActivity {
	
	
	private Toolbar _toolbar;
	private FloatingActionButton _fab;
	private HashMap<String, Object> add_project = new HashMap<>();
	private String name = "";
	private String path = "";
	private String default_settings = "";
	private double pos_af = 0;
	
	private ArrayList<HashMap<String, Object>> projects = new ArrayList<>();
	private ArrayList<String> all_files = new ArrayList<>();
	
	private LinearLayout linear2;
	private ListView listview1;
	private LinearLayout linear_nopro;
	private ImageView imageview2;
	private TextView textview2;
	
	private Intent i = new Intent();
	private AlertDialog.Builder confirmation;
	private SharedPreferences file;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.projects);
		initialize(_savedInstanceState);
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
		}
		else {
			initializeLogic();
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		_toolbar = (Toolbar) findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		_fab = (FloatingActionButton) findViewById(R.id._fab);
		
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		listview1 = (ListView) findViewById(R.id.listview1);
		linear_nopro = (LinearLayout) findViewById(R.id.linear_nopro);
		imageview2 = (ImageView) findViewById(R.id.imageview2);
		textview2 = (TextView) findViewById(R.id.textview2);
		confirmation = new AlertDialog.Builder(this);
		file = getSharedPreferences("file", Activity.MODE_PRIVATE);
		
		listview1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				confirmation.setTitle("Confirm Delete?");
				confirmation.setMessage("Are you sure, you wanna delete this project? It also deleted the whole code");
				confirmation.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						projects.remove((int)(_position));
						((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
					}
				});
				confirmation.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				confirmation.create().show();
				return true;
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_create_dialog();
			}
		});
	}
	private void initializeLogic() {
		textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		getWindow().setNavigationBarColor(0xFF000000);
		getWindow().setStatusBarColor(0xFF000000);
		getSupportActionBar().setElevation(0);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.parseColor("#000000")));
		setTheme(android.R.style.Theme_Material);
	}
	@Override
		public boolean onCreateOptionsMenu(Menu menu){
				menu.add(0, 0, 0, "Create Project");
				menu.add(0, 1, 0, "Settings");
		     menu.add(0, 2, 0, "Exit");
				return super.onCreateOptionsMenu(menu);
	}
	@Override
	  public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
			
			   case 0:
			
			_create_dialog();
			
			   return true;
				case 1:
			
			_settings();
			
			   return true;
			   case 2:
			
			_exit();
			
			   return true;
			
		}
		
		return true;
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (!file.getString("path", "").equals("")) {
			if (FileUtil.isExistFile(file.getString("path", "").trim())) {
				_refresh();
			}
			else {
				SketchwareUtil.showMessage(getApplicationContext(), "Error: Can't find projects! path doesn't exists");
			}
		}
	}
	private void _create_dialog () {
		final AlertDialog create = new AlertDialog.Builder(ProjectsActivity.this).create();
		View options = getLayoutInflater().inflate(R.layout.create_file,null); 
		create.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		create.setView(options);
		TextView ok = (TextView) options.findViewById(R.id.textview3);
		
		TextView cancel = (TextView) options.findViewById(R.id.textview4);
		
		TextView title = (TextView) options.findViewById(R.id.textview1);
		
		TextView message = (TextView) options.findViewById(R.id.textview2);
		
		LinearLayout bg = (LinearLayout) options.findViewById(R.id.linear1);
		
		final EditText input = (EditText) options.findViewById(R.id.edittext1);
		input.setFocusable(true);
		input.setFocusableInTouchMode(true);
		title.setText("Create Project");
		message.setText("Enter your project name to continue, when you click create, you would be redireced to pick folder");
		input.setHint("Project Name");
		title.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		message.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		ok.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		cancel.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		input.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFF212121);
			bg.setElevation(d*10);
			bg.setBackground(SketchUi);
		}
		ok.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				if (!input.getText().toString().equals("")) {
					name = input.getText().toString();
					create.dismiss();
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "Empty File Name");
				}
			} });
		cancel.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				create.dismiss();
			} });
		create.show();
	}
	
	
	private void _settings () {
		i.setClass(getApplicationContext(), SettingsActivity.class);
		startActivity(i);
		finish();
	}
	
	
	private void _exit () {
		finishAffinity();
	}
	
	
	private void _refresh () {
		add_project.clear();
		all_files.clear();
		FileUtil.listDir(file.getString("path", ""), all_files);
		pos_af = 0;
		for(int _repeat13 = 0; _repeat13 < (int)(all_files.size()); _repeat13++) {
			if (FileUtil.isDirectory(all_files.get((int)(pos_af)))) {
				add_project = new HashMap<>();
				add_project.put("name", Uri.parse(all_files.get((int)(pos_af))).getLastPathSegment());
				add_project.put("path", all_files.get((int)(pos_af)));
				projects.add(add_project);
			}
			pos_af++;
		}
		listview1.setAdapter(new Listview1Adapter(projects));
		((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
	}
	
	
	public class Listview1Adapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		@Override
		public View getView(final int _position, View _view, ViewGroup _viewGroup) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _v = _view;
			if (_v == null) {
				_v = _inflater.inflate(R.layout.project_item, null);
			}
			
			final LinearLayout linear1 = (LinearLayout) _v.findViewById(R.id.linear1);
			final LinearLayout linear2 = (LinearLayout) _v.findViewById(R.id.linear2);
			final TextView textview1 = (TextView) _v.findViewById(R.id.textview1);
			final TextView textview2 = (TextView) _v.findViewById(R.id.textview2);
			
			{
				android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
				int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
				SketchUi.setColor(0xFF212121);
				SketchUi.setCornerRadius(d*360);
				android.graphics.drawable.RippleDrawable SketchUiRD = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{0xFFEEEEEE}), SketchUi, null);
				linear1.setBackground(SketchUiRD);
				linear1.setClickable(true);
			}
			textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
			textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
			textview1.setText(_data.get((int)_position).get("name").toString());
			textview2.setText(_data.get((int)_position).get("path").toString());
			linear1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					i.setClass(getApplicationContext(), CodeActivity.class);
					startActivity(i);
					finish();
				}
			});
			
			return _v;
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}
	
}
