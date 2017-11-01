package makery.makerspace.t.makery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import makery.makerspace.t.makery.R;
import makery.makerspace.t.makery.fragment.ManualFragment;
import makery.makerspace.t.makery.model.DetailManualModel;
import makery.makerspace.t.makery.model.ManualStep2Model;
import makery.makerspace.t.makery.model.request.RequestModel;
import makery.makerspace.t.makery.model.request.RequestStepModel;
import makery.makerspace.t.makery.net.Net;
import makery.makerspace.t.makery.util.U;
import makery.makerspace.t.makery.util.UIFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class DetailManualActivity extends AppCompatActivity {

    DetailManualModel model;
    ArrayList<ManualStep2Model> step_list;
    ArrayList<String> blueprint_list;

    UIFactory uiFactory;

    ImageView manual_image, manual_level, step, code, blueprint, back_btn, blueprint_img, manual_share;
    TextView manual_title, manual_desc, manual_url, manual_component, null_code, null_blueprint;
    ImageButton favorite;
    RecyclerView recyclerView_step, recyclerView_blueprint;
    WebView codeWebView;
    RelativeLayout code_web, blueprint_relative, progress;
    LinearLayout componentLinear;
    StepAdapter stepAdapter;
    BlueprintAdapter blueprintAdapter;

    String man_id, member_token;

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent();
        setResult(ManualFragment.RESULT_DETAIL_MANUAL, intent1);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_manual);

        uiFactory = UIFactory.getInstance(this);

        manual_image = uiFactory.createView(R.id.manual_image);
        manual_level = uiFactory.createView(R.id.manual_level);
        manual_title = uiFactory.createView(R.id.manual_title);
        manual_desc = uiFactory.createView(R.id.manual_desc);
        manual_url = uiFactory.createView(R.id.manual_url);
        manual_component = uiFactory.createView(R.id.manual_component);
        codeWebView = uiFactory.createView(R.id.codeWebView);
        code_web = uiFactory.createView(R.id.code_web);
        componentLinear = uiFactory.createView(R.id.componentLinear);
        recyclerView_step = uiFactory.createView(R.id.recyclerView_step);
        blueprint_img = uiFactory.createView(R.id.blueprint_img);
        recyclerView_blueprint = uiFactory.createView(R.id.recyclerView_blueprint);
        null_code = uiFactory.createView(R.id.null_code);
        null_blueprint = uiFactory.createView(R.id.null_blueprint);
        blueprint_relative = uiFactory.createView(R.id.blueprint_relative);
        manual_share = uiFactory.createView(R.id.manual_share);

        step = uiFactory.createView(R.id.step);
        code = uiFactory.createView(R.id.code);
        blueprint = uiFactory.createView(R.id.blueprint);
        back_btn = uiFactory.createView(R.id.back_btn);
        favorite = uiFactory.createView(R.id.favorite);

        progress = uiFactory.createView(R.id.progress);

        showProgress();

        step_list = new ArrayList();
        stepAdapter = new StepAdapter();
        recyclerView_step.setAdapter(stepAdapter);

        blueprint_list = new ArrayList();
        blueprintAdapter = new BlueprintAdapter();
        recyclerView_blueprint.setAdapter(blueprintAdapter);

        // Intent 값 받기 : 제목, 이미지, 설명 서버 연결
        Intent intent = getIntent();
        man_id = intent.getStringExtra("man_id");
        member_token = U.getInstance().getPreferences(this, "token"); // member_id 대신 토큰값으로 좋아요 구분
        // 서버 통신
        Log.i("TACA", "man_id = " + man_id + " member_token = " + member_token);
        Call<RequestModel<DetailManualModel>> res = Net.getInstance().getMakeryFactoryIm().detailManual(man_id, member_token);
        // 레트로핏으로 통신하는 애가 Net 이니까 Net에 받는다.
        res.enqueue(new Callback<RequestModel<DetailManualModel>>() {
            @Override
            public void onResponse(Call<RequestModel<DetailManualModel>> call, Response<RequestModel<DetailManualModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1 && response.body().getMessage().equals("OK")) {
                        model = response.body().getResult();
                        setDetailManual(model);
                    } else {
                        Log.i("TACA", "error1" + response.body().getSuccess());
                        hideProgress();
                    }
                } else {
                    Log.i("TACA", "error2 = ");
                    hideProgress();
                }
            }

            @Override
            public void onFailure(Call<RequestModel<DetailManualModel>> call, Throwable t) {
                Toast.makeText(DetailManualActivity.this, "서버와의 연결에 실패하였습니다." + t, Toast.LENGTH_LONG).show();
                Log.i("TACA", "error3" + t);
                hideProgress();
            }
        });
        setManualStep();
        // 원문가기 클릭시 웹뷰 띄우기
        manual_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailManualActivity.this, WebViewActivity.class);
                intent.putExtra("data", model.getMan_origin_url());
                startActivity(intent);
            }
        });
        // 스텝, 코드, 설계도 클릭시 이미지 변경, 서버 연결
        step.setImageResource(R.mipmap.man_02);
        step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!view.isSelected()) {
                    step.setImageResource(R.mipmap.man_02);
                    code.setImageResource(R.mipmap.man_03);
                    blueprint.setImageResource(R.mipmap.man_05);

                    code_web.setVisibility(GONE);
                    recyclerView_blueprint.setVisibility(GONE);
                    blueprint_relative.setVisibility(GONE);
                    componentLinear.setVisibility(View.VISIBLE);

                    setManualStep();
                }
            }
        });
        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!view.isSelected()) {
                    step.setImageResource(R.mipmap.man_01);
                    code.setImageResource(R.mipmap.man_04);
                    blueprint.setImageResource(R.mipmap.man_05);

                    componentLinear.setVisibility(GONE);
                    recyclerView_blueprint.setVisibility(GONE);
                    blueprint_relative.setVisibility(GONE);
                    code_web.setVisibility(View.VISIBLE);
                    // 코드 없으면 null_code 보여주기
                    if (model.getMan_code_url().isEmpty()) {
                        null_code.setVisibility(View.VISIBLE);
                        codeWebView.setVisibility(View.GONE);
                    } else {
                        null_code.setVisibility(View.GONE);
                        codeWebView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        blueprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!view.isSelected()) {
                    step.setImageResource(R.mipmap.man_01);
                    code.setImageResource(R.mipmap.man_03);
                    blueprint.setImageResource(R.mipmap.man_06);

                    code_web.setVisibility(GONE);
                    componentLinear.setVisibility(GONE);
                    recyclerView_blueprint.setVisibility(View.VISIBLE);
                    blueprint_relative.setVisibility(View.VISIBLE);
                    // 설계도 없으면 null_blueprint
                    if (model.getMan_blueprint_img_url().isEmpty()) {
                        recyclerView_blueprint.setVisibility(GONE);
                        null_blueprint.setVisibility(View.VISIBLE);
                    } else {
                        null_blueprint.setVisibility(GONE);
                        recyclerView_blueprint.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        // < 버튼 눌렀을 때 뒤로가기
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                setResult(ManualFragment.RESULT_DETAIL_MANUAL, intent1);
                finish();
            }
        });
        // 빈 하트 누르면 꽉 찬 하트
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeCount();
            }
        });
        // 리사이클러뷰의 방향성 설정
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView_step.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView_blueprint.setLayoutManager(linearLayoutManager2);
    }

    // 좋아요 눌렀을 때 서버 통신
    public void likeCount() {
        showProgress();
        // 좋아요 & 취소
        DetailManualModel model = new DetailManualModel(man_id, member_token);
        Call<RequestModel<String>> like = Net.getInstance().getMakeryFactoryIm().detailManualisLike(model);
        like.enqueue(new Callback<RequestModel<String>>() {
            @Override
            public void onResponse(Call<RequestModel<String>> call, Response<RequestModel<String>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1 && response.body().getMessage().equals("OK")) {
                        selectedLike();
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

    // 하트 바꾸는거
    public void selectedLike() {
        if (!favorite.isSelected()) {
            favorite.setImageResource(R.mipmap.favorite_act);
            favorite.setSelected(true);
        } else {
            favorite.setImageResource(R.mipmap.favorite);
            favorite.setSelected(false);
        }
    }

    // 매뉴얼 제목, 설명, 이미지 꽂기
    public void setDetailManual(DetailManualModel item) {
        manual_title.setText(item.getMan_title());
        manual_desc.setText(item.getMan_desc());
        Log.i("LOG", "loglog =" + model.getMan_img_url());

        if( model.getMan_is_like() == 1 ) {
            favorite.setImageResource(R.mipmap.favorite_act);
            favorite.setSelected(true);
        }else {
            favorite.setImageResource(R.mipmap.favorite);
            favorite.setSelected(false);
        }

        Glide.with(this)
                .load(model.getMan_img_url())
                .into(manual_image);
        // 난이도별 이미지 전환환
        switch (item.getMan_level()) {
            case "lev001": {
                Glide.with(this)
                        .load(R.mipmap.lev001)
                        .fitCenter()
                        .into(manual_level);
            }
            break;

            case "lev002": {
                Glide.with(this)
                        .load(R.mipmap.lev002)
                        .fitCenter()
                        .into(manual_level);
            }
            break;

            case "lev003": {
                Glide.with(this)
                        .load(R.mipmap.lev003)
                        .fitCenter()
                        .into(manual_level);
            }
            break;
        }

        // code_url 웹뷰 꽂기
        Log.i("CODE", "code_url = " + item.getMan_code_url());
        codeWebView.loadUrl(item.getMan_code_url());
        codeWebView.setWebViewClient(new WebViewClient());

        // blue_print_img 리사이클러뷰 꽂기
        blueprint_list = item.getMan_blueprint_img_url();
        blueprintAdapter.notifyDataSetChanged();    // 어댑터한테 알려주기!!!!!!!!

        hideProgress();
    }

    // 스텝 눌렀을 때 서버 통신
    public void setManualStep() {
        Call<RequestModel<RequestStepModel<ManualStep2Model>>> step = Net.getInstance().getMakeryFactoryIm().manualStep(member_token);
        step.enqueue(new Callback<RequestModel<RequestStepModel<ManualStep2Model>>>() {
            @Override
            public void onResponse(Call<RequestModel<RequestStepModel<ManualStep2Model>>> call, Response<RequestModel<RequestStepModel<ManualStep2Model>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1 && response.body().getMessage().equals("OK")) {
                        step_list = response.body().getResult().getMan_steps();
                        manual_component.setText(response.body().getResult().getMan_comments());
                        stepAdapter.notifyDataSetChanged();
                        Log.i("MIYEON", "success = " + response.body().getResult().getMan_steps().size());
                    } else {
                        Log.i("MIYEON", "ERROR1" + response.body().getMessage());
                    }
                } else {
                    Log.i("MIYEON", "ERROR2");
                }
            }

            @Override
            public void onFailure(Call<RequestModel<RequestStepModel<ManualStep2Model>>> call, Throwable t) {
                Toast.makeText(DetailManualActivity.this, "서버와의 연결에 실패하였습니다." + t, Toast.LENGTH_LONG).show();
                Log.i("MIYEON", "ERROR3");
            }
        });
    }

    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    // 스텝 리사이클러뷰 뷰홀더
    public class StepViewHolder extends RecyclerView.ViewHolder {
        TextView text_step, step_order, step_desc;
        ImageView step_img;

        public StepViewHolder(View itemView) {
            super(itemView);

            text_step = itemView.findViewById(R.id.text_step);
            step_order = itemView.findViewById(R.id.step_order);
            step_img = itemView.findViewById(R.id.step_img);
            step_desc = itemView.findViewById(R.id.step_desc);
        }
    }

    // 스텝 리사이클러뷰 어댑터
    public class StepAdapter extends RecyclerView.Adapter<StepViewHolder> {
        @Override
        public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(DetailManualActivity.this).inflate(R.layout.cell_step_layout, parent, false);
            StepViewHolder viewHolder = new StepViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(StepViewHolder holder, int position) {
            Log.i("MIYEON", "position = " + position);
            ManualStep2Model step2Model = step_list.get(position);
            holder.step_order.setText("" + step2Model.getStep_order());   // setText할 때 ""+ 넣어야 에러 피할 수 있음!!!
            holder.step_desc.setText(step2Model.getStep_desc());
            if (step2Model.getStep_imgs_url().size() > 0) {
                Glide.with(DetailManualActivity.this)
                        .load(step2Model.getStep_imgs_url().get(0))
                        .into(holder.step_img);
            } else {
                holder.step_img.setVisibility(GONE);
            }
        }

        @Override
        public int getItemCount() {
            return step_list.size();
        }
    }

    // 설계도 리사이클러뷰 뷰홀더
    public class BlueprintViewHolder extends RecyclerView.ViewHolder {
        ImageView blueprint_img;

        public BlueprintViewHolder(View itemView) {
            super(itemView);
            blueprint_img = itemView.findViewById(R.id.blueprint_img);
        }
    }

    // 설계도 리사이클러뷰 어댑터
    public class BlueprintAdapter extends RecyclerView.Adapter<BlueprintViewHolder> {
        // 셀을 만들어서 뷰홀더에 넣어놔라~
        @Override
        public BlueprintViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(DetailManualActivity.this).inflate(R.layout.cell_blueprint_layout, parent, false);
            BlueprintViewHolder blueprintViewHolder = new BlueprintViewHolder(view);
            return blueprintViewHolder;
        }

        // 뷰홀더에 값을 바인딩해줘라~
        @Override
        public void onBindViewHolder(BlueprintViewHolder holder, int position) {
            String url = blueprint_list.get(position);
            Glide.with(DetailManualActivity.this)
                    .load(url)
                    .placeholder(R.mipmap.img_bi)
                    .into(holder.blueprint_img);
        }

        @Override
        public int getItemCount() {
            return blueprint_list.size();
        }
    }
}

































