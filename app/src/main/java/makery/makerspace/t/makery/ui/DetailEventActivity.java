package makery.makerspace.t.makery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import makery.makerspace.t.makery.R;
import makery.makerspace.t.makery.model.DetailEventModel;
import makery.makerspace.t.makery.model.request.RequestModel;
import makery.makerspace.t.makery.net.Net;
import makery.makerspace.t.makery.util.U;
import makery.makerspace.t.makery.util.UIFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailEventActivity extends AppCompatActivity {

    DetailEventModel detailEventModel, eventLikeModel;
    ImageView cancel, favorite, event_img, event_url;
    UIFactory uiFactory;
    String member_token, evt_id;
    RelativeLayout progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        uiFactory = UIFactory.getInstance(this);

        cancel = uiFactory.createView(R.id.cancel);
        favorite = uiFactory.createView(R.id.favorite);
        event_img = uiFactory.createView(R.id.event_img);
        event_url = uiFactory.createView(R.id.event_url);
        progress = uiFactory.createView(R.id.progress);

        showProgress();

        // X 누르면 뒤로가기
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 하트 눌렀을 때 이미지 전환
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                sendEventLike();
                /*if (!favorite.isSelected()) {
                    favorite.setImageResource(R.mipmap.favorite_act);
                    favorite.setSelected(true);
                } else {
                    favorite.setImageResource(R.mipmap.favorite);
                    favorite.setSelected(false);
                }*/
            }
        });

        // 이벤트 이미지 연결!
        // 이벤트 셀을 눌러서 상세페이지로 들어가니까 이벤트 고유의 아이디(evt_id) 필요하고,
        // 좋아요 눌렀을 때 토큰값으로 서버에 전송해야해서 토큰값 대신 member_id 이용 => SharedPreferences 사용 => U
        Intent intent = getIntent();
        evt_id = intent.getStringExtra("evt_id");
        member_token = U.getInstance().getPreferences(this, "token");

        Call<RequestModel<DetailEventModel>> event = Net.getInstance().getMakeryFactoryIm().detailEvent(evt_id, member_token);
        event.enqueue(new Callback<RequestModel<DetailEventModel>>() {
            @Override
            public void onResponse(Call<RequestModel<DetailEventModel>> call, Response<RequestModel<DetailEventModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1 && response.body().getMessage().equals("OK")) {
                        detailEventModel = response.body().getResult();
                        Glide.with(DetailEventActivity.this)
                                .load(detailEventModel.getEvt_img_url())
                                .placeholder(R.mipmap.img_bi)
                                .into(event_img);

                        if (detailEventModel.getEvt_is_like() == 0){
                            favorite.setImageResource(R.mipmap.favorite);
                            favorite.setSelected(false);
                        } else {
                            favorite.setImageResource(R.mipmap.favorite_act);
                            favorite.setSelected(true);
                        }
                        hideProgress();
                        // 자세히보기 눌렀을 때 url연결 (웹뷰 띄우기)
                        event_url.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intentURL = new Intent(DetailEventActivity.this, WebViewActivity.class);
                                intentURL.putExtra("data", detailEventModel.getEvt_origin_url());
                                startActivity(intentURL);
                            }
                        });
                    }
                }else{
                    hideProgress();
                }
            }

            @Override
            public void onFailure(Call<RequestModel<DetailEventModel>> call, Throwable t) {
                Toast.makeText(DetailEventActivity.this, "서버와의 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                hideProgress();
            }
        });
    }

    public void sendEventLike(){
        // 좋아요 서버로 전송
        DetailEventModel item = new DetailEventModel(evt_id, member_token);  // 순서 중요! 모델이랑 비교
        Call<RequestModel<String>> like = Net.getInstance().getMakeryFactoryIm().eventLike(item);
        like.enqueue(new Callback<RequestModel<String>>() {
            @Override
            public void onResponse(Call<RequestModel<String>> call, Response<RequestModel<String>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1 && response.body().getMessage().equals("OK")) {
                        Log.i("LIKE", "success = " + response.body().getSuccess());
                        if (detailEventModel.getEvt_is_like() == 0) {
                            Log.i("LIKE", "select false =" + detailEventModel.getEvt_is_like());
                            detailEventModel.setEvt_is_like(1);
                            favorite.setImageResource(R.mipmap.favorite_act);
                            favorite.setSelected(true);
                        } else {
                            Log.i("LIKE", "select true =" + detailEventModel.getEvt_is_like());
                            detailEventModel.setEvt_is_like(0);
                            favorite.setImageResource(R.mipmap.favorite);
                            favorite.setSelected(false);
                        }
                        hideProgress();
                    } else {
                        Log.i("Like", "success = " + response.body().getSuccess());
                        hideProgress();
                    }
                } else {
                    Log.i("Like", "error1");
                    hideProgress();
                }
            }

            @Override
            public void onFailure(Call<RequestModel<String>> call, Throwable t) {
                Log.i("Like", "error2" + t);
                hideProgress();
            }
        });
    }

    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

}

