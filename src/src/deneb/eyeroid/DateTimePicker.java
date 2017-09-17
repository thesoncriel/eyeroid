package deneb.eyeroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker.OnTimeChangedListener;

public class DateTimePicker {
	public static int DATE_ONLY = 0xff00;
	public static int TIME_ONLY = 0x00ff;
	public static int DUAL = 0xffff;
	
	private Activity act = null;
	private TextView btn = null;
	private View entry = null;
	private DatePicker dp = null;
	private TimePicker tp = null;
	private AlertDialog ad = null;
	private DateTimeManager dtm = null;
	
	private int option;
	
	public DateTimePicker(Activity act, Button ett){
		this(act, ett, DUAL);
	}
	
	public DateTimePicker(Activity act, TextView ett, int option){
		this.act = act;
		this.btn = ett;
		this.entry = act.getLayoutInflater().inflate(R.layout.date_time_picker, null);
		this.option = option;
		dp = (DatePicker)entry.findViewById(R.id.DateTimePicker_date);
		tp = (TimePicker)entry.findViewById(R.id.DateTimePicker_time);
		if(option == DATE_ONLY){
			tp.setVisibility(tp.GONE);
		}
		else if(option == TIME_ONLY){
			dp.setVisibility(tp.GONE);
		}
		//new msg(act, (dp == null) + "-" + (tp == null));
		try{
			String first = btn.getText().toString();
			if(first.equals("")){
				throw new Exception();
			}
			else{
				dtm = new DateTimeManager(first);
			}
		}catch(Exception ex){
			//new msg(act, ex.toString());
			dtm = new DateTimeManager();
			dtm.setToday();
			dtm.setNow();
		}
		
		dp.init(dtm.getYear(), dtm.getMonth() - 1, dtm.getDay(), new OnDateChangedListener(){
			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				dtm.setYear(year);
				dtm.setMonth(monthOfYear + 1);
				dtm.setDay(dayOfMonth);
				//btn.setText(dtm.getFullDateTime());
			}
		});
		
		tp.setCurrentHour(dtm.getHour());
		tp.setCurrentMinute(dtm.getMin());
		tp.setOnTimeChangedListener(new OnTimeChangedListener(){
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				dtm.setHour(hourOfDay);
				dtm.setMin(minute);
				//btn.setText(dtm.getFullDateTime());
			}
		});
		//R.drawable.bg_main
		int title = 0;
		if(option == DATE_ONLY){
			title = R.string.date_time_picker_title_date;
		}
		else if(option == TIME_ONLY){
			title = R.string.date_time_picker_title_time;
		}
		else{
			title = R.string.date_time_picker_title;
		}
		ad = new AlertDialog.Builder(act)
		.setIcon(R.drawable.icon_clock)
		.setTitle(act.getResources().getString(title))
		.setView(entry)
		.setPositiveButton("»Æ¿Œ", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(DateTimePicker.this.option == 0xffff){
					btn.setText(dtm.getFullDateTime());
				}
				else if(DateTimePicker.this.option == DATE_ONLY){
					btn.setText(dtm.getDate());
				}
				else if(DateTimePicker.this.option == TIME_ONLY){
					btn.setText(dtm.getTime());
				}
				dialog.dismiss();
				//String date = dp.getYear() + "-" + dp.getMonth() + "-" + dp.getDayOfMonth();
				//String time = tp.getCurrentHour() + ":" + tp.getCurrentMinute() + ":";
				//btn.setText(date + " " + time);
			}
		})
		.show();
	}
	
	public void show(){
		ad.show();
	}
}
