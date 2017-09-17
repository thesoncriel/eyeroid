package deneb.camera;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.LuminanceSource;

public class BitmapLuminanceSource extends LuminanceSource {
    private Bitmap mBitmap;
    private int dataWidth;
    private int dataHeight;
    
    public BitmapLuminanceSource(Bitmap bitmap, int width, int height) {
        super(width, height);
        mBitmap = bitmap;
        dataWidth = width;
        dataHeight = height;
    }

    @Override
    public byte[] getMatrix() {
        int width = getWidth();
        int height = getHeight();

        // �������ު������ʪ���
        int[] pixels = new int[width * height];
        byte[] matrix = new byte[width * height];
        mBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        return toGrayScale(pixels, matrix);
    }

    @Override
    public byte[] getRow(int y, byte[] row) {
        if (y < 0 || y >= getHeight()) {
            throw new IllegalArgumentException("Requested row is outside the image: " + y);
        }
        int width = getWidth();
        if (row == null || row.length < width) {
          row = new byte[width];
        }
        // TODO: �Ȫꪢ����rect���Ū��ʪ�
//        int offset = (y + top) * dataWidth + left;
//        System.arraycopy(yuvData, offset, row, 0, width);
        int offset = y * dataWidth;
//        int[] pixels = new int[width * 4];
        int[] pixels = new int[width];
        mBitmap.getPixels(pixels, 0, width, 0, y, width, 1);
        return toGrayScale(pixels, row);
    }
    
    public int getDataWidth() {
        return dataWidth;
    }

    public int getDataHeight() {
        return dataHeight;
    }

    private byte[] toGrayScale(int[] pixels, byte[] row){
        int red, blue, green, y;
//        for(int i = 0; i < pixels.length; i += 4){
//            //RBG������
//            red = pixels[i];
//            green = pixels[i + 1];
//            blue= pixels[i + 2];
//            // �������ê�������
//            y = (red * 299 / 1000) + (green * 587 / 1000) + (blue * 114 / 1000);
//            row[i] = (byte) y;
//        }
        for(int i = 0; i < pixels.length; i++){
            //RBG������
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue= Color.blue(pixels[i]);
            // �������ê�������
            y = (red * 299 / 1000) + (green * 587 / 1000) + (blue * 114 / 1000);
            row[i] = (byte) y;
        }
        return row;
    }
}
