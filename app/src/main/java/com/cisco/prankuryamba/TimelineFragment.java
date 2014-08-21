package com.cisco.prankuryamba;



import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class TimelineFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String[] FROM = {
            TimelineContract.Columns.MESSAGE,
            TimelineContract.Columns.TIME_CREATED
    };

    private static final int[] TO = {
            R.id.textViewMessage,
            R.id.textViewTime
    };
    private static final String TAG = "prankgup.yamba." + TimelineFragment.class.getSimpleName();
    private SimpleCursorAdapter adapter;

    public TimelineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_timeline, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        //Currently we will use a null cursor, we will provide the cursor on loadFinished
        //This just identifies the cursor and loads the TO fields from the FROM fields
        //displays on the layout.
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.friend_status, null, FROM, TO,
                                          CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        setListAdapter(adapter);
        //Here ID of the loader changes with different loader.
        //We have only 1 loader so use statically define it as 0
        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");
        return new CursorLoader(getActivity(), TimelineContract.CONTENT_URI, null,
                                null, null, TimelineContract.Columns.TIME_CREATED+ " desc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished");
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }
}
