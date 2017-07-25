package com.example.mrwen.interfaces;

import com.example.mrwen.bean.Answer;
import com.example.mrwen.bean.ChatGroup;
import com.example.mrwen.bean.Exercise;
import com.example.mrwen.bean.FillInBlankExercise;
import com.example.mrwen.bean.FriendRequest;
import com.example.mrwen.bean.Info;
import com.example.mrwen.bean.InfoDetail;
import com.example.mrwen.bean.Knowledge;
import com.example.mrwen.bean.LoginInResult;
import com.example.mrwen.bean.MultipleChoicesExercise;
import com.example.mrwen.bean.QueryItem;
import com.example.mrwen.bean.RegisterResult;
import com.example.mrwen.bean.Result;
import com.example.mrwen.bean.RosterGroup;
import com.example.mrwen.bean.UniversalResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by mrwen on 2017/2/14.
 */

public interface InterfaceTeacher {
    @FormUrlEncoded
    @POST("servlet/Register")
    Call<RegisterResult> register(@FieldMap Map<String,String> map);

    //接受好友请求
    @POST("servlet/IsUsernameUnique")
    @FormUrlEncoded
    Call<UniversalResult> isUsernameUnique(@Field("username") String username);

    @FormUrlEncoded
    @POST("servlet/LoginIn")
    Call<LoginInResult> loginIn(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("servlet/TeacherInfoRevise")
    Call<UniversalResult> teacherInfoRevise(@FieldMap Map<String,String> map);

    @Multipart
    @POST("servlet/ImageUpload")
    Call<UniversalResult> imageUpload(@Part("fileName") String id, @Part("file\";filename=\"*.jpg") RequestBody image);

    //接受好友请求
    @POST("AddFriendServlet")
    @FormUrlEncoded
    Call<UniversalResult> addFriend(@Field("rid")int rid , @Field("group")String group, @Field("remark")String remark);

    //发送好友请求
    @POST("RequestFriendServlet")
    @FormUrlEncoded
    Call<UniversalResult> requestFriend(@Field("quid")String quid ,@Field("muid")String muid ,@Field("group")String group,@Field("remark")String remark,@Field("message")String message);

    //获取好友请求
    @GET("GetFriendRequestServlet")
    Call<ArrayList<FriendRequest>> getFriendRequest(@Query("uid") String uid);

    //好友请求数目
    @GET("GetRequestCountServlet")
    Call<Result> getRequestCount(@Query("uid") String uid);

    //查找好友
    @GET("QueryContactsServlet")
    Call<ArrayList<QueryItem>> queryContacts(@Query("uid")String uid, @Query("query") String query);

    //获取自己的分组
    @GET("GetGroupNamesServlet")
    Call<ArrayList<String>> getGroupNames(@Query("uid") String uid);

    //获取好友来显示
    @GET("GetContactsServlet")
    Call<ArrayList<RosterGroup>> getContacts(@Query("uid") String uid);

    //获取详细信息
    @GET("GetDetailedInfoServlet")
    Call<InfoDetail> getDetailedInfo(@Query("uid") String uid);

    //获得联系人信息
    @GET("GetUserInfoServlet")
    Call<Info> getUserInfo(@Query("quid")String queryUid, @Query("muid")String myUid);

    //获得token
    @GET("RequestTokenServlet")
    Call<Result> requestToken(@Query("uid") String uid);

    //获取我的回答
    @GET("LoadMyAnswersServlet")
    Call<ArrayList<Answer>> loadMyAnswer(@Query("uid")String uid);

    //删除回答
    @GET("DeleteAnswerServlet")
    Call<Result> deleteAnswer(@Query("aid")int aid);

    //修改密码
    @FormUrlEncoded
    @POST("servlet/PasswordRevise")
    Call<UniversalResult> passwordRevise(@Field("uid")String uid ,@Field("password")String password);

    //创建班级
    @POST("servlet/CreateClass")
    @FormUrlEncoded
    Call<UniversalResult> createClass(@FieldMap Map<String,String> map);

    //同意学生加班请求
    @POST("servlet/AddStudent")
    @FormUrlEncoded
    Call<UniversalResult> addStudent(@FieldMap Map<String, String> map);

    //添加好友后删除加班请求
    @POST("servlet/DelStuReqServlet")
    @FormUrlEncoded
    Call<UniversalResult> deleteStudentRequest(@FieldMap Map<String, String> map);

    //获取群组
    @GET("GetGroupsServlet")
    Call<ArrayList<ChatGroup>> getGroups(@Query("uid") String uid);

    //获取题目
    @GET("servlet/GetTest")
    Call<ArrayList<Exercise>> getTest(@Query("tid") String tid);

    //忘记密码时验证手机或邮箱
    @FormUrlEncoded
    @POST("servlet/VerifyPhoneOrEmailServlet")
    Call<UniversalResult> verifyPhoneOrEmail(@Field("flag") int flag, @Field("username") String username, @Field("vrfInfo") String vrfInfo);

    //通过认证后设置新密码
    @FormUrlEncoded
    @POST("servlet/NewPasswordServlet")
    Call<UniversalResult> setNewPassword(@Field("username") String username, @Field("password") String password);

    //上传知识图谱
    @FormUrlEncoded
    @POST("servlet/UploadKnowledgeServlet")
    Call<UniversalResult> uploadKnowledge(@FieldMap Map<String, String> map);

    //获取所有知识图谱
    @GET("servlet/GetKnowledgeServlet")
    Call<ArrayList<Knowledge>> getKnowledge();

    //删除知识标签
    @GET("servlet/DeleteKnowledgeServlet")
    Call<UniversalResult> deleteKnowledge(@Query("kid") int id);

    //获取特定知识图谱
    @GET("servlet/GetKnowledgeByInfo")
    Call<ArrayList<Knowledge>> getKnowledgeByInfo(@Query("grade") int grade, @Query("subject") String subject);

    //上传填空题
    @FormUrlEncoded
    @POST("servlet/UploadFillInBlankExercise")
    Call<UniversalResult> uploadFillInBlankExercise(@FieldMap Map<String, String> map);

    //上传选择题
    @FormUrlEncoded
    @POST("servlet/UploadMultipleChoicesExercise")
    Call<UniversalResult> uploadMultipleChoicesExercise(@FieldMap Map<String, String> map);

    //获取填空题
    @GET("servlet/GetFillInBlankExercisesServlet")
    Call<ArrayList<FillInBlankExercise>> getFillInBlankExercises();

    //获取选择题
    @GET("servlet/GetMultipleChoicesExercisesServlet")
    Call<ArrayList<MultipleChoicesExercise>> getMultipleChoicesExercises();

    //删除某道题目
    @GET("servlet/DeleteExerciseServlet")
    Call<UniversalResult> deleteExercise(@Query("eid") int id, @Query("type") int type);

}
