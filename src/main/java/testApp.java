import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class testApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 사이트 연결
		URL url = new URL("vupp.io/api/test01");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Data-Type", "json");
		// 토큰 추가
		conn.setRequestProperty("Authorization", "Bearer " + token);
		// 요청 내역 보내기
		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();
	}

}
