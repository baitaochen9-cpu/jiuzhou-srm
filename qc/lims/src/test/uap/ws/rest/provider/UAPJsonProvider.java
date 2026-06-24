package uap.ws.rest.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Stack;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import nc.vo.jcom.lang.StringUtil;

import org.codehaus.jettison.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.restlet.Request;
import org.restlet.data.CharacterSet;
import org.restlet.engine.io.BioUtils;
import org.restlet.ext.jaxrs.internal.provider.AbstractProvider;
import org.restlet.representation.Representation;

import uap.ws.rest.util.UAPRSConstance;

import com.google.gson.Gson;

/**
 * JsonProvider
 * 
 * @author dingxm
 * 
 */
@Provider

public class UAPJsonProvider extends AbstractProvider<Object> {
	private Gson gson;
	private static ThreadLocal<Stack<String>> paramStack_local = new ThreadLocal<Stack<String>>();

	public UAPJsonProvider() {
		gson = new Gson();
	}

	@Override
	public long getSize(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	public static void clearLocalParam() {
		paramStack_local.remove();
	}

	@Override
	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpResponseHeaders, InputStream entityStream) throws IOException {
		String jsonParamStr = "";
		Stack<String> paramStack = paramStack_local.get();
		if (paramStack == null) {
			// String bodyContent = BioUtils.toString(entityStream,
			// getCurrentRequestEntityCharacterSet())
			String bodyContent = BioUtils.toString(entityStream, getCurrentRequestEntityCharacterSet());
			jsonParamStr = bodyContent;
		} else {
			jsonParamStr = paramStack.pop();
		}
		if (StringUtil.isEmptyWithTrim(jsonParamStr)) {
			clearLocalParam();
		}
		if (org.codehaus.jettison.json.JSONObject.class.isAssignableFrom(type)) {
			try {
				return new org.codehaus.jettison.json.JSONObject(jsonParamStr);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return gson.fromJson(jsonParamStr, genericType);
	}

	private CharacterSet getCurrentRequestEntityCharacterSet() {
		Representation entity = Request.getCurrent().getEntity();

		if (entity == null)
			return null;

		return entity.getCharacterSet();
	}

	@Override
	public void writeTo(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException {
		// entityStream.write(gson.toJson(object, genericType).getBytes());
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		if (JSONObject.class.isAssignableFrom(type)) {
			return false;
		}
		if (JSONArray.class.isAssignableFrom(type)) {
			return false;
		}
		if (JSONString.class.isAssignableFrom(type)) {
			return false;
		}
		if (UAPRSConstance.MEDIATYPE_JSON.equalsIgnoreCase(mediaType.toString())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return false;
		// return isReadable(type, genericType, annotations, mediaType);
	}
}
