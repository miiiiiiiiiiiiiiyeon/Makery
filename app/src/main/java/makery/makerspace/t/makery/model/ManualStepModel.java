package makery.makerspace.t.makery.model;

import java.util.ArrayList;

/**
 * 매뉴얼 상세보기 중 스텝
 */

public class ManualStepModel {
    String man_comments;
    ArrayList<ManualStep2Model> man_steps;

    public String getMan_comments() {
        return man_comments;
    }

    public void setMan_comments(String man_comments) {
        this.man_comments = man_comments;
    }

    public ArrayList<ManualStep2Model> getMan_steps() {
        return man_steps;
    }

    public void setMan_steps(ArrayList<ManualStep2Model> man_steps) {
        this.man_steps = man_steps;
    }
}
