package deneb.camera;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import deneb.eyeroid.R;
import deneb.eyeroid.msg;


public class SocketCameraView extends Activity {
	private Button tvPrint = null;
	private SocketPreview scImage = null;
	private Bundle extra = null;
	private Intent intent = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.socket_camera);
        init();
	}
	public void init(){
		tvPrint = (Button)findViewById(R.id.socketCamera_btnOK);
        scImage = (SocketPreview)findViewById(R.id.scImage);
        tvPrint.setText("ÀÔ·Â");
        tvPrint.setOnClickListener(tvPrint_OnClick);
        
        extra = new Bundle();
        intent = new Intent();
        extra.putInt("SCAN_FROM", 0xff3344);
	}
	
	private final OnClickListener tvPrint_OnClick = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Bitmap data = scImage.getPicture();
			BitmapLuminanceSource source = new BitmapLuminanceSource(data, data.getWidth(), data.getHeight());
			BinaryBitmap bBmp = new BinaryBitmap(new HybridBinarizer(source));
//			new msg(SocketCameraView.this, data.getWidth() + " : " + data.getHeight() + 
//					"\n3,4 ÇÈ¼¿ µ¥ÀÌÅÍ  = " + Integer.toHexString(data.getPixel(3, 4)));
			Reader reader = new MultiFormatReader();
			
			try {
				Result result = reader.decode(bBmp);
				extra.putString("SCAN_RESULT", result.getText());
				extra.putString("SCAN_RESULT_FORMAT", result.getBarcodeFormat().toString());
				intent.putExtras(extra);
				setResult(RESULT_OK, intent);
			} catch (NotFoundException e) {
				extra.putString("STATE", "NotFound");
				intent.putExtras(extra);
				setResult(RESULT_CANCELED, intent);
			} catch (ChecksumException e) {
//				intent.putExtras(extra);
//				setResult(RESULT_CANCELED, intent);
			} catch (FormatException e) {
//				intent.putExtras(extra);
//				setResult(RESULT_CANCELED, intent);
			} catch(Exception ex){
//				new msg(SocketCameraView.this, ex.toString());
//				intent.putExtras(extra);
//				setResult(RESULT_CANCELED, intent);
			}
			finally{
				SocketCameraView.this.finish();
			}
		}
	};

	
	
	public void test(){
		int CONNECT_TIMEOUT = 1000;
		int SOCKET_TIMEOUT = 1000;
		Rect bounds = null;
		bounds = new Rect(0, 0, 320, 240);
		
		boolean preserveAspectRatio = true;	
		
		String address = "210.110.121.54";
		int port = 8998;
		Socket socket = null;
		try {
			socket = new Socket();
			socket.bind(null);
			socket.setSoTimeout(SOCKET_TIMEOUT);
			socket.connect(new InetSocketAddress(address, port), SOCKET_TIMEOUT);

			//obtain the bitmap
			InputStream in = socket.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			
			new msg(this, (bitmap == null) + "Çæ?");
			
			Canvas canvas = null;
			//render it to canvas, scaling if necessary
			if (
					bounds.right == bitmap.getWidth() &&
					bounds.bottom == bitmap.getHeight()) {
				Rect dest = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
				//canvas = holder.getSurface().lockCanvas(dest);
				//canvas.drawBitmap(bitmap, 0, 0, null);
			} else {
				Rect dest;
				if (preserveAspectRatio) {
					dest = new Rect(bounds);
					dest.bottom = bitmap.getHeight() * bounds.right / bitmap.getWidth();
					dest.offset(0, (bounds.bottom - dest.bottom)/2);
				} else {
					dest = bounds;
				}
				//canvas.drawBitmap(bitmap, null, dest, paint);
			}
			
			tvPrint.setText(bitmap.getPixel(2, 2) + "ÀÎ°¡ -_-?");

		} catch (RuntimeException e) {
			new msg(this, e.toString());
			//Log.i(LOG_TAG, "Failed to obtain image over network", e);
			//return false;
		} catch (IOException e) {
			new msg(this, e.toString());
			//Log.i(LOG_TAG, "Failed to obtain image over network", e);
			//return false;
//		} catch (OutOfResourcesException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				/* ignore */
			}
		}
		/* Àý°³¼± ÂÞ¿ì¿í ¤Ñ.¤Ñ;;;;;;;;;;;; */
//		String urlstr = "http://127.0.0.1:8080";
//
//		Bitmap bitmap = null;
//		InputStream in = null;
//		int response = -1;
//		try {
//			//we use URLConnection because it's anticipated that it is lighter-weight than HttpClient
//			//NOTE: At this time, neither properly support connect timeouts
//			//as a consequence, this implementation will hang on a failure to connect
//			URL url = new URL(urlstr);
//			URLConnection conn = url.openConnection();
//			if (!(conn instanceof HttpURLConnection)) throw new IOException("Not an HTTP connection.");
//			HttpURLConnection httpConn = (HttpURLConnection) conn;
//			httpConn.setAllowUserInteraction(false);
//			httpConn.setConnectTimeout(CONNECT_TIMEOUT);
//			httpConn.setReadTimeout(SOCKET_TIMEOUT);
//			httpConn.setInstanceFollowRedirects(true);
//			httpConn.setRequestMethod("GET");
//			httpConn.connect();
//			response = httpConn.getResponseCode();
//			if (response == HttpURLConnection.HTTP_OK) {
//				in = httpConn.getInputStream();
//				bitmap = BitmapFactory.decodeStream(in);
//				tvPrint.setText(bitmap.getPixel(2, 2) + " - ¿ä°íÀÌ ÇÈ¼¿? ¤»");
//			}
//		} catch (MalformedURLException e) {
//			new msg(this, e.toString() + " : ¾Æ¿ì ¤Ð¤Ð");
//			e.printStackTrace();
//		} catch (IOException e) {
//			new msg(this, e.toString() + " Èå¹Ì IO");
//			e.printStackTrace();
//		} finally {
//			if (in != null) try {
//				in.close();
//			} catch (IOException e) {
//				/* ignore */
//			}
//		}
		
	}
}
