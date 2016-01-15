package com.CivilLife.Widget;

import com.aysy_mytool.ScreenUtils;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import Downloadimage.ImageUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class URLImageParser implements Html.ImageGetter {  
    TextView mTextView;
	private Context context;
  
    public URLImageParser(Context context, TextView textView) {  
        this.context = context;
		this.mTextView = textView;
    }  
  
    @Override  
    public Drawable getDrawable(String source) {  
        final URLDrawable urlDrawable = new URLDrawable(context);  
        ImageUtils.loadImageBitmap(context, source, new SimpleImageLoadingListener(){
			 @Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				urlDrawable.bitmap = loadedImage;  
				int intrinsicWidth = urlDrawable.bitmap.getWidth();
				int screenWitdh = (int) (ScreenUtils.getScreenWitdh(context)*0.9);
				int h=screenWitdh/intrinsicWidth;
				urlDrawable.setBounds(0, 0,
							screenWitdh,
							loadedImage.getHeight()*h);
	            mTextView.invalidate();  
	            mTextView.setText(mTextView.getText()); // 解决图文重叠  
				super.onLoadingComplete(imageUri, view, loadedImage);
			}
        });

        return urlDrawable;  
    }  
}  