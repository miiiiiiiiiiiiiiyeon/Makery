package makery.makerspace.t.makery.model;

import java.util.ArrayList;

/**
 * 메뉴얼 상세보기
 */

public class DetailManualModel {
    String man_id;
    String man_level;
    String man_title;
    String member_token;
    String member_nickname;
    String member_profile_pic_url;
    String man_desc;
    String man_origin_url;
    int man_hit;
    int man_like_cnt;
    String man_img_url;
    String man_regdate;
    int man_is_like;
    String man_code_url;
    ArrayList<String> man_blueprint_img_url;

    public DetailManualModel(String man_id, String member_token) {
        this.man_id = man_id;
        this.member_token = member_token;
    }

    public String getMan_id() {
        return man_id;
    }

    public void setMan_id(String man_id) {
        this.man_id = man_id;
    }

    public String getMan_level() {
        return man_level;
    }

    public void setMan_level(String man_level) {
        this.man_level = man_level;
    }

    public String getMan_title() {
        return man_title;
    }

    public void setMan_title(String man_title) {
        this.man_title = man_title;
    }

    public String getMember_token() {
        return member_token;
    }

    public void setMember_token(String member_token) {
        this.member_token = member_token;
    }

    public String getMember_nickname() {
        return member_nickname;
    }

    public void setMember_nickname(String member_nickname) {
        this.member_nickname = member_nickname;
    }

    public String getMember_profile_pic_url() {
        return member_profile_pic_url;
    }

    public void setMember_profile_pic_url(String member_profile_pic_url) {
        this.member_profile_pic_url = member_profile_pic_url;
    }

    public String getMan_desc() {
        return man_desc;
    }

    public void setMan_desc(String man_desc) {
        this.man_desc = man_desc;
    }

    public String getMan_origin_url() {
        return man_origin_url;
    }

    public void setMan_origin_url(String man_origin_url) {
        this.man_origin_url = man_origin_url;
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

    public String getMan_img_url() {
        return man_img_url;
    }

    public void setMan_img_url(String man_img_url) {
        this.man_img_url = man_img_url;
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

    public String getMan_code_url() {
        return man_code_url;
    }

    public void setMan_code_url(String man_code_url) {
        this.man_code_url = man_code_url;
    }

    public ArrayList<String> getMan_blueprint_img_url() {
        return man_blueprint_img_url;
    }

    public void setMan_blueprint_img_url(ArrayList<String> man_blueprint_img_url) {
        this.man_blueprint_img_url = man_blueprint_img_url;
    }

    public DetailManualModel(String man_id, String man_level, String man_title, String member_token, String member_nickname, String member_profile_pic_url, String man_desc, String man_origin_url, int man_hit, int man_like_cnt, String man_img_url, String man_regdate, int man_is_like, String man_code_url, ArrayList<String> man_blueprint_img_url) {
        this.man_id = man_id;
        this.man_level = man_level;
        this.man_title = man_title;
        this.member_token = member_token;
        this.member_nickname = member_nickname;
        this.member_profile_pic_url = member_profile_pic_url;
        this.man_desc = man_desc;
        this.man_origin_url = man_origin_url;
        this.man_hit = man_hit;
        this.man_like_cnt = man_like_cnt;
        this.man_img_url = man_img_url;
        this.man_regdate = man_regdate;
        this.man_is_like = man_is_like;
        this.man_code_url = man_code_url;
        this.man_blueprint_img_url = man_blueprint_img_url;
    }
}
