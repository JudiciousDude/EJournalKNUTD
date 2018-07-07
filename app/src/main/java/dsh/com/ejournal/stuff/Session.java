package dsh.com.ejournal.stuff;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Session{
    private Request.Builder requestBuilder = new Request.Builder().url("https://stud.knutd.edu.ua/controller.php");
    private OkHttpClient client = new OkHttpClient();

    private Response doCall(Request request){
        try {
            Response response = client.newCall(request).execute();
            return response;
        }
        catch (IOException e){
            Log.e("IORxception",e.getMessage());
        }
        return null;
    }

    public static boolean checkNetConnection(Context contex){
        ConnectivityManager connectivity = (ConnectivityManager)contex.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity != null){
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if(info != null && info.getState() == NetworkInfo.State.CONNECTED)
                return true;
        }
        return false;
    }

    public boolean login(String login, String password){
        Request request = new Request.Builder()
                .url("https://stud.knutd.edu.ua/index.php")
                .method("POST", RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), "login="+login+"&password="+password+"&key=register"))
                .build();
        Response response = doCall(request);

        if(response != null && response.isSuccessful()){
            try {
                Document result = Jsoup.parse(response.body().string());
                if(result.title().equals("Ошибка авторизации"))
                    return false;

                return true;
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

        return false;
    }
}
