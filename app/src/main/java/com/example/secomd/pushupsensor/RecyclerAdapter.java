package com.example.secomd.pushupsensor;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

/**
 * Created by secomd on 12/13/2016.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private String[] mDataset;
    private OnItemClick onItemClick;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextView;
        public View view;
        /*public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }*/

        public ViewHolder(View v) {
            super(v);
            v.setClickable(true);
            v.setOnClickListener(this);
            view = v;
        }
        @SuppressWarnings("deprecation")
        @Override
        public void onClick(View v) {
            if (onItemClick!=null) {
                onItemClick.onItemClicked(getPosition());
            }
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.textview, parent, false);
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
        TextView textClock =  (TextView) holder.view.findViewById(R.id.textClock2);
        textClock.setText(mDataset[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }


}
