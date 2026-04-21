FROM bellsoft/liberica-openjdk-rocky:17

ENV TZ Europe/Moscow

COPY ./target/cwe_examples-0.0.2-SNAPSHOT.jar /app/

WORKDIR /app
EXPOSE 7171

CMD [	"java", \
		"-jar", \
		"-Dfile.encoding=UTF-8", \
		"cwe_examples-0.0.2-SNAPSHOT.jar"]
