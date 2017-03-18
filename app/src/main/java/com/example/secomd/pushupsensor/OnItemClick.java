package com.example.secomd.pushupsensor;

import android.view.View;

/**
 * An interface signature.
 * TODO embed this in RecyclerAdapter as it is the only class that uses it
 */

public interface OnItemClick {
    public void onItemClicked(int position, View view, Alarm alarm);
}
