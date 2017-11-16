package pope.burnedcaloriescalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public TextView caloriesNumTv;
    public TextView heightTv;
    public Spinner feetSpinner;
    public TextView milesNumTv;
    public TextView caloriesBurnedTv;
    public SeekBar seekBar;
    public Spinner inchSpinner;
    public TextView bmiTv;
    public TextView bmiNumTv;
    public EditText nameEt;
    public EditText weightEt;
    public SharedPreferences saved;

    public int feet = 0;
    public int inches = 0;
    public int miles = 0;
    public float calBurned = 0.0f;
    public float bmi = 0.0f;
    public float weight = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get reference
        caloriesNumTv = (TextView) findViewById(R.id.caloriesNumTv);
        heightTv = (TextView) findViewById(R.id.heightTv);
        milesNumTv = (TextView) findViewById(R.id.milesNumTv);
        feetSpinner = (Spinner) findViewById(R.id.feetSpinner);
        inchSpinner = (Spinner) findViewById(R.id.inchSpinner);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        caloriesBurnedTv = (TextView) findViewById(R.id.caloriesBurnedTv);
        bmiTv = (TextView) findViewById(R.id.bmiTv);
        bmiNumTv = (TextView) findViewById(R.id.bmiNumTv);
        nameEt = (EditText) findViewById(R.id.nameEt);
        weightEt = (EditText) findViewById(R.id.weightEt);

        //Set reference
        EditorAction onEdit = new EditorAction();
        weightEt.setOnEditorActionListener(onEdit);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                miles = seekBar.getProgress();
                milesNumTv.setText(String.format("%d",miles));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // set array adapter for feet spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.feetArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        feetSpinner.setAdapter(adapter);

        // aonymous inner class as the listener
        feetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                feet = Integer.parseInt((feetSpinner.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
        caloriesNumTv.setOnEditorActionListener(onEdit);
        bmiNumTv.setOnEditorActionListener(onEdit);

        saved = getSharedPreferences("saved", MODE_PRIVATE);

        // set array adapter for inch spinner
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this, R.array.inchArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        inchSpinner.setAdapter(adapter1);

        // aonymous inner class as the listener
        inchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inches = Integer.parseInt((inchSpinner.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

    }


    class EditorAction implements TextView.OnEditorActionListener{

        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_DONE) {
                calculate();
            }
            return false;
        }

    }
    public void calculate(){
        weight = Float.parseFloat(weightEt.getText().toString());


        calBurned = 0.75f * weight * miles;
        bmi = (weight * 703)/((12* feet + inches)*(12 * feet + inches));
        System.out.println(calBurned);
        System.out.println(bmi);
        caloriesNumTv.setText(String.format("%d", calBurned));
        bmiNumTv.setText(String.format("%d",bmi));
    }
    public void onPause() {
        SharedPreferences.Editor edit = saved.edit();
        edit.putFloat("weight", weight);
        edit.putInt("milesRanTv", miles);
        edit.putFloat("bmi", bmi);
        edit.apply();
        super.onPause();
    }
    public void onResume() {
        weight = saved.getFloat("weight", 0.0f);
        miles = saved.getInt("miles",1);
        bmi = saved.getFloat("bmi", 0.0f);

        weightEt.setText(""+weight);
        super.onResume();
    }
}
