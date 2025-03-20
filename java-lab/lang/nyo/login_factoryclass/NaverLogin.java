package nyo.login_factoryclass;

public class NaverLogin implements LoginService {

	@Override
	public boolean type(final LoginType loginType) {
		return loginType == LoginType.KAKAO;
	}

	@Override
	public void login() {
		System.out.println("Naver LOGIN");
	}
}
