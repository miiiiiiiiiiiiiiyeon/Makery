package makery.makerspace.t.makery.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import makery.makerspace.t.makery.R;
import makery.makerspace.t.makery.model.DataDefinition;
import makery.makerspace.t.makery.model.DetailManualModel;
import makery.makerspace.t.makery.model.ManualModel;
import makery.makerspace.t.makery.model.request.RequestArrayModel;
import makery.makerspace.t.makery.model.request.RequestModel;
import makery.makerspace.t.makery.net.Net;
import makery.makerspace.t.makery.ui.DetailManualActivity;
import makery.makerspace.t.makery.util.U;
import makery.makerspace.t.makery.util.UIFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static makery.makerspace.t.makery.model.DataDefinition.DEFAULT_CTYPE;
import static makery.makerspace.t.makery.model.DataDefinition.DEFAULT_LEVEL;
import static makery.makerspace.t.makery.model.DataDefinition.DEFAULT_SUBJECT;
import static makery.makerspace.t.makery.model.DataDefinition.SEARCH_WAY_POP;
import static makery.makerspace.t.makery.model.DataDefinition.SEARCH_WAY_TIME;

public class ManualFragment extends Fragment {
    public static final int REQUEST_DETAIL_MANUAL = 2;
    public static final int RESULT_DETAIL_MANUAL = 22;

    public ManualFragment() {
    }

    String level, ctype, subject, search_way = SEARCH_WAY_TIME, search_keyword;

    ManagerAdapter managerAdapter;
    RecyclerView recyclerView;
    boolean isOpenning = true;
    LinearLayout search_layout, search_btn;
    Button button3;
    TextView manual_time, manual_pop, no_manual;
    RelativeLayout progress;
    EditText manual_search;

    UIFactory uiFactory;

    ArrayList<ManualModel> manuals;

    String member_token;

