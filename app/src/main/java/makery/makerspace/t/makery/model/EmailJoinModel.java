package makery.makerspace.t.makery.model;

/**
 * 이메일로 회원가입 요청
 */

public class EmailJoinModel {
    String member_email;
    String member_nickname;
    String member_pswd;
    String auth_way;

    public EmailJoinModel(String member_email, String member_nickname, String member_pswd, String auth_way) {
        this.member_email = member_email;
        this.member_nickname = member_nickname;
        this.member_pswd = member_pswd;
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

    public String getMember_pswd() {
        return member_pswd;
    }

    public void setMember_pswd(String member_pswd) {
        this.member_pswd = member_pswd;
    }

    public String getAuth_way() {
        return auth_way;
    }

    public void setAuth_way(String auth_way) {
        this.auth_way = auth_way;
    }
}
