package makery.makerspace.t.makery.model;

import java.io.Serializable;

/**
 * 구글 계정으로 회원가입 ; 값을 서버한테 보내기 위해 생성자를 만듦! => 값이 있는 객체를 만들기 위해, 객체를 전달하기 위해
 */

public class MemberModel implements Serializable{
    String auth_way;
    String member_email;
    String member_nickname;
    String member_profile_img_url;
    String member_profile_th_img_url;
    String member_token;

    public MemberModel(String auth_way, String member_email, String member_nickname, String member_profile_img_url) {
        this.auth_way = auth_way;
        this.member_email = member_email;
        this.member_nickname = member_nickname;
        this.member_profile_img_url = member_profile_img_url;
    }

    public String getAuth_way() {
        return auth_way;
    }

    public void setAuth_way(String auth_way) {
        this.auth_way = auth_way;
    }

    public String getMember_email() {
        return member_email;
    }

    public void setMember_email(String member_email) {
        this.member_email = member_email;
    }

    public String getMember_nickname() {
        return member_nickname;
    }

    public void setMember_nickname(String member_nickname) {
        this.member_nickname = member_nickname;
    }

    public String getMember_profile_img_url() {
        return member_profile_img_url;
    }

    public void setMember_profile_img_url(String member_profile_img_url) {
        this.member_profile_img_url = member_profile_img_url;
    }

    public String getMember_token() {
        return member_token;
    }

    public void setMember_token(String member_token) {
        this.member_token = member_token;
    }

    public String getMember_profile_th_img_url() {
        return member_profile_th_img_url;
    }

    public void setMember_profile_th_img_url(String member_profile_th_img_url) {
        this.member_profile_th_img_url = member_profile_th_img_url;
    }
}