    Spinner spinner_level, spinner_ctype, spinner_subject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manual, container, false);
        uiFactory = UIFactory.getInstance(view);

        recyclerView = uiFactory.createView(R.id.recyclerView);
        search_layout = uiFactory.createView(R.id.search_layout);
        no_manual = uiFactory.createView(R.id.no_manual);
        search_btn = uiFactory.createView(R.id.search_btn);

        progress = uiFactory.createView(R.id.progress);

        manuals = new ArrayList();

        // 상세검색 스피너
        spinner_level = uiFactory.createView(R.id.spinner_level);
        spinner_ctype = uiFactory.createView(R.id.spinner_ctype);
        spinner_subject = uiFactory.createView(R.id.spinner_subject);

        /*// 검색 자동완성
        String[] str = getResources().getStringArray(R.array.search);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_dropdown_item_1line, str);
        AutoCompleteTextView autx = uiFactory.createView(R.id.manual_search);
        autx.setAdapter(adapter);*/

        // 최신순, 인기순 눌렀을 때 색 바꾸기
        manual_time = uiFactory.createView(R.id.manual_time);
        manual_pop = uiFactory.createView(R.id.manual_pop);
        manual_time.setSelected(true);
        manual_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 최신순 선택되지 않았을 때 (인기순이 선택되었을 때)
                if (!view.isSelected()) {
                    view.setSelected(true);
                    manual_pop.setSelected(false);
                    search_way = SEARCH_WAY_TIME;
                    spinner_search();
                }
            }
        });
        manual_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!view.isSelected()) {
                    view.setSelected(true);
                    manual_time.setSelected(false);
                    search_way = SEARCH_WAY_POP;
                    spinner_search();
                }
            }
        });

        // 키보드 숨기기
        manual_search = uiFactory.createView(R.id.manual_search);
        manual_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(manual_search.getWindowToken(), 0);    // 키보드 숨기기
                    spinner_search();
                    return true;
                }
                return false;
            }
        });

        // 돋보기 눌러서 검색
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_keyword = manual_search.getText().toString();
                spinner_search();
            }
        });

        // 검색창 접기
        button3 = uiFactory.createView(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFold();
            }
        });

        //1. 리사이클러뷰의 방향성 설정
        RecyclerView.LayoutManager manager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(manager);
        managerAdapter = new ManagerAdapter();

        recyclerView.setAdapter(managerAdapter);

        level = DEFAULT_LEVEL;
        ctype = DEFAULT_CTYPE;
        subject = DEFAULT_SUBJECT;
        search_keyword = "";

        member_token = U.getInstance().getPreferences(getContext(), "token");

        spinner_search();
        // 상세검색 : 난이도
        spinner_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                level = getResources().getStringArray(R.array.level)[i]; // 선택한 레벨 값 (ex. 상, 중, 하)
                if (DataDefinition.Level_Map.get(level) != null) {
                    level = DataDefinition.Level_Map.get(level); // lev001 lev 002 ...
                    Log.i("SPINNER", "token = " + member_token);
                } else {
                    level = DEFAULT_LEVEL;
                    Log.i("SPINNER", "level = " + level);
                }
                spinner_search();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // 상세검색 : 종류
        spinner_ctype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ctype = getResources().getStringArray(R.array.hardware)[i]; // 선택한 종류 (ex. 아두이노, 라즈베리파이, ...)
                if (DataDefinition.Ctype_Map.get(ctype) != null)
                    ctype = DataDefinition.Ctype_Map.get(ctype); // ctype001, ctype 002 ...
                else
                    ctype = DEFAULT_CTYPE;
                Log.i("SPINNER", "ctype = " + ctype);
                spinner_search();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // 상세검색 : 주제
        //Call<RequestModel<DetailManualModel>> spinner3 = Net.getInstance().getMakeryFactoryIm().detailManual("man_id", member_token);
        spinner_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subject = getResources().getStringArray(R.array.subject)[i]; // 선택한 주제 (ex. 3D프린터, 홈오토매틱, ...)
                if (DataDefinition.Subject_Map.get(subject) != null)
                    subject = DataDefinition.Subject_Map.get(subject); // lev001 lev 002 ...
                else
                    subject = DEFAULT_SUBJECT;
                Log.i("SPINNER", "subject = " + subject);
                spinner_search();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    // 상세검색 스피너로 리스트 다시 불러오기!!
    public void spinner_search() {
        Call<RequestArrayModel<ManualModel>> search = Net.getInstance().getMakeryFactoryIm().manualList(search_way, search_keyword, level, ctype, subject, member_token);
        search.enqueue(new Callback<RequestArrayModel<ManualModel>>() {
            @Override
            public void onResponse(Call<RequestArrayModel<ManualModel>> call, Response<RequestArrayModel<ManualModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1) {
                        manuals = response.body().getResult();
                        if( manuals.size() > 0 ) {
                            managerAdapter.notifyDataSetChanged();

                            no_manual.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }else {
                            recyclerView.setVisibility(View.GONE);
                            no_manual.setVisibility(View.VISIBLE);
                        }
                        Log.i("SPINNER", "success = " + response.body().getSuccess()+" result = "+manuals.size());
                    } else {
                        Toast.makeText(getActivity(), "서버와의 연결에 실패하였습니다.2", Toast.LENGTH_SHORT).show();
                        Log.i("SPINNER", "success = " + response.body().getSuccess() + " message = " + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<RequestArrayModel<ManualModel>> call, Throwable t) {
                Toast.makeText(getActivity(), "서버와의 연결에 실패하였습니다." + t, Toast.LENGTH_LONG).show();
                Log.i("SPINNER", "result = " + t);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DETAIL_MANUAL && resultCode == RESULT_DETAIL_MANUAL) {
            //showManualList();
            spinner_search();
        }
    }
    // 뷰홀더
    public class ManagerViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        TextView title, totalView, likeCount;
        ImageButton manual_like_btn;

        public ManagerViewHolder(View itemView) {
            super(itemView);

            // 여기 안에서는 itemView라서 새로 생성해준다. ↓
            uiFactory = UIFactory.getInstance(itemView);
            picture = uiFactory.createView(R.id.picture);
            manual_like_btn = uiFactory.createView(R.id.manual_like_btn);
            title = uiFactory.createView(R.id.title);
            totalView = uiFactory.createView(R.id.totalView);
            likeCount = uiFactory.createView(R.id.likeCount);
        }
    }
    // 프로그레스
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    // 어댑터
    public class ManagerAdapter extends RecyclerView.Adapter<ManagerViewHolder> {
        @Override
        public ManagerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_manual_layout, parent, false);
            ManagerViewHolder viewHoder = new ManagerViewHolder(view);

            return viewHoder;
        }

        @Override
        public void onBindViewHolder(ManagerViewHolder holder, int position) {
            // 그릇이랑 연결하는 곳
            ManualModel model = manuals.get(position);
            holder.title.setText(model.getMan_title());
            holder.totalView.setText(model.getMan_hit() + ""); // 숫자라서 +"" 해준다. => String으로 바뀜
            holder.likeCount.setText(model.getMan_like_cnt() + "");
            Glide.with(getActivity())
                    .load(model.getMan_th_img_url())
                    .centerCrop()
                    .placeholder(R.mipmap.img_bi)
                    .into(holder.picture);

            if (model.getMan_is_like() == 0) {
                holder.manual_like_btn.setImageResource(R.mipmap.favorite);
                holder.manual_like_btn.setSelected(false);
            } else {
                holder.manual_like_btn.setImageResource(R.mipmap.favorite_act);
                holder.manual_like_btn.setSelected(true);
            }
            holder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Intent 값 전달
                    Intent intent = new Intent(getActivity(), DetailManualActivity.class);
                    intent.putExtra("man_id", model.getMan_id());
                    startActivityForResult(intent, REQUEST_DETAIL_MANUAL);
                }
            });
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), DetailManualActivity.class);
                    intent.putExtra("man_id", model.getMan_id());
                    startActivityForResult(intent, REQUEST_DETAIL_MANUAL);
                }
            });

            // 좋아요 버튼
            holder.manual_like_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProgress();
                    // 좋아요 & 취소
                    ImageView like_btn = (ImageView) view;
                    member_token = U.getInstance().getPreferences(getActivity(), "token");
                    DetailManualModel item = new DetailManualModel(model.getMan_id(), member_token);
                    Call<RequestModel<String>> like = Net.getInstance().getMakeryFactoryIm().detailManualisLike(item);
                    like.enqueue(new Callback<RequestModel<String>>() {
                        @Override
                        public void onResponse(Call<RequestModel<String>> call, Response<RequestModel<String>> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getSuccess() == 1 && response.body().getMessage().equals("OK")) {
                                    Log.i("LIKE", "success = " + response.body().getSuccess());
                                    if (model.getMan_is_like() == 0) {
                                        Log.i("LIKE", "select false =" + model.getMan_is_like());
                                        like_btn.setImageResource(R.mipmap.favorite_act);
                                        like_btn.setSelected(true);
                                    } else {
                                        Log.i("LIKE", "select true =" + model.getMan_is_like());
                                        like_btn.setImageResource(R.mipmap.favorite);
                                        like_btn.setSelected(false);
                                    }
//                                    찜 cnt : 2
//                                    좋아요 클릭 -> 2 + 1
//                                    2 + (0 * -2 + 1) => 2 + 1

//                                    좋아요 취소 -> 3 + (-1)
//                                    3 + (1 * -2 +1) => 3 - 1

                                    model.setMan_like_cnt(model.getMan_like_cnt() + (model.getMan_is_like() * -2 + 1));
                                    model.setMan_is_like((model.getMan_is_like() + 1) % 2);     // (0 + 1) % 2 =1

                                    managerAdapter.notifyDataSetChanged();  // 데이터 바뀌었으니 갱신해라.
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
            });
        }

        //아이템 개수
        @Override
        public int getItemCount() {
            if (manuals != null)
                return manuals.size();
            else
                return 0;
        }
    }
    // 검색창 접기
    public void onFold() {
        if (isOpenning) {
            search_layout.setVisibility(View.GONE);
            button3.setBackground(getResources().getDrawable(R.mipmap.ic_down_white_24dp));
            // 버튼 눌렀을 때 버튼 이미지 바꾸기
            isOpenning = false;
        } else {
            search_layout.setVisibility(View.VISIBLE);
            button3.setBackground(getResources().getDrawable(R.mipmap.ic_up_white_24dp));
            isOpenning = true;
        }
    }
}
