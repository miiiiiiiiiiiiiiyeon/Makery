package makery.makerspace.t.makery.net;

import makery.makerspace.t.makery.model.DetailEventModel;
import makery.makerspace.t.makery.model.DetailManualModel;
import makery.makerspace.t.makery.model.EmailJoinModel;
import makery.makerspace.t.makery.model.EmailLoginModel;
import makery.makerspace.t.makery.model.EventModel;
import makery.makerspace.t.makery.model.ManualModel;
import makery.makerspace.t.makery.model.ManualStep2Model;
import makery.makerspace.t.makery.model.MemberModel;
import makery.makerspace.t.makery.model.ResEventModel;
import makery.makerspace.t.makery.model.request.RequestArrayModel;
import makery.makerspace.t.makery.model.request.RequestModel;
import makery.makerspace.t.makery.model.request.RequestStepModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Api : 서버와의 약속
 */

public interface MakeryFactoryIm {

    // 매뉴얼 리스트 가져오기 /manuals/list/:search_way/:man_keyword/:man_level/:man_ctype/:man_subject/:member_token
    @GET("manuals/list/{search_way}/{man_keyword}/{man_level}/{man_ctype}/{man_subject}/{member_token}")
    Call<RequestArrayModel<ManualModel>> manualList(@Path("search_way") String search_way,
                                                    @Path("man_keyword") String man_keyword,
                                                    @Path("man_level") String man_level,
                                                    @Path("man_ctype") String man_ctype,
                                                    @Path("man_subject") String man_subject,
                                                    @Path("member_token") String member_token);

    // 매뉴얼 상세보기
    @GET("manuals/{man_id}/{member_token}")
    Call<RequestModel<DetailManualModel>> detailManual(@Path("man_id") String man_id, @Path("member_token") String member_token);

    // 매뉴얼 좋아요
    @PUT("manuals/like")
    Call<RequestModel<String>> detailManualisLike(@Body DetailManualModel model);

    // 매뉴얼 상세보기 중 스텝
    @GET("manuals/steps/59abc07b2d988009f278332e/{member_token}")
    Call<RequestModel<RequestStepModel<ManualStep2Model>>> manualStep(@Path("member_token") String member_token);

    // 구글 회원가입
    @POST("members/signup/gmail")
    Call<RequestModel<String>> googleJoin(@Body MemberModel memberModel);

    // 구글 로그인
    @POST("members/signin/gmail")
    Call<RequestModel<String>> googleLogin(@Body MemberModel memberModel);

    // 이메일 중복 확인
    @GET("members/signup/email/isExist/email/{member_email}/{auth_way}")
    Call<RequestModel<Void>> checkEmail(@Path("member_email") String member_email, @Path("auth_way") String auth_way);

    // 닉네임 중복 확인
    @GET("/members/signup/email/isExist/nickname/{member_nickname}/{auth_way}")
    Call<RequestModel<Void>> checkNickname(@Path("member_nickname") String member_nickname,
                                           @Path("auth_way") String auth_way);

    // 이메일 회원가입
    @POST("members/signup/email")
    Call<RequestModel<Void>> emailJoin(@Body EmailJoinModel emailJoinModel);

    // 이메일 로그인
    @POST("members/signin/email")
    Call<RequestModel<String>> emailLogin(@Body EmailLoginModel emailLoginModel);

    // 이벤트 화면에 메뉴, 지역이랑, 이벤트 종류 한번에 받게 해드렸어요
    @GET("menu/events")
    Call<RequestModel<ResEventModel>> eventSearch();

    // 이벤트 리스트
    @GET("events/list/{search_way}/{evt_keyword}/{evt_etype}/{evt_region}/{member_token}")
    Call<RequestArrayModel<EventModel>> eventModel(@Path("search_way") String search_way,
                                                   @Path("evt_keyword") String evt_keyword,
                                                   @Path("evt_etype") String evt_etype,
                                                   @Path("evt_region") String evt_region,
                                                   @Path("member_token") String member_token);

    // 이벤트 상세보기
    @GET("events/{evt_id}/{member_id}")
    Call<RequestModel<DetailEventModel>> detailEvent(@Path("evt_id") String evt_id,
                                                     @Path("member_id") String member_id);

    // 이벤트 좋아요
    @PUT("events/like")
    Call<RequestModel<String>> eventLike(@Body DetailEventModel eventLikeModel);

    // 내정보 닉네임, 프로필 사진
    @GET("members/mypage/profile/info/{member_token}")
    Call<RequestModel<MemberModel>> member(@Path("member_token")String member_token);

    // 내가 좋아한 게시글 썸네일 리스트
    @GET("members/mypage/likelist/thumbnail/{doctype}/{member_token}")
    Call<RequestArrayModel<String>> like_th(@Path("doctype")String doctype,
                                            @Path("member_token")String member_token);

    // 내가 쓴 프로젝트 썸네일 리스트
    @GET("members/mypage/docslist/thumbnail/{doctype}/{member_token}")
    Call<RequestArrayModel<String>> project_th(@Path("doctype")String doctype,
                                               @Path("member_token")String member_token);

    // 프로필 사진 수정
    @Multipart
    @PUT("members/mypage/profile/img/{member_token}")
    Call<RequestModel> profileImage(@Part("profile_img") MultipartBody.Part profile_img,
                                    @Part("member_token") RequestBody member_token);
}















