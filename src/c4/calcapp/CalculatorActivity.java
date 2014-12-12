package c4.calcapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class CalculatorActivity extends Activity
{
   private static final String LOG_TAG = "CalculatorActivity";
   private static final int NUM_DIGITS = 10;
   //private Handler handler;
   private Map<Button, Integer> numButtons = new HashMap<>();
   private BtnListener btnListener = new BtnListener();
   private TextView display;
   private StringBuilder displayStr = new StringBuilder("");
   //private Double result = 0.0;
   private long displayValue = 0;
   private long resultValue = 0;
   private String lastOperation = "";
   private SharedPreferences prefs;


   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      Log.d(LOG_TAG, "onCreate()");
      prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
      setContentView(R.layout.main);
      setup(savedInstanceState);
   }


   @Override
   protected void onResume()
   {
      Log.d(LOG_TAG, "onResume()");
      super.onResume();
   }

   @Override
   protected void onPause()
   {
      Log.d(LOG_TAG, "onPause()");
      super.onPause();
   }

   @Override
   protected void onSaveInstanceState(Bundle currentState)
   {
      Log.d(LOG_TAG, "onSaveInstanceState()");
      super.onSaveInstanceState(currentState);
      currentState.putString("display", (String) display.getText());
      currentState.putLong("resultValue", resultValue);
      currentState.putString("lastOperation", lastOperation);
   }

   @Override
   protected void onDestroy()
   {
      Log.d(LOG_TAG, "onDestroy()");
      SharedPreferences.Editor editor = prefs.edit();
      editor.putString("display", (String) display.getText());
      editor.apply();
      super.onDestroy();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {
      menu.add(Menu.NONE, 1, 1, R.string.item1);
      menu.add(Menu.NONE, 2, 2, R.string.item2);
      menu.add(Menu.NONE, 3, 3, R.string.item3);
      menu.add(Menu.NONE, 4, 4, R.string.item4);

      // Inflate the menu items for use in the action bar
      /*
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.main, menu);
      */
      return super.onCreateOptionsMenu(menu);
   }


   @Override
   public boolean onMenuItemSelected(int featureId, MenuItem item)
   {
      switch (item.getItemId()) {
         case 1/*R.id.action_item1*/:
            showToast(getString(R.string.item1), 5);
            break;
         case 2/*R.id.action_item2*/:
            showToast(getString(R.string.item2), 5);
            switchLanguage();
            break;
         case 3:
            showToast(getString(R.string.item3), 5);
            break;
         case 4:
            showToast(getString(R.string.item4), 5);
            break;
         default:
            super.onMenuItemSelected(featureId, item);
      }

      return true;
   }

   private void setup(final Bundle lastState)
   {
      ActionBar actionBar = getActionBar();

      if (actionBar != null) {
         actionBar.setDisplayShowTitleEnabled(false);
      }

      if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
         findViewById(R.id.spacer1).setLayoutParams(
                 new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0));
         if (actionBar != null) {
            actionBar.hide();
         }
      }

      Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");

      display = (TextView) findViewById(R.id.display);
      display.setTypeface(tf);

      numButtons.put((Button) findViewById(R.id.btn0), 0);
      numButtons.put((Button) findViewById(R.id.btn1), 1);
      numButtons.put((Button) findViewById(R.id.btn2), 2);
      numButtons.put((Button) findViewById(R.id.btn3), 3);
      numButtons.put((Button) findViewById(R.id.btn4), 4);
      numButtons.put((Button) findViewById(R.id.btn5), 5);
      numButtons.put((Button) findViewById(R.id.btn6), 6);
      numButtons.put((Button) findViewById(R.id.btn7), 7);
      numButtons.put((Button) findViewById(R.id.btn8), 8);
      numButtons.put((Button) findViewById(R.id.btn9), 9);

      for (Button btn : numButtons.keySet()) {
         btn.setOnClickListener(btnListener);
      }

      findViewById(R.id.btn_clear).setOnClickListener(btnListener);
      findViewById(R.id.btn_plus).setOnClickListener(btnListener);
      findViewById(R.id.btn_minus).setOnClickListener(btnListener);
      findViewById(R.id.btn_mul).setOnClickListener(btnListener);
      findViewById(R.id.btn_div).setOnClickListener(btnListener);
      findViewById(R.id.btn_dot).setOnClickListener(btnListener);


      if (lastState != null) {
         displayStr.append(lastState.getString("display"));
         display.setText(displayStr);
         resultValue = lastState.getLong("resultValue");
         lastOperation = lastState.getString("lastOperation");
      } else {
         if (prefs.contains("display")) {
            display.setText(prefs.getString("display", "0"));
         }
      }

   }

   private void switchLanguage()
   {
      Configuration config = getResources().getConfiguration();

      if (config.locale.equals(Locale.ENGLISH)) {
         Log.d("", "switch to german");
         config.locale = Locale.GERMAN;
      } else {
         Log.d("", "switch to english");
         config.locale = Locale.ENGLISH;
      }

      getResources().updateConfiguration(config, getResources().getDisplayMetrics());
      Intent refresh = new Intent(this, CalculatorActivity.class);
      startActivity(refresh);
      finish();
   }

   private void showToast(final String toastMsg, final int secs)
   {
      final Context ctx = getApplicationContext();
      final Toast toast = Toast.makeText(ctx, toastMsg, Toast.LENGTH_LONG);

      new Thread(new Runnable()
      {
         @Override
         public void run()
         {
            int s = secs;
            try {
               while (s-- > 0) {
                  runOnUiThread(new Runnable()
                  {
                     @Override
                     public void run()
                     {
                        toast.show();
                     }
                  });
                  Thread.sleep(1000);
               }
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
            toast.cancel();
         }
      }).start();
   }

   private class BtnListener implements View.OnClickListener
   {
      @Override
      public void onClick(View view)
      {
         Button btn = (Button) view;

         if (numButtons.containsKey(btn)) {
            displayStr.append(numButtons.get(btn));
            if (displayStr.length() <= NUM_DIGITS) {
               display.setText(displayStr);
            }
         }

         switch (btn.getId()) {
            case R.id.btn_clear:
               displayStr.setLength(0);
               displayValue = 0;
               resultValue = 0L;
               display.setText("0");
               lastOperation = "ce";
               break;

            case R.id.btn_plus:
               execOperation("plus");
               break;

            case R.id.btn_minus:
               execOperation("minus");
               break;

            case R.id.btn_mul:
               execOperation("mul");
               break;

            case R.id.btn_div:
               execOperation("div");
               break;

            case R.id.btn_dot:
               execOperation("=");
               break;
         }

      }


      private void execOperation(final String operation)
      {

         try {

            displayValue = Long.parseLong((String) display.getText());

            switch (lastOperation) {
               case "plus":
                  resultValue += displayValue;
                  break;
               case "minus":
                  resultValue -= displayValue;
                  break;
               case "mul":
                  resultValue *= displayValue;
                  break;
               case "div":
                  resultValue /= displayValue;
                  break;
               default:
                  resultValue = displayValue;
                  break;
            }
         } catch (ArithmeticException e) {
            resultValue = 0;
         }

         displayStr.setLength(0);

         if (resultValue > Integer.MAX_VALUE || resultValue < Integer.MIN_VALUE) {
            display.setText(resultValue > Integer.MAX_VALUE ? "Inf" : "-Inf");
            lastOperation = "";
         } else {
            display.setText(String.valueOf(resultValue));
            lastOperation = operation;
         }
      }
   }

}
