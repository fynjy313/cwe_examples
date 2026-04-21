//package com.example.cwe.xml_injecions;
//
//import com.saxonica.xqj.SaxonXQDataSource;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.server.ResponseStatusException;
//
//import javax.servlet.http.HttpServletResponse;
//import javax.xml.namespace.QName;
//import javax.xml.xquery.*;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//
///**
// * CWE-652: Improper Neutralization of Data within XQuery Expressions ('XQuery Injection')
// * <p>
// * Дополнительная литература
// * <a href="https://coderlessons.com/tutorials/xml-tekhnologii/uznaite-xquery/xquery-kratkoe-rukovodstvo">...</a>
// * <a href="https://www.progress.com/xquery/resources/tutorials/xqj-tutorial/binding-external-variables">...</a>
// * <a href="http://www.cfoster.net/articles/xqj-tutorial/binding-java-variables.xml">...</a>
// */
//
//
//@RestController
//@RequestMapping("xquery")
//public class XQueryInjectionController {
//    public static final String XML_PATH = "src/main/resources/xml/users.xml";
//    public static final String XQY_PATH = "src/main/resources/xml/abc.xqy";
//
//    @GetMapping("resolve-user-group-unsafe")
//    public void resolveUserGroupUnsafe(@RequestParam String username, @RequestParam String password
//            , HttpServletResponse response) throws IOException, XQException {
//
//        //Инъекции в поле login: ' or '1'='1' or ' | admin' or '
//
//        String query = "for $x in doc(\"" + XML_PATH + "\")/users/user " +
//                "where $x/username='" + username + "' and $x/password='" + DigestUtils.md5Hex(password) +
//                "' return $x/group/text()";
//
//        XQDataSource ds = new SaxonXQDataSource();
//        XQConnection conn = ds.getConnection();
//        XQExpression expression = conn.createExpression();
//        XQResultSequence resultSequence = expression.executeQuery(query);
//
//        StringBuilder groups = new StringBuilder();
//        while (resultSequence.next()) {
//            if (!groups.isEmpty()) groups.append("\n");
//            groups.append(resultSequence.getItemAsString(null));
//        }
//
//        conn.close();
//        expression.close();
//        resultSequence.close();
//
//        if (!groups.isEmpty()) {
//            response.getWriter().write("User group (found using XQuery): " + groups);
//        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown user. Go away!");
//    }
//
//    @GetMapping("resolve-user-group-safe")
//    public void resolveUserGroupSafe(@RequestParam String username, @RequestParam String password
//            , HttpServletResponse response) throws IOException, XQException {
//
//        String query = "declare variable $username external;" +
//                "declare variable $password external;" +
//                "for $x in doc(\"" + XML_PATH + "\")/users/user " +
//                "where $x/username=$username and $x/password=$password " +
//                "return $x/group/text()";
//
//        XQDataSource ds = new SaxonXQDataSource();
//        XQConnection conn = ds.getConnection();
//        XQPreparedExpression expression = conn.prepareExpression(query);
//
//        expression.bindString(new QName("username"), username, null);
//        expression.bindString(new QName("password"), DigestUtils.md5Hex(password), null);
//
//        XQResultSequence resultSequence = expression.executeQuery();
//
//        StringBuilder groups = new StringBuilder();
//        while (resultSequence.next()) {
//            if (!groups.isEmpty()) groups.append("\n");
//            groups.append(resultSequence.getItemAsString(null));
//        }
//
//        conn.close();
//        expression.close();
//        resultSequence.close();
//
//        if (!groups.isEmpty()) {
//            response.getWriter().write("User group (found using XQuery): " + groups);
//        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown user. Go away!");
//    }
//
//    @GetMapping("resolve-user-group-safe2")
//    public void resolveUserGroupSafeXqy(@RequestParam String username, @RequestParam String password
//            , HttpServletResponse response) throws IOException, XQException {
//
//        InputStream query = new FileInputStream(XQY_PATH);
//        XQDataSource ds = new SaxonXQDataSource();
//        XQConnection conn = ds.getConnection();
//        XQPreparedExpression expression = conn.prepareExpression(query);
//
//        expression.bindString(new QName("username"), username, null);
//        expression.bindString(new QName("password"), DigestUtils.md5Hex(password), null);
//
//        XQResultSequence resultSequence = expression.executeQuery();
//
//        StringBuilder groups = new StringBuilder();
//        while (resultSequence.next()) {
//            if (!groups.isEmpty()) groups.append("\n");
//            groups.append(resultSequence.getItemAsString(null));
//        }
//
//        conn.close();
//        expression.close();
//        resultSequence.close();
//
//        if (!groups.isEmpty()) {
//            response.getWriter().write("User group (found using XQuery): " + groups);
//        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown user. Go away!");
//    }
//
//}
