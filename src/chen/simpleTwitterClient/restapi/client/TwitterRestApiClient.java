package chen.simpleTwitterClient.restapi.client;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterRestApiClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "zNyMmmxwrYkqfD1nMISElPsgw";       // Change this
    public static final String REST_CONSUMER_SECRET = "4c1t1Ht4MQRfw3VF5iyL3RBYoH2MOSTxaeb1W5OkwXpfksxxLM"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://cpTwitterCallBack"; // Change this (here and in manifest)
    
    public TwitterRestApiClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    // CHANGE THIS
    // DEFINE METHODS for different API endpoints here
    /*
    public void getInterestingnessList(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        client.get(apiUrl, params, handler);
    }
    */
    
    
    public void getHomeTimeLine(long since, long max, long count, JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        Log.d("DEBUG", "Begint o load in rest api client");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        if(since > 0){
        	params.put("since_id", String.valueOf(since));
        }
        if(max > 0){
        	params.put("max_id", String.valueOf(max));
        }
        if(count > 0){
        	params.put("count", String.valueOf(count));
        }
        Log.d("DEBUG", "Params is " + params.toString());
        client.get(apiUrl, params, handler);
    }
    
    public void getCurrentUserInfo(JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        // Can specify query string params directly or through RequestParams.
        client.get(apiUrl, null, handler);
    }
    
    public void getMentions(long since, long max, long count, JsonHttpResponseHandler handler){
    	 String apiUrl = getApiUrl("statuses/mentions_timeline.json");
         Log.d("DEBUG", "Begint o load in rest api client");
         // Can specify query string params directly or through RequestParams.
         RequestParams params = new RequestParams();
         if(since > 0){
         	params.put("since_id", String.valueOf(since));
         }
         if(max > 0){
         	params.put("max_id", String.valueOf(max));
         }
         if(count > 0){
         	params.put("count", String.valueOf(count));
         }
         Log.d("DEBUG", "Params is " + params.toString());
         client.get(apiUrl, params, handler);
    }
    
    public void getUserTweets(String screen_name, long since, long max, long count, JsonHttpResponseHandler handler){
   	 String apiUrl = getApiUrl("statuses/user_timeline.json");
        Log.d("DEBUG", "Begint o load in rest api client");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        if(since > 0){
        	params.put("since_id", String.valueOf(since));
        }
        if(max > 0){
        	params.put("max_id", String.valueOf(max));
        }
        if(count > 0){
        	params.put("count", String.valueOf(count));
        }
        if(screen_name != null){
        	params.put("screen_name", screen_name.trim());
        }
        Log.d("DEBUG", "Params is " + params.toString());
        client.get(apiUrl, params, handler);
   }
    
    
    public void tweet(String body, JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", body);
        // Can specify query string params directly or through RequestParams.
        client.post(apiUrl, params, handler);
    }
    
    
    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */
}