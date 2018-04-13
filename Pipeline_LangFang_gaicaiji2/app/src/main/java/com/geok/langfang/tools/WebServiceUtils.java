package com.geok.langfang.tools;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @类描述 webService通用类
 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn Tel: ***********
 * @createDate 2013-6-14 上午09:35:40
 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
 */
public class WebServiceUtils {
	String nameSpace = "";
	String url = "";
	String methodName = "";
	String soapAction = "";
	SoapObject soapObject = null;
	Map<String, Object> simpleParamsMap = new HashMap<String, Object>();
	List<PropertyInfo> complexParamList = new ArrayList<PropertyInfo>();
	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

	/**
	 * 
	 * 构造函数[constructor fun] WebServiceUtils.
	 * 
	 * @param nameSpace
	 *            名称空间
	 * @param url
	 *            webService 的URL
	 * @param methodName
	 *            调用的方法名
	 */
	public WebServiceUtils(String nameSpace, String url, String methodName) {
		this.nameSpace = nameSpace;
		this.url = url;
		this.methodName = methodName;
		this.soapAction = nameSpace + methodName;
		this.soapObject = new SoapObject(nameSpace, methodName);
	}

	/**
	 * 
	 * @功能描述 给webService设置参数
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn Tel: ***********
	 * @param paramName
	 *            参数名称
	 * @param paramValue
	 *            参数值
	 * @createDate 2013-6-14 上午09:36:03
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void addSimpleParam(String paramName, Object paramValue) {
		simpleParamsMap.put(paramName, paramValue);
	}

	/**
	 * 
	 * @功能描述 添加复杂参数
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn Tel:***********
	 * @param paramName
	 * @param paramVaue
	 * @param clazz
	 * @createDate 2013-7-4 上午10:26:54
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void addComplexParam(String paramName, Object paramVaue, Class clazz) {
		PropertyInfo propertyInfo = new PropertyInfo();
		propertyInfo.setName(paramName);
		propertyInfo.setValue(paramVaue);
		propertyInfo.setType(clazz);
		complexParamList.add(propertyInfo);

	}

	/**
	 * 
	 * @功能描述 获取webService返回内容
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn Tel: ***********
	 * @return 返回符合soap1.1标准的soapObject对象
	 * @createDate 2013-6-14 上午09:36:28
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public SoapObject getContent() {
		SoapObject returnSoapObject = null;
		AndroidHttpTransport ht = null;
		try {
			Iterator it = simpleParamsMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Object> entry = (Entry<String, Object>) it.next();
				soapObject.addProperty(entry.getKey(), entry.getValue());
			}
			int size = complexParamList.size();
			for (int i = 0; i < size; i++) {
				soapObject.addProperty(complexParamList.get(i));
			}
			envelope.bodyOut = soapObject;
			envelope.setOutputSoapObject(soapObject);
			ht = new AndroidHttpTransport(url);
			ht.call(soapAction, envelope);
			// ht.debug =true;
			returnSoapObject = (SoapObject) envelope.bodyIn;
			// returnSoapObject = (SoapObject)
			// returnSoapObject.getProperty(methodName
			// + "Result");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return returnSoapObject;
	}
}
