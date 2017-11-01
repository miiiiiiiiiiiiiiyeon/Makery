package makery.makerspace.t.makery.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import makery.makerspace.t.makery.R;
import makery.makerspace.t.makery.util.UIFactory;
// 매뉴얼 상세페이지에서 원문가기 눌렀을 때 url 연결
public class WebViewActivity extends AppCompatActivity {

    WebView web;
    UIFactory uiFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        uiFactory = UIFactory.getInstance(this);

        String data = getIntent().getStringExtra("data");
        web = uiFactory.createView(R.id.web);
        web.loadUrl(data);

        // 다른 브라우저를 띄우지않고 웹뷰에다 띄우겠따.
        web.setWebViewClient(new WebViewClient());
    }
}















