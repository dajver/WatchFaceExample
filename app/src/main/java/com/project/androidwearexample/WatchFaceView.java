package com.project.androidwearexample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.format.Time;

/**
 * Created by gleb on 5/28/17.
 */

public class WatchFaceView {

    private static final String TIME_FORMAT_WITHOUT_SECONDS = "%02d:%02d";
    private static final String TIME_FORMAT_WITH_SECONDS = TIME_FORMAT_WITHOUT_SECONDS + ":%02d";
    private static final String DATE_FORMAT = "%02d.%02d.%d";
    private static final int DATE_AND_TIME_DEFAULT_COLOUR = Color.BLUE;
    private static final int BACKGROUND_DEFAULT_COLOUR = Color.BLACK;

    private final Paint timePaint;
    private final Paint datePaint;
    private final Paint batteryPaint;
    private final Paint backgroundPaint;
    private final Paint secondStickPaint;
    private final Time time;

    private boolean shouldShowSeconds = true;

    private String batteryText = "100%";

    private int mWidth;
    private int mHeight;

    public static WatchFaceView newInstance(Context context) {
        Paint timePaint = new Paint();
        timePaint.setColor(context.getResources().getColor(R.color.primaryColorBlue));
        timePaint.setTextSize(context.getResources().getDimension(R.dimen.time_size));
        timePaint.setAntiAlias(true);

        Paint datePaint = new Paint();
        datePaint.setColor(context.getResources().getColor(R.color.primaryColorBlue));
        datePaint.setTextSize(context.getResources().getDimension(R.dimen.date_size));
        datePaint.setAntiAlias(true);

        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(context.getResources().getColor(R.color.black));

        Paint batteryPaint = new Paint();
        batteryPaint.setColor(context.getResources().getColor(R.color.primaryColorBlue));
        batteryPaint.setTextSize(context.getResources().getDimension(R.dimen.date_size));
        batteryPaint.setAntiAlias(true);

        Paint secondStickPaint = new Paint();
        batteryPaint.setColor(context.getResources().getColor(R.color.primaryColorBlue));
        batteryPaint.setStrokeWidth(3);
        batteryPaint.setAntiAlias(true);

        return new WatchFaceView(timePaint, datePaint, batteryPaint, secondStickPaint, backgroundPaint, new Time());
    }

    WatchFaceView(Paint timePaint, Paint datePaint, Paint batteryPaint, Paint secondStickPaint, Paint backgroundPaint, Time time) {
        this.timePaint = timePaint;
        this.datePaint = datePaint;
        this.backgroundPaint = backgroundPaint;
        this.batteryPaint = batteryPaint;
        this.secondStickPaint = secondStickPaint;
        this.time = time;
    }

    public void draw(Canvas canvas, Rect bounds) {
        mWidth = canvas.getWidth();
        mHeight = canvas.getHeight();

        float mCenterX = mWidth / 2f;
        float mCenterY = mHeight / 2f;

        time.setToNow();
        canvas.drawRect(0, 0, bounds.width(), bounds.height(), backgroundPaint);

        String timeText = String.format(shouldShowSeconds ? TIME_FORMAT_WITH_SECONDS : TIME_FORMAT_WITHOUT_SECONDS, time.hour, time.minute, time.second);
        float timeXOffset = computeXOffset(timeText, timePaint, bounds);
        float timeYOffset = bounds.centerY();
        canvas.drawText(timeText, timeXOffset, timeYOffset, timePaint);

        String dateText = String.format(DATE_FORMAT, time.monthDay, (time.month + 1), time.year);
        float dateXOffset = computeXOffset(dateText, datePaint, bounds);
        float dateYOffset = computeYOffset(dateText, datePaint);
        canvas.drawText(dateText, dateXOffset, timeYOffset + dateYOffset, datePaint);

        float batteryXOffset = computeXOffset(batteryText, batteryPaint, bounds);
        float batteryYOffset = computeYOffset(batteryText, batteryPaint);
        canvas.drawText(batteryText, batteryXOffset, timeYOffset + dateYOffset + batteryYOffset, batteryPaint);

        float mSecondHandLength = mCenterX;
        float secondsRotation = time.second * 6f;
        canvas.rotate(secondsRotation, mCenterX, mCenterY);
        canvas.drawLine(mCenterX, mCenterY - 120, mCenterX, mCenterY - mSecondHandLength, batteryPaint);
    }

    private float computeXOffset(String text, Paint paint, Rect watchBounds) {
        float centerX = watchBounds.exactCenterX();
        float timeLength = paint.measureText(text);
        return centerX - (timeLength / 2.0f);
    }

    private float computeYOffset(String dateText, Paint datePaint) {
        Rect textBounds = new Rect();
        datePaint.getTextBounds(dateText, 0, dateText.length(), textBounds);
        return textBounds.height() + 10.0f;
    }

    public void updateTimeZoneWith(String timeZone) {
        time.clear(timeZone);
        time.setToNow();
    }

    public void updateBattery(String batteryText) {
        this.batteryText = batteryText;
    }

    public void updateBackgroundColourToDefault() {
        backgroundPaint.setColor(BACKGROUND_DEFAULT_COLOUR);
    }
}
