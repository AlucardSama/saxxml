## 使用SAX解析简单的XML数据

#### 首先了解一下SAX：

> SAX方式解析是一种基于事件驱动的api，有两个部分，解析器和事件处理器，解析器就是XMLReader接口，负责读取XML文档，和向事件处理器发送事件(也是事件源)，事件处理ContentHandler接口，负责对发送的事件响应和进行XML文档处理。

下面是ContentHandler接口的常用方法
``` java
public abstract void characters (char[] ch, int start, int length)
```
这个方法来接收字符块通知，解析器通过这个方法来报告字符数据块，解析器为了提高解析效率把读到的所有字符串放到一个字符数组（ch）中，作为参数传递给character的方法中，如果想获取本次事件中读取到的字符数据，需要使用start和length属性。
``` java
//接收文档开始的通知
public abstract void startDocument () 
//接收文档结束的通知
public abstract void endDocument () 
//接收文档开始的标签
public abstract void startElement (String uri, String localName, String qName, Attributes atts) 
//接收文档结束的标签
public abstract void endElement (String uri, String localName, String qName) 

```

在一般使用中为了简化开发，在org.xml.sax.helpers提供了一个DefaultHandler类，它实现了ContentHandler的方法，我们只想继承DefaultHandler方法即可。

另外SAX解析器提供了一个工厂类：**SAXParserFactory**，SAX的解析类为**SAXParser**可以调用它的**parser**方法进行解析。

SAX解析的一般流程是：

1. 实例化一个工厂SAXParserFactory
2. 实例化SAXPraser对象，创建XMLReader 解析器
3. 实例化handler，处理器
4. 解析器注册一个事件
5. 读取文件流
6. 解析文件

#### 举个栗子
现在要解析的是colors.xml 文件，文件的内容很简单：

``` xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="colorPrimary">#3F51B5</color>
    <color name="colorPrimaryDark">#303F9F</color>
    <color name="colorAccent">#FF4081</color>
</resources>

```
对于该对象要创建一个实体的类 ColorBean:
```java
/**
 * Created by zheng on 2016/9/1.
 */
public class ColorBean {

    //values/colors.xml
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ColorBean{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
```
有了实体类之后，我们就可以实现自己的XML数据解析类ColorSaxHelper：
``` java
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

```

做好了以上的准备之后，就可以读取XML数据进行解析了；

``` java
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

```
代码只给出了解析简单XML数据的关键性代码，具体代码请戳[我的GitHub](https://github.com/zhengjianan1993/saxxml)








