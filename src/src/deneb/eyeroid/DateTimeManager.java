package deneb.eyeroid;

import java.util.Calendar;
import java.util.Date;

import android.text.format.DateFormat;

public class DateTimeManager {
	private String dateFormat = null;
	private String timeFormat = null;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int min;
	private int sec;
	
	public DateTimeManager(String fullDateTime){
		this();
		setDateTime(fullDateTime);
	}
	public DateTimeManager(){
		dateFormat = "yyyy-MM-dd";
		timeFormat = "hh:mm:ss";
	}
	public void setDateTime(String fullDateTime){
		String[] data = fullDateTime.split(" ");
		String[] date = null;
		String[] time = null;
		if(data.length == 1){
			if(data[0].contains("-")){
				date = data[0].split("-");
				year = Integer.parseInt(date[0]);
				month = Integer.parseInt(date[1]);
				day = Integer.parseInt(date[2]);
			}
			else{
				time = data[0].split(":");
				hour = Integer.parseInt(time[0]);
				min = Integer.parseInt(time[1]);
				sec = Integer.parseInt(time[2]);
			}
		}
		else{
			date = data[0].split("-");
			time = data[1].split(":");
			
			year = Integer.parseInt(date[0]);
			month = Integer.parseInt(date[1]);
			day = Integer.parseInt(date[2]);
			
			hour = Integer.parseInt(time[0]);
			min = Integer.parseInt(time[1]);
			sec = Integer.parseInt(time[2]);
		}
	}
	public String getToday(){
		return DateFormat.format(dateFormat, Calendar.getInstance()).toString();
	}
	public String getNow(){
		return DateFormat.format(timeFormat, Calendar.getInstance()).toString();
	}
	public String getYesterday(){
        Date date = new Date();
        Date date2 = new Date();
        date2.setTime( date.getTime() + ((long)1000 * 60 * 60 * 24) * -1);
        Calendar c = Calendar.getInstance();
        c.setTime(date2);
        return DateFormat.format(dateFormat, c).toString();
	}
	public DateTimeManager setToday(){
		String str = DateFormat.format(dateFormat, Calendar.getInstance()).toString();
		String[] date = str.split("-");
		year = Integer.parseInt(date[0]);
		month = Integer.parseInt(date[1]);
		day = Integer.parseInt(date[2]);
		return this;
	}
	public DateTimeManager setNow(){
		String str = DateFormat.format(timeFormat, Calendar.getInstance()).toString();
		String[] time = str.split(":");
		hour = Integer.parseInt(time[0]);
		min = Integer.parseInt(time[1]);
		sec = Integer.parseInt(time[2]);
		return this;
	}
	public DateTimeManager setTodayNow(){
		setToday();
		setNow();
		return this;
	}
	public String getFullDateTime(){
		return getFullDateTime(year, month, day, hour, min, sec);
	}
	public String getFullDateTime(int year, int month, int day, int hour, int minute, int second){
		String[] str = new String[5];
		str[0] = tenZero(month);
		str[1] = tenZero(day);
		str[2] = tenZero(hour);
		str[3] = tenZero(minute);
		str[4] = tenZero(second);
		return year + "-" + str[0] + "-" + str[1] + " " + str[2] + ":" + str[3] + ":" + str[4];
	}
	public String getDate(){
		return year + "-" + tenZero(month) + "-" + tenZero(day);
	}
	public String getTime(){
		return tenZero(hour) + ":" + tenZero(min) + ":" + tenZero(sec);
	}
	private String tenZero(int d){
		if(d < 10) return "0" + d;
		else return d + "";
	}
	public int getYear(){
		return year;
	}
	public int getMonth() {
		return month;
	}
	public int getDay() {
		return day;
	}
	public int getHour() {
		return hour;
	}
	public int getMin() {
		return min;
	}
	public int getSec() {
		return sec;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public void setSec(int sec) {
		this.sec = sec;
	}
}
