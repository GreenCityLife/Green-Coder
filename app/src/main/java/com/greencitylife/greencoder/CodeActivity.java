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
import android.widget.LinearLayout;
import android.widget.EditText;
import android.content.Intent;
import android.content.ClipData;
import android.net.Uri;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.Manifest;
import android.content.pm.PackageManager;

public class CodeActivity extends Activity {
	
	public final int REQ_CD_DIRECTORYCHOOSER = 101;
	public final int REQ_CD_FILECHOOSER = 102;
	
	private String directory = "";
	private String path = "";
	private String newfile = "";
	private String newfilepath = "";
	
	private LinearLayout linear1;
	private EditText edittext1;
	
	private Intent directoryChooser = new Intent(Intent.ACTION_GET_CONTENT);
	private Intent fileChooser = new Intent(Intent.ACTION_GET_CONTENT);
	private Intent i = new Intent();
	private SharedPreferences file;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.code);
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
		
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		edittext1 = (EditText) findViewById(R.id.edittext1);
		directoryChooser.setType("*/*");
		directoryChooser.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		fileChooser.setType("text/*");
		fileChooser.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		file = getSharedPreferences("file", Activity.MODE_PRIVATE);
	}
	private void initializeLogic() {
		if (!file.getString("directory", "").equals("")) {
			directory = file.getString("directory", "");
			path = file.getString("filepath", "");
			setTitle(Uri.parse(path).getLastPathSegment());
			_activity(path);
			edittext1.setText(FileUtil.readFile(path));
			file.edit().putString("finalpath", "").commit();
			file.edit().putString("directory", "").commit();
		}
		else {
			
		}
		edittext1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/jetbrains_mono_regular.ttf"), 0);
		newfile = "This is newly created file! Change me to whatever you want from Green Coder app";
		getWindow().setStatusBarColor(0xFF212121);
		getWindow().setNavigationBarColor(0xFF212121);
		getActionBar().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.parseColor("#212121")));
		_srt(edittext1);
	}
	@Override
		public boolean onCreateOptionsMenu(Menu menu){
				menu.add(0, 0, 0, "Open File");
				menu.add(0, 1, 0, "Open Folder");
		     menu.add(0, 2, 0, "Create File");
		menu.add(0, 3, 0, "Save").setIcon(R.drawable.ic_save).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, 4, 0, "Navigate").setIcon(R.drawable.ic_navigate).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				return super.onCreateOptionsMenu(menu);
	}
	@Override
	  public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
			
			   case 0:
			
			_openfile();
			
			   return true;
				case 1:
			
			_openfolder();
			
			   return true;
			   case 2:
			
			_createfile();
			
			   return true;
			   case 3:
			
			_savefile();
			
			return true;
			case 4:
			
			_navigate();
			
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
				directory = FileUtil.getExternalStorageDir() + "/" + getDocumentPathFromTreeUri(_data.getData());
				path = "";
				if (!FileUtil.isExistFile(directory)) {
					SketchwareUtil.showMessage(getApplicationContext(), "Error finding path");
				}
				else {
					setTitle("Internal Storage");
					_activity(directory);
				}
			}
			else {
				
			}
			break;
			
			case REQ_CD_FILECHOOSER:
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
				try {
					path =getFilePath(this, _data.getData());
					directory = "";
					if (!FileUtil.isExistFile(path)) {
						SketchwareUtil.showMessage(getApplicationContext(), "Error finding path");
					}
					else {
						setTitle(Uri.parse(path).getLastPathSegment());
						_activity(path);
						edittext1.setText(FileUtil.readFile(path));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	private void _openfolder () {
		_chooseDirectory();
	}
	
	
	private void _openfile () {
		startActivityForResult(fileChooser, REQ_CD_FILECHOOSER);
	}
	
	
	private void _savefile () {
		if (!path.equals("")) {
			FileUtil.writeFile(path, edittext1.getText().toString());
			SketchwareUtil.showMessage(getApplicationContext(), "Saved file successfully!");
		}
		else {
			SketchwareUtil.showMessage(getApplicationContext(), "No file selected to handle this operation!");
		}
	}
	
	
	private void _createfile () {
		if (!directory.equals("")) {
			_create_dialog();
		}
		else {
			SketchwareUtil.showMessage(getApplicationContext(), "You need to open folder to use this feature!");
		}
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
	
	
	private void _Extras () {
	}
	
	
	
	
	
	String getFileNameFromUri(Uri uri){
		String fileName = "";
		String[] projection = {android.provider.MediaStore.MediaColumns.DISPLAY_NAME};
		android.database.Cursor metaCursor = getContentResolver().query(uri, projection, null, null, null);
		if (metaCursor != null) {
			try {
				if (metaCursor.moveToFirst()) {
					fileName = metaCursor.getString(0);
				}
			} finally {
				metaCursor.close();
			}
		}
		return fileName;
	}
	
	
	
	
	
	
	public static String getFilePath(Context context, Uri uri) throws Exception {
		        String selection = null;
		        String[] selectionArgs = null;
		        // Uri is different in versions after KITKAT (Android 4.4), we need to
		        if (Build.VERSION.SDK_INT >= 19 && android.provider.DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
			            if (isExternalStorageDocument(uri)) {
				                final String docId = android.provider.DocumentsContract.getDocumentId(uri);
				                final String[] split = docId.split(":");
				                return Environment.getExternalStorageDirectory() + "/" + split[1];
				            } else if (isDownloadsDocument(uri)) {
				                final String id = android.provider.DocumentsContract.getDocumentId(uri);
				                uri = ContentUris.withAppendedId(
				                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
				            } else if (isMediaDocument(uri)) {
				                final String docId = android.provider.DocumentsContract.getDocumentId(uri);
				                final String[] split = docId.split(":");
				                final String type = split[0];
				                if ("image".equals(type)) {
					                    uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
					                } else if ("video".equals(type)) {
					                    uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
					                } else if ("audio".equals(type)) {
					                    uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
					                }
				                selection = "_id=?";
				                selectionArgs = new String[]{
					                        split[1]
					                };
				            }
			        }
		        if ("content".equalsIgnoreCase(uri.getScheme())) {
			
			
			          if (isGooglePhotosUri(uri)) {
				              return uri.getLastPathSegment();
				           }
			
			            String[] projection = {
				                    android.provider.MediaStore.Images.Media.DATA
				            };
			            android.database.Cursor cursor = null;
			            try {
				                cursor = context.getContentResolver()
				                        .query(uri, projection, selection, selectionArgs, null);
				                int column_index = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA);
				                if (cursor.moveToFirst()) {
					                    return cursor.getString(column_index);
					                }
				            } catch (Exception e) {
				                e.printStackTrace();
				            }
			        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
			            return uri.getPath();
			        }
		        return null;
		    }
	
	    public static boolean isExternalStorageDocument(Uri uri) {
		        return "com.android.externalstorage.documents".equals(uri.getAuthority());
		    }
	
	    public static boolean isDownloadsDocument(Uri uri) {
		        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
		    }
	
	    public static boolean isMediaDocument(Uri uri) {
		        return "com.android.providers.media.documents".equals(uri.getAuthority());
		    }
	
	  public static boolean isGooglePhotosUri(Uri uri) {
		    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
	private void _activity (final String _title) {
		getActionBar().setSubtitle(_title);
	}
	
	
	private void _create_dialog () {
		final AlertDialog create = new AlertDialog.Builder(CodeActivity.this).create();
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
					newfilepath = directory.concat("/".concat(input.getText().toString()));
					FileUtil.writeFile(newfilepath, newfile);
					path = newfilepath;
					if (!FileUtil.isExistFile(path)) {
						path = path.replace("/storage/emulated/0/", "/storage/143A-B90B/");
						setTitle(Uri.parse(path).getLastPathSegment());
						_activity(path);
						edittext1.setText(FileUtil.readFile(path));
					}
					else {
						setTitle(Uri.parse(path).getLastPathSegment());
						_activity(path);
						edittext1.setText(FileUtil.readFile(path));
					}
				} else {
					SketchwareUtil.showMessage(getApplicationContext(), "Empty File Name");
				}
			} });
		cancel.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				create.dismiss();
			} });
		create.show();
	}
	
	
	private void _navigate () {
		if (!directory.equals("")) {
			i.putExtra("path", directory);
			i.setClass(getApplicationContext(), NavigateActivity.class);
			startActivity(i);
			finish();
			overridePendingTransition(0,0);
		}
		else {
			SketchwareUtil.showMessage(getApplicationContext(), "Navigate is only available for folders!");
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
