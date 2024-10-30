package lang.java1.login_factoryclass;

public class KakaoLogin implements LoginService {

	@Override
	public boolean type(final LoginType loginType) {
		return loginType == LoginType.KAKAO;
	}

	@Override
	public void login() {
		System.out.println("KAKAO LOGIN");
	}
}
