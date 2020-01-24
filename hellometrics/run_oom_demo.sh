HOME_PATH=.
CLASS_PATH=./target/hellometrics-1.0-SNAPSHOT-jar-with-dependencies.jar
MAIN_CLASS=com.github.walterfan.hellometrics.OomDemo
startTime=$(date +%Y%m%d)

JAVA_OPTS="-server -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$HOME_PATH/heapdump.hprof -Xms128m -Xmx256m -XX:StringTableSize=100003 -Dsun.net.inetaddr.ttl=15 -Djava.security.egd=file:/dev/./urandom -Duser.timezone=GMT -Dcom.datastax.driver.FORCE_NIO=true -Dfile.encoding=UTF8  -cp $CLASS_PATH $MAIN_CLASS"

JMX_OPS="-Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.port=8091 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=true -Dcom.sun.management.jmxremote.password.file=./jmxremote.password -Dcom.sun.management.jmxremote.access.file=./jmxremote.access"


cmd="java $JAVA_OPTS $JMX_OPS >> $HOME_PATH/oom_demo.$startTime.log 2>&1 &"

$cmd