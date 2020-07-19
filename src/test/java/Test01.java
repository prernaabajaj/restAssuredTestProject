import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class Test01 {

	@Test
	void test_01() {
		
		Response response = get("https://apiproxy.paytm.com/v2/movies/upcoming");
		
		System.out.println("Response:\n" + response.asString());
		System.out.println("Body:\n" + response.getBody());
		System.out.println("Status:\n" + response.getStatusCode());
		System.out.println("Content-Type:\n" + response.getHeader("content-type"));

	}
	
	@Test
	void test_02() {
		
		given().get("https://apiproxy.paytm.com/v2/movies/upcoming").then().statusCode(200);
	
	}
	
	@Test
	void test_03() {
		Response response = get("https://apiproxy.paytm.com/v2/movies/upcoming");
		JSONObject jsonObject = new JSONObject(response.getBody().asString());
		JSONArray jsonArray = jsonObject.getJSONArray("upcomingMovieData");
		for (int i=0; i<jsonArray.length(); i++) {
			JSONObject position = jsonArray.getJSONObject(i);
			try {
//				Assert.assertNotEquals(position.get("releaseDate").toString(), "null");
				Assert.assertEquals(position.getString("moviePosterUrl").endsWith("jpg"), true);
				Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse(position.getString("releaseDate")).after(java.util.Calendar.getInstance().getTime()), true);
				for (int j=0; j<i; j++) {
					JSONObject compare = jsonArray.getJSONObject(j);
					Assert.assertNotEquals(position.getString("paytmMovieCode"), compare.getString("paytmMovieCode"));
				}
			} catch (JSONException e) {
//				e.printStackTrace();
				System.out.println("JSON Parsing Exception. At array position: " + i + "\n" + e.getMessage());
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
	}
	
}
