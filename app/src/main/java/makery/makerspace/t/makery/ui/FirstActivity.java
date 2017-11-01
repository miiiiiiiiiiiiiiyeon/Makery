package makery.makerspace.t.makery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import makery.makerspace.t.makery.MakeryApplication;
import makery.makerspace.t.makery.R;
import makery.makerspace.t.makery.model.EmailLoginModel;
import makery.makerspace.t.makery.model.MemberModel;
import makery.makerspace.t.makery.model.request.RequestModel;
import makery.makerspace.t.makery.net.Net;
import makery.makerspace.t.makery.util.U;
import makery.makerspace.t.makery.util.UIFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    UIFactory uiFactory;
    RelativeLayout progress;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (!isFirstEnd) {
            // 백키를 한 번 누름
            isFirstEnd = true;
            // 3초후에 초기화 (백키를 한 번 누른 상황)
            handler.sendEmptyMessageDelayed(1000, 1000 * 3); // 1초에 1000
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    boolean isFirstEnd; // 한 번 백키를 눌렀는가?

    Handler handler = new Handler() {
        // 이 메소드는 큐에 메세지가 존재하면 뽑아서 호출된다.
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1000) {      // 3초가 지났다. 다시 초기화
                isFirstEnd = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        uiFactory = UIFactory.getInstance(this);
        progress = uiFactory.createView(R.id.progress);

        // 구글 구글 구글 구글 구글 구글
        initGoogleLoginInit();
    }

    // 구글 로그인 연동
    private static final int RC_SIGN_IN = 9001; // 구글 로그인 요청 코드 ( 값은 변경 가능! )
    private FirebaseAuth mAuth;                 // fb 인증 객체
    private GoogleApiClient mGoogleApiClient;   // 구글 로그인 담당 객체(Api 담당 객체)


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 구글
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Log.i("T", "구글 로그인 실패!");
            }
        }
    }

    // 초기화
    public void initGoogleLoginInit() {
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        U.getInstance().setmGoogleApiClient(mGoogleApiClient);
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    public void onGoogleLogin(View view) {
        showProgress();
        signIn();   // 사용자가 구글 로그인 버튼 누르면 진행
    }

    public void onGoogleLogout(View view) {
        signOut();  // 구글 로그아웃
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("T", "연결 실패 :" + connectionResult);
    }

    // 구글 로그인 시작점
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        // 다른 액티비티를 구동해서 결과를 돌려 받을 때 startActivityForResult
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // 구글 로그인 성공 후 호출 코드
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("T", "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.i("GOOGLE", "displayName = " + user.getDisplayName());
                            Log.i("GOOGLE", "email = " + user.getEmail());
                            //Log.i("GOOGLE", "" + user.getUid());
                            Log.i("GOOGLE", "photoUrl = " + user.getPhotoUrl().toString());
                            Log.i("GOOGLE", "mGoogleApiClient = " + (mGoogleApiClient == null));
                            Log.i("GOOGLE", "mGoogleApiClient = " + (mGoogleApiClient.isConnected()));


                            //Toast.makeText(FirstActivity.this, "구글 계정으로 로그인 성공", Toast.LENGTH_SHORT).show();

                            MemberModel model = new MemberModel("byGmail", user.getEmail(), user.getDisplayName(), user.getPhotoUrl().toString());
                            Net.getInstance().getMakeryFactoryIm().googleJoin(model);
                            Call<RequestModel<String>> res = Net.getInstance().getMakeryFactoryIm().googleJoin(model);
                            res.enqueue(new Callback<RequestModel<String>>() {
                                @Override
                                public void onResponse(Call<RequestModel<String>> call, Response<RequestModel<String>> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getSuccess() == 0) {
                                            Toast.makeText(FirstActivity.this, "success 0 : 에러", Toast.LENGTH_LONG).show();
                                            Log.i("GOOGLE", "success =" + response.body().getSuccess());
                                        } else if (response.body().getSuccess() == 1 || response.body().getSuccess() == 2) {
                                            onLogin(model);
                                            Log.i("GOOGLE", "success =" + response.body().getSuccess());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<RequestModel<String>> call, Throwable t) {
                                    Toast.makeText(FirstActivity.this, "서버와의 연결에 실패하였습니다.", Toast.LENGTH_LONG).show();
                                }
                            });

                            ((MakeryApplication) getApplication()).setmGoogleApiClient(mGoogleApiClient);
                            startActivity(new Intent(FirstActivity.this, BottomActivity.class));
                            hideProgress();
                            finish();

                            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                    new ResultCallback<Status>() {
                                        @Override
                                        public void onResult(@NonNull Status status) {
                                            Log.i("T", "구글 로그아웃");
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(FirstActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            hideProgress();
                            finish();
                        }
                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    // 로그인
    public void onLogin(MemberModel model) {
        Call<RequestModel<String>> res = Net.getInstance().getMakeryFactoryIm().googleLogin(model);
        Log.d("TACA", "token = " + model.getMember_token() + " email=" + model.getMember_email() + " sign_way" + model.getAuth_way());
        res.enqueue(new Callback<RequestModel<String>>() {
            @Override
            public void onResponse(Call<RequestModel<String>> call, Response<RequestModel<String>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 0) {
                        Toast.makeText(FirstActivity.this, "에러", Toast.LENGTH_LONG).show();
                        Log.d("TACA", "message = " + response.body().getMessage());
                    } else if (response.body().getSuccess() == 1) {
                        String token = response.body().getResult();
                        // model 객체에 담는다.
                        model.setMember_token(token);
                        U.getInstance().savePreferences(FirstActivity.this, "token", token);

                        Intent intent = new Intent(FirstActivity.this, BottomActivity.class);
                        intent.putExtra("member", model); // map형식
                        // 하나만 띄울거야
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Toast.makeText(FirstActivity.this, "구글 계정으로 로그인", Toast.LENGTH_LONG).show();
                        Log.i("TOKEN","token = "+ token);
                    } else {
                        Toast.makeText(FirstActivity.this, "회원정보 미일치", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RequestModel<String>> call, Throwable t) {
                Toast.makeText(FirstActivity.this, "서버와의 연결에 실패하였습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        // 구글 자동로그인
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Log.i("T", "" + user.getDisplayName());
            Log.i("T", "" + user.getEmail());
            Log.i("T", "" + user.getUid());
            Log.i("T", "" + user.getPhotoUrl().toString());

            Intent intent = new Intent(FirstActivity.this, BottomActivity.class);
            // 하나만 띄울거야
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            hideProgress();
            finish();
        }

        // 이메일 자동 로그인
        String email = U.getInstance().getPreferences(FirstActivity.this, "email");
        String password = U.getInstance().getPreferences(FirstActivity.this, "password");
        Log.i("LOGIN", "email = " + email + "password = " + password);
        if( !email.equals("") && !password.equals("") ) {
            EmailLoginModel loginModel = new EmailLoginModel(email, password, "directly");
            Call<RequestModel<String>> res = Net.getInstance().getMakeryFactoryIm().emailLogin(loginModel);
            res.enqueue(new Callback<RequestModel<String>>() {
                @Override
                public void onResponse(Call<RequestModel<String>> call, Response<RequestModel<String>> response) {
                    if(response.isSuccessful()) {
                        if(response.body().getSuccess()==0) {
                            Toast.makeText(FirstActivity.this, "success 0 : 에러", Toast.LENGTH_SHORT).show();
                            Log.i("LOGIN", "success1 = "+ response.body().getSuccess());
                        }else if(response.body().getSuccess()==1) {
                            Toast.makeText(FirstActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                            // 토큰저장
                            U.getInstance().savePreferences(FirstActivity.this, "token", response.body().getResult());

                            startActivity(new Intent(FirstActivity.this, BottomActivity.class));
                            finish();
                            Log.i("LOGIN", "success2 = "+ response.body().getSuccess());
                        }else {
                            Toast.makeText(FirstActivity.this, "이메일과 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            Log.i("LOGIN", "success3 = "+ response.body().getSuccess());
                        }
                    }
                }

                @Override
                public void onFailure(Call<RequestModel<String>> call, Throwable t) {
                    Toast.makeText(FirstActivity.this, "이메일 또는 비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show();
                    Log.i("LOGIN", "success = "+ t);
                }
            });
        }
    }

    // 구글
    private void signOut() {

        // Firebase sign out
        mAuth.signOut();
        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Log.i("T", "구글 로그아웃");
                    }
                });
    }

    public void onEmailLogin(View view) {
        Log.i("T", "이메일로 로그인");
        startActivity(new Intent(FirstActivity.this, EmailLoginActivity.class));
        finish();
    }

    public void onJoin(View view) {
        startActivity(new Intent(FirstActivity.this, JoinActivity.class));
        finish();
    }

    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }
}
