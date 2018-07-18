package com.example.android.bakingbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.bakingbuddy.R;
import com.example.android.bakingbuddy.model.CookingStep;
import com.example.android.bakingbuddy.model.Recipe;

public class StepDetails extends AppCompatActivity {
    private static final String TAG = StepDetails.class.getSimpleName();
    public static final String KEY_INTENT_CLICKED_STEP = "clicked_step";

    private CookingStep mStep;
    private TextView mStepDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

//        Set member variables
        mStepDescriptionTextView = findViewById(R.id.tv_step_description);

//        Get incoming intent
        Intent inIntent = getIntent();
        if(inIntent != null) {
            mStep =  inIntent.getParcelableExtra(KEY_INTENT_CLICKED_STEP);
            mStepDescriptionTextView.setText(mStep.getDescription());
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedID = item.getItemId();

        switch (selectedID){
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                throw  new UnsupportedOperationException();
        }

        return super.onOptionsItemSelected(item);
    }
}
