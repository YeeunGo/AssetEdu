package kr.co.kfs.assetedu.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 클래스의 각 field를 map으로 변환한다
 * ex) Map<String,Object> map = AssetUtil.toMap(sys01User);
 * @author Kim Do Young
 *
 */
public class AssetUtil {
	public static Map<String, Object> toMap(Object o ) {
		
		// o의 객체를 가져와서 해당 객체의 모든 매서드를 가져온다.
		Method[] methods = o.getClass().getDeclaredMethods();
		Map<String,Object> map = new HashMap<>();

		for (int i = 0; i < methods.length; i++) {
			//name이 get으로 시작한다면
			if (methods[i].getName().startsWith("get")) {
				try {
					// 매개변수의 갯수가 0이면 이러한 코드를 실행한다.(getter라는 뜻)
					if( methods[i].getGenericParameterTypes().length == 0){
								// 예를 들어서 getUserName이라는 매서드의 이름에서 userName 이라는 필드 명만 추출하기 위한 것.
						map.put(methods[i].getName().substring(3,4).toLowerCase()+ methods[i].getName().substring(4),
								// 해당 매서드의 결과값을 받아오는 것 getter의 값은 그 필드의 값이다.
								methods[i].invoke(o));
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	public static String removeDash(String ymd) {
		if (ymd == null || ymd.length() < 1) return null;
		return ymd.replaceAll("-", "");
	}

	public static Long removeComma(String amt) {
		if (amt == null || amt.length() < 1) return null;
		return Long.parseLong(amt.replaceAll(",", ""));
	}

	public static String today() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		return LocalDate.now().format(formatter);
	}

	/**
	 * 날짜를 ymd yyyy-mm-dd로 변환한다. 
	 * @param frDate
	 * @return
	 */
	public static String displayYmd(String ymd) {
		if(ymd == null) return ymd;
		if(ymd.replaceAll("\\D", "").length() == 8) {
			return ymd.substring(0,4) +"-"+ymd.substring(4,6) +"-"+ymd.substring(6); 
		}
		return ymd;
	}

	public static String ymd() {
		return today().replaceAll("\\D", "");
	}
}
