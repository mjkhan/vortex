package vortex.support.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.ExtendedProperties;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;

import vortex.support.AbstractObject;

public class PropertyManager extends AbstractObject implements InitializingBean, DisposableBean, ResourceLoaderAware {
	private ResourceLoader resourceLoader;
	private ExtendedProperties extendedProperties;
	private Set<?> extFilenames;
	private Map<?, ?> properties;
	
	/**extFilenames를 지정할 때 Attribute로 정의
	 * @param extFilenames
	 */
	public void setExtFilenames(Set<?> extFilenames) {
		this.extFilenames = extFilenames;
	}

	/**properties를 지정할 때 Attribute로 정의
	 * @param properties
	 */
	public void setProperties(Map<?, ?> properties) {
		this.properties = properties;
	}
	/**boolean 타입의 프로퍼티 값 얻기
	 * @param name  프로퍼티키
	 * @return boolean 타입의 값
	 */
	public boolean getBoolean(String name) {
		return getConfiguration().getBoolean(name);
	}

	/**boolean 타입의 프로퍼티 값 얻기(디폴트값을 입력받음)
	 * @param name 프로퍼티키
	 * @param def 디폴트 값
	 * @return boolean 타입의 값
	 */
	public boolean getBoolean(String name, boolean def) {
		return getConfiguration().getBoolean(name, def);
	}

	/**double 타입의 프로퍼티 값 얻기
	 * @param name 프로퍼티키
	 * @return double 타입의 값
	 */
	public double getDouble(String name) {
		return getConfiguration().getDouble(name);
	}

	/**double 타입의 프로퍼티 값 얻기
	 * @param name 프로퍼티키
	 * @param def 디폴트 값
	 * @return double 타입의 값
	 */
	public double getDouble(String name, double def) {
		return getConfiguration().getDouble(name, def);
	}

	/**float 타입의 프로퍼티 값 얻기
	 * @param name 프로퍼티키
	 * @return Float 타입의 값
	 */
	public float getFloat(String name) {
		return getConfiguration().getFloat(name);
	}

	/**float 타입의 프로퍼티 값 얻기
	 * @param name 프로퍼티키
	 * @param def 디폴트 값
	 * @return float 타입의 값
	 */
	public float getFloat(String name, float def) {
		return getConfiguration().getFloat(name, def);
	}

	/**int 타입의 프로퍼티 값 얻기
	 * @param name 프로퍼티키
	 * @return int 타입의 값
	 */
	public int getInt(String name) {
		return getConfiguration().getInt(name);
	}

	/**int 타입의 프로퍼티 값 얻기
	 * @param name 프로퍼티키
	 * @param def 디폴트 값
	 * @return int 타입의 값
	 */
	public int getInt(String name, int def) {
		return getConfiguration().getInt(name, def);
	}

	/**프로퍼티 키 목록 읽기
	 * @return Key를 위한 Iterator
	 */
	public Iterator<?> getKeys() {
		return getConfiguration().getKeys();
	}

	/**prefix를 이용한 키 목록 읽기
	 * @param prefix prefix
	 * @return prefix에 매칭되는 키목록
	 */
	public Iterator<?> getKeys(String prefix) {
		return getConfiguration().getKeys(prefix);
	}

	/**long 타입의 프로퍼티 값 얻기
	 * @param name 프로퍼티키
	 * @return long 타입의 값
	 */
	public long getLong(String name) {
		return getConfiguration().getLong(name);
	}

	/**long 타입의 프로퍼티 값 얻기
	 * @param name 프로퍼티키
	 * @param def 디폴트 값
	 * @return long 타입의 값
	 */
	public long getLong(String name, long def) {
		return getConfiguration().getLong(name, def);
	}

	/**String 타입의 프로퍼티 값 얻기
	 * @param name 프로퍼티키
	 * @return String 타입의 값
	 */
	public String getString(String name) {
		return getConfiguration().getString(name);
	}

	/**String 타입의 프로퍼티 값 얻기
	 * @param name 프로퍼티키
	 * @param def 디폴트 값
	 * @return String 타입의 값
	 */
	public String getString(String name, String def) {
		return getConfiguration().getString(name, def);
	}

	/**String[] 타입의 프로퍼티 값 얻기
	 * @param name 프로퍼티키
	 * @return String[] 타입의 값
	 */
	public String[] getStringArray(String name) {
		return getConfiguration().getStringArray(name);
	}

	/**List 타입의 프로퍼티 값 얻기
	 * 
	 * @param name 프로퍼티키
	 * @return List 타입의 값
	 */
	public List<?> getList(String name) {
		return getConfiguration().getList(name);
	}

	/**List 타입의 프로퍼티 값 얻기
	 * @param name 프로퍼티키
	 * @param def 디폴트 값
	 * @return List 타입의 값
	 */
	public List<?> getList(String name, List<?> def) {
		return getConfiguration().getList(name, def);
	}

	/**전체 키/값 쌍 얻기
	 * @return Vector 타입의 값
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> getValues() {
		return getConfiguration().values();
	}

	/**extendedProperties 얻기
	 * @return Properties of requested Service.
	 */
	private ExtendedProperties getConfiguration() {
		return extendedProperties;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			extendedProperties = new ExtendedProperties();
			// 외부파일이 정의되었을때
			if (extFilenames != null)
				refreshPropertyFiles();

			if (properties != null)
				properties.forEach(extendedProperties::put);
		} catch (Exception e) {
			throw runtimeException(e);
		}
	}
	/**파일위치정보를 가지고 resources 정보 추출
	 * @param location 파일위치
	 * @param encoding Encoding 정보
	 * @throws Exception
	 */
	private void loadPropertyResources(String location, String encoding) throws Exception {
		if (resourceLoader instanceof ResourcePatternResolver) {
			try {
				Resource[] resources = ((ResourcePatternResolver) resourceLoader).getResources(location);

				loadPropertyLoop(resources, encoding);
			} catch (IOException ex) {
				throw new BeanDefinitionStoreException("Could not resolve Properties resource pattern [" + location + "]", ex);
			}
		} else {
			Resource resource = resourceLoader.getResource(location);
			loadPropertyRes(resource, encoding);
		}
	}

	/**멀티로 지정된 경우 처리를 위해 LOOP 처리
	 * @param resources 리소스정보
	 * @param encoding 인코딩정보
	 * @throws Exception
	 */
	private void loadPropertyLoop(Resource[] resources, String encoding) throws Exception {
		for (Resource res: notEmpty(resources, "resources"))
			loadPropertyRes(res, encoding);
	}

	/**파일 정보를 읽어서 extendedProperties에 저장
	 * @param resources 리소스정보
	 * @param encoding 인코딩정보
	 * @throws Exception
	 */
	private void loadPropertyRes(Resource resource, String encoding) throws Exception {
		ExtendedProperties tmp = new ExtendedProperties();
		tmp.load(resource.getInputStream(), encoding);
		extendedProperties.combine(tmp);
	}

	/**resource 변경시 refresh
	 */
	public void refreshPropertyFiles() {
		if (isEmpty(extFilenames)) return;
		
		extFilenames.forEach(entry -> {
			String filename = null,
				   encoding = null;
			if (entry instanceof Map) {
				Map<?, ?> map = (Map<?, ?>)entry;
				filename = (String)map.get("filename");
				encoding = (String)map.get("encoding");
			} else {
				filename = (String)entry;
			}
			try {
				loadPropertyResources(filename, encoding);
			} catch (Exception e) {
				throw runtimeException(e);
			}
		});
	}

	@Override
	public void destroy() throws Exception {
		extendedProperties = null;
	}
}