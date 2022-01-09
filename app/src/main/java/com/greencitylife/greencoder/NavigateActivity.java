package com.greencitylife.greencoder;

import androidx.appcompat.app.AppCompatActivity;
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
import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.AdapterView;
import android.view.View;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class NavigateActivity extends AppCompatActivity {
	
	
	private String directory = "";
	private String newfile = "";
	private HashMap<String, Object> add_files = new HashMap<>();
	private double pos_ls = 0;
	private String finalpath = "";
	private String path = "";
	private String newfilepath = "";
	
	private ArrayList<String> files_ls = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> files = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout linear2;
	private ListView listview1;
	private LinearLayout linear3;
	private ImageView imageview2;
	private TextView textview1;
	
	private Intent i = new Intent();
	private SharedPreferences file;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.navigate);
		initialize(_savedInstanceState);
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
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
		
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		listview1 = (ListView) findViewById(R.id.listview1);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		imageview2 = (ImageView) findViewById(R.id.imageview2);
		textview1 = (TextView) findViewById(R.id.textview1);
		file = getSharedPreferences("file", Activity.MODE_PRIVATE);
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				if (files.get((int)_position).get("type").toString().equals("folder")) {
					path = files.get((int)_position).get("path").toString();
					_Refresh();
				}
				else {
					finalpath = files.get((int)_position).get("path").toString();
					i.putExtra("navigate", "true");
					file.edit().putString("filepath", finalpath).commit();
					file.edit().putString("directory", directory).commit();
					i.setClass(getApplicationContext(), CodeActivity.class);
					startActivity(i);
					finish();
					overridePendingTransition(0,0);
				}
			}
		});
		
		imageview2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_create_dialog();
			}
		});
	}
	private void initializeLogic() {
		textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		getWindow().setNavigationBarColor(0xFF212121);
		newfile = "Hello World!";
		directory = file.getString("navigation", "");
		path = file.getString("navigation", "");
		textview1.setText(directory);
		try {
			_Refresh();
		} catch (Exception e) {
			SketchwareUtil.showMessage(getApplicationContext(), "Error: Failed to retrive path!");
		}
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	private void _Refresh () {
		add_files.clear();
		files_ls.clear();
		files.clear();
		FileUtil.listDir(path, files_ls);
		pos_ls = 0;
		for(int _repeat12 = 0; _repeat12 < (int)(files_ls.size()); _repeat12++) {
			if (FileUtil.isDirectory(files_ls.get((int)(pos_ls)))) {
				add_files = new HashMap<>();
				add_files.put("type", "folder");
				add_files.put("name", Uri.parse(files_ls.get((int)(pos_ls))).getLastPathSegment());
				add_files.put("path", files_ls.get((int)(pos_ls)));
				files.add(add_files);
			}
			else {
				add_files = new HashMap<>();
				add_files.put("type", "file");
				add_files.put("name", Uri.parse(files_ls.get((int)(pos_ls))).getLastPathSegment());
				add_files.put("path", files_ls.get((int)(pos_ls)));
				files.add(add_files);
			}
			pos_ls++;
		}
		listview1.setAdapter(new Listview1Adapter(files));
		((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
	}
	
	
	private void _create_dialog () {
		final AlertDialog create = new AlertDialog.Builder(NavigateActivity.this).create();
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
				create.dismiss();
				if (!input.getText().toString().equals("")) {
					newfilepath = path.concat("/".concat(input.getText().toString()));
					FileUtil.writeFile(newfilepath, newfile);
					_Refresh();
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "Empty File Name");
				}
			} });
		cancel.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				create.dismiss();
			} });
		create.show();
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
				_v = _inflater.inflate(R.layout.navigate_item, null);
			}
			
			final LinearLayout linear1 = (LinearLayout) _v.findViewById(R.id.linear1);
			final LinearLayout linear2 = (LinearLayout) _v.findViewById(R.id.linear2);
			final ImageView imageview1 = (ImageView) _v.findViewById(R.id.imageview1);
			final TextView textview1 = (TextView) _v.findViewById(R.id.textview1);
			
			textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
			textview1.setText(_data.get((int)_position).get("name").toString());
			if (_data.get((int)_position).get("type").toString().equals("folder")) {
				imageview1.setImageResource(R.drawable.folder_icon);
			}
			else {
				imageview1.setImageResource(R.drawable.file_icon);
			}
			
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
