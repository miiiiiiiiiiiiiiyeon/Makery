package makery.makerspace.t.makery.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import makery.makerspace.t.makery.R;
import makery.makerspace.t.makery.model.DataDefinition;
import makery.makerspace.t.makery.model.EventEventsModel;
import makery.makerspace.t.makery.model.EventModel;
import makery.makerspace.t.makery.model.EventRegionsModel;
import makery.makerspace.t.makery.model.request.RequestArrayModel;
import makery.makerspace.t.makery.net.Net;
import makery.makerspace.t.makery.ui.BottomActivity;
import makery.makerspace.t.makery.ui.DetailEventActivity;
import makery.makerspace.t.makery.util.U;
import makery.makerspace.t.makery.util.UIFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static makery.makerspace.t.makery.model.DataDefinition.DEFAULT_ETYPE;
import static makery.makerspace.t.makery.model.DataDefinition.DEFAULT_REGION;
import static makery.makerspace.t.makery.model.DataDefinition.SEARCH_WAY_POP;
import static makery.makerspace.t.makery.model.DataDefinition.SEARCH_WAY_TIME;


public class EventFragment extends Fragment {
    public EventFragment() {
    }

    LinearLayout search_layout;
    boolean isOpenning = true;
    Button button3;
    GridView eventGridView;
    int columnCnt;
    RoundedImageView event_img;
    TextView event_title, event_Dday, event_search, no_event;
    EventAdapter eventAdapter;

    UIFactory uiFactory;
    ArrayList<EventEventsModel> eventEventsModels;
    ArrayList<EventRegionsModel> eventRegionsModels;
    ArrayList<EventModel> eventModels;

    String etype, region, search_way = SEARCH_WAY_TIME, member_token;
    Spinner spinner_etype, spinner_region;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        eventEventsModels = new ArrayList();
        eventRegionsModels = new ArrayList();

        uiFactory = UIFactory.getInstance(view);
        columnCnt = 2;
        eventGridView = uiFactory.createView(R.id.eventGridView);
        eventModels = new ArrayList();
        eventAdapter = new EventAdapter();
        eventGridView.setAdapter(eventAdapter);
        no_event = uiFactory.createView(R.id.no_event);

        // 상세검색 스피너
        spinner_etype = uiFactory.createView(R.id.spinner_etype);
        spinner_region = uiFactory.createView(R.id.spinner_region);

        search_layout = uiFactory.createView(R.id.search_layout);
        button3 = uiFactory.createView(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFold();
            }
        });

        event_search = uiFactory.createView(R.id.event_search);
        event_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(event_search.getWindowToken(), 0);    // 키보드 숨기기
                    Toast.makeText(getContext(), "이벤트 검색", Toast.LENGTH_LONG).show(); // 토스트 띄우기
                    return true;
                }
                return false;
            }
        });

        // 마감순, 인기순 눌렀을 때 색상 변경
        TextView event_time = view.findViewById(R.id.event_time);
        event_time.setSelected(true);
        final TextView event_pop = uiFactory.createView(R.id.event_pop);
        event_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!view.isSelected()) {
                    view.setSelected(true);
                    event_pop.setSelected(false);
                    search_way = SEARCH_WAY_TIME;
                    spinner_search();
                }
            }
        });
        event_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !view.isSelected() ) {
                    view.setSelected(true);
                    event_time.setSelected(false);
                    search_way = SEARCH_WAY_POP;
                    spinner_search();
                }
            }
        });

        etype = DEFAULT_ETYPE;
        region = DEFAULT_REGION;

        member_token = U.getInstance().getPreferences(getContext(), "token");

        // 상세검색 : 종류
        spinner_etype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                etype = getResources().getStringArray(R.array.event_type)[i]; // 선택한 종류 (ex. 교육, 창업, ...)
                if (DataDefinition.Etype_Map.get(etype) != null)
                    etype = DataDefinition.Etype_Map.get(etype); // etype001, etype 002 ...
                else
                    etype = DEFAULT_ETYPE;
                Log.i("SPINNER", "etype = " + etype);
                spinner_search();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // 상세검색 : 지역
        spinner_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                region = getResources().getStringArray(R.array.event_location)[i]; // 선택한 지역 (ex. 서울, 경기, 인천)
                if (DataDefinition.Region_Map.get(region) != null)
                    region = DataDefinition.Region_Map.get(region); // region001, region002, ...
                else
                    region = DEFAULT_REGION;
                Log.i("SPINNER", "region = " + region);
                spinner_search();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_search();

        return view;
    }
    // 검색창 접기
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
    // 이벤트 이미지, 제목, 디데이
    public class EventAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return eventModels.size();
        }

        @Override
        public EventModel getItem(int i) {
            return eventModels.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if( view == null ) {
                view = ((BottomActivity)getContext()).getLayoutInflater().inflate(R.layout.cell_event_grid_layout, viewGroup, false);
            }
            event_img = view.findViewById(R.id.event_img);
            event_title = view.findViewById(R.id.event_title);
            event_Dday = view.findViewById(R.id.event_Dday);

            Glide.with(getActivity())
                    .load(getItem(i).getEvt_th_img_url())
                    .into(event_img);

            event_title.setText(getItem(i).getEvt_title());
            event_Dday.setText(getItem(i).getEvt_deadline()+"");
            event_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), DetailEventActivity.class);
                    intent.putExtra("evt_id", getItem(i).getEvt_id());
                    Log.i("EVENT", "evt_id = "+getItem(i).getEvt_id());
                    startActivity(intent);
                }
            });
            return view;
        }
    }
    // 상세검색 스피너
    public void spinner_search() {
        Call<RequestArrayModel<EventModel>> list = Net.getInstance().getMakeryFactoryIm().eventModel(search_way, "", etype, region, member_token);
        list.enqueue(new Callback<RequestArrayModel<EventModel>>() {
            @Override
            public void onResponse(Call<RequestArrayModel<EventModel>> call, Response<RequestArrayModel<EventModel>> response) {
                if(response.isSuccessful()) {
                    if(response.body().getSuccess()==1 && response.body().getMessage().equals("OK")) {
                        eventModels.clear();
                        eventModels.addAll(response.body().getResult());
                        if( eventModels.size() > 0 ) {
                            eventAdapter.notifyDataSetChanged();
                            eventGridView.setVisibility(View.VISIBLE);
                            no_event.setVisibility(View.GONE);
                        }else {
                            eventGridView.setVisibility(View.GONE);
                            no_event.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RequestArrayModel<EventModel>> call, Throwable t) {
                Toast.makeText(getActivity(), "서버와의 연결에 실패하였습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }

}
