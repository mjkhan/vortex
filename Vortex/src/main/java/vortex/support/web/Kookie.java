package vortex.support.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vortex.support.AbstractObject;

/**쿠키 처리를 돕는 유틸리티.
 * <p>Kookie는 {@link #get(HttpServletRequest) HttpServletRequest}에서 얻는다.<br />
 * 이렇게 얻은 Kookie로 저장된 {@link #getValue(String) 값을 읽을 수 있다}.
 * </p>
 * <p>Kookie로 {@link #save(String, String, int) 값을 저장하려면 }, {@link #setResponse(HttpServletResponse) HttpServletResponse를 설정}해야 한다.<br />
 * 쿠키값 저장을 돕는 편의성 메소드로 {@link #longSave(String, String) longSave(...)}와 {@link #shortSave(String, String) shortSave(...)}가 제공된다.
 * </p>
 */
public class Kookie extends AbstractObject {
	private static final String NAME = "vtx_kookie";

	public static final Kookie get(HttpServletRequest request) {
		Kookie kookie = (Kookie)request.getAttribute(NAME);
		if (kookie == null)
			request.setAttribute(NAME, kookie = new Kookie().setRequest(request));
		return kookie;
	}

	private String
		domain,
		path;
	private HttpServletRequest request;
	private HttpServletResponse response;
	/**Kookie를 생성한다.
	 */
	public Kookie() {}
	/**Kookie에 request를 설정한다. request가 설정되면 Kookie의 경로는 request의 context path로 설정된다.
	 * @param request HttpServletRequest
	 * @return Kookie
	 */
	public Kookie setRequest(HttpServletRequest request) {
		this.request = request;
		setPath(request.getContextPath());
		return this;
	}
	/**쿠키값을 저장할 때 사용할 response를 설정한다..
	 * @param response HttpServletResponse
	 * @return Kookie
	 */
	public Kookie setResponse(HttpServletResponse response) {
		this.response = response;
		return this;
	}
	/**쿠키의 도메인을 설정한다.
	 * @param domain domain for cookies
	 * @return the Kookie
	 */
	public Kookie setDomain(String domain) {
		this.domain = domain;
		return this;
	}

	private String domain() {
		return domain;
	}
	/**쿠키의 경로를 설정한다.
	 * @param path 쿠키의 경로
	 * @return Kookie
	 */
	public Kookie setPath(String path) {
		this.path = path;
		return this;
	}

	private String path() {
		return ifEmpty(path, "/");
	}
	/**지정된 이름의 쿠키값을 반환한다.
	 * @param name 쿠키 이름
	 * @return 쿠키값
	 */
	public String getValue(String name) {
		String value = (String)request.getAttribute("kookie-" + name);
		if (value != null) return value;

    	Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie: cookies) {
				if (cookie == null) continue;
				if (name.equals(cookie.getName()))
					return cookie.getValue();
			}
		return null;
	}
	/**지정하는 이름의 쿠키가 존재하는지 반환한다.
	 * @param name 쿠키 이름
	 * @return
	 * <ul><li>지정하는 이름의 쿠키가 존재하면 true</li>
	 * 	   <li>그렇지 않으면 false</li>
	 * </ul>
	 */
	public boolean exists(String name) {
		return !isEmpty(getValue(name));
	}
	/**주어진 값을 지정하는 이름의 쿠키로 지정한 수명으로 저장한다.
	 * @param name	쿠키 이름
	 * @param value	쿠키값
	 * @param age	쿠키 만료 기간
	 * @return Kookie
	 */
	public Kookie save(String name, String value, int age) {
        Cookie cookie = new Cookie(notEmpty(name, "name"), value);
        cookie.setMaxAge(age);
        cookie.setPath(path());
        if (!isEmpty(domain())) cookie.setDomain(domain());
        notEmpty(response, "response").addCookie(cookie);

        if (isEmpty(value)) request.removeAttribute("kookie-" + name);
        else request.setAttribute("kookie-" + name, value);

        return this;
    }
	/**주어진 값을 지정한 이름의 쿠키로 수명 -1로 저장한다.
	 * @param name	쿠키 이름
	 * @param value	쿠키값
	 * @return Kookie
	 */
	public Kookie shortSave(String name, String value) {
		return !isEmpty(value) ? save(name, value, -1) : remove(name);
	}
	/**주어진 값을 지정한 이름의 쿠키로 수명 Integer.MAX_VALUE로 저장한다.
	 * @param name	쿠키 이름
	 * @param value	쿠키값
	 * @return Kookie
	 */
	public Kookie longSave(String name, String value) {
		return !isEmpty(value) ? save(name, value, Integer.MAX_VALUE) : remove(name);
	}
	/**지정한 이름의 쿠키들을 삭제한다.
	 * @param names	쿠키 이름
	 * @return Kookie
	 */
	public Kookie remove(String... names) {
		if (!isEmpty(names))
			for (String name: names)
				save(name, null, 0);
		return this;
	}
	/**Kookie를 비운다.
	 * @return Kookie
	 */
	public Kookie clear() {
		if (request != null)
			request.removeAttribute(NAME);
		domain = path = null;
		request = null;
		response = null;
		return this;
	}
}
