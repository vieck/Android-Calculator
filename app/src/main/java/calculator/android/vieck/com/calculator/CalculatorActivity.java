package calculator.android.vieck.com.calculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;


public class CalculatorActivity extends ActionBarActivity {
    private static final String DEBUG_TAG = "DEBUG";

    //Class for ProcessArrayList
    ProcessArrayList processObject = new ProcessArrayList();

    //Booleans for AsyncTask
    private boolean setTextEqualToNumber = true;
    private boolean pushNumber = true;
    private boolean guiPutNumberFirst = true;
    private boolean addOperator = true;
    private boolean insideParenthesis = false;
    private boolean addParenthesis = true;
    private boolean error = false;

    //Integers to keep track of parenthesis
    private int openBracket = 0;
    public int closedBracket = 0;

    //Constants for the viewflipper
    public final int THEME_FRAGMENT_INTENT_KEY = 0;
    public final int THEME_DO_NOTHING_VALUE = -1;
    public final int THEME_DEFAULT_VALUE = 0;
    public final int THEME_INVERTED_VALUE = 1;
    public final int THEME_RED_GLOW_VALUE = 2;
    public final int THEME_GREEN_GLOW_VALUE = 3;
    public final int THEME_YELLOW_GLOW_VALUE = 4;
    public final int THEME_ORANGE_GLOW_VALUE = 5;
    public final int THEME_BLUE_GLOW_VALUE = 6;
    public final int THEME_PURPLE_GLOW_VALUE = 7;
    public final int THEME_PINK_GLOW_VALUE = 8;

    //AsyncTasks so I can sync them
    private AsyncTask<String, String, String> MathTask;
    private AsyncTask<String, String, String> GUITask;

    //TextViews
    TextView answerTextView;
    TextView mathTextView;

    //ArrayList for displaying text
    ArrayList<String> arrayList;

    //Strings for numbers
    String number = "";
    String primaryText = "";

    //ViewFlipper to be able to flip through views
    private ViewFlipper viewFlipper;

