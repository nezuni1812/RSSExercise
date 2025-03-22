package com.example.rss;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DownloadRssFeed extends AsyncTask<String, Void, ArrayList<SingleItem>> {
    // Use supplied URL to download web-feed. This process is inherently
// slow and MUST be performed inside a thread or asynctask (as in here)
    ShowArticle callerContext; //caller class
    String urlAddress, urlCaption;
    ProgressDialog dialog = null;

    public DownloadRssFeed(Context callerContext) {
        this.callerContext = (ShowArticle) callerContext;
        dialog = new ProgressDialog(callerContext);
    }

    protected void onPreExecute() {
        this.dialog.setMessage("Please wait\nReading RSS feed ...");
        this.dialog.setCancelable(false); //outside touching doesn’t dismiss you
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<SingleItem> result) {
        super.onPostExecute(result);
        ArrayList<String> channelListNew = new ArrayList<>();

        for (SingleItem item : result) {
            channelListNew.add(item.getTitle()); // Thêm tiêu đề vào danh sách mới
        }
        callerContext.headlines = channelListNew;
// the ‘result’ list contains headlines for selected news category
// use custom row layout (small font, blue background on state-pressed)
//        int layoutID = R.layout.my_simple_list_item_1;
        ArrayAdapter<SingleItem> adapterNews = new ArrayAdapter<SingleItem>(callerContext, android.R.layout.simple_list_item_1, result);
        callerContext.newsList.setAdapter(adapterNews);
        callerContext.allNews = result;
        dialog.dismiss();
    }

    public SingleItem dissectItemNode(NodeList nodeList, int i) {
// disassemble i-th entry in NodeList collection get the first child of elements: extract fields:
// title, description, pubData, and link. Put those pieces
// together into a POJO ‘SingleItem’ object, and return it
        try {
            Element entry = (Element) nodeList.item(i);

            String titleValue = entry.getElementsByTagName("title").item(0) != null ?
                    entry.getElementsByTagName("title").item(0).getTextContent() : "Không có tiêu đề";
            String descriptionValue = entry.getElementsByTagName("description").item(0) != null ?
                    entry.getElementsByTagName("description").item(0).getTextContent() : "Không có mô tả";
            String dateValue = entry.getElementsByTagName("pubDate").item(0) != null ?
                    entry.getElementsByTagName("pubDate").item(0).getTextContent() : "Không có ngày";
            String linkValue = entry.getElementsByTagName("link").item(0) != null ?
                    entry.getElementsByTagName("link").item(0).getTextContent() : "#";

//            String titleValue = title.getFirstChild().getNodeValue();
//            String descriptionValue = description.getFirstChild().getNodeValue();
//            String dateValue = pubDate.getFirstChild().getNodeValue();
//            String linkValue = link.getFirstChild().getNodeValue();

            titleValue = Html.fromHtml(Html.fromHtml(titleValue, Html.FROM_HTML_MODE_LEGACY).toString(), Html.FROM_HTML_MODE_LEGACY).toString().trim();
            descriptionValue = Html.fromHtml(Html.fromHtml(descriptionValue, Html.FROM_HTML_MODE_LEGACY).toString(), Html.FROM_HTML_MODE_LEGACY).toString().trim();


            SingleItem singleItem = new SingleItem(dateValue, titleValue, descriptionValue, linkValue);
            return singleItem;
        } catch (DOMException e) {
            return new SingleItem("", "Error", e.getMessage(), null);
        }
    }//dissectNode

    @Override
    protected ArrayList<SingleItem> doInBackground(String... params) {
        ArrayList<SingleItem> newsList = new ArrayList<SingleItem>();
        urlAddress = params[0]; // eg. "http://www.npr.org/rss/rss.php?id=1004"
        urlCaption = params[1]; // eg. "World News"
        this.dialog.setMessage("Please wait\nReading RSS feed " + urlCaption + "...");
        try { // try to get connected to RSS source
            URL url = new URL(urlAddress);
            URLConnection connection;
            connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpConnection.getInputStream();
                System.out.println("inputstream");
// define a document builder to work on incoming stream
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
// make DOM-tree for incoming XML stream
                Document dom = db.parse(in);
// make available all access nodes in the parse tree
                Element treeElements = dom.getDocumentElement();
                System.out.println("dom");

                // look for individual ‘stories’ (<items> in this case)
// add each found item to a NodeList collection (newsList)
                newsList.clear();
                NodeList itemNodes = treeElements.getElementsByTagName("item");
                System.out.println("list");

                if ((itemNodes != null) && (itemNodes.getLength() > 0)) {
                    for (int i = 0; i < itemNodes.getLength(); i++) {
                        newsList.add(dissectItemNode(itemNodes, i));
                    }// for
                }// if
            }// if
// time to close. we don't need the connection anymore
            httpConnection.disconnect();
        } catch (Exception e) {
            Log.e("Error >> ", e.getMessage());
        }
        return newsList; //to be consumed by onPostExecute
    }//doInBackground
}//AsyncTask

