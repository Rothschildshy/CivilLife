package com.CivilLife.Widget;

import com.aysy_mytool.ScreenUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-11-30
 * Time: 00:09
 * FIXME
 */
public class URLDrawable extends BitmapDrawable {
    protected Bitmap bitmap;
	private Context context;
    @SuppressWarnings("deprecation")
	URLDrawable(Context context){
		this.context = context;
    	
    }
    @Override  
    public void draw(Canvas canvas) {
        if (bitmap != null) {
//            canvas.drawBitmap(bitmap, 0, 0, getPaint());
        	Paint paint = getPaint();
        	paint.setAntiAlias(true); 
        	canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));  
        	int intrinsicWidth = bitmap.getWidth();
			int screenWitdh = (int) (ScreenUtils.getScreenWitdh(context)*0.9);
			int h=screenWitdh/intrinsicWidth;
        	
            Rect dst = new Rect();// 屏幕  
            // 下面的 dst 是表示 绘画这个图片的位置  
            dst.left = 0;   //miDTX,//这个是可以改变的，也就是绘图的起点X位置  
            dst.top = 0;    //mBitQQ.getHeight();//这个是QQ图片的高度。 也就相当于 桌面图片绘画起点的Y坐标  
            dst.right = screenWitdh;  //miDTX + mBitDestTop.getWidth();// 表示需绘画的图片的右上角  
            dst.bottom = h*bitmap.getHeight(); // mBitQQ.getHeight() + mBitDestTop.getHeight();//表示需绘画的图片的右下角  
            canvas.drawBitmap(bitmap, null, dst, paint);
        }
    }
}
