package com.greencitylife.greencoder;

import android.app.Activity;
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
import android.content.ClipData;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.AdapterView;
import android.graphics.Typeface;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.Manifest;
import android.content.pm.PackageManager;

public class ProjectsActivity extends Activity {
	
	public final int REQ_CD_DIRECTORYCHOOSER = 101;
	
	private HashMap<String, Object> add_project = new HashMap<>();
	private String name = "";
	private String path = "";
	private String default_settings = "";
	
	private ArrayList<HashMap<String, Object>> projects = new ArrayList<>();
	
	private LinearLayout linear2;
	private ListView listview1;
	private LinearLayout linear_nopro;
	private ImageView imageview2;
	private TextView textview2;
	
	private Intent directoryChooser = new Intent(Intent.ACTION_GET_CONTENT);
	private SharedPreferences sp;
	private Intent i = new Intent();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.projects);
		initialize(_savedInstanceState);
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
			|| checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
			}
			else {
				initializeLogic();
			}
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
		
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		listview1 = (ListView) findViewById(R.id.listview1);
		linear_nopro = (LinearLayout) findViewById(R.id.linear_nopro);
		imageview2 = (ImageView) findViewById(R.id.imageview2);
		textview2 = (TextView) findViewById(R.id.textview2);
		directoryChooser.setType("*/*");
		directoryChooser.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		sp = getSharedPreferences("sp", Activity.MODE_PRIVATE);
		
		listview1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				projects.remove((int)(_position));
				((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
				return true;
			}
		});
	}
	private void initializeLogic() {
		
		textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		getWindow().setNavigationBarColor(0xFF000000);
		getWindow().setStatusBarColor(0xFF000000);
		getActionBar().setElevation(0);
		getActionBar().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.parseColor("#000000")));
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
			case REQ_CD_DIRECTORYCHOOSER:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				Uri uri = _data.getData();
				path = FileUtil.getExternalStorageDir() + "/" + getDocumentPathFromTreeUri(_data.getData());
				if (!FileUtil.isExistFile(path)) {
					SketchwareUtil.showMessage(getApplicationContext(), "Error when creating new project");
				}
				else {
					add_project = new HashMap<>();
					add_project.put("name", name);
					add_project.put("path", path);
					FileUtil.writeFile(path.concat("/index.json"), default_settings);
					projects.add(add_project);
					sp.edit().putString("projectbackup", new Gson().toJson(projects)).commit();
					((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
				}
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (!sp.getString("projectbackup", "").equals("")) {
			linear_nopro.setVisibility(View.GONE);
			listview1.setVisibility(View.VISIBLE);
			projects = new Gson().fromJson(sp.getString("projectbackup", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			listview1.setAdapter(new Listview1Adapter(projects));
			((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
		}
		else {
			listview1.setVisibility(View.GONE);
			linear_nopro.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		sp.edit().putString("projectbackup", new Gson().toJson(projects)).commit();
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
					_chooseDirectory();
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
	
	
	private void _chooseDirectory () {
		directoryChooser = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
		directoryChooser.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		directoryChooser.addCategory(Intent.CATEGORY_DEFAULT);
		startActivityForResult(Intent.createChooser(directoryChooser,"Choose Directory"), REQ_CD_DIRECTORYCHOOSER);
	}
	private String getFileName(Uri uri) throws IllegalArgumentException { 
		// Obtain a cursor with information regarding this uri
		android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		 if (cursor.getCount() <= 0) { 
			cursor.close(); 
			throw new IllegalArgumentException("Can't obtain file name, cursor is empty"); 
		} 
		cursor.moveToFirst(); 
		String fileName = cursor.getString(cursor.getColumnIndexOrThrow(android.provider.OpenableColumns.DISPLAY_NAME)); 
		cursor.close(); 
		return fileName;
		 }
	
	private String getRealPathFromURI(Uri contentURI) { 
		String result = "";
		 android.database.Cursor cursor = getContentResolver().query(contentURI, null, null, null, null); 
		if (cursor == null) { 
			// Source is Dropbox or other similar local file path result = contentURI.getPath(); 
		} else { cursor.moveToFirst(); int idx = cursor.getColumnIndex(android.provider.MediaStore.Images.ImageColumns.DATA);
			 result = cursor.getString(idx);
			 cursor.close(); 
		} return result; 
	}
	private String getDocumentPathFromTreeUri(final Uri treeUri) {
		 final String docId = android.provider. DocumentsContract.getTreeDocumentId(treeUri); 
		final String[] split = docId.split(":");
		 if ((split.length >= 2) && (split[1] != null)) { 
			return split[1]; 
		} else { 
			return java.io.File.separator; 
		} 
	}
	
	
	private void _settings () {
		i.setClass(getApplicationContext(), SettingsActivity.class);
		startActivity(i);
		finish();
	}
	
	
	private void _exit () {
		finishAffinity();
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
