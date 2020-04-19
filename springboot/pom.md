### pom篇
+ 常用依赖
    + springboot启动器
        ```
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
        ```
    + springsecurity启动器
        ```
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-security</artifactId>
            </dependency>
        ```
    + 热部署（idea中ctrl+F9生效）
        ```
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-devtools</artifactId>
            </dependency>
        ```
    + 使用jdbc连接mysql
        ```
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>8.0.18</version>
            </dependency>
        ```
    + 连接池
        ```
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid</artifactId>
                <version>1.0.29</version>
            </dependency>
        ```
    + mybatis
        ```
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>1.3.1</version>
            </dependency>
        ```
    + fastjson
        + 能够支持将java bean序列化成JSON字符串，也能够将JSON字符串反序列化成Java bean
        ```
           <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>fastjson</artifactId>
                <version>1.2.8</version>
            </dependency> 
        ```
    + xml以及properties配置（spring默认使用yml，当需要使用到如上配置就需要引入该依赖）
        ```
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-configuration-processor</artifactId>
            </dependency>
        ```
+ build配置
    + maven-compiler-plugin
        + 作用
            + maven是个项目管理工具，如果我们不告诉它我们的代码要使用什么样的jdk版本编译的话，它就会用maven-compiler-plugin默认的jdk版本来进行处理，这样就容易出现版本不匹配，以至于可能导致编译不通过的问题。
            ```
                <build>
                    <plugins>
                        <plugin>
                            <groupId>org.apache.maven.plugins</groupId>
                            <artifactId>maven-compiler-plugin</artifactId>
                            <version>3.8.1</version>
                            <configuration>
                                <source>1.8</source>
                                <target>1.8</target>
                                <encoding>UTF-8</encoding>
                            </configuration>
                        </plugin>
                    </plugins>
                </build>
            ```
    + resource
        + 作用指定需打包目录（在mybatis中提到了将xml放在非resource目录下的方法就是这样）
            ```
                <resources>
                    <resource>
                        <directory>src/main/java</directory>
                        <includes>
                            <include>**/*.xml</include>
                        </includes>
                    </resource>
                    <resource>
                        <directory>src/main/resources</directory>
                    </resource>
                </resources>
            ```
+ 多模块
    + 添加父模块自声明和pom打包方式
        ```
            <groupId>com.Ivan</groupId>
            <artifactId>Ivan-spring-parent</artifactId>
            <version>1.0-SNAPSHOT</version>
            <packaging>pom</packaging>

            <parent>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-parent</artifactId>
                <version>2.2.4.RELEASE</version>
                <relativePath/>
            </parent>
        ```
    + 子模块声明依赖父模块，以及其他依赖模块
        + 这里base，core，web都是parent的子模块，但是web模块依赖core模块，core依赖base模块
        + base
            ```
                <parent>
                    <artifactId>Ivan-spring-parent</artifactId>
                    <groupId>com.Ivan</groupId>
                    <version>1.0-SNAPSHOT</version>
                    <relativePath>../pom.xml</relativePath>
                </parent>
                <modelVersion>4.0.0</modelVersion>

                <artifactId>Ivan-spring-base</artifactId>
            ```
        + core
            ```
                <parent>
                    <artifactId>Ivan-spring-parent</artifactId>
                    <groupId>com.Ivan</groupId>
                    <version>1.0-SNAPSHOT</version>
                    <relativePath>../pom.xml</relativePath>
                </parent>
                <modelVersion>4.0.0</modelVersion>

                <artifactId>Ivan-spring-core</artifactId>

                <dependencies>
                    <dependency>
                        <groupId>com.Ivan</groupId>
                        <artifactId>Ivan-spring-base</artifactId>
                        <version>1.0-SNAPSHOT</version>
                    </dependency>
                </dependencies>
            ```
        + web
            ```
                // 此处的逻辑是
                <parent>
                    <artifactId>Ivan-spring-parent</artifactId>
                    <groupId>com.Ivan</groupId>
                    <version>1.0-SNAPSHOT</version>
                    <relativePath>../pom.xml</relativePath>
                </parent>
                <modelVersion>4.0.0</modelVersion>

                <artifactId>Ivan-spring-web</artifactId>
                <dependencies>
                    <dependency>
                        <groupId>com.Ivan</groupId>
                        <artifactId>Ivan-spring-core</artifactId>
                        <version>1.0-SNAPSHOT</version>
                    </dependency>
                </dependencies>
            ```
    + 打包为jar参考链接：https://blog.csdn.net/baidu_41885330/article/details/81875395
    + web模块为主要功能模块，添加依赖
        ```
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-maven-plugin</artifactId>
                        <configuration>
                            <!-- 指定该Main Class为全局的唯一入口 -->
                            <mainClass>com.Ivan.WebApplication</mainClass>
                            <layout>ZIP</layout>
                        </configuration>
                        <executions>
                            <execution>
                                <goals>
                                    <goal>repackage</goal><!--可以把依赖的包都打包到生成的Jar包中-->
                                </goals>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        ```
    + 然后在idea中选中parent模块package就可以，第一次打包不需要clean，之后每一次打包都需要clean