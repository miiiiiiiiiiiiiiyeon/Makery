package makery.makerspace.t.makery.model;

/**
 * 이벤트 리스트 모델
 */

public class EventModel {
    String evt_id;
    String evt_title;
    int evt_hit;
    int evt_like_cnt;
    String evt_th_img_url;
    String evt_deadline;
    int evt_is_like;
    String member_token;

    public EventModel(String evt_id, String evt_title, int evt_hit, int evt_like_cnt, String evt_th_img_url, String evt_deadline, int evt_is_like, String member_token) {
        this.evt_id = evt_id;
        this.evt_title = evt_title;
        this.evt_hit = evt_hit;
        this.evt_like_cnt = evt_like_cnt;
        this.evt_th_img_url = evt_th_img_url;
        this.evt_deadline = evt_deadline;
        this.evt_is_like = evt_is_like;
        this.member_token = member_token;
    }

    public String getEvt_id() {
        return evt_id;
    }

    public void setEvt_id(String evt_id) {
        this.evt_id = evt_id;
    }

    public String getEvt_title() {
        return evt_title;
    }

    public void setEvt_title(String evt_title) {
        this.evt_title = evt_title;
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

    public String getEvt_th_img_url() {
        return evt_th_img_url;
    }

    public void setEvt_th_img_url(String evt_th_img_url) {
        this.evt_th_img_url = evt_th_img_url;
    }

    public String getEvt_deadline() {
        return evt_deadline;
    }

    public void setEvt_deadline(String evt_deadline) {
        this.evt_deadline = evt_deadline;
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
