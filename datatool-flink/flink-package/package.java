<assembly
        xmlns="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.0"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.0 http://maven.apache.org/xsd/assembly-1.1.0.xsd">
    <id></id>
    <formats>
        <format>tar.gz</format>
        <format>dir</format>
    </formats>
    <includeBaseDirectory>false</includeBaseDirectory>
    <fileSets>
        <fileSet>
            <directory>../flink-sql-submit/target/lib/</directory>
            <includes>
                <include>flink-udf-1.0-SNAPSHOT.jar</include>
                <include>flink-connector-*.jar</include>
                <!-- 驱动jar -->
                <include>kafka-clients-*.jar</include>
                <include>postgresql-*.jar</include>
                <include>secComponentApi-*.jar</include>
                <include>fastjson*.jar</include>
                <!-- 覆盖log4j版本 -->
                <include>log4j-*.jar</include>
            </includes>
            <outputDirectory>lib</outputDirectory>
        </fileSet>

        <!-- submit -->
        <fileSet>
            <directory>../flink-sql-submit/target/</directory>
            <includes>
                <include>flink-sql-submit-1.0-SNAPSHOT.jar</include>
            </includes>
            <outputDirectory>submit</outputDirectory>
        </fileSet>
    </fileSets>

</assembly>
