package academy.learnprogramming.top10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ParseApplication {
    private static final String TAG = "ParseApplication";
    private ArrayList<FeedEntry> applications;

    public ParseApplication() {
        this.applications = new ArrayList<>();
    }

    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }

    public boolean parse(String xmlData) {
        boolean status = true;
        FeedEntry currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
//                        Log.d(TAG, "parse: Starting tag for " + tagName);
                        if ("item".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new FeedEntry();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
//                        Log.d(TAG, "parse: Rnding tag for " + tagName);
                        if (inEntry) {
                            if ("item".equalsIgnoreCase(tagName)) {
                                applications.add(currentRecord);
                                inEntry = false;
                            } else if("title".equalsIgnoreCase(tagName)){
                                currentRecord.setTitle(textValue);
                            } else if("link".equalsIgnoreCase(tagName)){
                                currentRecord.setLink(textValue);
                            } else if("pubdate".equalsIgnoreCase(tagName)){
                                currentRecord.setPubDate(textValue);
                            } else if("description".equalsIgnoreCase(tagName)){
                                currentRecord.setDescription(textValue);
                            } else if("image".equalsIgnoreCase(tagName)){
                                currentRecord.setImageURL(textValue);
                            }
                        }
                        break;

                    default:
                        //Nothing else to do
                }
                eventType = xpp.next();
            }
//            for (FeedEntry app: applications) {
//                Log.d(TAG, "*****************");
//                Log.d(TAG, app.toString());
//            }

        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }
}
