package makery.makerspace.t.makery.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 어느곳에서든지 사용할 수 있는 데이터를 저장하는 클래스 ( 태현 만듦 )
 */

public class DataDefinition {
    public static Map<String, String> Level_Map, Ctype_Map, Subject_Map;
    public static Map<String, String> Etype_Map, Region_Map;

    static {
        // lev000 난이도
        Level_Map = new HashMap<>();
        Level_Map.put("상","lev003");
        Level_Map.put("중","lev002");
        Level_Map.put("하","lev001");

        // ctype000 종류
        Ctype_Map = new HashMap<>();
        Ctype_Map.put("아두이노","ctype001");
        Ctype_Map.put("라즈베리파이","ctype002");
        Ctype_Map.put("갈릴레오","ctype003");
        Ctype_Map.put("비글본블랙","ctype004");
        Ctype_Map.put("기타","ctype999");

        // sbj000 주제
        Subject_Map = new HashMap<>();
        Subject_Map.put("3D프린터","sbj001");
        Subject_Map.put("홈오토매틱","sbj002");
        Subject_Map.put("조명","sbj003");
        Subject_Map.put("로봇","sbj004");
        Subject_Map.put("주제","sbj999");
    }

    public static final String SEARCH_WAY_POP = "pop"
                                , SEARCH_WAY_TIME = "time"
                                , DEFAULT_LEVEL = "lev000"
                                , DEFAULT_CTYPE = "ctype000"
                                , DEFAULT_SUBJECT = "sbj000";

    static {
        // etype000 종류
        Etype_Map = new HashMap<>();
        Etype_Map.put("교육", "etype001");
        Etype_Map.put("컨설팅", "etype002");
        Etype_Map.put("공모전", "etype003");
        Etype_Map.put("창업", "etype004");
        Etype_Map.put("기타", "etype999");

        // region000 지역
        Region_Map = new HashMap<>();
        Region_Map.put("서울", "region002");
        Region_Map.put("경기", "region003");
        Region_Map.put("인천", "region004");
    }

    public static final String DEFAULT_ETYPE = "etype000"
                                , DEFAULT_REGION = "region000";

}

