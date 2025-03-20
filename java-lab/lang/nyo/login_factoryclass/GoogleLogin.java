package nyo.login_factoryclass;

public class GoogleLogin implements LoginService {

	@Override
	public boolean type(final LoginType loginType) {
		return loginType == LoginType.GOOGLE;
	}

	@Override
	public void login() {
		System.out.println("Google LOGIN");
	}
}
