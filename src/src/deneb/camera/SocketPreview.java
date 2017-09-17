package deneb.camera;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import deneb.eyeroid.msg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SocketPreview extends SurfaceView implements SurfaceHolder.Callback, Runnable {
	int CONNECT_TIMEOUT = 1000;
	int SOCKET_TIMEOUT = 1000;
	int WIDTH = 640;
	int HEIGHT = 480;
	Rect bounds = null;
	boolean preserveAspectRatio = true;	
	String address = "210.110.121.54";
	int port = 8998;
	Socket socket = null;
	InputStream in = null;
	Bitmap bitmap = null;
	Paint paint = null;
	
	private Thread thread = null;
    SurfaceHolder mHolder;
    //webcamactivity mCamera;
    //CameraSource mCamera = null;
    Canvas canvas;
    Context con;
    Camera camera = null;
    //HttpCamera cs = new HttpCamera("210.110.121.54:8080", 320, 240, true); 
    //SocketCamera cs = null;
   
     public SocketPreview(Context context) {
    	 super(context);
    	 init(context);
     }
     public SocketPreview(Context context, AttributeSet attr){
    	 super(context, attr);
    	 init(context);
     }
     public SocketPreview(Context context, AttributeSet attr, int defStyle){
    	 super(context, attr, defStyle);
    	 init(context);
     }
     private void init(Context context){
    	 con = context;
    	 //mCamera = new SocketCamera("120.110.121.54", 8088, 320, 240,true);
          
         mHolder = getHolder();
         mHolder.addCallback(this);
         mHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
         canvas = mHolder.lockCanvas(new Rect(0,0, WIDTH, HEIGHT));
         //mCamera = new GenuineCamera(mHolder, 320, 240);
         mHolder.setFixedSize(WIDTH, HEIGHT);
     }
    /*
     public void run(){
           while (true) {
           Canvas c = null;
           try {
                   c = mHolder.lockCanvas(null);
           synchronized (mHolder){
                   cs.capture(c);
                   Thread.sleep(0);
              }//sync
             } catch (InterruptedException e){
            e.printStackTrace();
             } finally {
                 if (c != null) {mHolder.unlockCanvasAndPost(c);      }
         }
       }//while()
     }//run(){}
    */
 public void surfaceCreated(SurfaceHolder holder)
 {
	 thread = new Thread(this);
	 thread.start();
	 try {
		
		//bounds = new Rect(0, 0, 320, 240);
		paint = new Paint();
		
		
		//tvPrint.setText(bitmap.getPixel(2, 2) + "인가 -_-?");
	
	} catch (RuntimeException e) {
		new msg(con, e.toString());
		//Log.i(LOG_TAG, "Failed to obtain image over network", e);
		//return false;
//	} catch (IOException e) {
//		new msg(con, e.toString());
		//Log.i(LOG_TAG, "Failed to obtain image over network", e);
		//return false;
	//		} catch (OutOfResourcesException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	} finally {
		
	}
     //thread = new Thread(this);
     //thread.start();
//	 camera = Camera.open();
//	 try {
//		camera.setPreviewDisplay(holder);
//	} catch (IOException e) {
//		camera.release();
//		camera = null;
//	}
	 /*하하..*/
//	 if(!mCamera.open()){
//		 new msg(con, "역시 안되나 ㅠㅠ");
//		 return;
//	 }
//	 while(true){
//		 mCamera.capture(mHolder);
//	 }
 }
public Bitmap getPicture(){
	return bitmap;
}

public void surfaceDestroyed(SurfaceHolder holder){
	thread = null;
	try {
		socket.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       //thread.stop();
	//mCamera.close();
//	camera.stopPreview();
//	camera.release();
//	camera = null;
}


public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
	//obtain the bitmap

		
	

    //mCamera.setParams(format, w, h);
//	Camera.Parameters parameters = camera.getParameters();
//	parameters.setPreviewSize(w, h);
//	camera.setParameters(parameters);
//	camera.startPreview();
}

	public void run(){
		Canvas canvas = null;
		while(thread != null){
			try {
				socket = new Socket();
				socket.bind(null);
				socket.setSoTimeout(SOCKET_TIMEOUT);
				socket.connect(new InetSocketAddress(address, port), SOCKET_TIMEOUT);
				in = socket.getInputStream();
			
				bitmap = BitmapFactory.decodeStream(in);
				
				//new msg(con, (bitmap == null) + "헐?");
				
				
				//render it to canvas, scaling if necessary
				//Rect dest = new Rect(0,0,w,h);
				
				canvas = mHolder.lockCanvas();
				synchronized(mHolder){
					canvas.drawBitmap(bitmap, 0,0, null);
					//socket.close();
					Thread.sleep(50);
				}
				
				//new msg(con, Integer.toHexString(bitmap.getPixel(10, 10)));
				
	//			if (bounds.right == bitmap.getWidth() && 
	//					bounds.bottom == bitmap.getHeight()) {
	//				Rect dest = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
	//				canvas = holder.lockCanvas(dest);//.getSurface().lockCanvas(dest);
	//				canvas.drawBitmap(bitmap, 0, 0, null);
	//			} else {
	//				Rect dest;
	//				if (preserveAspectRatio) {
	//					dest = new Rect(bounds);
	//					//dest.bottom = bitmap.getHeight() * bounds.right / bitmap.getWidth();
	//					//dest.offset(0, (bounds.bottom - dest.bottom)/2);
	//				} else {
	//					dest = bounds;
	//				}
	//				canvas = holder.lockCanvas(dest);
	//				canvas.drawBitmap(bitmap, null, dest, paint);
	//			}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//new msg(con, e.toString());
				e.printStackTrace();
	//		} catch (InterruptedException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
			} catch(Exception e){
				//new msg(con, e.toString());
			} finally {
				if(canvas != null){
					mHolder.unlockCanvasAndPost(canvas);
				}
	//			try {
	//				//socket.close();
	//			} catch (IOException e) {
	//				/* ignore */
	//			}
			}
		}
	}

}


