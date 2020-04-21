// Name: Liliya Kokalova Matric number: S1630528
package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView rawDataDisplay;
    private String result = "";
    private Button startButton;
    private Button currentButton;
    private Button plannedButton;

    Roadworks roadwork = null;

    private String urlSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String urlSource2 = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String urlSource3 = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rawDataDisplay = (TextView) findViewById(R.id.rawDataDisplay);

        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

        currentButton = (Button) findViewById(R.id.currentButton);
        currentButton.setOnClickListener(this);

        plannedButton = (Button) findViewById(R.id.plannedButton);
        plannedButton.setOnClickListener(this);


    }

    public void onClick(View aview) {
        switch (aview.getId()) {

            case R.id.startButton:
                rawDataDisplay.setText("");
                startProgress(urlSource);
                break;

            case R.id.currentButton:
                rawDataDisplay.setText("");
                startProgress(urlSource3);
                break;

            case R.id.plannedButton:
                rawDataDisplay.setText("");
                startProgress(urlSource2);
                break;

            default:
                break;
        }



    }

    public void startProgress(String url) {

        new Thread(new Task(url)).start();
    }

    public class Task implements Runnable {
        private String url;

        public Task(String aurl) {
            url = aurl;
        }

        @Override
        public void run() {


            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";



            try {
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                inputLine = in.readLine();
                inputLine = "";
                int count= 1;
                while ((inputLine = in.readLine()) != null) {
                    count++;
                    if (count < 4 || count > 5 ) {
                        result = result + inputLine;

                    }
                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception");
            }


            Roadworks roadwork = null;

            ArrayList<Roadworks> alist = null;

            try {
                String title = null;
                String descritpion = null;

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(result));

                int eventType = xpp.getEventType();


                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {

                        if (xpp.getName().equalsIgnoreCase("channel")){
                            alist  = new ArrayList<Roadworks>();
                        }
                        else if (xpp.getName().equalsIgnoreCase("item")) {
                            roadwork = new Roadworks();

                        } else
                            if (xpp.getName().equalsIgnoreCase("title")){
                                title = xpp.nextText();
                                roadwork.setTitle(title);

                            }else
                            if (xpp.getName().equalsIgnoreCase("description")){
                                descritpion = xpp.nextText();
                                roadwork.setDescription(descritpion);
                            }

                    } else if (eventType == XmlPullParser.END_TAG) {

                        if (xpp.getName().equalsIgnoreCase("item"))
                        {
                            alist.add(roadwork);
                        }
                        else
                        if (xpp.getName().equalsIgnoreCase("channel"))
                        {
                            int size;
                            size = alist.size();
                        }

                    }

                    eventType = xpp.next();

                }


            } catch (XmlPullParserException ae1) {
                Log.e("MyTag", "Parsing error" + ae1.toString());
            } catch (IOException ae1) {
                Log.e("MyTag", "IO error during parsing");
            }


            final ArrayList<Roadworks> finalAlist = alist;
            MainActivity.this.runOnUiThread(new Runnable()
             {
               public void run() {

                   StringBuilder builder = new StringBuilder();

                   for (Roadworks roadworks : finalAlist){
                       builder.append(roadworks.getTitle()).append("\n").append(roadworks.getDescription()).append("\n\n");

                   }
                   rawDataDisplay.setText(builder.toString());


              }
            });
        }

    }
}
