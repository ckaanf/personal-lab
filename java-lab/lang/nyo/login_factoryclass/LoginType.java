package nyo.login_factoryclass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum LoginType {
	KAKAO("KAKAO", new ArrayList<>()),
	NAVER("NAVER", new ArrayList<>()),
	GOOGLE("GOOGLE",new ArrayList<>());

	private static final Map<String, LoginType> loginTypeMap = Collections.unmodifiableMap(Stream.of(values()).collect(
		Collectors.toMap(LoginType::type, Function.identity())));

	private final String type;
	private final List<LoginService> loginServiceList;

	LoginType(final String key, List<LoginService> loginServiceList) {
		this.type = key;
		this.loginServiceList = loginServiceList;
	}

	public String type() {
		return type;
	}

	public static LoginType findByType(final String type) {
		if (loginTypeMap.containsKey(type)) {
			System.out.println("로그인 타입: " + type);
			return loginTypeMap.get(type);
		}
		throw new IllegalArgumentException("로그인 타입을 찾을 수 없습니다.: " + type);
	}
}

