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
import android.widget.EditText;
import android.content.Intent;
import android.net.Uri;
import android.app.Activity;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class CodeActivity extends AppCompatActivity {
	
	
	private Toolbar _toolbar;
	private String directory = "";
	private String path = "";
	private String editorfont = "";
	private double number = 0;
	
	private ArrayList<HashMap<String, Object>> render_data = new ArrayList<>();
	
	private ScrollView vscroll1;
	private LinearLayout linear1;
	private LinearLayout linear3;
	private EditText edittext1;
	
	private Intent i = new Intent();
	private SharedPreferences file;
	private AlertDialog.Builder confirm;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.code);
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
		vscroll1 = (ScrollView) findViewById(R.id.vscroll1);
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		edittext1 = (EditText) findViewById(R.id.edittext1);
		file = getSharedPreferences("file", Activity.MODE_PRIVATE);
		confirm = new AlertDialog.Builder(this);
	}
	private void initializeLogic() {
		edittext1.setVisibility(View.GONE);
		if (getIntent().getStringExtra("navigate").equals("false")) {
			if (!getIntent().getStringExtra("project_path").equals("")) {
				if (FileUtil.isExistFile(getIntent().getStringExtra("project_path").concat("/.gncode/index.json"))) {
					render_data = new Gson().fromJson(FileUtil.readFile(getIntent().getStringExtra("project_path").concat("/.gncode/index.json")), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
					setTitle(render_data.get((int)0).get("title").toString());
					directory = render_data.get((int)3).get("directory").toString();
					_Subtitle(Uri.parse(directory).getLastPathSegment());
					path = render_data.get((int)2).get("opening_file").toString();
					edittext1.setText(FileUtil.readFile(path));
					edittext1.setVisibility(View.VISIBLE);
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "Error: Path doesn't exist?");
				}
			}
			else {
				SketchwareUtil.showMessage(getApplicationContext(), "Error: Path Empty?");
			}
		}
		else {
			if (getIntent().getStringExtra("navigate").equals("quick")) {
				setTitle(Uri.parse(getIntent().getStringExtra("file_path")).getLastPathSegment());
				_Subtitle(getIntent().getStringExtra("file_path"));
				path = getIntent().getStringExtra("file_path");
				directory = getIntent().getStringExtra("file_path");
				edittext1.setText(FileUtil.readFile(path));
				edittext1.setVisibility(View.VISIBLE);
			}
			else {
				setTitle(Uri.parse(file.getString("filepath", "")).getLastPathSegment());
				_Subtitle(Uri.parse(file.getString("directory", "")).getLastPathSegment());
				path = file.getString("filepath", "");
				directory = file.getString("directory", "");
				edittext1.setText(FileUtil.readFile(path));
				edittext1.setVisibility(View.VISIBLE);
			}
		}
		if (file.getString("editor_font", "").equals("default")) {
			edittext1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/jetbrains_mono_regular.ttf"), 0);
		}
		else {
			editorfont = file.getString("editor_font", "");
			edittext1.setTypeface(Typeface.createFromFile(editorfont));
		}
		_srt(edittext1);
		getWindow().setStatusBarColor(0xFF212121);
		getWindow().setNavigationBarColor(0xFF212121);
		getSupportActionBar().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.parseColor("#212121")));
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		setTheme(android.R.style.Theme_Material);
	}
	@Override
		public boolean onCreateOptionsMenu(Menu menu){
		      menu.add(0, 0, 0, "Save").setIcon(R.drawable.ic_save).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		    menu.add(0, 1, 0, "Navigate").setIcon(R.drawable.ic_navigate).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				return super.onCreateOptionsMenu(menu);
	}
	@Override
	  public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
			
			   case 0:
			
			_savefile();
			
			return true;
			case 1:
			
			_navigate();
			
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
	public void onBackPressed() {
		if (FileUtil.readFile(path).equals(edittext1.getText().toString())) {
			i.setClass(getApplicationContext(), ProjectsActivity.class);
			startActivity(i);
			finish();
		}
		else {
			confirm.setTitle("Changes");
			confirm.setMessage("You made changes to some files and didn't save? Are you sure to leave changes not saved?");
			confirm.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					i.setClass(getApplicationContext(), ProjectsActivity.class);
					startActivity(i);
					finish();
				}
			});
			confirm.setNegativeButton("Save and Exit", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					_savefile();
					i.setClass(getApplicationContext(), ProjectsActivity.class);
					startActivity(i);
					finish();
				}
			});
			confirm.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					
				}
			});
			confirm.create().show();
		}
	}
	private void _savefile () {
		if (!path.equals("")) {
			FileUtil.writeFile(path, edittext1.getText().toString());
			SketchwareUtil.showMessage(getApplicationContext(), "Saved file successfully!");
		}
	}
	
	
	private void _navigate () {
		if (FileUtil.isFile(directory)) {
			SketchwareUtil.showMessage(getApplicationContext(), "Error: Can't navigate when directory is a file!");
		}
		else {
			i.setClass(getApplicationContext(), NavigateActivity.class);
			startActivity(i);
			file.edit().putString("navigation", directory).commit();
			finish();
			overridePendingTransition(0,0);
		}
	}
	
	
	private void _srt (final TextView _view) {
		final String secondaryColor = "#678cb1";
		final String primaryColor = "#86b55a";
		final String numbersColor = "#f6c921";
		final String quotesColor = "#009688";
		final String commentsColor = "#9e9e9e";
		final String charColor = "#ff5722";
		final TextView regex1 = new TextView(this);
		final TextView regex2 = new TextView(this);
		final TextView regex3 = new TextView(this);
		final TextView regex4 = new TextView(this);
		final TextView regex5 = new TextView(this);
		final TextView regex6 = new TextView(this);
		final TextView regex7 = new TextView(this);
		final TextView regex8 = new TextView(this);
		final TextView regex9 = new TextView(this);
		final TextView regex10 = new TextView(this);
		final TextView regex11 = new TextView(this);
		
		regex1.setText("\\b(out|print|println|valueOf|toString|concat|equals|for|while|switch|getText");
		
		regex2.setText("|println|printf|print|out|parseInt|round|sqrt|charAt|compareTo|compareToIgnoreCase|concat|contains|contentEquals|equals|length|toLowerCase|trim|toUpperCase|toString|valueOf|substring|startsWith|split|replace|replaceAll|lastIndexOf|size)\\b");
		
		regex3.setText("\\b(public|private|protected|void|switch|case|class|import|package|extends|Activity|TextView|EditText|LinearLayout|CharSequence|String|int|onCreate|ArrayList|float|if|else|static|Intent|Button|SharedPreferences");
		
		regex4.setText("|abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|implements|import|instanceof|interface|long|native|new|package|private|protected|");
		
		regex5.setText("public|return|short|static|strictfp|super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while|true|false|null)\\b");
		
		regex6.setText("\\b([0-9]+)\\b");
		
		regex7.setText("(\\w+)(\\()+");
		
		regex8.setText("\\@\\s*(\\w+)");
		
		regex9.setText("\"(.*?)\"|'(.*?)'");
		
		regex10.setText("/\\*(?:.|[\\n\\r])*?\\*/|//.*");
		
		regex11.setText("\\b(Uzuakoli|Amoji|Bright|Ndudirim|Ezinwanne|Lightworker|Isuochi|Abia|Ngodo)\\b");
		_view.addTextChangedListener(new TextWatcher() {
			ColorScheme keywords1 = new ColorScheme(java.util.regex.Pattern.compile(regex1.getText().toString().concat(regex2.getText().toString())), Color.parseColor(secondaryColor));
			
			ColorScheme keywords2 = new ColorScheme(java.util.regex.Pattern.compile(regex3.getText().toString().concat(regex4.getText().toString().concat(regex5.getText().toString()))), Color.parseColor(primaryColor));
			
			ColorScheme keywords3 = new ColorScheme(java.util.regex.Pattern.compile(regex6.getText().toString()), Color.parseColor(numbersColor));
			
			ColorScheme keywords4 = new ColorScheme(java.util.regex.Pattern.compile(regex7.getText().toString()), Color.parseColor(secondaryColor));
			
			ColorScheme keywords5 = new ColorScheme(java.util.regex.Pattern.compile(regex9.getText().toString()), Color.parseColor(quotesColor));
			
			ColorScheme keywords6 = new ColorScheme(java.util.regex.Pattern.compile(regex10.getText().toString()), Color.parseColor(commentsColor));
			
			ColorScheme keywords7 = new ColorScheme(java.util.regex.Pattern.compile(regex8.getText().toString()), Color.parseColor(numbersColor));
			
			
			ColorScheme keywords8 = new ColorScheme(java.util.regex.Pattern.compile(regex11.getText().toString()), Color.parseColor(charColor));
			
			final ColorScheme[] schemes = {keywords1, keywords2, keywords3, keywords4, keywords5, keywords6, keywords7, keywords8}; @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { } @Override public void onTextChanged(CharSequence s, int start, int before, int count) { } @Override public void afterTextChanged(Editable s) { removeSpans(s, android.text.style.ForegroundColorSpan.class); for(ColorScheme scheme : schemes) { for(java.util.regex.Matcher m = scheme.pattern.matcher(s);
					
					m.find();) { if (scheme == keywords4) { s.setSpan(new android.text.style.ForegroundColorSpan(scheme.color), m.start(), m.end()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						} 
						else { s.setSpan(new android.text.style.ForegroundColorSpan(scheme.color), m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); } } } } void removeSpans(Editable e, Class type) { android.text.style.CharacterStyle[] spans = e.getSpans(0, e.length(), type); for (android.text.style.CharacterStyle span : spans) { e.removeSpan(span); } } class ColorScheme { final java.util.regex.Pattern pattern; final int color; ColorScheme(java.util.regex.Pattern pattern, int color) { this.pattern = pattern; this.color = color; } } });
	}
	
	
	private void _Subtitle (final String _sub) {
		getSupportActionBar().setSubtitle(_sub);
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
