package makery.makerspace.t.makery.model;

import java.util.ArrayList;

/**
 * 매뉴얼 스텝2 모델
 */

public class ManualStep2Model {
    int step_order;
    String step_desc;
    ArrayList<String> step_imgs_url;
    ArrayList<String> step_videos_url;

    public int getStep_order() {
        return step_order;
    }

    public void setStep_order(int step_order) {
        this.step_order = step_order;
    }

    public String getStep_desc() {
        return step_desc;
    }

    public void setStep_desc(String step_desc) {
        this.step_desc = step_desc;
    }

    public ArrayList<String> getStep_imgs_url() {
        return step_imgs_url;
    }

    public void setStep_imgs_url(ArrayList<String> step_imgs_url) {
        this.step_imgs_url = step_imgs_url;
    }

    public ArrayList<String> getStep_videos_url() {
        return step_videos_url;
    }

    public void setStep_videos_url(ArrayList<String> step_videos_url) {
        this.step_videos_url = step_videos_url;
    }
}
