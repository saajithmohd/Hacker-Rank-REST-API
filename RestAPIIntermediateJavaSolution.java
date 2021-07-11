import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.google.gson.Gson;

class Result {

	/*
	 * Complete the 'getTotalGoals' function below.
	 *
	 * The function is expected to return an INTEGER. The function accepts following
	 * parameters: 1. STRING team 2. INTEGER year
	 */

	public static int getTotalGoals(String team, int year) {
		int pages = callApi(team, year, 1, true, true);
		int goals = 0;
		for (int i = 1; i <= pages; i++) {
			goals = goals + callApi(team, year, i, true, false) + callApi(team, year, i, false, false);
		}
		return goals;
	}

	public static int callApi(String team, int year, int page, boolean isTeam1, boolean isPageCount) {
		String op = "";
		Integer goals = 0;
		String urlAddress;
		try {
			if (isTeam1) {
				urlAddress = "https://jsonmock.hackerrank.com/api/football_matches?year=" + year + "&team1=" + team
						+ "&page=" + page;
			} else {
				urlAddress = "https://jsonmock.hackerrank.com/api/football_matches?year=" + year + "&team2=" + team
						+ "&page=" + page;
			}
			URL url = new URL(urlAddress);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			while ((output = br.readLine()) != null) {
				op = output;
			}
			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Gson gson = new Gson();
		Test thing = gson.fromJson(op, Test.class);
		if (isPageCount) {
			return thing.getTotal();
		} else {
			if (thing.getData() != null && !thing.getData().isEmpty()) {
				for (Data a : thing.getData()) {
					if (isTeam1 && team.equals(a.getTeam1())) {
						goals += Integer.parseInt(a.getTeam1goals());
					}
					if (!isTeam1 && team.equals(a.getTeam2())) {
						goals += Integer.parseInt(a.getTeam2goals());
					}
				}
			}
		}
		return goals;
	}
}

class Test {
	private Integer page;
	private Integer per_page;
	private Integer total;
	private Integer total_pages;
	private List<Data> data;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPer_page() {
		return per_page;
	}

	public void setPer_page(Integer per_page) {
		this.per_page = per_page;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getTotal_pages() {
		return total_pages;
	}

	public void setTotal_pages(Integer total_pages) {
		this.total_pages = total_pages;
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}
}

class Data {
	private String team1;
	private String team2;
	private String team1goals;
	private String team2goals;

	public String getTeam1() {
		return team1;
	}

	public void setTeam1(String team1) {
		this.team1 = team1;
	}

	public String getTeam2() {
		return team2;
	}

	public void setTeam2(String team2) {
		this.team2 = team2;
	}

	public String getTeam1goals() {
		return team1goals;
	}

	public void setTeam1goals(String team1goals) {
		this.team1goals = team1goals;
	}

	public String getTeam2goals() {
		return team2goals;
	}

	public void setTeam2goals(String team2goals) {
		this.team2goals = team2goals;
	}

}

public class Solution {
	public static void main(String[] args) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

		String team = bufferedReader.readLine();

		int year = Integer.parseInt(bufferedReader.readLine().trim());

		int result = Result.getTotalGoals(team, year);
		bufferedWriter.write(String.valueOf(result));
		bufferedWriter.newLine();

		bufferedReader.close();
		bufferedWriter.close();
	}
}
