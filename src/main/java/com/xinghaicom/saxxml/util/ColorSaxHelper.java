package com.xinghaicom.saxxml.util;

import com.xinghaicom.saxxml.bean.ColorBean;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zheng on 2016/9/1.
 */
public class ColorSaxHelper extends DefaultHandler {

    private int currentState = 0;
    private final static int ITEM = 0x0005;

    private ColorBean color;
    private List<ColorBean> list;
    public List<ColorBean> getColorList() {
        return this.list;
    }
    //开始解析文档
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        list = new ArrayList<>();
    }

    //结束解析文档
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    //开始解析标签
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //super.startElement(uri, localName, qName, attributes);
        color = new ColorBean();
        if (localName.equals("color")) {
            for (int i = 0; i < attributes.getLength(); i++) {
                //属性值
                if (attributes.getLocalName(i).equals("name")) {
                    color.setName(attributes.getValue(i));
                }
            }
            currentState = ITEM;
            return;
        }
        currentState = 0;
        return;

    }

    //标签解析结束
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //super.endElement(uri, localName, qName);
        if (localName.equals("color"))
            list.add(color);
    }

    //接收字符块通知
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        //super.characters(ch, start, length);
        String theString = String.valueOf(ch, start, length);
        if (currentState != 0) {
            color.setValue(theString);
            currentState = 0;
        }
        return;
    }
}
