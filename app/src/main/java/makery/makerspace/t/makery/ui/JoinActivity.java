package makery.makerspace.t.makery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import makery.makerspace.t.makery.R;
import makery.makerspace.t.makery.model.EmailJoinModel;
import makery.makerspace.t.makery.model.request.RequestModel;
import makery.makerspace.t.makery.net.Net;
import makery.makerspace.t.makery.util.U;
import makery.makerspace.t.makery.util.UIFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends AppCompatActivity {

    TextView cancel, emailCheck, joinComplete, email_error, pw_error, nick_error, nicknameCheck;
    UIFactory uiFactory;
    EditText member_email, member_nickname, member_pw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        uiFactory = UIFactory.getInstance(this);
        emailCheck = uiFactory.createView(R.id.emailCheck);
        nicknameCheck = uiFactory.createView(R.id.nicknameCheck);
        joinComplete = uiFactory.createView(R.id.joinComplete);
        // edittext
        member_email = uiFactory.createView(R.id.member_email);
        member_nickname = uiFactory.createView(R.id.member_nickname);
        member_pw = uiFactory.createView(R.id.member_pw);
        // 에러 메시지
        email_error = uiFactory.createView(R.id.email_error);
        pw_error = uiFactory.createView(R.id.pw_error);
        nick_error = uiFactory.createView(R.id.nick_error);

        // 취소 누르면 첫 화면으로
        cancel = uiFactory.createView(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JoinActivity.this, FirstActivity.class));
            }
        });
        // 이메일 중복 체크
        emailCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<RequestModel<Void>> res = Net.getInstance().getMakeryFactoryIm().checkEmail(member_email.getText().toString(), "directly");
                res.enqueue(new Callback<RequestModel<Void>>() {
                    @Override
                    public void onResponse(Call<RequestModel<Void>> call, Response<RequestModel<Void>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess() == 0) {
                                Toast.makeText(JoinActivity.this, "success 0 : 에러", Toast.LENGTH_LONG).show();
                                Log.i("CHECK", "success =" + response.body().getSuccess());
                            } else if (response.body().getSuccess() == 1) {
                                Toast.makeText(JoinActivity.this, "사용 가능한 이메일입니다.", Toast.LENGTH_LONG).show();
                                emailCheck.setTextColor(view.getResources().getColor(R.color.gray));
                                emailCheck.setClickable(false);
                                Log.i("CHECK", "success =" + response.body().getSuccess());
                            } else {
                                Toast.makeText(JoinActivity.this, "중복된 이메일입니다.", Toast.LENGTH_LONG).show();
                                Log.i("CHECK", "success =" + response.body().getSuccess());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RequestModel<Void>> call, Throwable t) {
                        Toast.makeText(JoinActivity.this, "이메일 형식을 확인해주세요.", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
        // 닉네임 중복 체크
        nicknameCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<RequestModel<Void>> res = Net.getInstance().getMakeryFactoryIm().checkNickname(member_nickname.getText().toString(), "directly");
                res.enqueue(new Callback<RequestModel<Void>>() {
                    @Override
                    public void onResponse(Call<RequestModel<Void>> call, Response<RequestModel<Void>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess() == 0) {
                                Toast.makeText(JoinActivity.this, "success 0 : 에러", Toast.LENGTH_LONG).show();
                                Log.i("CHECK", "success =" + response.body().getSuccess());
                            } else if (response.body().getSuccess() == 1) {
                                Toast.makeText(JoinActivity.this, "사용 가능한 이름(닉네임)입니다.", Toast.LENGTH_LONG).show();
                                nicknameCheck.setTextColor(view.getResources().getColor(R.color.gray));
                                nicknameCheck.setClickable(false);
                                Log.i("CHECK", "success =" + response.body().getSuccess());
                            } else {
                                Toast.makeText(JoinActivity.this, "중복된 이름(닉네임)입니다.", Toast.LENGTH_LONG).show();
                                Log.i("CHECK", "success =" + response.body().getSuccess());
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<RequestModel<Void>> call, Throwable t) {
                        Toast.makeText(JoinActivity.this, "서버와의 연결에 실패하였습니다." + t, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        // 완료 버튼 눌렀을 때
        joinComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( email_error.getVisibility() == View.INVISIBLE && nick_error.getVisibility() == View.INVISIBLE
                        && pw_error.getVisibility() == View.INVISIBLE
                        && !emailCheck.isClickable() && !nicknameCheck.isClickable() ) {
                    // 메일로 회원가입 요청

                    EmailJoinModel join = new EmailJoinModel(member_email.getText().toString(), member_nickname.getText().toString(), member_pw.getText().toString(), "directly");
                    Net.getInstance().getMakeryFactoryIm().emailJoin(join);
                    Call<RequestModel<Void>> res = Net.getInstance().getMakeryFactoryIm().emailJoin(join);
                    res.enqueue(new Callback<RequestModel<Void>>() {
                        @Override
                        public void onResponse(Call<RequestModel<Void>> call, Response<RequestModel<Void>> response) {
                            if(response.isSuccessful()) {
                                if(response.body().getSuccess() == 0) {
                                    Toast.makeText(JoinActivity.this, "success 0 : 에러", Toast.LENGTH_SHORT).show();
                                    Log.i("CHECK", "success =" + response.body().getSuccess());
                                }else if(response.body().getSuccess() == 1) {
                                    Toast.makeText(JoinActivity.this, "회원가입 요청 완료", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(JoinActivity.this, EmailLoginActivity.class));
                                    finish();
                                    Log.i("CHECK", "success =" + response.body().getSuccess());
                                }else {
                                    Toast.makeText(JoinActivity.this, "정보 재입력", Toast.LENGTH_SHORT).show();
                                    Log.i("CHECK", "success =" + response.body().getSuccess());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RequestModel<Void>> call, Throwable t) {
                            Toast.makeText(JoinActivity.this, "서버와의 연결에 실패하였습니다.", Toast.LENGTH_LONG).show();
                            Log.i("CHECK", "success = " + t);
                        }
                    });
                }else {
                    Toast.makeText(JoinActivity.this, "정보를 확인해주세요!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // 이메일 입력할 때 이메일 유효값 검사하기 (abc@abc.com)
        member_email.addTextChangedListener(new TextWatcher() {
            // 입력하기 전에
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            // 입력이 끝났을 때
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            // 입력되는 텍스트에 변화가 있을 때
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    email_error.setVisibility(View.VISIBLE);
                    email_error.setText("이메일을 입력하세요.");
                } else {
                    if (U.getInstance().isValidEmail(editable)) {
                        email_error.setVisibility(View.INVISIBLE);
                    } else {
                        email_error.setVisibility(View.VISIBLE);
                        email_error.setText("이메일 형식이 맞지 않습니다.");
                    }
                }
            }
        });
        // 비밀번호 유효값 검사 (8자 이상 16자 이하 입력)
        member_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    pw_error.setVisibility(View.VISIBLE);
                    pw_error.setText("영문, 숫자 포함 8자 이상 16자 이하 비밀번호를 입력해주세요.");
                } else {
                    if (U.getInstance().isValidPw(editable.toString())) {
                        pw_error.setVisibility(View.INVISIBLE);
                    } else {
                        pw_error.setVisibility(View.VISIBLE);
                        pw_error.setText("비밀번호 형식이 맞지 않습니다.");
                    }
                }
            }
        });
        // 닉네임 유효값 검사
        member_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    nick_error.setVisibility(View.VISIBLE);
                    nick_error.setText("8자 이하 이름(닉네임)을 입력해주세요.");
                } else {
                    nick_error.setVisibility(View.INVISIBLE);
                }
            }
        });

        // 띄어쓰기 방지
        member_email.setFilters(new InputFilter[]{U.getInstance().getFilter()});
        member_nickname.setFilters(new InputFilter[]{U.getInstance().getFilter(), new InputFilter.LengthFilter(8)});
        member_pw.setFilters(new InputFilter[]{U.getInstance().getFilter(), new InputFilter.LengthFilter(16)});
    }

}
