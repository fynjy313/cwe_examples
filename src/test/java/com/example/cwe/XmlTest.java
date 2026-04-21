//package com.example.cwe;
//
//import com.saxonica.xqj.SaxonXQDataSource;
//import org.testng.annotations.Test;
//
//import javax.xml.xquery.*;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class XmlTest {
//
//
//    @Test
//    private static void execute(/*User user*/) throws Exception {
//        String userName = "qwe";
//        String password = "asd";
//
//        String query = "for $x in doc(\"src/main/resources/xml/users.xml\")/users/user " +
//                "where $x/username='" + userName +
//                "' and $x/password='" + password +
//                "' return $x/group/text()";
//
//        System.out.println("Your querry: " + query);
////        XQDataSource ds = (XQDataSource) Class.forName("net.sf.saxon.xqj.SaxonXQDataSource").newInstance();
//
//        XQDataSource ds = new SaxonXQDataSource();
//        XQConnection conn = ds.getConnection();
//        XQExpression expression = conn.createExpression();
//        XQResultSequence resultSequence = expression.executeQuery(query);
//
//        System.out.println("Результат выполнения НЕ параметризованного запроса: ");
//        while (resultSequence.next())
//            System.out.println(resultSequence.getItemAsString(null));
//        conn.close();
//        expression.close();
//        resultSequence.close();
//    }
//
//    @Test
//    public void dateTest() {
//        Date date = new Date();
//        String res = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
//        System.out.println(res);
//    }
//
//}
