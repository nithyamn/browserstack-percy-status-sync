import okhttp3.*;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

public class MarkTestStatus {
    @Test
    public void test() throws Exception {
        String status = getPercyBuildStatus();
        if(status.equals("finished")){
            setSessionStatus("passed","Percy Build has been successfully completed!");
        }else{
            setSessionStatus("failed","Percy Build has failed!");
        }
    }
    public String getPercyBuildStatus() throws IOException {
        String buildID = System.getProperty("buildid");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://percy.io/api/v1/builds/"+buildID)
                .get()
                .addHeader("Authorization", "Token "+System.getenv("PERCY_TOKEN"))
                .build();

        Response response = client.newCall(request).execute();

        JSONObject jsonObject = new JSONObject(response.body().string());
        JSONObject jsonpObject2 = new JSONObject(jsonObject.get("data").toString());
        JSONObject jsonObject3 = new JSONObject(jsonpObject2.get("attributes").toString());
        System.out.println("State:"+jsonObject3.get("state").toString());
        return jsonObject3.get("state").toString();
    }
    public void setSessionStatus(String status, String reason) throws Exception{
        String sessionID = "";
        try (InputStream input = new FileInputStream("src/test/resources/session-data/currentSessionID.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            sessionID = prop.getProperty("sessionId");
            System.out.println(sessionID);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("status",status)
                .addFormDataPart("reason",reason)
                .build();
        Request request = new Request.Builder()
                .url("https://api.browserstack.com/automate/sessions/"+sessionID+".json")
                .method("PUT", body)
                .addHeader("Authorization", basicAuthHeaderGeneration())
                .build();
        Response response = client.newCall(request).execute();
    }
    public String basicAuthHeaderGeneration(){
        String username = System.getenv("BROWSERSTACK_USERNAME");
        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
        String authCreds = username+":"+accessKey;
        return "Basic " + Base64.getEncoder().encodeToString(authCreds.getBytes());
    }
}
