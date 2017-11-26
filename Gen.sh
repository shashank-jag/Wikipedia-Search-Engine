rm *.class
javac Shashank.java
echo "Compiled ....................................................."
# last two arguments for first and last file
java -Xmx4g -DtotalEntitySizeLimit=2147480000 -Djdk.xml.totalEntitySizeLimit=2147480000 Shashank 1 379
