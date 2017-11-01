package makery.makerspace.t.makery.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.View;
import android.widget.ImageView;

import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import makery.makerspace.t.makery.R;
import makery.makerspace.t.makery.util.U;
import makery.makerspace.t.makery.util.UIFactory;

public class NewProjectActivity extends AppCompatActivity {

    ImageView back_btn, add_photo;
    UIFactory uiFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        uiFactory = UIFactory.getInstance(this);

        back_btn = uiFactory.createView(R.id.back_btn);
        add_photo = uiFactory.createView(R.id.add_photo);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                U.getInstance().showPopup3(NewProjectActivity.this,
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
    }

    public void onCamera(View view){
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

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

                        return;
                    }
                    bind(response.data());
                });
    }
    public void onFiles(View view){
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

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
    public void onPhoto(View view){
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

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
            Picasso.with(add_photo.getContext())
                    .load(file)
                    .error(R.mipmap.ic_launcher_round)
                    .into(add_photo);
        } else {
            Drawable drawable = AppCompatDrawableManager.get().getDrawable(add_photo.getContext(), R.mipmap.ic_launcher_round);
            add_photo.setImageDrawable(drawable);
        }
    }

    public void add_project(View view) {
        startActivity(new Intent(this, AddProjectActivity.class));
    }
}
