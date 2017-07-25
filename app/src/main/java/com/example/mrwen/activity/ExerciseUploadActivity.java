package com.example.mrwen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ExerciseUploadActivity extends Activity {

    @Bind(R.id.bt_back)
    Button bt_back;
    @Bind(R.id.bt_upload_multiple_choices_exercise)
    Button bt_upload_multiple_choices_exercise;
    @Bind(R.id.bt_upload_fill_in_blank_exercise)
    Button bt_upload_fill_in_blank_exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_upload_exercise_type);

        ButterKnife.bind(this);

        bt_upload_multiple_choices_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadMultipleChoicesExerciseIntent = new Intent(ExerciseUploadActivity.this, UploadMultipleChoicesActivity.class);
                startActivity(uploadMultipleChoicesExerciseIntent);
            }
        });

        bt_upload_fill_in_blank_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadFillInBlankExerciseIntent = new Intent(ExerciseUploadActivity.this, UploadFillInBlankExerciseActivity.class);
                startActivity(uploadFillInBlankExerciseIntent);
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
