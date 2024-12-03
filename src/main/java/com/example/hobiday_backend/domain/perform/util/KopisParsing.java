package com.example.hobiday_backend.domain.perform.util;

import java.util.HashMap;

public class KopisParsing {
    protected final static String SERVICE_KEY = "ecb03304355244159098962ad6c4a1eb"; // => secret으로 빼기
    protected final static String BASE_URL =  "http://www.kopis.or.kr/openApi/restful/pblprfr";
    protected final static String DETAIL_URL =  BASE_URL + "/";
    protected final static String FACILITY_URL =  "http://www.kopis.or.kr/openApi/restful/prfplc/";
    protected final static String STDATE = "20241215"; // 시작 검색기간
    protected final static String EDDATE = "20240113"; // 종료 검색기간
    protected final static String ROWS = "30";         // 공연 개수

    //genre : code
    public final static HashMap<String, String> GENRE_CODES_REQUEST = new HashMap<>() {{
            put("연극", "AAAA");
            put("무용", "BBBC");
            put("대중무용", "BBBE");
            put("서양음악", "CCCA");
            put("한국음악", "CCCC");
            put("대중음악", "CCCD");
            put("복합", "EEEA");
            put("서커스", "EEEB");
            put("뮤지컬", "GGGA");
    }};

// =====================================no use =====================================
//    public static HashMap<String, String> performBaseRequest = new HashMap<>(){{
//        put("baseUrl", "http://www.kopis.or.kr/openApi/restful/pblprfr");
//        put("service", "ecb03304355244159098962ad6c4a1eb");
//        put("stdate", "20241215");
//        put("eddate", "20240113");
//        put("rows", "20");
//        put("cpage", "1");
////        put("signgucode", "11");
//        put("shcate", "AAAA");
//    }};
//
//    // 상세정보 : 아이디
//    public final static HashMap<String, String> PERFORM_DETAILS_REQUEST = new HashMap<>() {{
//        put("공연상세", "mt20id");
//        put("시설상세", "mt10id");
//    }};

}
