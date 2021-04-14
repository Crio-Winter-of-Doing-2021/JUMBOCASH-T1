package com.jumbotail.cashflow.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import java.io.Serializable;
import java.util.TimeZone;

public class ViewUtils  {
  public static void toast(Context context, String message){
    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
  }

  public static ProgressDialog progressBar(Context context, String title, String message){
    ProgressDialog dialog = new ProgressDialog(context);
    dialog.setTitle(title);
    dialog.setMessage(message);
    return dialog;
  }

  public static void intent(Activity activity, Class<?> cls){
    Intent intent = new Intent(activity, cls);
    activity.startActivity(intent);
  }

  public static <T> void intent(Activity activity, Class<?> cls, String key, T data){
    Intent intent = new Intent(activity, cls);
    Bundle bundle = new Bundle();
    bundle.putSerializable(key, (Serializable) data);
    intent.putExtras(bundle);
    activity.startActivity(intent);
  }

  public static <T> T getIntentData(Activity activity, String key) {
    Intent intent = activity.getIntent();
    T data = (T) intent.getSerializableExtra(key);
    return data;
  }

  public static SingleDateAndTimePickerDialog bringDateTimePicker(Context context){
    return new SingleDateAndTimePickerDialog.Builder(context)
        .setTimeZone(TimeZone.getDefault())
        .displayMinutes(true)
        .displayHours(true)
        .displayDays(true)
        .minutesStep(1)
        .mainColor(Color.parseColor("#1A5728"))
        .title("Choose timing")
        .displayListener(picker -> {
        }).build();
  }

  public static SingleDateAndTimePickerDialog bringMonthPicker(Context context){
    return new SingleDateAndTimePickerDialog.Builder(context)
        .setTimeZone(TimeZone.getDefault())
        .displayMinutes(false)
        .displayHours(false)
        .displayDays(false)
        .displayMonth(true)
        .displayYears(true)
        .minutesStep(1)
        .mainColor(Color.parseColor("#1A5728"))
        .title("Select month")
        .displayListener(picker -> {
        }).build();
  }

}
