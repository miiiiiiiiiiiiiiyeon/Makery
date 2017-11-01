package makery.makerspace.t.makery.net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ListTest에 있던 Net
 */
public class Net {
    // 싱글톤
    // =========================================================
    private static Net ourInstance = new Net();
    public static Net getInstance() {
        return ourInstance;
    }
    private Net() {}
    // =========================================================
    // retrofit 생성
    private Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://makery.cf:3000") // 기본 도메인 설정 (규순님이 만든거)
                .addConverterFactory(GsonConverterFactory.create()) // 응답데이터를 json 자동 변환
                .build();

    public Retrofit getRetrofit() {
        return retrofit;
    }
    // =========================================================
    // API 담당 인터페이스 생성
    // API 담당 인터페이스의 객체를 생성
    MakeryFactoryIm makeryFactoryIm;
    // 객체를 리턴해 주는 getter 준비
    public MakeryFactoryIm getMakeryFactoryIm()
    {
        if( makeryFactoryIm == null ){
            // 인터페이스로 정의된 메소드를 사용할수 있게 객체화 시켜준다
            makeryFactoryIm = retrofit.create(MakeryFactoryIm.class);
        }
        return makeryFactoryIm;
    }
}






