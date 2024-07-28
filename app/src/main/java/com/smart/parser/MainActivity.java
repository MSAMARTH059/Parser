package com.smart.parser;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> numberlist = new ArrayList<>();
    Button parsexml,parsejson;
    TextView displayResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        displayResult = findViewById(R.id.textView2);
        parsexml = findViewById(R.id.btnxml);
        parsejson = findViewById(R.id.btnjson);

        parsexml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    InputStream is = getAssets().open("example.xml");
                    DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance();
                    DocumentBuilder db =dbf.newDocumentBuilder();
                    Document doc = db.parse(is);

                    Element element = doc.getDocumentElement();
                    element.normalize();

                    NodeList nList =doc.getElementsByTagName("place");

                    displayResult.setText(" ");
                    for (int i=0;i<nList.getLength();i++){
                        Node node =nList.item(i);

                        if (node.getNodeType()==Node.ELEMENT_NODE){
                            Element element1 = (Element) node;

                            String output = "\n Name : " + getValue("name",element1) + "\n"
                                            +"Latitude : "+getValue("lat",element1)+ "\n"
                                            +"Longitude : "+getValue("long",element1)+ "\n"
                                            +"temperature : "+getValue("temperature",element1)+ "\n"
                                            +"humidity : "+getValue("humidity",element1)+ "\n"
                                            +"----------------------------------------------";

                            displayResult.setText(displayResult.getText()+output);

                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        parsejson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json;

                try {
                    InputStream is = getAssets().open("example.json");
                    int size = is.available();
                    byte [] buffer = new byte[size];
                    is.read(buffer);
                    is.close();

                    json = new String(buffer,"UTF-8");
                    JSONArray jsonArray = new JSONArray(json);

                    displayResult.setText(" ");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String output="\n Name : " + obj.getString("name") + "\n"
                                    +"Latitude : "+obj.getString("lat")+ "\n"
                                    +"Longitude : "+obj.getString("long")+ "\n"
                                    +"temperature : "+obj.getString("temperature")+ "\n"
                                    +"humidity : "+obj.getString("humidity")+ "\n"
                                    +"*********************************************";

                        displayResult.setText(displayResult.getText()+output);
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private static String getValue(String tag,Element element){
        return element.getElementsByTagName(tag).item(0).getChildNodes().item(0).getNodeValue();
    }
}