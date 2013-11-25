// implement here you package
//package your.package;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Used to compress URL using the bit.ly service. 
 * 
 * <IMPLEMENT>
 *
 * 	BitlyAndroid bitly = new BitlyAndroid("bitlyapi", "000000000000");
 * 	String shortUrl = bitly.getShortUrl("www.google.com.br");
 *
 * </IMPLEMENT>
 * 
 * Throws Exception. You can try catch it and check <code>bitly.lastResponse</code> for message from bit.ly.
 * 
 */

public class BitlyAndroid {
	
	/** Change this if you want to use j.mp instead. */
	public static BitlyService service = BitlyService.BITLY;

	private final String bitlyAuth;

	private HttpClient httpclient = null;

	/** Last response kept in case user gets exception and wants to see the response from bit.ly. */
	private BitlyReply lastResponse = null;

	public BitlyAndroid(String login, String apiKey) 
	{
		bitlyAuth = "&format=json&login=" + login + "&apiKey=" + apiKey;
		httpclient = new DefaultHttpClient();
	}

	public String getShortUrl(String urlToShorten) throws Exception 
	{
		BitlyReply reply = null;
		String httpResponse = null;
		httpResponse = getBitlyHttpResponseText(urlToShorten);
		reply = new BitlyReply(urlToShorten, httpResponse);
		lastResponse = reply;
		return reply.getShortUrl();
	}

	BitlyReply getBitlyReply(String urlToShorten) throws JSONException, IOException 
	{
		String httpResponse = getBitlyHttpResponseText(urlToShorten);
		return new BitlyReply(urlToShorten, httpResponse);
	}

	private String getBitlyHttpResponseText(String urlToShorten) throws IOException 
	{
		@SuppressWarnings("deprecation")
		String uri = getBitlyUrl() + URLEncoder.encode(urlToShorten) + bitlyAuth;
		HttpGet httpGet = new HttpGet(uri);
		HttpResponse response = httpclient.execute(httpGet);
		String json = getText(response);
		return json;
	}

	private String getText(HttpResponse response) throws IOException 
	{
		InputStream is = response.getEntity().getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		
		while ((line = reader.readLine()) != null) 
		{
			sb.append(line);
		}
		return sb.toString();
	}

	public BitlyReply getLastResponseFromBitLy() 
	{
		return lastResponse;
	}

	private String getBitlyUrl() 
	{
		return "http://api." + BitlyAndroid.service + "/shorten?version=2.0.1&longUrl=";
	}

	/** Represents a response from bit.ly. Mimics the structure of the JSON data. */
	public class BitlyReply 
	{
		public String longUrl = "";
		public Integer errorCode;
		public String errorMessage;
		public String statusCode;
		public BitlyResult result;

		public BitlyReply(String longUrl, String jsonText) throws JSONException 
		{
			this.longUrl = longUrl;
			JSONObject bitlyMessage = new JSONObject(jsonText);
			this.errorCode = bitlyMessage.getInt("errorCode");
			this.errorMessage = bitlyMessage.getString("errorMessage");
			this.statusCode = bitlyMessage.getString("statusCode");

			JSONObject results = bitlyMessage.getJSONObject("results");
			JSONObject urlResult = results.getJSONObject(longUrl);
		  	result = new BitlyResult(urlResult.getString("hash"), //
				urlResult.getString("shortCNAMEUrl"), //
			  	urlResult.getString("shortKeywordUrl"), //
			  	urlResult.getString("shortUrl"), //
			  	urlResult.getString("userHash"));//
		}

		public String getShortUrl() 
		{
			return result.shortUrl;
		}

	}

	/** Result -object which contains the shortUrl. */
	public class BitlyResult 
	{
		public String hash;
		public String shortCNAMEUrl;
		public String shortKeywordUrl;
		public String shortUrl;
		public String userHash;

		public BitlyResult(String hash, String shortCNAMEUrl, String shortKeywordUrl, String shortUrl, String userHash)
		 {
			super();
			this.hash = hash;
			this.shortCNAMEUrl = shortCNAMEUrl;
			this.shortKeywordUrl = shortKeywordUrl;
			this.shortUrl = shortUrl;
			this.userHash = userHash;
		}
	}

	public enum BitlyService 
	{
		BITLY {
			@Override
			public String toString() {
				return "bit.ly";
			}
		},
		JMP {
			@Override
			public String toString() {
				return "j.mp";
			}
		}
	}
}
