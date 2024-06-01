all: run

clean:
	rm -f out/*.jar

out/FermatTest.jar: out/parcs.jar src/FermatTest.java
	@javac -cp out/parcs.jar --release 11 src/FermatTest.java
	@jar cf out/FermatTest.jar -C src FermatTest.class
	@rm -f src/FermatTest.class

out/FermatTestApplication.jar: out/parcs.jar src/FermatTestApplication.java
	@javac -cp out/parcs.jar --release 11 src/FermatTestApplication.java
	@jar cf out/FermatTestApplication.jar -C src FermatTestApplication.class
	@rm -f src/FermatTestApplication.class

build: out/FermatTest.jar out/FermatTestApplication.jar

run: out/FermatTestApplication.jar out/FermatTest.jar
	@cd out && java -cp "FermatTestApplication.jar:parcs.jar" FermatTestApplication
