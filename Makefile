all: run

clean:
	rm -f out/*.jar

out/FermatTask.jar: out/parcs.jar src/FermatTask.java
	@javac -cp out/parcs.jar --release 11 src/FermatTask.java
	@jar cf out/FermatTask.jar -C src FermatTask.class
	@rm -f src/FermatTask.class

out/FermatHost.jar: out/parcs.jar src/FermatHost.java
	@javac -cp out/parcs.jar --release 11 src/FermatHost.java
	@jar cf out/FermatHost.jar -C src FermatHost.class
	@rm -f src/FermatHost.class

build: out/FermatTask.jar out/FermatHost.jar

run: out/FermatHost.jar out/FermatTask.jar
	@cd out && java -cp "FermatHost.jar:parcs.jar" FermatHost
