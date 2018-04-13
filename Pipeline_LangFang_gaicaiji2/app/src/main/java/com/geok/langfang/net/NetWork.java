package com.geok.langfang.net;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.geok.langfang.Bean.CurrentProjectionBean;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class NetWork {

	/**
	 * 
	 * @功能描述 获取当前屏幕下的GeoPoint
	 * @author 孙士海[sunshihai] Email:shihais@geo-k.cn Tel:
	 * @param Name_Space
	 * @param Method_Name
	 * @param Parame
	 * @return
	 * @createDate 2013-7-3 下午03:30:20
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static String getMessage(String Name_Space, String Method_Name,
			CurrentProjectionBean Parame) {
		try {
			/**
			 * 实例化SoapObject 对象，指定webService的命名空间（从相关WSDL文档中可以查看命名空间），以及调用方法名称
			 */
			SoapObject soapObject = new SoapObject(Name_Space, Method_Name);
			soapObject.addProperty("getGeoPointByDrag", Parame);
			/**
			 * 设置SOAP请求信息(参数部分为SOAP协议版本号，与你要调用的webService中版本号一致)
			 */
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;

			envelope.setOutputSoapObject(soapObject);
			// 注册envelope
			(new MarshalBase64()).register(envelope);
			// 构建传输对象，并指明WSDL文档URL
			AndroidHttpTransport httpsTransportSE = new AndroidHttpTransport(Constant.URL);
			httpsTransportSE.debug = true;
			// 调用WebService(其中参数为1：命名空间+方法名称，2：Envelope对象)

			httpsTransportSE.call(Name_Space + Method_Name, envelope);
			if (envelope.getResponse() != null) {
				return envelope.bodyIn.toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1 + "";
	}

	/**
	 * 
	 * @功能描述 获取当前比例尺下面的GeoPoint
	 * @author 孙士海[sunshihai] Email:shihais@geo-k.cn Tel:
	 * @param Name_Space
	 * @param Method_Name
	 * @param level
	 * @return
	 * @createDate 2013-7-3 下午03:32:34
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static String getMessage(String Name_Space, String Method_Name, int level) {
		try {
			/**
			 * 实例化SoapObject 对象，指定webService的命名空间（从相关WSDL文档中可以查看命名空间），以及调用方法名称
			 */
			SoapObject soapObject = new SoapObject(Name_Space, Method_Name);
			soapObject.addProperty("getGeoPointByLevel", level);
			/**
			 * 设置SOAP请求信息(参数部分为SOAP协议版本号，与你要调用的webService中版本号一致)
			 */
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;

			envelope.setOutputSoapObject(soapObject);
			// 注册envelope
			(new MarshalBase64()).register(envelope);
			// 构建传输对象，并指明WSDL文档URL
			AndroidHttpTransport httpsTransportSE = new AndroidHttpTransport(Constant.URL);
			httpsTransportSE.debug = true;
			// 调用WebService(其中参数为1：命名空间+方法名称，2：Envelope对象)

			httpsTransportSE.call(Name_Space + Method_Name, envelope);
			if (envelope.getResponse() != null) {
				return envelope.bodyIn.toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1 + "";
	}

	/**
	 * 
	 * @功能描述 获取所有的Geopoint
	 * @author 孙士海[sunshihai] Email:shihais@geo-k.cn Tel:
	 * @param Name_Space
	 * @param Method_Name
	 * @return
	 * @createDate 2013-7-3 下午03:34:00
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static String getMessage(String Name_Space, String Method_Name) {
		try {
			/**
			 * 实例化SoapObject 对象，指定webService的命名空间（从相关WSDL文档中可以查看命名空间），以及调用方法名称
			 */
			SoapObject soapObject = new SoapObject(Name_Space, Method_Name);
			/**
			 * 设置SOAP请求信息(参数部分为SOAP协议版本号，与你要调用的webService中版本号一致)
			 */
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;

			envelope.setOutputSoapObject(soapObject);
			// 注册envelope
			(new MarshalBase64()).register(envelope);
			// 构建传输对象，并指明WSDL文档URL
			AndroidHttpTransport httpsTransportSE = new AndroidHttpTransport(Constant.URL);
			httpsTransportSE.debug = true;
			// 调用WebService(其中参数为1：命名空间+方法名称，2：Envelope对象)

			httpsTransportSE.call(Name_Space + Method_Name, envelope);
			if (envelope.getResponse() != null) {
				return envelope.bodyIn.toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1 + "";
	}

	/**
	 * 
	 * @功能描述 根据经纬度获得当前城市的名称
	 * @author 孙士海[sunshihai] Email:shihais@geo-k.cn Tel:
	 * @param Name_Space
	 * @param Method_Name
	 * @param point
	 * @return
	 * @createDate 2013-7-4 下午01:10:28
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */

	public static String getMessage(String Name_Space, String Method_Name, GeoPoint point) {
		try {
			/**
			 * 实例化SoapObject 对象，指定webService的命名空间（从相关WSDL文档中可以查看命名空间），以及调用方法名称
			 */
			SoapObject soapObject = new SoapObject(Name_Space, Method_Name);
			/**
			 * 设置SOAP请求信息(参数部分为SOAP协议版本号，与你要调用的webService中版本号一致)
			 */
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			soapObject.addProperty("getCityByGeoPoint", point);
			envelope.setOutputSoapObject(soapObject);
			// 注册envelope
			(new MarshalBase64()).register(envelope);
			// 构建传输对象，并指明WSDL文档URL
			AndroidHttpTransport httpsTransportSE = new AndroidHttpTransport(Constant.URL);
			httpsTransportSE.debug = true;
			// 调用WebService(其中参数为1：命名空间+方法名称，2：Envelope对象)

			httpsTransportSE.call(Name_Space + Method_Name, envelope);
			if (envelope.getResponse() != null) {
				return envelope.bodyIn.toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1 + "";
	}

	/**
	 * 
	 * @功能描述 在搜索里面根据名称获得当前的点的集合 这个解析出来应该是GeoPoint数组
	 * @author 孙士海[sunshihai] Email:shihais@geo-k.cn Tel:
	 * @param Name_Space
	 * @param Method_Name
	 * @param name
	 * @return
	 * @createDate 2013-7-4 下午01:14:03
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static String getMessage(String Name_Space, String Method_Name, String name) {
		try {
			/**
			 * 实例化SoapObject 对象，指定webService的命名空间（从相关WSDL文档中可以查看命名空间），以及调用方法名称
			 */
			SoapObject soapObject = new SoapObject(Name_Space, Method_Name);
			/**
			 * 设置SOAP请求信息(参数部分为SOAP协议版本号，与你要调用的webService中版本号一致)
			 */
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			soapObject.addProperty("getGeoPointByName", name);
			envelope.setOutputSoapObject(soapObject);
			// 注册envelope
			(new MarshalBase64()).register(envelope);
			// 构建传输对象，并指明WSDL文档URL
			AndroidHttpTransport httpsTransportSE = new AndroidHttpTransport(Constant.URL);
			httpsTransportSE.debug = true;
			// 调用WebService(其中参数为1：命名空间+方法名称，2：Envelope对象)

			httpsTransportSE.call(Name_Space + Method_Name, envelope);
			if (envelope.getResponse() != null) {
				return envelope.bodyIn.toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1 + "";
	}

}
