package deneb.eyeroid;

import android.database.Cursor;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AfterService {
	private String accNum;
	private String accDate;
	private String cusName;
	private String addr;
	private String cusPhon;
	private String model;
	private String makeNum;
	private String salDate;
	private String warranty;
	private String accCont;
	private String accName;
	private String proDate;
	private int fixPrice;
	private String carNum;
	private int runDist;
	private String carBody;
	
	/*
	 * cusAftReg_etAccNum
cusAftReg_btnAccDate
cusAftReg_etCusName
cusAftReg_etCusPhon
cusAftReg_etAddr
cusAftReg_etModel
cusAftReg_etMakeNum
cusAftReg_btnSalDate
cusAftReg_spWarranty
cusAftReg_etAccCont

	 */
	
	public void getValues(EditText a1, Button a2, EditText a3, EditText a4, 
			EditText a5, EditText a6, EditText a7, Button a8,
			Spinner a9, EditText a10){
		String[] item = {"유상", "무상"};
		int num = (item[0].equals(warranty))? 0 : 1;
		a1.setText(accNum);
		a2.setText(accDate);
		a3.setText(cusName);
		a4.setText(addr);
		a5.setText(cusPhon.replace("-", ""));
		a6.setText(model);
		a7.setText(makeNum);
		a8.setText(salDate);
		a9.setSelection(num);
		a10.setText(accCont);
	}
	public void setValues(EditText a1, Button a2, EditText a3, EditText a4, 
			EditText a5, EditText a6, EditText a7, Button a8,
			Spinner a9, EditText a10){
		String[] item = {"유상", "무상"};
		accNum = a1.getText().toString();
		accDate = a2.getText().toString();
		cusName = a3.getText().toString();
		addr = a4.getText().toString();
		cusPhon = a5.getText().toString();
		model = a6.getText().toString();
		makeNum = a7.getText().toString();
		salDate = a8.getText().toString();
		warranty = (a9.getSelectedItemPosition() == 0)? item[0] : item[1];
		accCont = a10.getText().toString();
	}
	/*
	 * private EditText etAccName = null;
	private Button btnProDate = null;
	private EditText etFixPrice = null;
	private EditText etCarNum = null;
	private EditText etRunDist = null;
	private EditText etCarBody = null;
	 */
	
	public void getValues2(EditText a1, Button a2, EditText a3, EditText a4, EditText a5, EditText a6){
		a1.setText(accName);
		a2.setText(proDate);
		a3.setText((fixPrice == 0)? "" : (fixPrice + ""));
		a4.setText(carNum);
		a5.setText((runDist == 0)? "" : (runDist + ""));
		a6.setText(carBody);
	}
	
	public void setValues2(String s1, String s2, String s3, String s4, String s5, String s6){
		accName = s1;
		proDate = s2;
		fixPrice = Integer.parseInt(s3);
		carNum = s4;
		runDist = Integer.parseInt(s5);
		carBody = s6;
	}
	public void setValues2(EditText a1, Button a2, EditText a3, EditText a4, EditText a5, EditText a6){
		accName = a1.getText().toString();
		proDate = a2.getText().toString();
		fixPrice = setText(a3.getText().toString());
		carNum = a4.getText().toString();
		runDist = setText(a5.getText().toString());
		carBody = a6.getText().toString();
	}
	/*
	 * accNum;
	private String accDate;
	private String cusName;
	private String addr;
	private String cusPhon;
	private String model;
	private String makeNum;
	private String salDate;
	private String warranty;
	private String accCont;
	private String accName;
	private String proDate;
	private int fixPrice;
	private String carNum;
	private int runDist;
	private String carBody;
	 */
	public void setSqlValuesAll(Cursor c){
		
	}
	public String[] getSqlValuesAll(){
		return new String[]{sm(accNum), sm(accDate), sm(cusName), sm(addr), 
			sm(cusPhon), sm(model), sm(makeNum), sm(salDate),
			sm(warranty), sm(accCont), sm(accName), sm(proDate),
			fixPrice + "", sm(carNum), runDist + "", sm(carBody)
		};
	}
	private String sm(String str){
		try{
			if(str.equals("") || str.equals("0") || str == null){
				return "null";
			}
			else{
				return "'" + str + "'";
			}
		}
		catch(NullPointerException ex){
			return "null";
		}
	}
	private int setText(String str){
		if(str.equals("")){
			return 0;
		}
		else{
			return Integer.parseInt(str);
		}
	}
	
	public String getAccNum() {
		return accNum;
	}
	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}
	public String getAccDate() {
		return accDate;
	}
	public void setAccDate(String accDate) {
		this.accDate = accDate;
	}
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	public String getAddr() {
		return addr;
	}
	public void setCusAddr(String addr) {
		this.addr = addr;
	}
	public String getCusPhon() {
		return cusPhon;
	}
	public void setCusPhon(String cusPhon) {
		this.cusPhon = cusPhon;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getMakeNum() {
		return makeNum;
	}
	public void setMakeNum(String makeNum) {
		this.makeNum = makeNum;
	}
	public String getPurDate() {
		return salDate;
	}
	public void setPurDate(String salDate) {
		this.salDate = salDate;
	}
	public String getWarranty() {
		return warranty;
	}
	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}
	public String getAccCont() {
		return accCont;
	}
	public void setAccCont(String accCont) {
		this.accCont = accCont;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getProDate() {
		return proDate;
	}
	public void setProDate(String proDate) {
		this.proDate = proDate;
	}
	public int getFixPrice() {
		return fixPrice;
	}
	public void setFixPrice(int fixPrice) {
		this.fixPrice = fixPrice;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public int getRunDist() {
		return runDist;
	}
	public void setRunDist(int runDist) {
		this.runDist = runDist;
	}
	public String getCarBody() {
		return carBody;
	}
	public void setCarBody(String carBody) {
		this.carBody = carBody;
	}
	
	
}
