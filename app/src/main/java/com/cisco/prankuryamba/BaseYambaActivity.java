package com.cisco.prankuryamba;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by prankurgupta on 8/21/14.
 */
public class BaseYambaActivity extends Activity{

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflates the menu, this adds item to the action bas if it is present
        getMenuInflater().inflate(R.menu.yamba, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent settingsActivityIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsActivityIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
