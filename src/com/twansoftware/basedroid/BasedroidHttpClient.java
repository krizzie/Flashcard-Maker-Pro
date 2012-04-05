package com.twansoftware.basedroid;

import android.content.SharedPreferences;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import roboguice.util.Ln;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class BasedroidHttpClient {
  private static HttpClient httpClient = HttpConnectionManager.getInstance();

  private SharedPreferences sharedPreferences;

  @Inject
  public BasedroidHttpClient(SharedPreferences sharedPreferences) {
    this.sharedPreferences = sharedPreferences;
    Ln.d("Spinning up BasedroidHttpClient");
    Ln.d(String.valueOf(sharedPreferences.getBoolean("test", false)));
  }

  /**
   * this is an example of an API call that returns a 'long'.  say my activity wants to find this long value.
   * client method returns exactly what we need.
   *
   * @return the userId associated with the search-for username
   */
  public long exampleJsonSearch() {
    final String url = "http://www.speakbin.com/api/json/interaction/searchidbyusername.sb?username=tony";
    Map<String, String> requestParams = new HashMap<String, String>();

    requestParams.put("username", "achuinard");

    try {
      JSONObject jo = fetchJsonObject(url, requestParams, RequestType.GET);
      return jo.getLong("userId");
    } catch (Exception e) {
      Ln.w(e);
    }
    return 0;
  }

  private enum RequestType {
    GET, POST
  }

  private JSONObject fetchJsonObject(String url, Map<String, String> requestParams, RequestType requestType) throws Exception {
    return new JSONObject(fetchStringData(url, requestParams, requestType));
  }

  private JSONArray fetchJsonArray(String url, Map<String, String> requestParams, RequestType requestType) throws IOException, JSONException {
    return new JSONArray(fetchStringData(url, requestParams, requestType));
  }

  private String fetchStringData(String url, Map<String, String> requestParams, RequestType requestType) throws IOException {
    if (requestType == RequestType.GET) {
      StringBuilder realUrl = new StringBuilder();
      realUrl.append(url);
      if (!requestParams.isEmpty()) {
        realUrl.append("?");
        for (String key : requestParams.keySet()) {
          realUrl.append(key);
          realUrl.append("=");
          realUrl.append(URLEncoder.encode(requestParams.get(key), "UTF-8"));
        }
      }

      Ln.d("Making GET request to %s.", realUrl.toString());
      return httpClient.execute(new HttpGet(realUrl.toString()), new BasicResponseHandler());
    } else if (requestType == RequestType.POST) {
      HttpPost httpPost = new HttpPost(url);
      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
      for (String key : requestParams.keySet()) {
        nameValuePairs.add(new BasicNameValuePair(key, requestParams.get(key)));
      }
      httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

      Ln.d("Making POST request to %s.", url);
      return httpClient.execute(httpPost, new BasicResponseHandler());
    }

    // should never come to this
    return "";
  }
}