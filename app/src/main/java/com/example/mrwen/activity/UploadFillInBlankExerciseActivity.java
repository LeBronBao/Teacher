package com.example.mrwen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mrwen.Utils.MyDialog;
import com.example.mrwen.Utils.PictureCut;
import com.example.mrwen.bean.Knowledge;
import com.example.mrwen.bean.UniversalResult;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UploadFillInBlankExerciseActivity extends Activity {
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;

    @Bind(R.id.bt_back)
    Button bt_back;
    @Bind(R.id.sp_subject)
    Spinner sp_subject;
    @Bind(R.id.sp_grade)
    Spinner sp_grade;
    @Bind(R.id.sp_knowledge)
    Spinner sp_knowledge;
    @Bind(R.id.bt_choose_knowledge_question)
    Button bt_choose_knowledge_question;
    @Bind(R.id.et_question)
    EditText et_question;
    @Bind(R.id.et_answer)
    EditText et_answer;
    @Bind(R.id.et_analysis)
    EditText et_analysis;
    @Bind(R.id.iv_question_image)
    ImageView iv_question_image;
    @Bind(R.id.bt_upload_fill_in_blank_exercise)
    Button bt_upload_fill_in_blank_exercise;

    private ArrayList<Knowledge> knowledgeList;
    private ArrayAdapter<String> adapter;
    private int grade;
    private String subject;
    private String knowledge;
    private String question;
    private String answer;
    private String analysis;

    private Bitmap photo;
    int imageChange=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_fill_in_blank_exercise);
        ButterKnife.bind(this);

        //未对科目和年级进行选择，则使用初始的科目和年级加载知识点
        grade = getIntGrade(sp_grade.getSelectedItem().toString());
        subject = sp_subject.getSelectedItem().toString();
        loadKnowledgeByInfo(grade, subject);

        final String[] grades = getResources().getStringArray(R.array.grade);
        final String[] subjects = getResources().getStringArray(R.array.subject);
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, new ArrayList<String>());
        adapter.setDropDownViewResource(R.layout.dropdown_style);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_choose_knowledge_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "请选择最能细致地描述该题目的知识点，若无请选择最相近的知识点", Toast.LENGTH_SHORT).show();
            }
        });

        sp_grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int grade = getIntGrade(grades[position]);
                String subject = sp_subject.getSelectedItem().toString();
                loadKnowledgeByInfo(grade, subject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String subject = subjects[position];
                int grade = getIntGrade(sp_grade.getSelectedItem().toString());
                loadKnowledgeByInfo(grade, subject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        iv_question_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });

        bt_upload_fill_in_blank_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade = getIntGrade(sp_grade.getSelectedItem().toString());
                subject = sp_subject.getSelectedItem().toString();
                question = et_question.getText().toString();
                answer = et_answer.getText().toString();
                if(sp_knowledge.getSelectedItem()==null){
                    knowledge = "";
                }else{
                    knowledge = sp_knowledge.getSelectedItem().toString();
                }
                analysis = et_analysis.getText().toString();
                if(question.equals("")||answer.equals("")||knowledge.equals("")){
                    Toast.makeText(UploadFillInBlankExerciseActivity.this, "知识点、题目或答案还未填写", Toast.LENGTH_SHORT).show();
                }else{
                    uploadFillInBlankExercise();
                }
            }
        });

    }

    //根据年级和科目加载知识标签
    private void loadKnowledgeByInfo(int grade, String subject){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final InterfaceTeacher getKnowledgeByInfo = retrofit.create(InterfaceTeacher.class);
        final Call<ArrayList<Knowledge>> call = getKnowledgeByInfo.getKnowledgeByInfo(grade, subject);
        call.enqueue(new Callback<ArrayList<Knowledge>>() {
            @Override
            public void onResponse(Call<ArrayList<Knowledge>> call, Response<ArrayList<Knowledge>> response) {
                if(response.body()!=null){
                    //每次更改科目和年级先将原来的Spinner清空
                    if(!adapter.isEmpty()){
                        adapter.clear();
                        sp_knowledge.setAdapter(adapter);
                    }
                    if(knowledgeList!=null)
                        knowledgeList.clear();
                    knowledgeList = response.body();
                    if(knowledgeList.size()!=0){
                        adapter.addAll(getKnowledgeLabel());
                        sp_knowledge.setAdapter(adapter);
                        Toast.makeText(UploadFillInBlankExerciseActivity.this, "获取相应知识点成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(UploadFillInBlankExerciseActivity.this, "该科目该年级下没有知识标签，请重新选择", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(UploadFillInBlankExerciseActivity.this, "获取知识失败，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Knowledge>> call, Throwable t) {
                Toast.makeText(UploadFillInBlankExerciseActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFillInBlankExercise(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Map<String, String> map = formMap();
        final InterfaceTeacher uploadFillInBlankExercise = retrofit.create(InterfaceTeacher.class);
        final Call<UniversalResult> call = uploadFillInBlankExercise.uploadFillInBlankExercise(map);
        call.enqueue(new Callback<UniversalResult>() {
            @Override
            public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                if(response.body()!=null){
                    UniversalResult universalResult = response.body();
                    if(universalResult.getResultCode()==1)
                        //上传题目图片
                        retrofitImageUpload(String.valueOf(universalResult.getId()));
                        Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UploadFillInBlankExerciseActivity.this, "上传题目失败，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UniversalResult> call, Throwable t) {
                Toast.makeText(UploadFillInBlankExerciseActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getIntGrade(String grade){
        int intGrade = 0;
        switch (grade){
            case "一年级":
                intGrade = 1;
                break;
            case "二年级":
                intGrade = 2;
                break;
            case "三年级":
                intGrade = 3;
                break;
            case "四年级":
                intGrade = 4;
                break;
            case "五年级":
                intGrade = 5;
                break;
            case "六年级":
                intGrade = 6;
                break;
            case "七年级":
                intGrade = 7;
                break;
            case "八年级":
                intGrade = 8;
                break;
            case "九年级":
                intGrade = 9;
                break;
        }
        return intGrade;
    }

    //获取所有Knowledge对象的标签
    private ArrayList<String> getKnowledgeLabel(){
        ArrayList<String> list = new ArrayList<>();
        for(Knowledge knowledge:knowledgeList){
            list.add(knowledge.getLabel());
        }
        return list;
    }

    //将需要上传的数据存入map
    private Map<String, String> formMap(){
        Map<String, String> map = new HashMap<>();
        map.put("grade", String.valueOf(grade));
        map.put("subject", subject);
        map.put("knowledge", knowledge);
        map.put("question", question);
        map.put("answer", answer);
        map.put("analysis", analysis);
        return map;
    }

    //选择需要上传的题目图片
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传题目图片");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                        imageChange=1;
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            photo = PictureCut.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            iv_question_image.setImageBitmap(photo);
        }
    }

    private void retrofitImageUpload(String exerciseId){
        if(imageChange==1) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            RequestBody body = RequestBody.create(MediaType.parse("*.png"), outputStream.toByteArray());

            final MyDialog alertDialog = new MyDialog();
            Retrofit retrofitImageUpload = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.baseURL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            InterfaceTeacher imageUpload = retrofitImageUpload.create(InterfaceTeacher.class);
            final Call<UniversalResult> call = imageUpload.fillInBlankExerciseImageUpload(exerciseId, body);
            call.enqueue(new Callback<UniversalResult>() {
                @Override
                public void onResponse(Call<UniversalResult> call, Response<UniversalResult> response) {
                    if(response.body().getResultCode()==1){

                    }else {
                        alertDialog.showAlertDialog(UploadFillInBlankExerciseActivity.this,"上传头像失败");
                    }
                }

                @Override
                public void onFailure(Call<UniversalResult> call, Throwable t) {
                    alertDialog.showAlertDialog(UploadFillInBlankExerciseActivity.this,t.toString());
                }
            });
        }
    }

}
