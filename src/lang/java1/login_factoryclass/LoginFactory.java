package lang.java1.login_factoryclass;

import java.util.List;
import java.util.Map;

public class LoginFactory {
	private final List<LoginService> loginServiceList;
	private final Map<LoginType, LoginService> loginServiceMap;

	public LoginFactory(List<LoginService> loginServiceList, Map<LoginType, LoginService> loginServiceMap) {
		this.loginServiceList = loginServiceList;
		this.loginServiceMap = loginServiceMap;
	}

	public LoginService findFromList(final LoginType loginType) {
		LoginService loginService = loginServiceMap.get(loginType);
		if (loginService != null) {
			return loginService;
		}

		loginService = loginServiceList.stream()
			.filter(l -> l.type(loginType))
			.findFirst()
			.orElseThrow();

		loginServiceMap.put(loginType, loginService);
		return loginService;
	}

	public LoginService findFromMap(final LoginType loginType) {
		return loginServiceMap.get(loginType);
	}
}
