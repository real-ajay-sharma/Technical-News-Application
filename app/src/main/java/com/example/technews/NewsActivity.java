package com.example.technews;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    public static final String REQUEST_URL =
            "https://newsapi.org/v2/top-headlines?sources=techcrunch&apiKey=ff7622efab144867a4d8bec10d9c9298";
    public static final String REQUEST_URL2 ="https://newsapi.org/v2/top-headlines?country=in&apiKey=ff7622efab144867a4d8bec10d9c9298";
    ProgressDialog pd;
    SwipeRefreshLayout swipeRefreshLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        pd = new ProgressDialog(this);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        //swipeRefreshLayout.setColorSchemeColors(R.color.refresh,R.color.refresh1,R.color.refresh2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NewsAsyncTask task = new NewsAsyncTask();
                task.execute();
            }
        });
        pd.setMessage("Loading...");
        pd.show();
        NewsAsyncTask task = new NewsAsyncTask();
        task.execute();
    }

    public void updateUi(final ArrayList<News> news){

        ListView listView = findViewById(R.id.listview);
        final NewsAdapter adapter = new NewsAdapter(this,news);
        listView.setAdapter(adapter);
        pd.dismiss();
        swipeRefreshLayout.setRefreshing(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               News n = news.get(position);
               Uri uri = Uri.parse(n.getUrl());
                startActivity(new Intent(NewsActivity.this,webActivity.class)
                .putExtra("url",n.getUrl()));
            }
        });
    }

    public class NewsAsyncTask extends AsyncTask<URL, Void, ArrayList<News>> {

        @Override
        protected void onPostExecute(ArrayList<News> news) {
           if(news == null) return;;
               updateUi(news);
        }

        @Override
        protected ArrayList<News> doInBackground(URL... urls) {
            URL url = createUrl(REQUEST_URL);
            URL url2 = createUrl(REQUEST_URL2);

            String jsonResponse = "";
            String jsonResponse2 = "";
            jsonResponse = makeHttpRequest(url);
            jsonResponse2 = makeHttpRequest(url2);
            ArrayList<News> news = extractFromJson(jsonResponse,jsonResponse2);


            return news;
        }

        private URL createUrl(String REQUEST_URL) {
            try {
                return new URL(REQUEST_URL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String makeHttpRequest(URL url) {
            HttpURLConnection urlConnection = null;
            String jsonResponse = "";
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
             catch (IOException e) {
                System.err.println("Exception Caught:" + e.getMessage());
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {

            StringBuilder stringBuilder = new StringBuilder();
            if(inputStream!=null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                while (line!=null){
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }
            }
            return stringBuilder.toString();

        }
        private ArrayList<News> extractFromJson(String jsonResponse,String jsonResponse2) {
            ArrayList<News> news = new ArrayList<News>();
            try {

                JSONObject root = new JSONObject(jsonResponse);
                JSONArray jsonArray = root.getJSONArray("articles");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String title = object.getString("title");
                    String description = object.getString("description");
                    String url = object.getString("url");
                    String urlToImage = object.getString("urlToImage");
                    news.add(new News(urlToImage, title, description, url));
                }
                JSONObject root2 = new JSONObject(jsonResponse2);
                JSONArray jsonArray2 = root2.getJSONArray("articles");
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject object = jsonArray2.getJSONObject(i);
                    String title = object.getString("title");
                    String description = object.getString("description");
                    String url = object.getString("url");
                    String urlToImage = object.getString("urlToImage");
                    news.add(new News(urlToImage, title, description, url));
                }
            }
            catch (JSONException e){
                System.err.println("Exception Caught:"+e.getMessage());
            }
                return news;
        }

    }
}

