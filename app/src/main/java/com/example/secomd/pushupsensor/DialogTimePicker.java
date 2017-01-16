package com.example.secomd.pushupsensor;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

/**
 * Created by secomd on 1/3/2017.
 */

 public class DialogTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    OnTimeSetListener onTimeSetListener;
    int position;


    public interface OnTimeSetListener{
        public void onTimeSelected(TimePicker view, int hourOfDay, int minute, int position, int flag);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onTimeSetListener = (OnTimeSetListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @TargetApi(24)
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }


    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        position = getArguments().getInt("position");
        int flags = getArguments().getInt("flag");
        onTimeSetListener.onTimeSelected(view, hourOfDay, minute, position, flags);
    }
}
