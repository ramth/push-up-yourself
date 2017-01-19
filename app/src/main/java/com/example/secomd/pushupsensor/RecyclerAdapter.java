package com.example.secomd.pushupsensor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;

/**
 * Created by secomd on 12/13/2016.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private String[] mDataset;
    private OnItemClick onItemClick;
    protected CursorAdapter cursorAdapter;
    protected final Context mcontext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextView;

        /*public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }*/


        public ViewHolder(View v) {
            super(v);
            TextView tv = (TextView) v.findViewById(R.id.textClock2);
            ImageButton ib = (ImageButton) v.findViewById(R.id.imbutton);
            tv.setClickable(true);
            tv.setOnClickListener(this);
            ib.setClickable(true);
            ib.setOnClickListener(this);

        }
        @SuppressWarnings("deprecation")
        @Override
        public void onClick(View v) {
            if (onItemClick!=null) {
                int flag = 1;

                if (v instanceof TextView) {
                    flag = 0;
                } else if (v instanceof ImageView) {
                    flag = 2;
                }

                onItemClick.onItemClicked(getPosition(), flag);

            }
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapter(Context context) {
        this.mcontext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        //View v = LayoutInflater.from(parent.getContext())
        // .inflate(R.layout.textview, parent, false);
        View v = cursorAdapter.newView(mcontext, null, parent); //offloading to newView
        // set the view's size, margins, paddings and layout parameters
        //v.setOnClickListener(onClickListener);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]); <--- replace this

        cursorAdapter.getCursor().moveToPosition(position);
        cursorAdapter.bindView(holder.itemView, mcontext, cursorAdapter.getCursor());

    }

    @SuppressWarnings("SameParameterValue")
    protected void setupCursorAdapter(Cursor cursor, int flags, final int resource, final boolean attachToRoot) {
        this.cursorAdapter = new CursorAdapter(mcontext, cursor, flags) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.textview, parent, false);
                // set the view's size, margins, paddings and layout parameters
                //v.setOnClickListener(onClickListener);
                return v;

            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                // Bind cursor to our ViewHolder
                TextView textClock = (TextView) view.findViewById(R.id.textClock2);
                String timeStr = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("Hour"))) + ":" +
                        String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("Minute")));
                textClock.setText(timeStr);

            }
        };
    }

    public void swapCursor(Cursor cursor) {
        this.cursorAdapter.swapCursor(cursor);
        notifyDataSetChanged();
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cursorAdapter.getCount();
    }


}
