package com.example.mrwen.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.mrwen.fragment.ClassFragment;
import com.example.mrwen.fragment.CourseFragment;
import com.example.mrwen.fragment.QuestionFragment;
import com.example.mrwen.interfaces.InterfaceTeacher;
import com.example.mrwen.staticClass.StaticInfo;

import java.util.UUID;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager mFragmentManager;
    private QuestionFragment questionFragment = null;
    private ClassFragment classFragment = null;
    private ConversationListFragment messageFragment = null;

    ImageView iv_nav_photo;
    TextView tv_nav_name;
    TextView tv_nav_signature;

    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        radioGroup=(RadioGroup)findViewById(R.id.radioGroup1);
        mFragmentManager=getSupportFragmentManager();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                hideFragment(transaction);
                switch (checkedId) {
                    case R.id.radioButton1:
                        if (classFragment == null) {
                            classFragment = new ClassFragment();
                            transaction.add(R.id.content_main, classFragment);
                        } else {
                            transaction.show(classFragment);
                        }
                        toolbar.getMenu().clear();
                        toolbar.inflateMenu(R.menu.menu_refresh);
                        break;
                    case R.id.radioButton2:
                        if (questionFragment == null) {
                            questionFragment = new QuestionFragment();
                            transaction.add(R.id.content_main, questionFragment);
                        } else {
                            transaction.show(questionFragment);
                        }
                        toolbar.getMenu().clear();
                        break;
                    case R.id.radioButton3:
                        if (messageFragment == null) {
                            messageFragment = new ConversationListFragment();
                            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                                    .appendPath("conversationlist")
                                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话，该会话聚合显示
                                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话，该会话非聚合显示
                                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置群组会话，该会话非聚合显示
                                    .build();
                            messageFragment.setUri(uri);
                            transaction.add(R.id.content_main, messageFragment);
                        } else {
                            transaction.show(messageFragment);
                        }
                        toolbar.getMenu().clear();
                        toolbar.inflateMenu(R.menu.menu_my_contact);
                        break;
                }
                transaction.commit();
            }

        });
        radioGroup.check(R.id.radioButton1);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //顶部信息
        View view = navigationView.getHeaderView(0);
        iv_nav_photo=(ImageView) view.findViewById(R.id.iv_nav_photo);
        tv_nav_name=(TextView)view.findViewById(R.id.tv_nav_name);
        tv_nav_signature=(TextView)view.findViewById(R.id.tv_nav_signature);

        //刷新顶部信息
        //iv_nav_photo.setImageURI(Uri.parse(StaticInfo.imageURL) );
        if(StaticInfo.imageURL!=null)
            Glide.with(this).load(getResources().getString(R.string.baseURL)+StaticInfo.imageURL)
                    .signature(new StringSignature(UUID.randomUUID().toString())).into(iv_nav_photo);
        tv_nav_name.setText(StaticInfo.realname);
        tv_nav_signature.setText(StaticInfo.signature);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.refresh_fragment:
                Intent refreshIntent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(refreshIntent);
                return true;
            case R.id.action_my_contact:
                Intent intentContact=new Intent(MainActivity.this,ContactActivity.class);
                startActivity(intentContact);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.nav_my_info){
            Intent intentPersonInfo = new Intent(MainActivity.this,PersonInfoActivity.class);
            startActivityForResult(intentPersonInfo,0);
        } else if (id == R.id.nav_my_answer) {
            Intent intentMyAnswer = new Intent(MainActivity.this,MyAnswersActivity.class);
            startActivity(intentMyAnswer);
        } else if (id == R.id.nav_setting) {
            Intent intentSetting = new Intent(MainActivity.this,SettingActivity.class);
            startActivity(intentSetting);
        } else if (id == R.id.nav_login_out) {
            Intent intentLogin = new Intent(MainActivity.this,LoginInActivity.class);
            startActivity(intentLogin);
            finish();
        } else if (id == R.id.nav_manage) {
            Intent intentPassRevise = new Intent(MainActivity.this,PassReviseActivity.class);
            startActivity(intentPassRevise);
            finish();
        } else if (id == R.id.nav_share) {
            Intent classIntent = new Intent(MainActivity.this, MyClassActivity.class);
            if(StaticInfo.classSum==0){     //判断该班主任是否已经创建班级
                classIntent.putExtra("cid", 1);
                startActivity(classIntent);
            }else{
                classIntent.putExtra("cid", 2);
                startActivity(classIntent);
            }
        }else if(id == R.id.nav_upload_exercise){
            Intent uploadExerciseIntent = new Intent(MainActivity.this, ExerciseActivity.class);
            startActivity(uploadExerciseIntent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //将fragment全部隐藏避免销毁
    public void hideFragment(FragmentTransaction transaction){
        if(classFragment != null)
            transaction.hide(classFragment);
        if(questionFragment != null)
            transaction.hide(questionFragment);
        if(messageFragment != null)
            transaction.hide(messageFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}

