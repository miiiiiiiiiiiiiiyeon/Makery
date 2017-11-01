package makery.makerspace.t.makery.model;

/**
 * 매뉴얼 리스트 ; 작은 그릇
 */

public class ManualModel {
    String man_id;
    String man_title;
    String member_nickname;
    int man_hit;
    int man_like_cnt;
    String man_th_img_url;
    String man_regdate;
    int man_is_like;

    public String getMan_id() {
        return man_id;
    }

    public void setMan_id(String man_id) {
        this.man_id = man_id;
    }

    public String getMan_title() {
        return man_title;
    }

    public void setMan_title(String man_title) {
        this.man_title = man_title;
    }

    public String getMember_nickname() {
        return member_nickname;
    }

    public void setMember_nickname(String member_nickname) {
        this.member_nickname = member_nickname;
    }

    public int getMan_hit() {
        return man_hit;
    }

    public void setMan_hit(int man_hit) {
        this.man_hit = man_hit;
    }

    public int getMan_like_cnt() {
        return man_like_cnt;
    }

    public void setMan_like_cnt(int man_like_cnt) {
        this.man_like_cnt = man_like_cnt;
    }

    public String getMan_th_img_url() {
        return man_th_img_url;
    }

    public void setMan_th_img_url(String man_th_img_url) {
        this.man_th_img_url = man_th_img_url;
    }

    public String getMan_regdate() {
        return man_regdate;
    }

    public void setMan_regdate(String man_regdate) {
        this.man_regdate = man_regdate;
    }

    public int getMan_is_like() {
        return man_is_like;
    }

    public void setMan_is_like(int man_is_like) {
        this.man_is_like = man_is_like;
    }
}
