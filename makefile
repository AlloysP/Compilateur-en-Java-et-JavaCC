all:jagger
jagger:
	java -cp ./javacc.jar javacc jaggerCC.jj
	javac *.java
	java Start

check:
	java -cp ./javacc.jar javacc jaggerCC.jj
	javac *.java
	java Start Programmes/tests/good
	java Start Programmes/tests/wrong
	
MesProgrammes:
	java -cp ./javacc.jar javacc jaggerCC.jj
	javac *.java
	java Start Programmes/MesProgrammes

clear:
	rm *.class
