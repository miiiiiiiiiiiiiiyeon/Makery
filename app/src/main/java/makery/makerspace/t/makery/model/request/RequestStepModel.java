package makery.makerspace.t.makery.model.request;

import java.util.ArrayList;

/**
 * Created by MIYEON on 2017-08-31.
 */

public class RequestStepModel<T> {
    private String man_comments;
    private ArrayList<T> man_steps;

    public String getMan_comments() {
        return man_comments;
    }

    public void setMan_comments(String man_comments) {
        this.man_comments = man_comments;
    }

    public ArrayList<T> getMan_steps() {
        return man_steps;
    }

    public void setMan_steps(ArrayList<T> man_steps) {
        this.man_steps = man_steps;
    }
}
