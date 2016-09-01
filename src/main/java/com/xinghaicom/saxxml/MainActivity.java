package com.xinghaicom.saxxml;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.xinghaicom.saxxml.bean.ColorBean;
import com.xinghaicom.saxxml.util.ColorSaxHelper;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        try {
            List<ColorBean> list = getColorList();
            if(list!=null){

                tv.setText(list.toString());
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private List<ColorBean> getColorList() throws ParserConfigurationException, SAXException, IOException {
        //实例化一个SAXParserFactory对象
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser;
        //实例化SAXParser对象，创建XMLReader对象，解析器
        parser = factory.newSAXParser();
        XMLReader xmlReader = parser.getXMLReader();
        //实例化handler，事件处理器
        ColorSaxHelper helperHandler = new ColorSaxHelper();
        //解析器注册事件
        xmlReader.setContentHandler(helperHandler);
        //读取文件流
        InputStream stream = getResources().openRawResource(R.raw.colors);
        InputSource is = new InputSource(stream);
        //解析文件
        xmlReader.parse(is);
        return helperHandler.getColorList();
    }
}
