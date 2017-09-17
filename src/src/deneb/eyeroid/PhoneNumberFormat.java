package deneb.eyeroid;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

public class PhoneNumberFormat implements OnKeyListener{
	private String data = null;
	private String[] formatSample = null;
	private EditText et = null;
	
	public PhoneNumberFormat(){
		formatSample = new String[]{"xxx-xxxx", "xx)xxx-xxxx", "xxx)xxx-xxxx", "xxx-xxx-xxxx", "xxx-xxxx-xxxx", "xx-xxx-xxxx"};
	}
	public PhoneNumberFormat(EditText et){
		this();
		this.et = et;
	}
	public PhoneNumberFormat(String num){
		this();
		set(num);
	}
	public PhoneNumberFormat(int num){
		this(num + "");
	}
	
	public void set(String num){
		data = num;
	}
	public void set(int num){
		set(num + "");
	}
	public String format(String format, String num){
		StringBuilder sb = new StringBuilder(format);
		int sn = 0;
		for(int i = 0; i < sb.length(); i++){
			if(sb.charAt(i) == 'x'){
				sb.replace(i, i+1, num.charAt(sn) + "");
				sn++;
			}	
		}
		return sb.toString();
	}
	public String get(){
		if(data.length() == 10){
			return format(formatSample[3], data);
		}
		else if(data.length() == 11){
			return format(formatSample[4], data);
		}
		else if(data.length() == 9){
			return format(formatSample[5], data);
		}
		return null;
	}
	public String convert(EditText et){
		set(et.getText().toString());
		return get();
	}
	/**
	 * 폰번호 데이터를 알아서 바꿔주는 기능.<br>
	 * @param data - 바꿀 폰 번호 데이터. 숫자로만 되어 있거나 -(하이픈)이 2개 이하로 첨가되어 있어야 한다.
	 * @return 양식에 맞춰 바꿔진 폰번호
	 */
	public String autoConvert(String data)
	{
		String[] str = data.split("-");
		StringBuilder sb = new StringBuilder(str[0]);
		for(int i = 1; i < str.length; i++){
			sb.append(str[i]);
		}
		int opt = 1;
		//앞번호가 02인 서울 지방 전화번호일 경우
		if(sb.length() >= 2 && sb.charAt(0) == '0' && sb.charAt(1) == '2'){
			opt = 0;
		}
		switch(sb.length() - opt){
		case 2:
			sb.append('-');
			break;
		case 3:
		case 4:
			sb.insert(2 + opt, '-');
			break;
		case 5:
			sb.insert(2 + opt, '-');
			sb.append('-');
			break;
		case 6:
		case 7:
		case 8:
		case 9:
			sb.insert(2 + opt, '-');
			sb.insert(6 + opt, '-');
			break;
		case 10:
			sb.insert(2 + opt, '-');
			sb.insert(7 + opt, '-');
			break;
		default:
			return data;
		}
		return sb.toString();
	}
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		et = (EditText)v;
		String str = et.getText().toString();
		//msg.show(v.getContext(), keyCode + "");
		
		if(keyCode != event.KEYCODE_DEL){
			et.getText().clear();
			//msg.show(v.getContext(), keyCode + "하하");
			et.getText().append(autoConvert(str));
			//event.
			return false;
		}
		return false;
	}
//	@Override
//	public void afterTextChanged(Editable s) {
//		
//		switch(s.length()){
//		case 2:
//			if(s.charAt(0) == '0' && s.charAt(1) == '2'){
//				s.append('-');
//			}
//			break;
//		case 3:
//			if(s.charAt(0) == '0' && s.charAt(2) != '-'){
//				s.append('-');
//			}
//			break;
//		case 6:
//			if(s.charAt(0) == '0' && s.charAt(2) == '-'){
//				s.append('-');
//			}
//			break;
//		case 7:
//			if(s.charAt(6) != '-'){
//				s.insert(5, "-");
//			}
//			break;
//		case 13:
//			if(s.charAt(6) != '-'){
//				s.delete(7, 7);
//				s.insert(8, "-");
//			}
//			break;
//		}
//	}
//	@Override
//	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//	@Override
//	public void onTextChanged(CharSequence s, int start, int before, int count) {
//		if((start - before) <= -1 && s.charAt(s.length()-1) == '-'){
//			StringBuffer sb = new StringBuffer(s);
//			sb.deleteCharAt(s.length());
//			et.setText(sb);
//		}
//		
////		StringBuffer sb = new StringBuffer(s);
////		switch(s.length()){
////		case 2:
////			if(s.charAt(0) == '0' && s.charAt(1) == '2'){
////				sb.append('-');
////			}
////			break;
////		case 3:
////			if(s.charAt(0) == '0' && s.charAt(2) != '-'){
////				sb.append('-');
////			}
////			break;
////		case 6:
////			if(s.charAt(0) == '0' && s.charAt(2) != '-'){
////				sb.append('-');
////			}
////			break;
////		case 7:
////			if(s.charAt(6) != '-'){
////				sb.insert(5, '-');
////			}
////			break;
////		case 13:
////			if(s.charAt(6) != '-'){
////				sb.delete
////				sb.insert(8, '-');
////			}
////			break;
////		}
////		et.setText(s);
	
}