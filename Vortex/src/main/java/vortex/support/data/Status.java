package vortex.support.data;

import vortex.support.Assert;

/**엔티티의 상태를 나타내는 열거형 클래스
 */
public enum Status {
	/**엔티티가 생성된 상태. 의미는 애플리케이션에서 정한다.
	 */
	CREATED("000"),
	/**엔티티가 활성화된 상태
	 */
	ACTIVE("001"),
	/**엔티티가 잠긴 상태*/
	LOCKED("900"),
	/**엔티티가 비활성화된 상태
	 */
	INACTIVE("998"),
	/**엔티티가 제거된 상태
	 */
	REMOVED("999");
	
	private final String code;
	
	private Status(String code) {
		this.code = code;
	}
	
	/**현재 상태의 코드값을 반환한다.
	 * @return 현재 상태의 코드값
	 */
	public String code() {
		return code;
	}
	
	/**code에 해당하는 Status를 반환한다.
	 * @param code 상태의 코드값
	 * @return
	 * <ul><li>code에 해당하는 Status</li>
	 * 	   <li>code가 비어있으면 null</li>
	 * </ul>
	 * @throws 잘못된 code일 경우 IllegalArgumentException
	 */
	public static Status codeOf(String code) {
		if (Assert.isEmpty(code))
			return null;
		for (Status status: values()) {
			if (code.equals(status.code))
				return status;
		}
		throw new IllegalArgumentException("Invalid Status code:" + code);
	}
}