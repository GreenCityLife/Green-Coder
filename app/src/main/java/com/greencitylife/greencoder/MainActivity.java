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
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;
import android.net.Uri;
import android.app.Activity;
import android.content.SharedPreferences;
import android.animation.ObjectAnimator;
import android.view.animation.LinearInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class MainActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private String splash = "";
	
	private LinearLayout linear1;
	private ImageView imageview1;
	private TextView textview1;
	
	private TimerTask t;
	private Intent i = new Intent();
	private SharedPreferences file;
	private ObjectAnimator animator = new ObjectAnimator();
	private ObjectAnimator animator2 = new ObjectAnimator();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
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
		imageview1 = (ImageView) findViewById(R.id.imageview1);
		textview1 = (TextView) findViewById(R.id.textview1);
		file = getSharedPreferences("file", Activity.MODE_PRIVATE);
	}
	private void initializeLogic() {
		getWindow().setStatusBarColor(0xFF212121);
		getWindow().setNavigationBarColor(0xFF212121);
		textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		animator.setTarget(textview1);
		animator.setPropertyName("translationY");
		animator.setFloatValues((float)(575), (float)(-50));
		animator2.setTarget(imageview1);
		animator2.setPropertyName("translationY");
		animator2.setFloatValues((float)(-475), (float)(0));
		animator.start();
		animator2.start();
		t = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						file.edit().putString("directory", "").commit();
						file.edit().putString("filepath", "").commit();
						i.setClass(getApplicationContext(), ProjectsActivity.class);
						startActivity(i);
						finish();
					}
				});
			}
		};
		_timer.schedule(t, (int)(2000));
		if (file.getString("path", "").equals("")) {
			file.edit().putString("path", FileUtil.getExternalStorageDir().concat("/Projects/")).commit();
			if (!FileUtil.isExistFile(FileUtil.getExternalStorageDir().concat("/Projects/"))) {
				FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/Projects/"));
			}
		}
		if (file.getString("editor_font", "").equals("")) {
			file.edit().putString("editor_font", "default").commit();
		}
		if (!FileUtil.isExistFile("/storage/emulated/0/.gncode")) {
			splash = "[{\"0\":\"Made with Orginal Sketchware!\"},{\"1\":\"OMG OMG!!\"},{\"2\":\"Bro, Why don't you take rest?\"},{\"3\":\"Made by GreenCityLife!\"},{\"4\":\"Practise makes a man perfect!\"},{\"5\":\"Programming is thinking, not typing\"},{\"6\":\"Teamwork makes the Dreamwork!\"}]";
			FileUtil.writeFile("/storage/emulated/0/.gncode/splash_screen.json", splash);
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
