<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.huawei.smartcampus</groupId>
        <artifactId>datatool-flink</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <groupId>com.huawei.smartcampus</groupId>
    <artifactId>flink-package</artifactId>
    <packaging>pom</packaging>
    <version>1.0-SNAPSHOT</version>

    <!-- 加上flink-sql-submit依赖，避免并行构建时报错 -->
    <dependencies>
        <dependency>
            <groupId>com.huawei.datatool</groupId>
            <artifactId>flink-sql-submit</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <artifactId>maven-assembly-plugin</artifactId>
                <configuration>
                    <finalName>flink</finalName>
                    <descriptors>
                        <descriptor>package.xml</descriptor>
                    </descriptors>
                </configuration>
                <executions>
                    <execution>
                        <id>make-assembly</id>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>