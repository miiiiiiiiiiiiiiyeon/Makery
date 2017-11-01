package makery.makerspace.t.makery.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;

/**
 * 유틸리티
 */

public class U {
    // =============================================================================================
    private static final U ourInstance = new U();

    public static U getInstance() {
        return ourInstance;
    }

    private U() {
    }
    // =============================================================================================

    public GoogleApiClient mGoogleApiClient;

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }
    ArrayList<Activity> apps = new ArrayList<>();

    public ArrayList<Activity> getApps() {
        return apps;
    }

    public void setApps(ArrayList<Activity> apps) {
        this.apps = apps;
    }
    public void appendApp(Activity activity)
    {
        apps.add(activity);
    }
    public void closeApps()
    {
        for( Activity activity: apps)
        {
            if( activity != null && !activity.isFinishing())
                activity.finish();
        }
    }

    // 팝업
    // =============================================================================================
    public void showPopup3(Context context, String title, String msg,
                           String cName, SweetAlertDialog.OnSweetClickListener cEvent,
                           String oName, SweetAlertDialog.OnSweetClickListener oEvent)
    {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(msg)
                .setConfirmText(cName)
                .setConfirmClickListener(cEvent)
                .setCancelText(oName)
                .setCancelClickListener(oEvent)
                .show();

    }
    // =============================================================================================

    // 실습, onCamera, onPhoto 메소드 생성성
    /*
    public void onCamera(Context context){
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

        // crop(options) => 사진을 찍은 후 편집 메뉴를 띠운다.
        // usingCamera() => 카메라를 띠운다.
        // usingGallery() => 포토 앨범을 띠운다.
        RxPaparazzo.single(context)
                .crop(options)
                .usingCamera()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    // See response.resultCode() doc
                    if (response.resultCode() != RESULT_OK) {

                        return null;
                    }
                    return response.data();
                });
    }

    public void onPhoto(Context context){
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

        // crop(options) => 사진을 찍은 후 편집 메뉴를 띠운다.
        // usingCamera() => 카메라를 띠운다.
        // usingGallery() => 포토 앨범을 띠운다.
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

    public void onFiles(Context context){
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

        // crop(options) => 사진을 찍은 후 편집 메뉴를 띠운다.
        // usingCamera() => 카메라를 띠운다.
        // usingGallery() => 포토 앨범을 띠운다.
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
    */

    public SweetAlertDialog showLoading(Context context)
    {
        return showLoading(context, "LOADING");
    }

    public SweetAlertDialog showLoading(Context context, String msg)
    {
        return showLoading(context, msg, "#A5DC86");
    }
    public SweetAlertDialog showLoading(Context context, String msg, String color)
    {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false); // 백키를 눌러도 닫히지 않는다.
        pDialog.show();
        return pDialog;
    }

    public void showSimplePopup(Context context, String title, String msg, int type)
    {
        new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(msg)
                .show();
    }

    // 이메일 형식 체크
    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    // 비밀번호 형식 체크
    public boolean isValidPw(String hex) {
        String password_pattern = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,16}$";
        Pattern pattern = Pattern.compile(password_pattern);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    // 빈칸 제거 inputfilter
    InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    };

    public InputFilter getFilter() {
        return filter;
    }

    // 값 불러오기
    public String getPreferences(Context context, String key){
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getString(key, "");
    }

    // 값 저장하기
    public void savePreferences(Context context, String key, String value){
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    // 값(Key Data) 삭제하기
    private void removePreferences(Context context){
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("hi");
        editor.commit();
    }

    // 값(ALL Data) 삭제하기
    private void removeAllPreferences(Context context){
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    //이미지 파일 경로


}