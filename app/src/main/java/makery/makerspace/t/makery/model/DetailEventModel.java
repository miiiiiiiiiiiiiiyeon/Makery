package makery.makerspace.t.makery.model;

/**
 * 이벤트 상세보기 모델
 */

public class DetailEventModel {
    String evt_id;
    String evt_img_url;
    String evt_origin_url;
    int evt_hit;
    int evt_like_cnt;
    int evt_is_like;
    String member_token;

    public DetailEventModel(String evt_id, String member_token) {
        this.evt_id = evt_id;
        this.member_token = member_token;
    }

    public String getEvt_id() {
        return evt_id;
    }

    public void setEvt_id(String evt_id) {
        this.evt_id = evt_id;
    }

    public String getEvt_img_url() {
        return evt_img_url;
    }

    public void setEvt_img_url(String evt_img_url) {
        this.evt_img_url = evt_img_url;
    }

    public String getEvt_origin_url() {
        return evt_origin_url;
    }

    public void setEvt_origin_url(String evt_origin_url) {
        this.evt_origin_url = evt_origin_url;
    }

    public int getEvt_hit() {
        return evt_hit;
    }

    public void setEvt_hit(int evt_hit) {
        this.evt_hit = evt_hit;
    }

    public int getEvt_like_cnt() {
        return evt_like_cnt;
    }

    public void setEvt_like_cnt(int evt_like_cnt) {
        this.evt_like_cnt = evt_like_cnt;
    }

    public int getEvt_is_like() {
        return evt_is_like;
    }

    public void setEvt_is_like(int evt_is_like) {
        this.evt_is_like = evt_is_like;
    }

    public String getMember_token() {
        return member_token;
    }

    public void setMember_token(String member_token) {
        this.member_token = member_token;
    }
}
