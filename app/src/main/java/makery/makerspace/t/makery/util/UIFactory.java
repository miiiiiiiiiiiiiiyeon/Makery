package makery.makerspace.t.makery.util;

import android.app.Activity;
import android.view.View;

/**
 * 태현이가 준 findViewById 대체용
 */

public class UIFactory {
    private static UIFactory uiFactory = new UIFactory();
    private static boolean isActivity = false;
    private Activity activity;
    private View view;

    public static UIFactory getInstance(View view) {
        uiFactory.setResource(view);
        isActivity = false;
        return uiFactory;
    }

    public static UIFactory getInstance(Activity activity) {
        uiFactory.setResource(activity);
        isActivity = true;
        return uiFactory;
    }

    public <E extends View> E createView(int id) {
        E e;
        if (isActivity)
            e = activity.findViewById(id);
        else
            e = view.findViewById(id);
        return e;
    }

    public void setResource(Activity activity) {
        this.activity = activity;
    }

    public void setResource(View view) {
        this.view = view;
    }

}