    //SharedPreferences Variables
    SharedPreferences myPrefs;
    SharedPreferences.Editor editor;
    final String MY_PREFS = "Calculator_Preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            arrayList = new ArrayList<>();
        editor = getSharedPreferences(
                MY_PREFS, Context.MODE_PRIVATE).edit();
        myPrefs = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_calculator_default);
        viewFlipper = (ViewFlipper) this.findViewById(R.id.viewflipperlayout);
        viewFlipper.setDisplayedChild(myPrefs.getInt("Layout",0));
        TextView tempTextView = (TextView) findViewById(myPrefs.getInt("FirstTextView", R.id.editNumbers));
        if (tempTextView != null) {
            answerTextView = tempTextView;
        } else {
            answerTextView = (TextView) findViewById(R.id.editNumbers);
        }
        tempTextView = (TextView) findViewById(myPrefs.getInt("SecondTextView", R.id.editHint));
        if (tempTextView != null) {
            mathTextView = tempTextView;
        } else {
            mathTextView = (TextView) findViewById(R.id.editHint);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calculator, menu);
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("setTextEqualToNumber", setTextEqualToNumber);
        savedInstanceState.putBoolean("pushNumber", pushNumber);
        savedInstanceState.putBoolean("Gui Put Number First", guiPutNumberFirst);
        savedInstanceState.putBoolean("addOperator", addOperator);
        savedInstanceState.putBoolean("Inside Parenthesis", insideParenthesis);
        savedInstanceState.putBoolean("add Parenthesis", addParenthesis);
        savedInstanceState.putBoolean("Error", error);
        savedInstanceState.putString("number", number);
        savedInstanceState.putInt("Open Bracket", openBracket);
        savedInstanceState.putInt("Closed Bracket", closedBracket);
        savedInstanceState.putStringArrayList("ArrayList", arrayList);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(DEBUG_TAG,"Saved instance state was not null");
        arrayList = savedInstanceState.getStringArrayList("arrayList");
        setTextEqualToNumber = savedInstanceState.getBoolean("setTextEqualToNumber");
        pushNumber = savedInstanceState.getBoolean("pushNumber");
        guiPutNumberFirst = savedInstanceState.getBoolean("Gui Put Number First");
        addOperator = savedInstanceState.getBoolean("addOperator");
        insideParenthesis = savedInstanceState.getBoolean("insideParenthesis");
        addParenthesis = savedInstanceState.getBoolean("add Parenthesis");
        error = savedInstanceState.getBoolean("Error");
        openBracket = savedInstanceState.getInt("Open Bracket");
        closedBracket = savedInstanceState.getInt("Closed Bracket");
        number = savedInstanceState.getString("number");
    }

    /*
    ** Method for the themeActivity result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(DEBUG_TAG, "Result code: " + resultCode);
        myPrefs = this.getSharedPreferences(
                "calculator.android.vieck.com.calculator", Context.MODE_PRIVATE);
        if (requestCode == THEME_FRAGMENT_INTENT_KEY) {
            switch (resultCode) {
                case THEME_INVERTED_VALUE:
                    viewFlipper.setDisplayedChild(1);
                    answerTextView = (TextView) findViewById(R.id.invertedEditNumbers);
                    mathTextView = (TextView) findViewById(R.id.invertedEditHint);
                    GUITask = new GUIThread().execute();
                    editor.putInt("Layout", 1);
                    editor.putInt("FirstTextView", R.id.invertedEditNumbers);
                    editor.putInt("SecondTextView", R.id.invertedEditHint);
                    editor.commit();
                    break;
                case THEME_RED_GLOW_VALUE:
                    viewFlipper.setDisplayedChild(2);
                    answerTextView = (TextView) findViewById(R.id.redEditNumbers);
                    mathTextView = (TextView) findViewById(R.id.redEditHint);
                    GUITask = new GUIThread().execute();
                    editor.putInt("Layout", 2);
                    editor.putInt("FirstTextView", R.id.redEditNumbers);
                    editor.putInt("SecondTextView", R.id.redEditHint);
                    editor.commit();
                    break;
                case THEME_GREEN_GLOW_VALUE:
                    viewFlipper.setDisplayedChild(3);
                    answerTextView = (TextView) findViewById(R.id.greenEditNumbers);
                    mathTextView = (TextView) findViewById(R.id.greenEditHint);
                    GUITask = new GUIThread().execute();
                    editor.putInt("Layout", 3);
                    editor.putInt("FirstTextView", R.id.greenEditNumbers);
                    editor.putInt("SecondTextView", R.id.greenEditHint);
                    editor.commit();
                    break;
                case THEME_YELLOW_GLOW_VALUE:
                    viewFlipper.setDisplayedChild(4);
                    answerTextView = (TextView) findViewById(R.id.yellowEditNumbers);
                    mathTextView = (TextView) findViewById(R.id.yellowEditHint);
                    GUITask = new GUIThread().execute();
                    editor.putInt("Layout", 4);
                    editor.putInt("FirstTextView", R.id.yellowEditNumbers);
                    editor.putInt("SecondTextView", R.id.yellowEditHint);
                    editor.commit();
                    break;
                case THEME_ORANGE_GLOW_VALUE:
                    viewFlipper.setDisplayedChild(5);
                    answerTextView = (TextView) findViewById(R.id.orangeEditNumbers);
                    mathTextView = (TextView) findViewById(R.id.orangeEditHint);
                    GUITask = new GUIThread().execute();
                    editor.putInt("Layout", 5);
                    editor.putInt("FirstTextView", R.id.orangeEditNumbers);
                    editor.putInt("SecondTextView", R.id.orangeEditHint);
                    editor.commit();
                    break;
                case THEME_BLUE_GLOW_VALUE:
                    viewFlipper.setDisplayedChild(6);
                    answerTextView = (TextView) findViewById(R.id.blueEditNumbers);
                    mathTextView = (TextView) findViewById(R.id.blueEditHint);
                    GUITask = new GUIThread().execute();
                    editor.putInt("Layout", 6);
                    editor.putInt("FirstTextView", R.id.blueEditNumbers);
                    editor.putInt("SecondTextView", R.id.blueEditHint);
                    editor.commit();
                    break;
                case THEME_PURPLE_GLOW_VALUE:
                    viewFlipper.setDisplayedChild(7);
                    answerTextView = (TextView) findViewById(R.id.purpleEditNumbers);
                    mathTextView = (TextView) findViewById(R.id.purpleEditHint);
                    GUITask = new GUIThread().execute();
                    editor.putInt("Layout", 7);
                    editor.putInt("FirstTextView", R.id.purpleEditNumbers);
                    editor.putInt("SecondTextView", R.id.purpleEditHint);
                    editor.commit();
                    break;
                case THEME_PINK_GLOW_VALUE:
                    viewFlipper.setDisplayedChild(8);
                    answerTextView = (TextView) findViewById(R.id.pinkEditNumbers);
                    mathTextView = (TextView) findViewById(R.id.pinkEditHint);
                    GUITask = new GUIThread().execute();
                    editor.putInt("Layout", 8);
                    editor.putInt("FirstTextView", R.id.pinkEditNumbers);
                    editor.putInt("SecondTextView", R.id.pinkEditHint);
                    editor.commit();
                    break;

                case THEME_DEFAULT_VALUE:
                    viewFlipper.setDisplayedChild(0);
                    answerTextView = (TextView) findViewById(R.id.editNumbers);
                    mathTextView = (TextView) findViewById(R.id.editHint);
                    break;
                case THEME_DO_NOTHING_VALUE:
                    break;
                default:
                    break;
            }
            GUITask = new GUIThread().execute();
            answerTextView.setText(primaryText);
        }
    }
    /*
    * @Params View view
    *
    * Starts ThemeActivity
    */
    public void startTheme(View view) {
        Intent intent = new Intent(this, ThemeActivity.class);
        startActivityForResult(intent, THEME_FRAGMENT_INTENT_KEY);
        setTextEqualToNumber = false;
        if (!answerTextView.getText().equals("") && answerTextView != null) {
            primaryText = answerTextView.getText().toString();
        } else {
            primaryText = "0";
        }
    }

    /*
    * Determines which button is pressed
    */
    public void onClick(View view) {
        if (error) {
            answerTextView.setText("0");
            arrayList.clear();
            mathTextView.setText("0");
            error = false;
        }
        switch (view.getId()) {
            case R.id.zero_button:
            case R.id.inverted_zero_button:
            case R.id.red_zero_button:
            case R.id.green_zero_button:
            case R.id.yellow_zero_button:
            case R.id.orange_zero_button:
            case R.id.blue_zero_button:
            case R.id.purple_zero_button:
            case R.id.pink_zero_button:
                number += "0";
                GUITask = new GUIThread().execute();
                addOperator = true;
                break;
            case R.id.one_button:
            case R.id.inverted_one_button:
            case R.id.red_one_button:
            case R.id.green_one_button:
            case R.id.yellow_one_button:
            case R.id.orange_one_button:
            case R.id.blue_one_button:
            case R.id.purple_one_button:
            case R.id.pink_one_button:
                number += "1";
                GUITask = new GUIThread().execute();
                addOperator = true;
                break;
            case R.id.two_button:
            case R.id.inverted_two_button:
            case R.id.red_two_button:
            case R.id.green_two_button:
            case R.id.yellow_two_button:
            case R.id.orange_two_button:
            case R.id.blue_two_button:
            case R.id.purple_two_button:
            case R.id.pink_two_button:
                number += "2";
                GUITask = new GUIThread().execute();
                addOperator = true;
                break;
            case R.id.three_button:
            case R.id.inverted_three_button:
            case R.id.red_three_button:
            case R.id.green_three_button:
            case R.id.yellow_three_button:
            case R.id.orange_three_button:
            case R.id.blue_three_button:
            case R.id.purple_three_button:
            case R.id.pink_three_button:
                number += "3";
                GUITask = new GUIThread().execute();
                addOperator = true;
                break;
            case R.id.four_button:
            case R.id.inverted_four_button:
            case R.id.red_four_button:
            case R.id.green_four_button:
            case R.id.yellow_four_button:
            case R.id.orange_four_button:
            case R.id.blue_four_button:
            case R.id.purple_four_button:
            case R.id.pink_four_button:
                number += "4";
                GUITask = new GUIThread().execute();
                addOperator = true;
                break;
            case R.id.five_button:
            case R.id.inverted_five_button:
            case R.id.red_five_button:
            case R.id.green_five_button:
            case R.id.yellow_five_button:
            case R.id.orange_five_button:
            case R.id.blue_five_button:
            case R.id.purple_five_button:
            case R.id.pink_five_button:
                number += "5";
                GUITask = new GUIThread().execute();
                addOperator = true;
                break;
            case R.id.six_button:
            case R.id.inverted_six_button:
            case R.id.red_six_button:
            case R.id.green_six_button:
            case R.id.yellow_six_button:
            case R.id.orange_six_button:
            case R.id.blue_six_button:
            case R.id.purple_six_button:
            case R.id.pink_six_button:
                number += "6";
                GUITask = new GUIThread().execute();
                addOperator = true;
                break;
            case R.id.seven_button:
            case R.id.inverted_seven_button:
            case R.id.red_seven_button:
            case R.id.green_seven_button:
            case R.id.yellow_seven_button:
            case R.id.orange_seven_button:
            case R.id.blue_seven_button:
            case R.id.purple_seven_button:
            case R.id.pink_seven_button:
                number += "7";
                GUITask = new GUIThread().execute();
                addOperator = true;
                break;
            case R.id.eight_button:
            case R.id.inverted_eight_button:
            case R.id.red_eight_button:
            case R.id.green_eight_button:
            case R.id.yellow_eight_button:
            case R.id.orange_eight_button:
            case R.id.blue_eight_button:
            case R.id.purple_eight_button:
            case R.id.pink_eight_button:
                number += "8";
                GUITask = new GUIThread().execute();
                addOperator = true;
                break;
            case R.id.nine_button:
            case R.id.inverted_nine_button:
            case R.id.red_nine_button:
            case R.id.green_nine_button:
            case R.id.yellow_nine_button:
            case R.id.orange_nine_button:
            case R.id.blue_nine_button:
            case R.id.purple_nine_button:
            case R.id.pink_nine_button:
                number += "9";
                GUITask = new GUIThread().execute();
                addOperator = true;
                break;
            case R.id.decimal_button:
            case R.id.inverted_decimal_button:
            case R.id.red_decimal_button:
            case R.id.green_decimal_button:
            case R.id.yellow_decimal_button:
            case R.id.orange_decimal_button:
            case R.id.blue_decimal_button:
            case R.id.purple_decimal_button:
            case R.id.pink_decimal_button:
                number += ".";
                GUITask = new GUIThread().execute();
                addOperator = true;
                break;
            case R.id.inParenthesis:
            case R.id.invertedInParenthesis:
            case R.id.redInParenthesis:
            case R.id.greenInParenthesis:
            case R.id.yellowInParenthesis:
            case R.id.orangeInParenthesis:
            case R.id.blueInParenthesis:
            case R.id.purpleInParenthesis:
            case R.id.pinkInParenthesis:
                if (arrayList.size()==0 || !number.equals("")){
                    arrayList.add(number);
                    number = "";
                }
                arrayList.add("(");
                setTextEqualToNumber = false;
                addOperator = true;
                    guiPutNumberFirst = false;
                pushNumber = true;
                openBracket++;
                GUITask = new GUIThread().execute();
                addOperator = false;
                break;
            case R.id.outParenthesis:
            case R.id.invertedOutParenthesis:
            case R.id.redOutParenthesis:
            case R.id.greenOutParenthesis:
            case R.id.yellowOutParenthesis:
            case R.id.orangeOutParenthesis:
            case R.id.blueOutParenthesis:
            case R.id.purpleOutParenthesis:
            case R.id.pinkOutParenthesis:
                if (!number.equals(""))
                    addParenthesis = true;
                if ((openBracket > closedBracket) && (addParenthesis)) {
                    insideParenthesis = true;
                    if (!number.equals("")) {
                        arrayList.add(number);
                        number = "";
                    }
                    pushNumber = false;
                    arrayList.add(")");
                    closedBracket++;
                    GUITask = new GUIThread().execute();
                    addOperator = true;
                    if (openBracket == closedBracket) {
                        insideParenthesis = false;
                    }
                }
                break;
            case R.id.equal_button:
            case R.id.inverted_equal_button:
            case R.id.red_equal_button:
            case R.id.green_equal_button:
            case R.id.yellow_equal_button:
            case R.id.orange_equal_button:
            case R.id.blue_equal_button:
            case R.id.purple_equal_button:
            case R.id.pink_equal_button:
                if (!number.equals(""))
                    arrayList.add(number);
                MathTask = new MathThread().execute();
                guiPutNumberFirst = true;
                pushNumber = false;
                number = "";
                addOperator = true;
                break;
            case R.id.add_button:
            case R.id.inverted_add_button:
            case R.id.red_add_button:
            case R.id.green_add_button:
            case R.id.yellow_add_button:
            case R.id.orange_add_button:
            case R.id.blue_add_button:
            case R.id.purple_add_button:
            case R.id.pink_add_button:
                Log.d(DEBUG_TAG, "PushNumber: " + pushNumber);
                if (addOperator) {
                    if (pushNumber) {
                        arrayList.add(number);
                        number = "";
                    }
                    arrayList.add("+");
                    addParenthesis = false;
                    pushNumber = true;
                    guiPutNumberFirst = false;
                    GUITask = new GUIThread().execute();
                    addOperator = false;
                }
                break;
            case R.id.subtract_button:
            case R.id.inverted_subtract_button:
            case R.id.red_subtract_button:
            case R.id.green_subtract_button:
            case R.id.yellow_subtract_button:
            case R.id.orange_subtract_button:
            case R.id.blue_subtract_button:
            case R.id.purple_subtract_button:
            case R.id.pink_subtract_button:
                if (addOperator) {
                    if (pushNumber) {
                        arrayList.add(number);
                        number = "";
                    }
                    arrayList.add("-");
                    guiPutNumberFirst = false;
                    addParenthesis = false;
                    pushNumber = true;
                    GUITask = new GUIThread().execute();
                    addOperator = false;
                }
                break;
            case R.id.multiply_button:
            case R.id.inverted_multiply_button:
            case R.id.red_multiply_button:
            case R.id.green_multiply_button:
            case R.id.yellow_multiply_button:
            case R.id.orange_multiply_button:
            case R.id.blue_multiply_button:
            case R.id.purple_multiply_button:
            case R.id.pink_multiply_button:
                if (addOperator) {
                    if (pushNumber) {
                        arrayList.add(number);
                        number = "";
                    }
                    arrayList.add("×");
                    guiPutNumberFirst = false;
                    addParenthesis = false;
                    pushNumber = true;
                    GUITask = new GUIThread().execute();
                    addOperator = false;
                }
                break;
            case R.id.divide_button:
            case R.id.inverted_divide_button:
            case R.id.red_divide_button:
            case R.id.green_divide_button:
            case R.id.yellow_divide_button:
            case R.id.orange_divide_button:
            case R.id.blue_divide_button:
            case R.id.purple_divide_button:
            case R.id.pink_divide_button:
                if (addOperator) {
                    if (pushNumber) {
                        arrayList.add(number);
                        number = "";
                    }
                    arrayList.add("÷");
                    guiPutNumberFirst = false;
                    addParenthesis = false;
                    pushNumber = true;
                    GUITask = new GUIThread().execute();
                    addOperator = false;
                }
                break;
            case R.id.back_button:
            case R.id.inverted_back_button:
            case R.id.red_back_button:
            case R.id.green_back_button:
            case R.id.yellow_back_button:
            case R.id.orange_back_button:
            case R.id.blue_back_button:
            case R.id.purple_back_button:
            case R.id.pink_back_button:
                if (!number.equals("")){
                    arrayList.add(number);
                    number = "";
                }
                if (arrayList.size() > 1) {
                    arrayList.remove(arrayList.size() - 1);
                } else {
                    arrayList.clear();
                }
                GUITask = new GUIThread().execute();
                break;
            case R.id.clear_button:
            case R.id.inverted_clear_button:
            case R.id.red_clear_button:
            case R.id.green_clear_button:
            case R.id.yellow_clear_button:
            case R.id.orange_clear_button:
            case R.id.blue_clear_button:
            case R.id.purple_clear_button:
            case R.id.pink_clear_button:
                arrayList.clear();
                answerTextView.setText("0");
                mathTextView.setText("0");
                number = "";
                pushNumber = true;
                guiPutNumberFirst = true;
                addOperator = true;
                setTextEqualToNumber = true;
                primaryText = answerTextView.getText().toString();
                break;
        }
    }

    protected class MathThread extends AsyncTask<String, String, String> {

        protected String doInBackground(String... params) {
            try {
                arrayList = processObject.processArray(arrayList);
                if (arrayList.contains("Error")){
                    number = "";
                    pushNumber = true;
                    guiPutNumberFirst = true;
                    addOperator = true;
                    setTextEqualToNumber = true;
                    error = true;
                }
                String output = arrayList.get(0);
                int decimalIndex = output.indexOf(".")+1;
                if (output.substring(decimalIndex,decimalIndex+1).equals("0") && output.substring(decimalIndex).length() == 1) {
                    output = output.substring(0, decimalIndex-1);
                }
                return output;
            } catch (Exception error) {
                return "Error";
            }
        }

        protected void onProgressUpdate(String... progress) {
            answerTextView.setText(arrayList.get(0));
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(DEBUG_TAG,"mathTextView: "+s);
            mathTextView.setText(s);
            answerTextView.setText(s);
        }
    }

    protected class GUIThread extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String display = "";
            if (guiPutNumberFirst) {
                Log.d(DEBUG_TAG, "Put Number first in GUIThread");
                for (int i = 0; i < arrayList.size(); i++) {
                    if (i == 0) {
                        Log.d(DEBUG_TAG,"Number: "+number);
                        display += number + " ";
                    }
                    Log.d(DEBUG_TAG,"ArrayList i: "+i+" "+arrayList.get(i));
                    display += arrayList.get(i) + " ";

                }
                Log.d(DEBUG_TAG, "Set GUI Text To: " + setTextEqualToNumber);
                if (setTextEqualToNumber || (arrayList.size() == 0 && !number.equals(""))) {
                    display = number;
                }
            } else {
                Log.d(DEBUG_TAG, "I'm in the regular one");
                for (String add : arrayList) {
                    display += add + " ";
                }
                //!containsParenth
                if ((pushNumber && !number.equals("")) || (!insideParenthesis && !number.equals(""))) {
                    display += number;
                }
            }
            if ((arrayList.equals("0") || arrayList.isEmpty()) && display == ""){
                display = "0";
            }
            Log.d(DEBUG_TAG,"display: "+display);
            Log.d(DEBUG_TAG, "GUI Thread ArrayList: " + arrayList.toString());
            return display;
        }

        @Override
        protected void onPostExecute(String display) {
            mathTextView.setText(display);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}

