package makery.makerspace.t.makery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import makery.makerspace.t.makery.R;
import makery.makerspace.t.makery.model.EmailLoginModel;
import makery.makerspace.t.makery.model.request.RequestModel;
import makery.makerspace.t.makery.net.Net;
import makery.makerspace.t.makery.util.U;
import makery.makerspace.t.makery.util.UIFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailLoginActivity extends AppCompatActivity {

    EditText userEmail, userPw;
    TextView emailLoginCheck, cancel, lost_password;
    //String loginId, loginPwd;

    UIFactory uiFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        uiFactory = UIFactory.getInstance(this);    // UIFactory 생성! this가 가능한 이유가 액티비티라서
                                                    // 액티비티나 프레그먼트가 아닌 경우는 this 대신 view
        userEmail = uiFactory.createView(R.id.userEmail);
        userPw   = uiFactory.createView(R.id.userPw);
        emailLoginCheck = uiFactory.createView(R.id.emailLoginCheck);
        lost_password = uiFactory.createView(R.id.lost_password);

        // 취소 누르면 첫 화면으로
        cancel = uiFactory.createView(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmailLoginActivity.this, FirstActivity.class));
            }
        });

        // 확인 버튼 눌렀을 때
        emailLoginCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !TextUtils.isEmpty(userEmail.getText()) && !TextUtils.isEmpty(userPw.getText())) {
                    EmailLoginModel loginModel = new EmailLoginModel(userEmail.getText().toString(), userPw.getText().toString(), "directly");
                    Call<RequestModel<String>> res = Net.getInstance().getMakeryFactoryIm().emailLogin(loginModel);
                                    res.enqueue(new Callback<RequestModel<String>>() {
                                        @Override
                                        public void onResponse(Call<RequestModel<String>> call, Response<RequestModel<String>> response) {
                                            if(response.isSuccessful()) {
                                                if(response.body().getSuccess()==0) {
                                                    Toast.makeText(EmailLoginActivity.this, "success 0 : 에러", Toast.LENGTH_SHORT).show();
                                                    Log.i("LOGIN", "success = "+ response.body().getSuccess());
                                                }else if(response.body().getSuccess()==1) {
                                                    Toast.makeText(EmailLoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                                    // 아이디, 비밀번호 저장 ( 자동로그인 )
                                                    U.getInstance().savePreferences(EmailLoginActivity.this, "email", userEmail.toString());
                                                    U.getInstance().savePreferences(EmailLoginActivity.this, "password", userPw.toString());

                                                    // 토큰저장
                                                    U.getInstance().savePreferences(EmailLoginActivity.this, "token", response.body().getResult());
                                                    Log.i("TOKEN","token = "+U.getInstance().getPreferences(EmailLoginActivity.this, "token"));

                                    startActivity(new Intent(EmailLoginActivity.this, BottomActivity.class));
                                    finish();
                                    Log.i("LOGIN", "success = "+ response.body().getSuccess());
                                }else {
                                    Toast.makeText(EmailLoginActivity.this, "이메일과 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                    Log.i("LOGIN", "success = "+ response.body().getSuccess());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RequestModel<String>> call, Throwable t) {
                            Toast.makeText(EmailLoginActivity.this, "이메일 또는 비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show();
                            Log.i("LOGIN", "success = "+ t);
                        }
                    });
                }
                else if(TextUtils.isEmpty(userEmail.getText())){
                    Toast.makeText(EmailLoginActivity.this, "이메일을 입력해주세요!", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(userPw.getText())) {
                    Toast.makeText(EmailLoginActivity.this, "비밀번호를 입력해주세요!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // 혹시, 비밀번호를 잊으셨나요?
        lost_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmailLoginActivity.this, PasswordActivity.class));
            }
        });
    }

    /*public void SharedPreferences() {
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        // SharedPreferences에 값을 저장할 키 생성(저장될 키, 값)

        loginId = auto.getString("inputId", null);
        loginPwd = auto.getString("inputPwd", null);


        if( loginId != null && loginPwd != null ) {
            if(response) {
                Toast.makeText(EmailLoginActivity.this, loginId + "님 자동 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EmailLoginActivity.this, BottomActivity.class);
                startActivity(intent);
                finish();
            }
        }else if(loginId == null && loginPwd == null) {
            emailLoginCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(userEmail.getText().toString().equals("aldus7261@naver.com") && userPw.getText().toString().equals("0000")) {
                        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                        // auto의 loginId와 loginPwd에 값을 저장

                        SharedPreferences.Editor autoLogin = auto.edit();
                        autoLogin.putString("inputId", userEmail.getText().toString());
                        autoLogin.putString("inputPwd", userPw.getText().toString());

                        autoLogin.commit(); // 해줘야 값이 저장
                        Toast.makeText(EmailLoginActivity.this, userEmail.getText().toString()+"님 환영합니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EmailLoginActivity.this, BottomActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }*/
}
