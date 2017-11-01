package makery.makerspace.t.makery;

import android.support.multidex.MultiDexApplication;

import com.google.android.gms.common.api.GoogleApiClient;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;

/**
 * Created by MIYEON on 2017-08-09.
 */

public class MakeryApplication extends MultiDexApplication {
    private static GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        RxPaparazzo.register(this);
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        MakeryApplication.mGoogleApiClient = mGoogleApiClient;
    }
}
