package makery.makerspace.t.makery.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import makery.makerspace.t.makery.R;
import makery.makerspace.t.makery.ui.NewProjectActivity;
import makery.makerspace.t.makery.util.UIFactory;

public class ProjectFragment extends Fragment {

    TextView title;
    RecyclerView recyclerView;
    HomeListAdapter homelistAdapter;
    LinearLayout search_layout;
    boolean isOpenning = true;
    Button button3;
    ImageButton new_project_btn;
    UIFactory uiFactory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, container, false);

        uiFactory = UIFactory.getInstance(view);
        title = uiFactory.createView(R.id.title);
        search_layout = uiFactory.createView(R.id.search_layout);

        button3 = uiFactory.createView(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFold();
            }
        });

        new_project_btn = uiFactory.createView(R.id.new_project_btn);
        new_project_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewProjectActivity.class));
            }
        });

        homelistAdapter = new HomeListAdapter();
        recyclerView = uiFactory.createView(R.id.recyclerView);
        recyclerView.setAdapter(homelistAdapter);

        final EditText project_search = uiFactory.createView(R.id.project_search);

        project_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(project_search.getWindowToken(), 0);    // 키보드 숨기기
                    Toast.makeText(getContext(), "프로젝트 검색", Toast.LENGTH_LONG).show(); // 토스트 띄우기
                    return true;
                }
                return false;
            }
        });

        final TextView project_time = uiFactory.createView(R.id.project_time);
        project_time.setSelected(true);
        final TextView project_pop = uiFactory.createView(R.id.project_pop);
        project_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!view.isSelected()) {
                    view.setSelected(true);
                    project_pop.setSelected(false);
                }
            }
        });
        project_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !view.isSelected() ) {
                    view.setSelected(true);
                    project_time.setSelected(false);
                }
            }
        });



        return view;
    }

    class HomeListAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }


    public void onFold(){
        if( isOpenning ) {
            search_layout.setVisibility(View.GONE);
            button3.setBackground(getResources().getDrawable(R.mipmap.ic_down_white_24dp));
            isOpenning = false;
        }
        else{
            search_layout.setVisibility(View.VISIBLE);
            button3.setBackground(getResources().getDrawable(R.mipmap.ic_up_white_24dp));
            isOpenning = true;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView, ImageView picture, ImageView makerFece, TextView title, TextView makerName) {
            super(itemView);
            this.picture = picture;
            this.makerFece = makerFece;
            this.title = title;
            this.makerName = makerName;
        }

        ImageView picture, makerFece;
        TextView title, makerName;
    }


}
