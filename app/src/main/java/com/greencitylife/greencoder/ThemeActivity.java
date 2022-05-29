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
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.graphics.Typeface;

public class ThemeActivity extends AppCompatActivity {
	
	
	private Toolbar _toolbar;
	private boolean is_expanded = false;
	private String bc_data = "";
	private String main_data = "";
	private String sec_data = "";
	private String desc_data = "";
	
	private ScrollView vscroll1;
	private LinearLayout linear1;
	private TextView textview1;
	private LinearLayout bc_color;
	private LinearLayout main_color;
	private LinearLayout second_color;
	private LinearLayout desc_color;
	private LinearLayout linear5;
	private LinearLayout preview;
	private LinearLayout set_theme;
	private LinearLayout export_theme;
	private LinearLayout import_theme;
	private TextView textview2;
	private TextView textview3;
	private TextView textview4;
	private TextView textview5;
	private TextView textview6;
	private TextView textview7;
	private TextView textview8;
	private TextView textview9;
	private TextView textview13;
	private ImageView imageview1;
	private TextView textview14;
	private LinearLayout preview_option;
	private TextView textview15;
	private TextView textview16;
	private TextView textview10;
	private TextView textview11;
	private TextView textview12;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.theme);
		initialize(_savedInstanceState);
		initializeLogic();
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
		bc_color = (LinearLayout) findViewById(R.id.bc_color);
		main_color = (LinearLayout) findViewById(R.id.main_color);
		second_color = (LinearLayout) findViewById(R.id.second_color);
		desc_color = (LinearLayout) findViewById(R.id.desc_color);
		linear5 = (LinearLayout) findViewById(R.id.linear5);
		preview = (LinearLayout) findViewById(R.id.preview);
		set_theme = (LinearLayout) findViewById(R.id.set_theme);
		export_theme = (LinearLayout) findViewById(R.id.export_theme);
		import_theme = (LinearLayout) findViewById(R.id.import_theme);
		textview2 = (TextView) findViewById(R.id.textview2);
		textview3 = (TextView) findViewById(R.id.textview3);
		textview4 = (TextView) findViewById(R.id.textview4);
		textview5 = (TextView) findViewById(R.id.textview5);
		textview6 = (TextView) findViewById(R.id.textview6);
		textview7 = (TextView) findViewById(R.id.textview7);
		textview8 = (TextView) findViewById(R.id.textview8);
		textview9 = (TextView) findViewById(R.id.textview9);
		textview13 = (TextView) findViewById(R.id.textview13);
		imageview1 = (ImageView) findViewById(R.id.imageview1);
		textview14 = (TextView) findViewById(R.id.textview14);
		preview_option = (LinearLayout) findViewById(R.id.preview_option);
		textview15 = (TextView) findViewById(R.id.textview15);
		textview16 = (TextView) findViewById(R.id.textview16);
		textview10 = (TextView) findViewById(R.id.textview10);
		textview11 = (TextView) findViewById(R.id.textview11);
		textview12 = (TextView) findViewById(R.id.textview12);
		
		bc_color.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_bc_dialog();
			}
		});
		
		main_color.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_main_dialog();
			}
		});
		
		second_color.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_sec_dialog();
			}
		});
		
		desc_color.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_desc_dialog();
			}
		});
		
		linear5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (is_expanded) {
					is_expanded = false;
					imageview1.setImageResource(R.drawable.ic_arrow_expand);
					preview.setVisibility(View.GONE);
				}
				else {
					is_expanded = true;
					imageview1.setImageResource(R.drawable.ic_arrow_unexpand);
					preview.setVisibility(View.VISIBLE);
				}
			}
		});
		
		preview_option.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
	}
	private void initializeLogic() {
		getWindow().setNavigationBarColor(0xFF212121);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFF4CAF50);
			SketchUi.setCornerRadius(d*25);
			set_theme.setElevation(d*5);
			android.graphics.drawable.RippleDrawable SketchUiRD = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{0xFFE0E0E0}), SketchUi, null);
			set_theme.setBackground(SketchUiRD);
			set_theme.setClickable(true);
		}
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFF4CAF50);
			SketchUi.setCornerRadius(d*25);
			export_theme.setElevation(d*5);
			android.graphics.drawable.RippleDrawable SketchUiRD = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{0xFFE0E0E0}), SketchUi, null);
			export_theme.setBackground(SketchUiRD);
			export_theme.setClickable(true);
		}
		{
			android.graphics.drawable.GradientDrawable SketchUi = new android.graphics.drawable.GradientDrawable();
			int d = (int) getApplicationContext().getResources().getDisplayMetrics().density;
			SketchUi.setColor(0xFF4CAF50);
			SketchUi.setCornerRadius(d*25);
			import_theme.setElevation(d*5);
			android.graphics.drawable.RippleDrawable SketchUiRD = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{0xFFE0E0E0}), SketchUi, null);
			import_theme.setBackground(SketchUiRD);
			import_theme.setClickable(true);
		}
		_Font();
		_rippleRoundStroke(bc_color, "#212121", "#e0e0e0", 0, 0, "#212121");
		_rippleRoundStroke(main_color, "#212121", "#e0e0e0", 0, 0, "#212121");
		_rippleRoundStroke(second_color, "#212121", "#e0e0e0", 0, 0, "#212121");
		_rippleRoundStroke(desc_color, "#212121", "#e0e0e0", 0, 0, "#212121");
		_rippleRoundStroke(preview_option, "#212121", "#e0e0e0", 0, 0, "#212121");
		setTitle("Themes");
		is_expanded = false;
		preview.setVisibility(View.GONE);
		setTheme(android.R.style.Theme_Material);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	private void _Font () {
		textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		textview4.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview5.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		textview6.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview7.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		textview8.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview9.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		textview10.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		textview11.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		textview12.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		textview13.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
		textview14.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview15.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 1);
		textview16.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/en_medium.ttf"), 0);
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
	
	
	private void _bc_dialog () {
		final AlertDialog bc = new AlertDialog.Builder(ThemeActivity.this).create();
		View options = getLayoutInflater().inflate(R.layout.create_file,null); 
		bc.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		bc.setView(options);
		TextView ok = (TextView) options.findViewById(R.id.textview3);
		
		TextView cancel = (TextView) options.findViewById(R.id.textview4);
		
		TextView title = (TextView) options.findViewById(R.id.textview1);
		
		TextView message = (TextView) options.findViewById(R.id.textview2);
		
		LinearLayout bg = (LinearLayout) options.findViewById(R.id.linear1);
		
		final LinearLayout linear6 = (LinearLayout) options.findViewById(R.id.linear6);
		
		final EditText input = (EditText) options.findViewById(R.id.edittext1);
		input.setFocusable(true);
		input.setFocusableInTouchMode(true);
		title.setText("Set Background color");
		message.setText("Type a color hex code (eg: #FFFFFF)");
		input.setHint("Color code");
		ok.setText("Ok");
		cancel.setText("Cancel");
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
				try {
					if (!input.getText().toString().equals("")) {
						bc_data = input.getText().toString();
						preview.setBackgroundColor(Color.parseColor(bc_data));
						preview_option.setBackgroundColor(Color.parseColor(bc_data));
						textview3.setText(bc_data);
						bc.dismiss();
					} else {
						input.setError("Empty Hex Code");
					}
				} catch (Exception e) {
					input.setError("Invalid Hex Code");
				}
			} });
		cancel.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				bc.dismiss();
			} });
		bc.show();
	}
	
	
	private void _main_dialog () {
		final AlertDialog main = new AlertDialog.Builder(ThemeActivity.this).create();
		View options = getLayoutInflater().inflate(R.layout.create_file,null); 
		main.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		main.setView(options);
		TextView ok = (TextView) options.findViewById(R.id.textview3);
		
		TextView cancel = (TextView) options.findViewById(R.id.textview4);
		
		TextView title = (TextView) options.findViewById(R.id.textview1);
		
		TextView message = (TextView) options.findViewById(R.id.textview2);
		
		LinearLayout bg = (LinearLayout) options.findViewById(R.id.linear1);
		
		final LinearLayout linear6 = (LinearLayout) options.findViewById(R.id.linear6);
		
		final EditText input = (EditText) options.findViewById(R.id.edittext1);
		input.setFocusable(true);
		input.setFocusableInTouchMode(true);
		title.setText("Set Main color");
		message.setText("Type a color hex code (eg: #FFFFFF)");
		input.setHint("Color code");
		ok.setText("Ok");
		cancel.setText("Cancel");
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
				try {
					if (!input.getText().toString().equals("")) {
						main_data = input.getText().toString();
						textview14.setTextColor(Color.parseColor(main_data));
						textview5.setText(main_data);
						main.dismiss();
					} else {
						input.setError("Empty Hex Code");
					}
				} catch (Exception e) {
					input.setError("Invalid Hex Code");
				}
			} });
		cancel.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				main.dismiss();
			} });
		main.show();
	}
	
	
	private void _sec_dialog () {
		final AlertDialog sec = new AlertDialog.Builder(ThemeActivity.this).create();
		View options = getLayoutInflater().inflate(R.layout.create_file,null); 
		sec.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		sec.setView(options);
		TextView ok = (TextView) options.findViewById(R.id.textview3);
		
		TextView cancel = (TextView) options.findViewById(R.id.textview4);
		
		TextView title = (TextView) options.findViewById(R.id.textview1);
		
		TextView message = (TextView) options.findViewById(R.id.textview2);
		
		LinearLayout bg = (LinearLayout) options.findViewById(R.id.linear1);
		
		final LinearLayout linear6 = (LinearLayout) options.findViewById(R.id.linear6);
		
		final EditText input = (EditText) options.findViewById(R.id.edittext1);
		input.setFocusable(true);
		input.setFocusableInTouchMode(true);
		title.setText("Set Secondary color");
		message.setText("Type a color hex code (eg: #FFFFFF)");
		input.setHint("Color code");
		ok.setText("Ok");
		cancel.setText("Cancel");
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
				try {
					if (!input.getText().toString().equals("")) {
						sec_data = input.getText().toString();
						textview15.setTextColor(Color.parseColor(sec_data));
						textview7.setText(sec_data);
						sec.dismiss();
					} else {
						input.setError("Empty Hex Code");
					}
				} catch (Exception e) {
					input.setError("Invalid Hex Code");
				}
			} });
		cancel.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				sec.dismiss();
			} });
		sec.show();
	}
	
	
	private void _desc_dialog () {
		final AlertDialog desc = new AlertDialog.Builder(ThemeActivity.this).create();
		View options = getLayoutInflater().inflate(R.layout.create_file,null); 
		desc.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		desc.setView(options);
		TextView ok = (TextView) options.findViewById(R.id.textview3);
		
		TextView cancel = (TextView) options.findViewById(R.id.textview4);
		
		TextView title = (TextView) options.findViewById(R.id.textview1);
		
		TextView message = (TextView) options.findViewById(R.id.textview2);
		
		LinearLayout bg = (LinearLayout) options.findViewById(R.id.linear1);
		
		final LinearLayout linear6 = (LinearLayout) options.findViewById(R.id.linear6);
		
		final EditText input = (EditText) options.findViewById(R.id.edittext1);
		input.setFocusable(true);
		input.setFocusableInTouchMode(true);
		title.setText("Set Description color");
		message.setText("Type a color hex code (eg: #FFFFFF)");
		input.setHint("Color code");
		ok.setText("Ok");
		cancel.setText("Cancel");
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
				try {
					if (!input.getText().toString().equals("")) {
						desc_data = input.getText().toString();
						textview16.setTextColor(Color.parseColor(desc_data));
						textview9.setText(desc_data);
						desc.dismiss();
					} else {
						SketchwareUtil.showMessage(getApplicationContext(), "Empty Hex Code");
					}
				} catch (Exception e) {
					input.setError("Invalid Hex Code");
				}
			} });
		cancel.setOnClickListener(new View.OnClickListener(){ public void onClick(View v){
				desc.dismiss();
			} });
		desc.show();
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
