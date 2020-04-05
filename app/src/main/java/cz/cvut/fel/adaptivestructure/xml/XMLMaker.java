package cz.cvut.fel.adaptivestructure.xml;

import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

public class XMLMaker {

    public static String generateXML(String fileName, String context, List<String> buttonNames) throws IOException {
        String xmlToReturn = "";
        Writer writer = null;
        final File configDir = new File(Environment.getExternalStorageDirectory(), "layout");
        configDir.mkdir();
        XmlSerializer serializer = Xml.newSerializer();
        File f = new File(configDir, fileName + ".xml");
        boolean delete = true;
        if (f.exists()) {
            delete = f.delete();
        }
        writer = new OutputStreamWriter(new FileOutputStream(new File(configDir, fileName + ".xml")));
        try {
            serializer.setOutput(writer);
            serializer.startDocument("utf-8", null);
            serializer.startTag("", "FrameLayout");
            serializer.attribute("", "xmlns:android", "http://schemas.android.com/apk/res/android");
            serializer.attribute("", "xmlns:tools", "http://schemas.android.com/tools");
            serializer.attribute("", "android:layout_width", "match_parent");
            serializer.attribute("", "android:layout_height", "match_parent");
            serializer.attribute("", "tools:context", ".fragments." + context);
            serializer.attribute("", "android:id","@+id/" +  fileName);
            serializer.startTag("", "LinearLayout");
            serializer.attribute("", "android:layout_width", "match_parent");
            serializer.attribute("", "android:layout_height", "match_parent");
            serializer.attribute("", "android:orientation", "vertical");
            for (String button : buttonNames){
                serializer.startTag("", "Button");
                serializer.attribute("", "android:layout_width", "match_parent");
                serializer.attribute("", "android:layout_height", "wrap_content");
                serializer.attribute("", "android:text", button);
                serializer.attribute("", "android:tag", "redirect");
                serializer.attribute("", "android:id", "@+id/" + button);
                serializer.endTag("", "Button");
            }
            serializer.startTag("", "SurfaceView");
            serializer.attribute("", "android:layout_width", "1px");
            serializer.attribute("", "android:layout_height", "1px");
            serializer.attribute("", "android:layout_marginTop", "10dp");
            serializer.attribute("", "android:id", "@+id/surfaceview");
            serializer.endTag("", "SurfaceView");
            serializer.endTag("", "LinearLayout");
            serializer.endTag("", "FrameLayout");
            serializer.endDocument();
            String fuck = "sss";
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) writer.close();
        }
        return xmlToReturn;
    }


}
