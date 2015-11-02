package com.smartsense.gifkar.utill;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class DataRequest extends Request<JSONObject> {

	private Listener<JSONObject> listener;
	ErrorListener errorListener;
	private Map<String, String> params;

	public DataRequest(int method, String url, Map<String, String> params, Listener<JSONObject> reponseListener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		this.listener = reponseListener;
		this.params = params;
		this.errorListener = errorListener;
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			Log.i("######################", jsonString);
			return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));

		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		listener.onResponse(response);
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return params;// super.getParams();
	}

	@Override
	public void deliverError(VolleyError error) {
		// super.deliverError(error);
		errorListener.onErrorResponse(error);
	}

	@Override
	protected VolleyError parseNetworkError(VolleyError volleyError) {
		if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
			VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
			volleyError = error;
		}

		return volleyError;
	}

}