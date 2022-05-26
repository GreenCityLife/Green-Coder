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
import android.content.ClipData;
import android.widget.AdapterView;
import android.view.View;
import com.google.gson.Gson;
import android.graphics.Typeface;
import com.google.gson.reflect.TypeToken;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class ProjectsActivity extends AppCompatActivity {
	
	public final int REQ_CD_IMPORTS = 101;
	public final int REQ_CD_DIRECTORYCHOOSER = 102;
	public final int REQ_CD_QUICK = 103;
	
	private Toolbar _toolbar;
	private FloatingActionButton _fab;
	private HashMap<String, Object> add_project = new HashMap<>();
	private String name = "";
	private String path = "";
	private String default_settings = "";
	private double pos_af = 0;
	private String import_path = "";
	private String add_path = "";
	private String new_project_path = "";
	private String delete_path = "";
	private String function_path = "";
	private String quick_path = "";
	private String function_name = "";
	private double randomness = 0;
	private String remove_path = "";
	
	private ArrayList<HashMap<String, Object>> projects = new ArrayList<>();
	private ArrayList<String> all_files = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> index_json = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> random = new ArrayList<>();
	
	private LinearLayout linear2;
	private ListView listview1;
	private LinearLayout linear_nopro;
	private ImageView imageview2;
	private TextView textview2;
	
	private Intent i = new Intent();
	private AlertDialog.Builder confirmation;
	private SharedPreferences file;
	private Intent imports = new Intent(Intent.ACTION_GET_CONTENT);
	private Intent directoryChooser = new Intent(Intent.ACTION_GET_CONTENT);
	private Intent quick = new Intent(Intent.ACTION_GET_CONTENT);
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.projects);
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
		imports.setType("application/zip");
		imports.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		directoryChooser.setType("*/*");
		directoryChooser.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		quick.setType("*/*");
		quick.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		
		listview1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				function_path = projects.get((int)_position).get("path").toString();
				function_name = projects.get((int)_position).get("name").toString();
				_project_option();
				return true;
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_create_option();
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
		if (FileUtil.isExistFile("/storage/emulated/0/.gncode/splash_screen.json")) {
			random = new Gson().fromJson(FileUtil.readFile("/storage/emulated/0/.gncode/splash_screen.json"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			randomness = SketchwareUtil.getRandom((int)(0), (int)(6));
			_Subtitle(random.get((int)randomness).get(String.valueOf((long)(randomness))).toString());
		}
		else {
			_Subtitle("Missingno");
		}
		setTheme(android.R.style.Theme_Material);
		if (!file.getString("path", "").equals("")) {
			if (FileUtil.isExistFile(file.getString("path", "").trim())) {
				_refresh();
			}
			else {
				SketchwareUtil.showMessage(getApplicationContext(), "Error: Can't find projects! path doesn't exists");
			}
		}
	}
	@Override
		public boolean onCreateOptionsMenu(Menu menu){
				menu.add(0, 0, 0, "Create Project");
				menu.add(0, 1, 0, "Settings");
		     menu.add(0, 2, 0, "Exit");
		     menu.add(0, 3, 0, "Refresh").setIcon(R.drawable.ic_refresh_white).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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
			   case 3:
			
			_refresher();
			
			   return true;
			
		}
		
		return true;
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_IMPORTS:
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
				import_path = uri.getPath();
				import_path = import_path.replace("document", "storage");
				import_path = import_path.replace(":", "/");
				import_path = import_path.replace("primary", "emulated/0");
				SketchwareUtil.showMessage(getApplicationContext(), "Coming Soon! For now, you need to manually unzip");
			}
			else {
				
			}
			break;
			
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
				add_path = uri.getPath();
				add_path = add_path.replace("tree", "storage");
				add_path = add_path.replace(":", "/");
				add_path = add_path.replace("primary", "emulated/0");
				if (!add_path.contains(file.getString("path", ""))) {
					SketchwareUtil.showMessage(getApplicationContext(), "Error: Can't add project, Directory not located in your project's path?");
				}
				else {
					FileUtil.writeFile(add_path.concat("/index.txt"), "Hello World!");
					index_json.clear();
					{
						HashMap<String, Object> _item = new HashMap<>();
						_item.put("title", "index.txt");
						index_json.add(_item);
					}
					
					{
						HashMap<String, Object> _item = new HashMap<>();
						_item.put("visible", "true");
						index_json.add(_item);
					}
					
					{
						HashMap<String, Object> _item = new HashMap<>();
						_item.put("opening_file", add_path.concat("/index.txt"));
						index_json.add(_item);
					}
					
					{
						HashMap<String, Object> _item = new HashMap<>();
						_item.put("directory", add_path);
						index_json.add(_item);
					}
					
					default_settings = new Gson().toJson(index_json);
					FileUtil.writeFile(add_path.concat("/.gncode/index.json"), default_settings);
					_refresher();
				}
			}
			else {
				
			}
			break;
			
			case REQ_CD_QUICK:
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
				quick_path = uri.getPath();
				quick_path = quick_path.replace("document", "storage");
				quick_path = quick_path.replace(":", "/");
				i.putExtra("navigate", "quick");
				i.putExtra("file_path", quick_path);
				i.setClass(getApplicationContext(), CodeActivity.class);
				startActivity(i);
				finish();
			}
			else {
				
			}
			break;
			default:
			break;
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
		message.setText("This is will create a new folder in your project's path");
		input.setHint("Project's Name");
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
					new_project_path = file.getString("path", "").concat("/".concat(name));
					FileUtil.writeFile(new_project_path.concat("/index.txt"), "This is a sample file created by green coder. Make sure to delete or edit or create new file using navigate option");
					{
						HashMap<String, Object> _item = new HashMap<>();
						_item.put("title", "index.txt");
						index_json.add(_item);
					}
					
					{
						HashMap<String, Object> _item = new HashMap<>();
						_item.put("visible", "true");
						index_json.add(_item);
					}
					
					{
						HashMap<String, Object> _item = new HashMap<>();
						_item.put("opening_file", new_project_path.concat("/index.txt"));
						index_json.add(_item);
					}
					
					{
						HashMap<String, Object> _item = new HashMap<>();
						_item.put("directory", new_project_path);
						index_json.add(_item);
					}
					
					default_settings = new Gson().toJson(index_json);
					FileUtil.writeFile(new_project_path.concat("/.gncode/index.json"), default_settings);
					i.setClass(getApplicationContext(), RefreshActivity.class);
					startActivity(i);
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
		FileUtil.listDir(file.getString("path", "").trim(), all_files);
		pos_af = 0;
		for(int _repeat13 = 0; _repeat13 < (int)(all_files.size()); _repeat13++) {
			if (FileUtil.isDirectory(all_files.get((int)(pos_af)))) {
				if (FileUtil.isExistFile(all_files.get((int)(pos_af)).concat("/.gncode/index.json"))) {
					if (FileUtil.readFile(all_files.get((int)(pos_af)).concat("/.gncode/index.json")).contains("\"visible\":\"true\"")) {
						add_project = new HashMap<>();
						add_project.put("name", Uri.parse(all_files.get((int)(pos_af))).getLastPathSegment());
						add_project.put("path", all_files.get((int)(pos_af)));
						projects.add(add_project);
					}
				}
			}
			pos_af++;
		}
		if (projects.size() == 0) {
			linear_nopro.setVisibility(View.VISIBLE);
			listview1.setVisibility(View.GONE);
		}
		else {
			listview1.setAdapter(new Listview1Adapter(projects));
			((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
		}
	}
	
	
	private void _create_option () {
		final com.google.android.material.bottomsheet.BottomSheetDialog create_opt = new com.google.android.material.bottomsheet.BottomSheetDialog(ProjectsActivity.this);
		
		View options;
		options = getLayoutInflater().inflate(R.layout.option,null );
		create_opt.setContentView(options);
		
		create_opt.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
		TextView t1 = (TextView) options.findViewById(R.id.textview1);
		
		TextView t2 = (TextView) options.findViewById(R.id.textview2);
		
		TextView t3 = (TextView) options.findViewById(R.id.textview3);
		
		TextView t4 = (TextView) options.findViewById(R.id.textview4);
		
		ImageView i1 = (ImageView) options.findViewById(R.id.imageview1);
		
		ImageView i2 = (ImageView) options.findViewById(R.id.imageview2);
		
		ImageView i3 = (ImageView) options.findViewById(R.id.imageview3);
		
		ImageView i4 = (ImageView) options.findViewById(R.id.imageview4);
		
		LinearLayout l1 = (LinearLayout) options.findViewById(R.id.linear1);
		
		LinearLayout l2 = (LinearLayout) options.findViewById(R.id.linear2);
		
		LinearLayout l3 = (LinearLayout) options.findViewById(R.id.linear3);
		
		LinearLayout l4 = (LinearLayout) options.findViewById(R.id.linear4);
		
		LinearLayout l5 = (LinearLayout) options.findViewById(R.id.linear5);
		t1.setText("Create New Project");
		t2.setText("Import (or) Restore Project");
		t3.setText("Add a non-green coder project");
		t4.setText("Quick Edit");
		t1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		t2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		t3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		t4.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		l5.setVisibility(View.GONE);
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFF212121);SketchUi.setCornerRadii(new float[]{
				d*40,d*40,d*40 ,d*40,d*0,d*0 ,d*0,d*0});
			l1.setElevation(d*5);
			android.graphics.drawable.RippleDrawable SketchUiRD = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{0xFFE0E0E0}), SketchUi, null);
			l1.setBackground(SketchUiRD);
			l1.setClickable(true);
		}
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFF212121);
			l2.setElevation(d*5);
			android.graphics.drawable.RippleDrawable SketchUiRD = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{0xFFE0E0E0}), SketchUi, null);
			l2.setBackground(SketchUiRD);
			l2.setClickable(true);
		}
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFF212121);
			l3.setElevation(d*5);
			android.graphics.drawable.RippleDrawable SketchUiRD = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{0xFFE0E0E0}), SketchUi, null);
			l3.setBackground(SketchUiRD);
			l3.setClickable(true);
		}
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFF212121);
			l4.setElevation(d*5);
			android.graphics.drawable.RippleDrawable SketchUiRD = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{0xFFE0E0E0}), SketchUi, null);
			l4.setBackground(SketchUiRD);
			l4.setClickable(true);
		}
		l1.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				create_opt.dismiss();
				_create_dialog();
			} });
		l2.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				create_opt.dismiss();
				startActivityForResult(imports, REQ_CD_IMPORTS);
			} });
		l3.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				create_opt.dismiss();
				_chooseDirectory();
			} });
		l4.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				create_opt.dismiss();
				startActivityForResult(quick, REQ_CD_QUICK);
			} });
		create_opt.show();
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
	
	
	private void _refresher () {
		i.setClass(getApplicationContext(), RefreshActivity.class);
		startActivity(i);
		finish();
		overridePendingTransition(0,0);
	}
	
	
	private void _project_option () {
		final com.google.android.material.bottomsheet.BottomSheetDialog pro_opt = new com.google.android.material.bottomsheet.BottomSheetDialog(ProjectsActivity.this);
		
		View option;
		option = getLayoutInflater().inflate(R.layout.option,null );
		pro_opt.setContentView(option);
		
		pro_opt.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
		TextView t1 = (TextView) option.findViewById(R.id.textview1);
		
		TextView t2 = (TextView) option.findViewById(R.id.textview2);
		
		TextView t3 = (TextView) option.findViewById(R.id.textview3);
		
		TextView t4 = (TextView) option.findViewById(R.id.textview4);
		
		TextView t5 = (TextView) option.findViewById(R.id.textview5);
		
		ImageView i1 = (ImageView) option.findViewById(R.id.imageview1);
		
		ImageView i2 = (ImageView) option.findViewById(R.id.imageview2);
		
		ImageView i3 = (ImageView) option.findViewById(R.id.imageview3);
		
		ImageView i4 = (ImageView) option.findViewById(R.id.imageview4);
		
		ImageView i5 = (ImageView) option.findViewById(R.id.imageview5);
		
		LinearLayout l1 = (LinearLayout) option.findViewById(R.id.linear1);
		
		LinearLayout l2 = (LinearLayout) option.findViewById(R.id.linear2);
		
		LinearLayout l3 = (LinearLayout) option.findViewById(R.id.linear3);
		
		LinearLayout l4 = (LinearLayout) option.findViewById(R.id.linear4);
		
		LinearLayout l5 = (LinearLayout) option.findViewById(R.id.linear5);
		t1.setText("Open");
		t2.setText("Backup (or) Export");
		t3.setText("Hide");
		t4.setText("Delete");
		t5.setText("Remove");
		i1.setImageResource(R.drawable.ic_open);
		i2.setImageResource(R.drawable.ic_backup);
		i3.setImageResource(R.drawable.ic_hide);
		i4.setImageResource(R.drawable.ic_delete);
		t1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		t2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		t3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		t4.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		t5.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFF212121);SketchUi.setCornerRadii(new float[]{
				d*40,d*40,d*40 ,d*40,d*0,d*0 ,d*0,d*0});
			l1.setElevation(d*5);
			android.graphics.drawable.RippleDrawable SketchUiRD = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{0xFFE0E0E0}), SketchUi, null);
			l1.setBackground(SketchUiRD);
			l1.setClickable(true);
		}
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFF212121);
			l2.setElevation(d*5);
			android.graphics.drawable.RippleDrawable SketchUiRD = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{0xFFE0E0E0}), SketchUi, null);
			l2.setBackground(SketchUiRD);
			l2.setClickable(true);
		}
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFF212121);
			l3.setElevation(d*5);
			android.graphics.drawable.RippleDrawable SketchUiRD = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{0xFFE0E0E0}), SketchUi, null);
			l3.setBackground(SketchUiRD);
			l3.setClickable(true);
		}
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFF212121);
			l4.setElevation(d*5);
			android.graphics.drawable.RippleDrawable SketchUiRD = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{0xFFE0E0E0}), SketchUi, null);
			l4.setBackground(SketchUiRD);
			l4.setClickable(true);
		}
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFF212121);
			l5.setElevation(d*5);
			android.graphics.drawable.RippleDrawable SketchUiRD = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{0xFFE0E0E0}), SketchUi, null);
			l5.setBackground(SketchUiRD);
			l5.setClickable(true);
		}
		l1.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				pro_opt.dismiss();
				i.putExtra("navigate", "false");
				i.putExtra("project_path", function_path);
				i.setClass(getApplicationContext(), CodeActivity.class);
				startActivity(i);
				finish();
			} });
		l2.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				pro_opt.dismiss();
				_backup();
			} });
		l3.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				pro_opt.dismiss();
				_hide_project(function_path);
			} });
		l4.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				pro_opt.dismiss();
				_delete(function_path);
			} });
		l5.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				pro_opt.dismiss();
				_remove(function_path);
			} });
		pro_opt.show();
	}
	
	
	private void _backup () {
		FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/.gncode/Backups/"), "");
		_zipDirectory(function_path, FileUtil.getExternalStorageDir().concat("/.gncode/Backups/").concat(function_name.concat(".zip")));
	}
	
	
	private void _delete (final String _delete_path) {
		confirmation.setTitle("Delete Project?");
		confirmation.setMessage("Are you sure, you want to delete this project? This is irreversible!!!");
		confirmation.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				FileUtil.deleteFile(_delete_path);
				_refresher();
			}
		});
		confirmation.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				
			}
		});
		confirmation.create().show();
	}
	
	
	private void _hide_project (final String _path) {
		confirmation.setTitle("Hide Project?");
		confirmation.setMessage("Are you sure, you want to hide this project? This is reversible though you need to edit some files to unhide again!!");
		confirmation.setPositiveButton("Hide", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				FileUtil.writeFile(function_path.concat("/.gncode/index.json"), FileUtil.readFile(function_path.concat("/.gncode/index.json")).replace("\"visible\":\"true\"", "\"visible\":\"false\""));
				_refresher();
			}
		});
		confirmation.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				
			}
		});
		confirmation.create().show();
	}
	
	
	private void _zipDirectory (final String _sourcePath, final String _destPath) {
		//❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗
		//Needed for zipper more block
		//❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗
		
		verifyStoragePermissions();
		String dir = _sourcePath;
		String zipDirName = _destPath;
		//zipDirectory(dir, zipDirName);
		zd = new ZipDirectory();
		zd.execute(dir, zipDirName);
	}
	ZipDirectory zd;
	
	//ℹ️ Verify Storage Permissions
	public void verifyStoragePermissions() { // Check if we have write permission 
		int permission = androidx.core.app.ActivityCompat.checkSelfPermission(ProjectsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE); 
		if (permission != PackageManager.PERMISSION_GRANTED) { 
			// We don't have permission so prompt the user 
			androidx.core.app.ActivityCompat.requestPermissions(ProjectsActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE }, 101 ); 
		}
	}
	
	class ZipDirectory extends AsyncTask<String, String, Void> {
		public String source = null;
		public String dest = null;
		
		protected void onPreExecute() {
			SketchwareUtil.showMessage(getApplicationContext(), "❗Starting to backup...");
		}
		
		protected Void doInBackground(String... path) {
			this.dest = path[1];
			this.source = path[0];
			zipDirectory(this.source, this.dest);
			return null;
		}
		
		protected void onProgressUpdate(String... values) {
			//ℹ️ Change textview to a textview that will print the log
			SketchwareUtil.showMessage(getApplicationContext(), ("📝 Adding: " + values[0]));
		}
		
		protected void onPostExecute(Void param){
			//ℹ️ Change textview to a textview that will print the log
			SketchwareUtil.showMessage(getApplicationContext(), "✅ Backuped to ".concat(FileUtil.getExternalStorageDir().concat("/.gncode/Backups")));
			
		}
		
		protected void onCancelled(){
			SketchwareUtil.showMessage(getApplicationContext(), "❌ Failed to Backup");
			
		}
		
		List<String> filesListInDir = new ArrayList<String>();
		
		private void zipDirectory(String dir, String zipDirName) {
			try {
				populateFilesList(dir);
				
				java.io.File sFile = new java.io.File(zipDirName);
				sFile.createNewFile();
				java.io.FileOutputStream fos = new java.io.FileOutputStream(zipDirName);
				java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(fos);
				for(String filePath : filesListInDir){
					if (isCancelled()){
						publishProgress((String)"🚫 Canceling!");
						break;
					}
					publishProgress((String)filePath);
					java.util.zip.ZipEntry ze = new java.util.zip.ZipEntry(filePath.substring(dir.length()+1, filePath.length()));
					zos.putNextEntry(ze);
					java.io.FileInputStream fis = new java.io.FileInputStream(filePath);
					byte[] buffer = new byte[1024];
					int len;
					while ((len = fis.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
					zos.closeEntry();
					fis.close();
				}
				zos.close();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
				SketchwareUtil.showMessage(getApplicationContext(), (e.toString()));
				cancel(true);
			}finally{
			}
		}
		
		private void populateFilesList(String dir){
			ArrayList<String> cc = new ArrayList<String>();
			FileUtil.listDir(dir,cc);
			//FileUtil.isDirectory(path);
			for(String filepath : cc){
				if(FileUtil.isFile(filepath)) {
					filesListInDir.add(filepath);
				}else {
					populateFilesList(filepath);
				}
			}
		}
		
	}
	
	
	private void _Subtitle (final String _text) {
		getSupportActionBar().setSubtitle(_text);
	}
	
	
	private void _remove (final String _path) {
		remove_path = _path.concat("/.gncode");
		if (FileUtil.isExistFile(remove_path)) {
			FileUtil.deleteFile(remove_path);
		}
		else {
			SketchwareUtil.showMessage(getApplicationContext(), "Error: Path doesn't exist?");
		}
		_refresher();
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
					i.putExtra("navigate", "false");
					i.putExtra("project_path", _data.get((int)_position).get("path").toString());
					i.setClass(getApplicationContext(), CodeActivity.class);
					startActivity(i);
					finish();
				}
			});
			linear1.setOnLongClickListener(new View.OnLongClickListener() {@Override public boolean onLongClick(View v){
					
					return false;
					
				}});
			
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
