package c4.calcapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class CalculatorActivity extends Activity
{
   private Map<Button, Integer> numButtons = new HashMap<>();
   private BtnListener btnListener = new BtnListener();
   private TextView display;
   private StringBuilder displayStr = new StringBuilder("");
   //private Double result = 0.0;
   private int displayValue = 0;
   private long resultValue = 0;
   private String lastOperation = "";

   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      setup(savedInstanceState);
   }

   @Override
   protected void onSaveInstanceState(Bundle currentState)
   {
      super.onSaveInstanceState(currentState);
      currentState.putString("display",(String)display.getText());
      currentState.putLong("resultValue", resultValue);
      currentState.putString("lastOperation",lastOperation);
   }

   @Override
   public void onDestroy()
   {
      super.onDestroy();
   }


   private void setup(final Bundle lastState)
   {
      if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
         findViewById(R.id.spacer1).setLayoutParams(
                 new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0));
      }

      display = (TextView) findViewById(R.id.display);

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

      if(lastState != null){
         displayStr.append(lastState.getString("display"));
         display.setText(displayStr);
         resultValue = lastState.getInt("resultValue");
         lastOperation = lastState.getString("lastOperation");
      }

   }

   private class BtnListener implements View.OnClickListener
   {

      @Override
      public void onClick(View v)
      {
         if (numButtons.containsKey(v)) {
            displayStr.append(numButtons.get(v));
            if (displayStr.length() < 9) {
               display.setText(displayStr);
            }
         }

         switch (v.getId()) {
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
         displayValue = Integer.parseInt((String) display.getText());

         try {

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
         }catch(ArithmeticException e){
            resultValue = 0;
         }

         displayStr.setLength(0);
         display.setText(String.valueOf(resultValue));
         lastOperation = operation;
      }
   }

}
