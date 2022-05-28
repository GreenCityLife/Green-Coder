package com.greencitylife.greencoder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Switch;
import android.content.Intent;
import android.net.Uri;
import android.content.ClipData;
import android.app.Activity;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class SettingsActivity extends AppCompatActivity {
	
	public final int REQ_CD_DIRECTORYCHOOSER = 101;
	public final int REQ_CD_FONT = 102;
	
	private Toolbar _toolbar;
	private String projects_path = "";
	private String versionName = "";
	private String versionCode = "";
	private boolean is_debug_log_enabled = false;
	private String fontpath = "";
	
	private ArrayList<HashMap<String, Object>> api_list = new ArrayList<>();
	private ArrayList<String> font_sizes = new ArrayList<>();
	
	private ScrollView vscroll1;
	private LinearLayout linear1;
	private TextView textview1;
	private LinearLayout project_path;
	private LinearLayout debug_log;
	private TextView textview22;
	private LinearLayout font_path;
	private LinearLayout theme;
	private TextView textview4;
	private LinearLayout report_bug;
	private LinearLayout git_repo;
	private LinearLayout help;
	private LinearLayout force_crash;
	private TextView textview13;
	private LinearLayout version;
	private LinearLayout clog;
	private TextView textview2;
	private TextView textview3;
	private LinearLayout linear3;
	private Switch switch1;
	private TextView textview18;
	private TextView textview19;
	private TextView textview20;
	private TextView textview21;
	private TextView textview23;
	private TextView textview24;
	private TextView textview5;
	private TextView textview6;
	private TextView textview7;
	private TextView textview8;
	private TextView textview9;
	private TextView textview10;
	private TextView textview11;
	private TextView textview12;
	private TextView textview14;
	private TextView textview15;
	private TextView textview16;
	private TextView textview17;
	
	private Intent i = new Intent();
	private Intent directoryChooser = new Intent(Intent.ACTION_GET_CONTENT);
	private SharedPreferences file;
	private RequestNetwork api;
	private RequestNetwork.RequestListener _api_request_listener;
	private AlertDialog.Builder dialog;
	private Intent font = new Intent(Intent.ACTION_GET_CONTENT);
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.settings);
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
		vscroll1 = (ScrollView) findViewById(R.id.vscroll1);
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		textview1 = (TextView) findViewById(R.id.textview1);
		project_path = (LinearLayout) findViewById(R.id.project_path);
		debug_log = (LinearLayout) findViewById(R.id.debug_log);
		textview22 = (TextView) findViewById(R.id.textview22);
		font_path = (LinearLayout) findViewById(R.id.font_path);
		theme = (LinearLayout) findViewById(R.id.theme);
		textview4 = (TextView) findViewById(R.id.textview4);
		report_bug = (LinearLayout) findViewById(R.id.report_bug);
		git_repo = (LinearLayout) findViewById(R.id.git_repo);
		help = (LinearLayout) findViewById(R.id.help);
		force_crash = (LinearLayout) findViewById(R.id.force_crash);
		textview13 = (TextView) findViewById(R.id.textview13);
		version = (LinearLayout) findViewById(R.id.version);
		clog = (LinearLayout) findViewById(R.id.clog);
		textview2 = (TextView) findViewById(R.id.textview2);
		textview3 = (TextView) findViewById(R.id.textview3);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		switch1 = (Switch) findViewById(R.id.switch1);
		textview18 = (TextView) findViewById(R.id.textview18);
		textview19 = (TextView) findViewById(R.id.textview19);
		textview20 = (TextView) findViewById(R.id.textview20);
		textview21 = (TextView) findViewById(R.id.textview21);
		textview23 = (TextView) findViewById(R.id.textview23);
		textview24 = (TextView) findViewById(R.id.textview24);
		textview5 = (TextView) findViewById(R.id.textview5);
		textview6 = (TextView) findViewById(R.id.textview6);
		textview7 = (TextView) findViewById(R.id.textview7);
		textview8 = (TextView) findViewById(R.id.textview8);
		textview9 = (TextView) findViewById(R.id.textview9);
		textview10 = (TextView) findViewById(R.id.textview10);
		textview11 = (TextView) findViewById(R.id.textview11);
		textview12 = (TextView) findViewById(R.id.textview12);
		textview14 = (TextView) findViewById(R.id.textview14);
		textview15 = (TextView) findViewById(R.id.textview15);
		textview16 = (TextView) findViewById(R.id.textview16);
		textview17 = (TextView) findViewById(R.id.textview17);
		directoryChooser.setType("*/*");
		directoryChooser.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		file = getSharedPreferences("file", Activity.MODE_PRIVATE);
		api = new RequestNetwork(this);
		dialog = new AlertDialog.Builder(this);
		font.setType("font/ttf");
		font.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		
		project_path.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_chooseDirectory();
			}
		});
		
		debug_log.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				is_debug_log_enabled = switch1.isChecked();
				if (is_debug_log_enabled) {
					switch1.setChecked(false);
				}
				else {
					switch1.setChecked(true);
				}
			}
		});
		
		font_path.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_Font_picker();
			}
		});
		
		theme.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				i.setAction(Intent.ACTION_VIEW);
				i.setClass(getApplicationContext(), ThemeActivity.class);
				startActivity(i);
				finish();
			}
		});
		
		report_bug.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				i.setAction(Intent.ACTION_VIEW);
				i.setData(Uri.parse("https://github.com/GreenCityLife/Green-Coder/issues"));
				startActivity(i);
			}
		});
		
		git_repo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				i.setAction(Intent.ACTION_VIEW);
				i.setData(Uri.parse("https://github.com/GreenCityLife/Green-Coder"));
				startActivity(i);
			}
		});
		
		help.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				SketchwareUtil.showMessage(getApplicationContext(), "Coming soon!");
			}
		});
		
		force_crash.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_throwError("Triggered Force Crash");
			}
		});
		
		version.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				api.startRequestNetwork(RequestNetworkController.GET, "https://raw.githubusercontent.com/GreenCityLife/Green-Coder/master/Server/info.json", "A", _api_request_listener);
			}
		});
		
		clog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				api.startRequestNetwork(RequestNetworkController.GET, "https://raw.githubusercontent.com/GreenCityLife/Green-Coder/master/Server/info.json", "B", _api_request_listener);
			}
		});
		
		_api_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _response = _param2;
				try {
					api_list = new Gson().fromJson(_response, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
					if (_tag.equals("A")) {
						if (versionName.equals(api_list.get((int)0).get("latest_version").toString())) {
							dialog.setTitle("Version");
							dialog.setMessage("You're using latest version. No worries!");
							dialog.setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface _dialog, int _which) {
									
								}
							});
							dialog.create().show();
						}
						else {
							dialog.setTitle("Version");
							dialog.setMessage("You're either using old version or  early build version! If you're using old version, consider updating to latest version!");
							dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface _dialog, int _which) {
									i.setAction(Intent.ACTION_VIEW);
									i.setData(Uri.parse(api_list.get((int)0).get("download_link").toString()));
									startActivity(i);
								}
							});
							
							dialog.create().show();
						}
					}
					if (_tag.equals("B")) {
						dialog.setTitle("Changelog");
						dialog.setMessage(api_list.get((int)0).get("changelog").toString());
						dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								
							}
						});
						dialog.create().show();
					}
				} catch (Exception e) {
					SketchwareUtil.showMessage(getApplicationContext(), "invalid json request!");
				}
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				SketchwareUtil.showMessage(getApplicationContext(), _message);
			}
		};
	}
	private void initializeLogic() {
		_Font();
		getWindow().setNavigationBarColor(0xFF212121);
		setTitle("Settings");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		_rippleRoundStroke(project_path, "#212121", "#e0e0e0", 0, 0, "#212121");
		_rippleRoundStroke(report_bug, "#212121", "#e0e0e0", 0, 0, "#212121");
		_rippleRoundStroke(git_repo, "#212121", "#e0e0e0", 0, 0, "#212121");
		_rippleRoundStroke(help, "#212121", "#e0e0e0", 0, 0, "#212121");
		_rippleRoundStroke(force_crash, "#212121", "#e0e0e0", 0, 0, "#212121");
		_rippleRoundStroke(version, "#212121", "#e0e0e0", 0, 0, "#212121");
		_rippleRoundStroke(clog, "#212121", "#e0e0e0", 0, 0, "#212121");
		_rippleRoundStroke(debug_log, "#212121", "#e0e0e0", 0, 0, "#212121");
		_rippleRoundStroke(font_path, "#212121", "#e0e0e0", 0, 0, "#212121");
		_rippleRoundStroke(theme, "#212121", "#e0e0e0", 0, 0, "#212121");
		String versionName = "null";
		int versionCode = -1;
		try {
			android.content.pm.PackageInfo packageInfo = SettingsActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
			versionName = packageInfo.versionName;
			versionCode = packageInfo.versionCode;
		} catch (android.content.pm.PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		versionName = "0.1.4 alpha";
		textview15.setText(versionName);
		setTheme(android.R.style.Theme_Material);
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
				projects_path = uri.getPath();
				projects_path = projects_path.replace("tree", "storage");
				projects_path = projects_path.replace(":", "/");
				projects_path = projects_path.replace("primary", "emulated/0");
				if (FileUtil.isExistFile(projects_path)) {
					textview3.setText(projects_path);
					file.edit().putString("path", projects_path).commit();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "Error: Path doesn't exist?");
				}
			}
			else {
				
			}
			break;
			
			case REQ_CD_FONT:
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
				fontpath = uri.getPath();
				fontpath = fontpath.replace("document", "storage");
				fontpath = fontpath.replace(":", "/");
				fontpath = fontpath.replace("primary", "emulated/0");
				if (FileUtil.isExistFile(fontpath)) {
					file.edit().putString("editor_font", fontpath).commit();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "Error: Can't find path!");
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
	public void onBackPressed() {
		i.setClass(getApplicationContext(), ProjectsActivity.class);
		startActivity(i);
		finish();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (!file.getString("path", "").equals("")) {
			projects_path = file.getString("path", "");
			textview3.setText(projects_path);
		}
		if (!file.getString("debug", "").equals("")) {
			if (file.getString("debug", "").equals("true")) {
				switch1.setChecked(true);
			}
			else {
				switch1.setChecked(false);
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		is_debug_log_enabled = switch1.isChecked();
		if (is_debug_log_enabled) {
			file.edit().putString("debug", "true").commit();
		}
		else {
			file.edit().putString("debug", "false").commit();
		}
	}
	private void _Font () {
		textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview4.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview13.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview22.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		textview5.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview6.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		textview7.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview8.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		textview9.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview10.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		textview11.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview12.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		textview14.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview15.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		textview16.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview17.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		textview18.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview19.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		textview20.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview21.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		textview23.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview24.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
	}
	
	
	private void _rippleRoundStroke (final View _view, final String _focus, final String _pressed, final double _round, final double _stroke, final String _strokeclr) {
		android.graphics.drawable.GradientDrawable GG = new android.graphics.drawable.GradientDrawable();
		GG.setColor(Color.parseColor(_focus));
		GG.setCornerRadius((float)_round);
		GG.setStroke((int) _stroke,
		Color.parseColor("#" + _strokeclr.replace("#", "")));
		android.graphics.drawable.RippleDrawable RE = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{ Color.parseColor(_pressed)}), GG, null);
		_view.setBackground(RE);
	}
	
	
	private void _throwError (final String _text) {
		throw new IllegalStateException(_text);
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
	
	
	private void _Font_picker () {
		final com.google.android.material.bottomsheet.BottomSheetDialog create_opt = new com.google.android.material.bottomsheet.BottomSheetDialog(SettingsActivity.this);
		
		View options;
		options = getLayoutInflater().inflate(R.layout.option,null );
		create_opt.setContentView(options);
		
		create_opt.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
		TextView t1 = (TextView) options.findViewById(R.id.textview1);
		
		TextView t2 = (TextView) options.findViewById(R.id.textview2);
		
		ImageView i1 = (ImageView) options.findViewById(R.id.imageview1);
		
		ImageView i2 = (ImageView) options.findViewById(R.id.imageview2);
		
		LinearLayout l1 = (LinearLayout) options.findViewById(R.id.linear1);
		
		LinearLayout l2 = (LinearLayout) options.findViewById(R.id.linear2);
		
		LinearLayout l3 = (LinearLayout) options.findViewById(R.id.linear3);
		
		LinearLayout l4 = (LinearLayout) options.findViewById(R.id.linear4);
		
		LinearLayout l5 = (LinearLayout) options.findViewById(R.id.linear5);
		
		t1.setText("Custom Font");
		t2.setText("Default Font");
		t1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		t2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		l3.setVisibility(View.GONE);
		l4.setVisibility(View.GONE);
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
		l1.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				create_opt.dismiss();
				startActivityForResult(font, REQ_CD_FONT);
			} });
		l2.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				create_opt.dismiss();
				file.edit().putString("editor_font", "default").commit();
			} });
		create_opt.show();
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
