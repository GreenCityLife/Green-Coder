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
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class ProjectsActivity extends AppCompatActivity {
	
	public final int REQ_CD_IMPORTS = 101;
	public final int REQ_CD_DIRECTORYCHOOSER = 102;
	
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
	
	private ArrayList<HashMap<String, Object>> projects = new ArrayList<>();
	private ArrayList<String> all_files = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> index_json = new ArrayList<>();
	
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
		
		listview1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				delete_path = projects.get((int)_position).get("path").toString();
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
				SketchwareUtil.showMessage(getApplicationContext(), "Error: Importing projects, Unsupported Zip file.");
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
				if (!add_path.contains(file.getString("path", ""))) {
					SketchwareUtil.showMessage(getApplicationContext(), "Error: Can't add project, Directory not located in your project's path?");
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "Will be soon!");
				}
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
					add_project = new HashMap<>();
					add_project.put("name", Uri.parse(all_files.get((int)(pos_af))).getLastPathSegment());
					add_project.put("path", all_files.get((int)(pos_af)));
					projects.add(add_project);
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
		
		ImageView i1 = (ImageView) options.findViewById(R.id.imageview1);
		
		ImageView i2 = (ImageView) options.findViewById(R.id.imageview2);
		
		ImageView i3 = (ImageView) options.findViewById(R.id.imageview3);
		
		LinearLayout l1 = (LinearLayout) options.findViewById(R.id.linear1);
		
		LinearLayout l2 = (LinearLayout) options.findViewById(R.id.linear2);
		
		LinearLayout l3 = (LinearLayout) options.findViewById(R.id.linear3);
		t1.setText("Create New Project");
		t2.setText("Import (or) Restore Project");
		t3.setText("Add a non-green coder project");
		t1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		t2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		t3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
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
		
		ImageView i1 = (ImageView) option.findViewById(R.id.imageview1);
		
		ImageView i2 = (ImageView) option.findViewById(R.id.imageview2);
		
		ImageView i3 = (ImageView) option.findViewById(R.id.imageview3);
		
		LinearLayout l1 = (LinearLayout) option.findViewById(R.id.linear1);
		
		LinearLayout l2 = (LinearLayout) option.findViewById(R.id.linear2);
		
		LinearLayout l3 = (LinearLayout) option.findViewById(R.id.linear3);
		t1.setText("Backup (or) Export");
		t2.setText("Hide");
		t3.setText("Delete");
		i1.setImageResource(R.drawable.ic_backup);
		i2.setImageResource(R.drawable.ic_hide);
		i3.setImageResource(R.drawable.ic_delete);
		t1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		t2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		t3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
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
		l1.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				pro_opt.dismiss();
				_backup();
			} });
		l2.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				pro_opt.dismiss();
				startActivityForResult(imports, REQ_CD_IMPORTS);
			} });
		l3.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				pro_opt.dismiss();
				_delete(delete_path);
			} });
		pro_opt.show();
	}
	
	
	private void _backup () {
		
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
