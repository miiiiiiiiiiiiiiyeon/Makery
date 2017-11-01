package makery.makerspace.t.makery.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import makery.makerspace.t.makery.MakeryApplication;
import makery.makerspace.t.makery.R;
import makery.makerspace.t.makery.model.MemberModel;
import makery.makerspace.t.makery.model.request.RequestModel;
import makery.makerspace.t.makery.net.Net;
import makery.makerspace.t.makery.util.U;
import makery.makerspace.t.makery.util.UIFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMypageActivity extends AppCompatActivity {
    // 구글 로그아웃
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    UIFactory uiFactory;
    TextView logout, profile_name, profile_email;
    CircleImageView profile_img;
    ImageView back_btn;
    ImageLoader imageLoader;
    SharedPreferences imgFilePath;
    SharedPreferences.Editor imgFilePathEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mypage);
        U.getInstance().appendApp(this);

        mAuth = FirebaseAuth.getInstance();
        mGoogleApiClient = ((MakeryApplication)getApplication()).getmGoogleApiClient();

        imgFilePath = getSharedPreferences("imgFilePath", MODE_PRIVATE);
        imgFilePathEditor = imgFilePath.edit();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                //.memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                //.diskCacheExtraOptions(480, 800, null)
                //.taskExecutor(...)
		        //.taskExecutorForCachedImages(...)
		        //.threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                //.diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                //.imageDownloader(new BaseImageDownloader(context)) // default
                //.imageDecoder(new BaseImageDecoder()) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);


        uiFactory = UIFactory.getInstance(this);
        profile_img = uiFactory.createView(R.id.profile_img);
        back_btn = uiFactory.createView(R.id.back_btn);
        logout = uiFactory.createView(R.id.logout);
        profile_name = uiFactory.createView(R.id.profile_name);
        profile_email = uiFactory.createView(R.id.profile_email);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                U.getInstance().showPopup3(DetailMypageActivity.this,
                        "알림",
                        "사진을 가져올 방법을 선택하세요.",
                        "camera",
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                onCamera(null);
                            }
                        },
                        "photo",
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                onPhoto(null);
                            }
                        }
                );
            }
        });

        // 구글 로그아웃
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        // 내정보 닉네임, 프로필 사진, 이메일 설정
        String member_token = U.getInstance().getPreferences(DetailMypageActivity.this, "token");
        Call<RequestModel<MemberModel>> myPage = Net.getInstance().getMakeryFactoryIm().member(member_token);
        myPage.enqueue(new Callback<RequestModel<MemberModel>>() {
            @Override
            public void onResponse(Call<RequestModel<MemberModel>> call, Response<RequestModel<MemberModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1 && response.body().getMessage().equals("OK")) {
                        profile_name.setText(response.body().getResult().getMember_nickname());
                        Glide.with(DetailMypageActivity.this).load(response.body().getResult().getMember_profile_th_img_url()).into(profile_img);
                        profile_email.setText(response.body().getResult().getMember_email());
                        Log.i("DetailMypageActivity","email = " + response.body().getResult().getMember_email());
                    }
                }
            }

            @Override
            public void onFailure(Call<RequestModel<MemberModel>> call, Throwable t) {
            }
        });
    }

    // 구글 로그아웃
    private void signOut() {
        // Firebase sign out
        // Google sign out
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth != null){
            mAuth.signOut();
            // Google sign out
            if(U.getInstance().getmGoogleApiClient() != null && U.getInstance().getmGoogleApiClient().isConnected()){
                Auth.GoogleSignInApi.signOut(U.getInstance().getmGoogleApiClient()).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                close();
                            }
                        });
            }else{
                close();
            }
        }
    }
    public void close()
    {
        Toast.makeText(DetailMypageActivity.this, "구글 로그 아웃", Toast.LENGTH_LONG).show();
        U.getInstance().closeApps();
        Intent intent = new Intent(this, FirstActivity.class);
        startActivity(intent);
    }

    public void onCamera(View view) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(DetailMypageActivity.this, R.color.colorPrimaryDark));

        // crop(options) => 사진을 찍은 후 편집 메뉴를 띄운다.
        // usingCamera() => 카메라를 띄운다.
        // usingGallery() => 포토 앨범을 띄운다.
        RxPaparazzo.single(this)
                .crop(options)
                .usingCamera()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    // See response.resultCode() doc
                    if (response.resultCode() != RESULT_OK) {
                        File file = response.data().getFile();
                        imgFilePathEditor.putString("imgFilePath", file.getAbsolutePath());
                        imgFilePathEditor.commit();
                        return;
                    }
                    bind(response.data());
                });
    }

    public void onFiles(View view) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(DetailMypageActivity.this, R.color.colorPrimaryDark));

        // crop(options) => 사진을 찍은 후 편집 메뉴를 띄운다.
        // usingCamera() => 카메라를 띄운다.
        // usingGallery() => 포토 앨범을 띄운다.
        RxPaparazzo.single(this)
                .crop(options)
                .usingFiles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    // See response.resultCode() doc
                    if (response.resultCode() != RESULT_OK) {

                        return;
                    }
                    bind(response.data());
                });
    }

    public void onPhoto(View view) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(DetailMypageActivity.this, R.color.colorPrimaryDark));

        // crop(options) => 사진을 찍은 후 편집 메뉴를 띄운다.
        // usingCamera() => 카메라를 띄운다.
        // usingGallery() => 포토 앨범을 띄운다.
        RxPaparazzo.single(this)
                .crop(options)
                .usingGallery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    // See response.resultCode() doc
                    if (response.resultCode() != RESULT_OK) {

                        return;
                    }
                    bind(response.data());
                });
    }

    void bind(FileData fileData) {
        // 이미지를 서버로 전송송
        File file = fileData.getFile();
        if (file != null && file.exists()) {
            Log.i("TACA", "COME 1"+file.getAbsolutePath());

            Picasso.with(profile_img.getContext())
                    .load(file)
                    //.load("http://i.imgur.com/DvpvklR.png")
                    //.resize(50, 50)
                    //.error(R.mipmap.btn_mail_mw9)
                    .into(profile_img);
        } else {
            Log.i("TACA", "COME 2");
            Drawable drawable = AppCompatDrawableManager.get().getDrawable(profile_img.getContext(), R.mipmap.ic_launcher_round);
            profile_img.setImageDrawable(drawable);
        }
    }
}



