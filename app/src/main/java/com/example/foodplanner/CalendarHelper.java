package com.example.foodplanner;



import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import com.example.foodplanner.model.PlannedMeal;

import java.util.TimeZone;

public class CalendarHelper {
    private static final String TAG = "CalendarHelper";
    private final Context context;
    private final String calendarAccountName;
    private long calendarId;

    public CalendarHelper(Context context, String calendarAccountName) {
        this.context = context;
        this.calendarAccountName = calendarAccountName;
        this.calendarId = getCalendarId();
    }

    private long getCalendarId() {
        Cursor cursor = null;
        try {
            Uri uri = CalendarContract.Calendars.CONTENT_URI;
            String[] projection = {CalendarContract.Calendars._ID};
            String selection = CalendarContract.Calendars.ACCOUNT_NAME + " = ? AND " +
                    CalendarContract.Calendars.ACCOUNT_TYPE + " = ?";
            String[] selectionArgs = {calendarAccountName, CalendarContract.ACCOUNT_TYPE_LOCAL};
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getLong(cursor.getColumnIndexOrThrow(CalendarContract.Calendars._ID));
            } else {
                return createCalendar();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting calendar ID", e);
            return -1;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    private long createCalendar() {
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Calendars.ACCOUNT_NAME, calendarAccountName);
        values.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(CalendarContract.Calendars.NAME, "Food Planner");
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "Food Planner");
        values.put(CalendarContract.Calendars.CALENDAR_COLOR, 0xFF0000);
        values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        values.put(CalendarContract.Calendars.OWNER_ACCOUNT, calendarAccountName);
        values.put(CalendarContract.Calendars.VISIBLE, 1);

        Uri uri = CalendarContract.Calendars.CONTENT_URI.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, calendarAccountName)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();

        try {
            Uri result = context.getContentResolver().insert(uri, values);
            return result != null ? ContentUris.parseId(result) : -1;
        } catch (Exception e) {
            Log.e(TAG, "Error creating calendar", e);
            return -1;
        }
    }

    public long addEvent(PlannedMeal meal) {
        if (calendarId == -1) return -1;

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
        values.put(CalendarContract.Events.TITLE, meal.getMealName());
        values.put(CalendarContract.Events.DESCRIPTION, "Planned Meal: " + meal.getMealName());
        values.put(CalendarContract.Events.DTSTART, meal.getDateTimeInMillis());
        values.put(CalendarContract.Events.DTEND, meal.getDateTimeInMillis() + 3600000); // 1 hour
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

        try {
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            return uri != null ? ContentUris.parseId(uri) : -1;
        } catch (Exception e) {
            Log.e(TAG, "Error adding event", e);
            return -1;
        }
    }

    public void deleteEvent(long eventId) {
        if (eventId == -1) return;
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
        try {
            context.getContentResolver().delete(deleteUri, null, null);
        } catch (Exception e) {
            Log.e(TAG, "Error deleting event", e);
        }
    }
}