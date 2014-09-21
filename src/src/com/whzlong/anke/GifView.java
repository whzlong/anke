package com.whzlong.anke;

import com.whzlong.anke.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

public class GifView extends View {
	private long movieStart;
	private Movie movie;
	
	public GifView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		//���ļ�����InputStream����ȡ��gifͼƬ��Դ
		movie=Movie.decodeStream(getResources().openRawResource(R.drawable.loading9));
	}

	@Override
	protected void onDraw(Canvas canvas){
		long curTime=android.os.SystemClock.uptimeMillis();
		//��һ�β���
		if (movieStart == 0) {
		movieStart = curTime;
		}
		if (movie != null) {
		int duraction = movie.duration();
		int relTime = (int) ((curTime-movieStart)%duraction);
		movie.setTime(relTime);
		movie.draw(canvas, 0, 0);
		//ǿ���ػ�
		invalidate();
		}
		super.onDraw(canvas);
	}
}
