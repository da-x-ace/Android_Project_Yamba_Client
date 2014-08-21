package com.cisco.prankuryamba;



import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.cisco.prankuryamba.TimelineContract.Columns.*;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class TimelineDetailsFragment extends Fragment {

    private static final String TAG = "prankgup.yamba." + TimelineDetailsFragment.class.getSimpleName();
    private TextView textViewMessage;
    private TextView textViewTime;

    private long rowId;

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public TimelineDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_timeline_details, container, false);
        textViewMessage = (TextView) layout.findViewById(R.id.textViewMessage);
        textViewTime = (TextView) layout.findViewById(R.id.textViewTime);
        update(rowId);
        return layout;
    }

    //For content provider you need a resolver
    public void update(Long id){
        if(id == null)
            return;
        Uri uri = ContentUris.withAppendedId(TimelineContract.CONTENT_URI, id);
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        if (!cursor.moveToFirst()){
            Log.e(TAG, "unable to find row "+id);
            return;
        }

        textViewMessage.setText(cursor.getString(cursor.getColumnIndex(MESSAGE)));
        Long time = cursor.getLong(cursor.getColumnIndex(TIME_CREATED));
        CharSequence friendlyTime = DateUtils.getRelativeTimeSpanString(time,
                                    System.currentTimeMillis(), 0);
        textViewTime.setText(friendlyTime);
    }
}
