/**
 *
 */
package de.meggsimum.w3w;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A Java wrapper for the What3Words Web-API.
 * 
 * @see http://developer.what3words.com/api/
 * 
 * @author Christian Mayer, meggsimum
 */
public class What3Words {

	/**
	 * the w3w API-Key
	 */
	private String apiKey;

	/**
	 * the current language
	 */
	private String language = "en";

	/**
	 * the base URL to the w3w Web-API
	 */
	private String baseUrl = "https://api.what3words.com/";

	/**
	 * Constructor creating a w3w-object bound to the given API-Key.
	 *
	 * @param apiKey
	 */
	public What3Words(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * Constructor creating your w3w-object bound to the given API-Key with a
	 * given language.
	 *
	 * @param apiKey
	 * @param language
	 */
	public What3Words(String apiKey, String language) {
		this.apiKey = apiKey;
		this.language = language;
	}

	/**
	 * Converts 3 words into a position.
	 *
	 * @param words
	 *            the 3 words representing the "w3w-address"
	 * @return array holding the position in the form [lat, lon]
	 * @throws IOException
	 */
	public double[] wordsToPosition(String[] words) throws IOException {

		String url = this.baseUrl + "w3w?key=" + this.apiKey + "&string="
				+ words[0] + "." + words[1] + "." + words[2] + "&lang="
				+ this.language;

		String response = this.doHttpGet(url);

		// parse the coordinates out of the JSON
		JSONObject json = new JSONObject(response);
		JSONArray jsonCoords = (JSONArray) json.get("position");
		double[] coords = new double[2];
		coords[0] = jsonCoords.getDouble(0);
		coords[1] = jsonCoords.getDouble(1);

		return coords;
	}

	/**
	 * Converts a position into a "w3w-address".
	 *
	 * @param coords
	 *            Coordinates to be transformed in the form [lat, lon]
	 * @return Array holding the "w3w-address" in the form [word1, word2, word3]
	 * @throws IOException
	 */
	public String[] positionToWords(Double[] coords) throws IOException {

		String url = this.baseUrl + "position?key=" + this.apiKey
				+ "&position=" + coords[0] + "," + coords[1] + "&lang="
				+ this.language;

		String response = this.doHttpGet(url);

		// parse the words out of the JSON
		JSONObject json = new JSONObject(response);
		JSONArray jsonWords = (JSONArray) json.get("words");
		String[] words = new String[3];
		words[0] = jsonWords.getString(0);
		words[1] = jsonWords.getString(1);
		words[2] = jsonWords.getString(2);

		return words;
	}

	/**
	 * Performs a HTTP GET request with the given URL.
	 *
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private String doHttpGet(String url) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// ensure we use HTTP GET
		con.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

}
