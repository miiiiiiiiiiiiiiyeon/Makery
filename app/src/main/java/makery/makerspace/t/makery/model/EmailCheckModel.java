package makery.makerspace.t.makery.model;

/**
 * 이메일 중복 확인
 */

public class EmailCheckModel {
    String member_email;
    String signup_way;

    public String getMember_email() {
        return member_email;
    }

    public void setMember_email(String member_email) {
        this.member_email = member_email;
    }

    public String getSignup_way() {
        return signup_way;
    }

    public void setSignup_way(String signup_way) {
        this.signup_way = signup_way;
    }
}
