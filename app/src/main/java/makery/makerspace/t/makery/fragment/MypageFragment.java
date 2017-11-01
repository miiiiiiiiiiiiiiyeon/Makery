package makery.makerspace.t.makery.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import makery.makerspace.t.makery.R;
import makery.makerspace.t.makery.model.MemberModel;
import makery.makerspace.t.makery.model.request.RequestArrayModel;
import makery.makerspace.t.makery.model.request.RequestModel;
import makery.makerspace.t.makery.net.Net;
import makery.makerspace.t.makery.ui.BottomActivity;
import makery.makerspace.t.makery.ui.MyProjectActivity;
import makery.makerspace.t.makery.ui.DetailMypageActivity;
import makery.makerspace.t.makery.util.U;
import makery.makerspace.t.makery.util.UIFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MypageFragment extends Fragment {
    public MypageFragment() {
    }

    UIFactory uiFactory;
    ImageView profile_change;
    GridView myGridView1, like_gridview;
    int columnCnt;
    GridAdapter gridAdapter;
    MyAdapter mypostAdapter;
    TextView mypage_manual, mypage_event, mypage_project, profile_name;
    CircleImageView profile_img;
    ArrayList<String> likePost, my_project;
    String member_token;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);


        uiFactory = UIFactory.getInstance(view);
        profile_change = uiFactory.createView(R.id.profile_change);
        myGridView1 = uiFactory.createView(R.id.myGridView1);

        likePost = new ArrayList();
        my_project = new ArrayList();

        profile_name = uiFactory.createView(R.id.profile_name);
        profile_img = uiFactory.createView(R.id.profile_img);
        like_gridview = uiFactory.createView(R.id.like_gridview);

        columnCnt = 4;

        mypage_manual = uiFactory.createView(R.id.mypage_manual);
        mypage_event = uiFactory.createView(R.id.mypage_event);
        mypage_project = uiFactory.createView(R.id.mypage_project);

        // 매뉴얼, 프로젝트, 이벤트 눌렀을 때
        mypage_manual.setSelected(true);
        mypage_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!view.isSelected()) {
                    view.setSelected(true);
                    mypage_event.setSelected(false);
                    mypage_project.setSelected(false);
                    showLikeList("manuals");
                }
            }
        });
        mypage_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!view.isSelected()) {
                    view.setSelected(true);
                    mypage_manual.setSelected(false);
                    mypage_project.setSelected(false);
                    showLikeList("events");
                }
            }
        });
        mypage_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!view.isSelected()) {
                    view.setSelected(true);
                    mypage_manual.setSelected(false);
                    mypage_event.setSelected(false);
                    showLikeList("projects");
                }
            }
        });
        // 프로필 수정으로 이동
        profile_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DetailMypageActivity.class));
            }
        });

        // 내가 쓴 글 눌러서 MyProjectActivity로 전환
        myGridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), MyProjectActivity.class));
                    }
                });
            }

        });
        // 내정보 매뉴얼, 프로젝트, 이벤트, 내가 쓴 글 어댑터 연결
        gridAdapter = new GridAdapter();
        mypostAdapter = new MyAdapter();
        myGridView1.setAdapter(mypostAdapter);
        like_gridview.setAdapter(gridAdapter);

        // 내정보 닉네임, 프로필 사진 설정
        member_token = U.getInstance().getPreferences(getContext(), "token");
        Call<RequestModel<MemberModel>> myPage = Net.getInstance().getMakeryFactoryIm().member(member_token);
        myPage.enqueue(new Callback<RequestModel<MemberModel>>() {
            @Override
            public void onResponse(Call<RequestModel<MemberModel>> call, Response<RequestModel<MemberModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1 && response.body().getMessage().equals("OK")) {
                        profile_name.setText(response.body().getResult().getMember_nickname());
                        Glide.with(getContext()).load(response.body().getResult().getMember_profile_th_img_url()).into(profile_img);
                    }
                }
            }

            @Override
            public void onFailure(Call<RequestModel<MemberModel>> call, Throwable t) {
            }
        });

        showLikeList("manuals");

        return view;
    }

    // 내정보에서 매뉴얼, 프로젝트, 이벤트 썸네일 꽂기
    public void showLikeList(String doctype) {
        Call<RequestArrayModel<String>> likeGrid = Net.getInstance().getMakeryFactoryIm().like_th(doctype, member_token);
        likeGrid.enqueue(new Callback<RequestArrayModel<String>>() {
            @Override
            public void onResponse(Call<RequestArrayModel<String>> call, Response<RequestArrayModel<String>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1 && response.body().getMessage().equals("OK")) {
                        Log.i("LIKE", "success = " + response.body().getSuccess());
                        Log.i("LIKE", "result = " + response.body().getResult().size());
                        likePost.clear();
                        likePost.addAll(response.body().getResult());
                        gridAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<RequestArrayModel<String>> call, Throwable t) {

            }
        });
    }

    // 내가 좋아한 글 어댑터
    class GridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public String getItem(int i) {
            if (likePost.size() > i)
                return likePost.get(i);
            else
                return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = ((BottomActivity) getContext()).getLayoutInflater().inflate(R.layout.cell_mypage_myproject_layout, viewGroup, false);
            }

            ImageView mypost_img = view.findViewById(R.id.mypost_img);

            if (getItem(i) != null) {
                Glide.with(getActivity())
                        .load(getItem(i))
                        .centerCrop()
                        .into(mypost_img);

                Log.i("Glide", "getEvt_th_img_url = " + getItem(i));
            }else {
                view = ((BottomActivity) getContext()).getLayoutInflater().inflate(R.layout.cell_mypage_myproject_layout, viewGroup, false);
            }

            return view;
        }
    }

    // 내가 쓴 글 어댑터
    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public String getItem(int i) {
            if (my_project.size() > i)
                return my_project.get(i);
            else

                return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = ((BottomActivity) getContext()).getLayoutInflater().inflate(R.layout.cell_mypage_myproject_layout, viewGroup, false);
            }

            ImageView mypost_img = view.findViewById(R.id.mypost_img);

            if (getItem(i) != null) {
                Glide.with(getActivity()).load(getItem(i)).into(mypost_img);
            }

            return view;
        }
    }
}

