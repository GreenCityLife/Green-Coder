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
	
	private Toolbar _toolbar;
	private String projects_path = "";
	private String versionName = "";
	private String versionCode = "";
	
	private ArrayList<HashMap<String, Object>> api_list = new ArrayList<>();
	
	private ScrollView vscroll1;
	private LinearLayout linear1;
	private TextView textview1;
	private LinearLayout project_path;
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
		
		project_path.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_chooseDirectory();
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
		String versionName = "null";
		int versionCode = -1;
		try {
			android.content.pm.PackageInfo packageInfo = SettingsActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
			versionName = packageInfo.versionName;
			versionCode = packageInfo.versionCode;
		} catch (android.content.pm.PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
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
	}
	private void _Font () {
		textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview4.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview13.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
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
