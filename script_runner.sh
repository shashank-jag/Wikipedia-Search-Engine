rm *.class
javac  index_creater.java 
echo "Compiled ....................................................."
java -Xmx2048m -DtotalEntitySizeLimit=2147480000 -Djdk.xml.totalEntitySizeLimit=2147480000 index_creater /Users/rooh/Documents/workspace/Assignment-3---Index-Creation-Final/enwiki-latest-pages-articles-multistream.xml
#$1

#-DtotalEntitySizeLimit=2147480000 -Djdk.xml.totalEntitySizeLimit=2147480000

#javac ScoreGenerator.java
#echo "Compiled ....................................................."
#java -DtotalEntitySizeLimit=2147480000 -Djdk.xml.totalEntitySizeLimit=2147480000 ScoreGenerator 


