package makery.makerspace.t.makery.model;

/**
 * 닉네임 중복 확인
 */

public class NicknameCheckModel {
    String member_nickname;
    String signup_way;

    public String getMember_nickname() {
        return member_nickname;
    }

    public void setMember_nickname(String member_nickname) {
        this.member_nickname = member_nickname;
    }

    public String getSignup_way() {
        return signup_way;
    }

    public void setSignup_way(String signup_way) {
        this.signup_way = signup_way;
    }
}
