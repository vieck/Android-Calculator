package calculator.android.vieck.com.calculator;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class ThemeActivity extends ActionBarActivity{
    int theme_number = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
    }

    public void themeClick (View view) {
        switch (view.getId()) {
            case R.id.default_theme_button:
                theme_number = 0;
                Log.d("Debug Tag", "Changed Theme to Default");
                break;
            case R.id.inverted_theme_button:
                theme_number = 1;
                Log.d("Debug Tag","Changed theme to inverted");
                break;
            case R.id.red_glow_theme_button:
                theme_number = 2;
                Log.d("Debug Tag", "Changed theme to red");
                break;
            case R.id.green_glow_theme_button:
                theme_number = 3;
                Log.d("Debug Tag", "Changed theme to green");
                break;
            case R.id.yellow_glow_theme_button:
                theme_number = 4;
                Log.d("Debug Tag", "Changed theme to yellow");
                break;
            case R.id.orange_glow_theme_button:
                theme_number = 5;
                Log.d("Debug Tag", "Changed theme to yellow");
                break;
            case R.id.blue_glow_theme_button:
                theme_number = 6;
                Log.d("Debug Tag", "Changed theme to blue");
                break;
            case R.id.purple_glow_theme_button:
                theme_number = 7;
                Log.d("Debug Tag", "Changed theme to purple");
                break;
            case R.id.pink_glow_theme_button:
                theme_number = 8;
                Log.d("Debug Tag", "Changed theme to pink");
                break;
            default:
                theme_number = -1;
                Log.d("Debug Tag", "Changed theme to saved");
        }
        setResult(theme_number);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_theme, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
