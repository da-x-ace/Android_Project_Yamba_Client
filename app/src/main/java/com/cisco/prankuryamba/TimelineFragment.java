package com.cisco.prankuryamba;



import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


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
    private Handler handler;

    //Inorder for activity to decide what to do with this piece of fragment
    public static interface TimelineItemSelectionCallback{
        public void onTimelineItemSelected(long id);
    }

    private static final String TAG = "prankgup.yamba." + TimelineFragment.class.getSimpleName();
    private SimpleCursorAdapter adapter;
    private SimpleCursorAdapter.ViewBinder rowViewBinder = new SimpleCursorAdapter.ViewBinder() {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            if (view.getId() == R.id.textViewTime){
                Long time = cursor.getLong(columnIndex);
                CharSequence friendlyTime = DateUtils.getRelativeTimeSpanString(time,
                                            System.currentTimeMillis(), 0);
                TextView textView = (TextView) view;
                textView.setText(friendlyTime);
                return  true;
            }
            return false;
        }
    };

    public TimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        //If you don't mention it The OptionsMenu wont be displayed
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    private Runnable refresh = new Runnable() {
        @Override
        public void run() {
            adapter.notifyDataSetChanged();
            handler.postDelayed(refresh, 5000);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(refresh,5000);
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
        adapter.setViewBinder(rowViewBinder);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.timeline, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh:
                Intent refreshIntent = new Intent(getActivity(), YambaTimeline.class);
                getActivity().startService(refreshIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (getActivity() instanceof TimelineItemSelectionCallback){
            TimelineItemSelectionCallback callback = (TimelineItemSelectionCallback) getActivity();
            callback.onTimelineItemSelected(id);
        }
        super.onListItemClick(l, v, position, id);
    }
}
