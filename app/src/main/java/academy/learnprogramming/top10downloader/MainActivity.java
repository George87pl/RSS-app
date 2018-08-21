package academy.learnprogramming.top10downloader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ListView listViewXml;
    private String feedUrl = "https://www.wykop.pl/rss/";
    private String feedCachedUrl = "INVALIDATED";
    public static final String STATE_URL= "feedUrl";

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewXml = findViewById(R.id.xmlListView);

        if(savedInstanceState != null){
            feedUrl = savedInstanceState.getString(STATE_URL);
        }

        Log.d(TAG, "onCreate: URI to :" +feedUrl);

        downloadUrl(feedUrl);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_URL, feedUrl);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id) {
            case R.id.mnuWykop:
                feedUrl = "https://www.wykop.pl/rss/";
                break;
            case R.id.mnuGry:
                feedUrl = "https://www.wykop.pl/tag/grybezpradu/rss/";
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        downloadUrl(feedUrl);
        return true;
    }

    private void downloadUrl(String feedUrl){
        if(!feedUrl.equalsIgnoreCase(feedCachedUrl)){
            Log.d(TAG, "downloadUrl: starting Asynctask");
            DownloadData downloadData = new DownloadData();
            downloadData.execute(feedUrl);
            feedCachedUrl = feedUrl;
            Log.d(TAG, "downloadUrl: done");
        }
        Log.d(TAG, "downloadUrl: URL not changed");
    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Log.d(TAG, "onPostExecute: parametr is " +s);

            ParseApplication parseApplication = new ParseApplication();
            parseApplication.parse(s);

//            ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<FeedEntry>(MainActivity.this, R.layout.list_item, parseApplication.getApplications());
//            listViewXml.setAdapter(arrayAdapter);

            FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this, R.layout.list_record, parseApplication.getApplications());
            listViewXml.setAdapter(feedAdapter);
        }
        
        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with " +strings[0]);
            String daneRss = pobierzXML(strings[0]);
            if(daneRss == null){
                Log.e(TAG, "doInBackground: Error downloading");
            }
            return daneRss;
        }

        private String pobierzXML(String urlSciezka){
            StringBuilder xmlRrzultat = new StringBuilder();

            try{
                URL url = new URL(urlSciezka);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int odpowiedz = connection.getResponseCode();

                Log.d(TAG, "pobierzXML: The response code was " +odpowiedz);

//                InputStream inputStream = connection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader reader = new BufferedReader(inputStreamReader);
                  BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsRead;
                char[] inputBuffer = new char[500];
                while (true){
                    charsRead = reader.read(inputBuffer);
                    if(charsRead < 0) {
                        break;
                    }
                    if(charsRead > 0) {
                        xmlRrzultat.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                reader.close();
                return xmlRrzultat.toString();

            } catch(MalformedURLException e) {
                Log.e(TAG, "pobierzXML: Invaild URL " + e.getMessage());
            } catch(IOException e){
                Log.e(TAG, "pobierzXML: IO Exepction reading data: " + e.getMessage());
            } catch(SecurityException e) {
                Log.e(TAG, "pobierzXML: Needs permission?");
//                e.printStackTrace();
            }

            return null;
        }
        
    }
}
