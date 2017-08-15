package com.example.mrwen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.example.mrwen.bean.Exercise;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ExerciseActivity extends Activity {
    @Bind(R.id.bt_back)
    Button bt_back;
    @Bind(R.id.bt_add_new_knowledge)
    Button bt_add_new_knowledge;
    @Bind(R.id.bt_relate_knowledge)
    Button bt_relate_knowledge;
    @Bind(R.id.bt_upload_exercise)
    Button bt_upload_exercise;
    @Bind(R.id.bt_manage_knowledge)
    Button bt_manage_knowledge;
    @Bind(R.id.bt_manage_exercise)
    Button bt_manage_exercise;
    @Bind(R.id.bt_show_knowledge_graph)
    Button bt_show_knowledge_graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_exercise);
        ButterKnife.bind(this);

        bt_add_new_knowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadKnowledgeIntent = new Intent(ExerciseActivity.this, KnowledgeUploadActivity.class);
                startActivity(uploadKnowledgeIntent);
            }
        });

        bt_relate_knowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent relateKnowledgeIntent = new Intent(ExerciseActivity.this, KnowledgeRelationActivity.class);
                startActivity(relateKnowledgeIntent);
            }
        });

        bt_upload_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadExerciseIntent = new Intent(ExerciseActivity.this, ExerciseUploadActivity.class);
                startActivity(uploadExerciseIntent);
            }
        });

        bt_manage_knowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manageKnowledgeIntent = new Intent(ExerciseActivity.this, KnowledgeManageActivity.class);
                startActivity(manageKnowledgeIntent);
            }
        });

        bt_manage_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manageExercisesIntent = new Intent(ExerciseActivity.this, ExerciseManageActivity.class);
                startActivity(manageExercisesIntent);
            }
        });

        bt_show_knowledge_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent knowledgeGraphIntent = new Intent(ExerciseActivity.this, KnowledgeGraphActivity.class);
                startActivity(knowledgeGraphIntent);
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
