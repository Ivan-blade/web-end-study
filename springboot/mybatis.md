### mybatis
+ springboot2使用mybatis
    + 参考https://www.cnblogs.com/ityouknow/p/6037431.html
    + 引入依赖
        ```
            <dependencies>
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-web</artifactId>
                </dependency>
                <dependency>
                    <groupId>org.mybatis.spring.boot</groupId>
                    <artifactId>mybatis-spring-boot-starter</artifactId>
                    <version>2.0.0</version>
                </dependency>
                <dependency>
                    <groupId>mysql</groupId>
                    <artifactId>mysql-connector-java</artifactId>
                </dependency>
            </dependencies>
        ```
    + 增加resource配置application.properties
        ```
            spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
            spring.datasource.url=jdbc:mysql://localhost:3306/tell_you?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
            spring.datasource.username=root
            spring.datasource.password=L1178594290
        ```
    + 文件配置版（非文件配置版到上一步直接编写mapper就可以了）
        + 在application.properties中添加两行基础配置文件位置和mapper文件位置
            ```
                mybatis.config-location=classpath:/mybatis-config.xml
                mybatis.mapper-locations=classpath:/mapper/*.xml
                // 这样在resource目录下如果存在mapper文件和config文件就会被识别
            ```
        + 如果想要将sql mapper文件与mapper放在一起可以不在application.properties中配置mapper路径而在pom中增加如下配置
            ```
                <build>
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
                </build>
            ```
        + 在rescourse中添加mybatis-config.xml配置文件(详细见官网)
            ```
                <?xml version="1.0" encoding="UTF-8" ?>
                <!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
                <configuration>
                    <settings>
                        <setting name="logImpl" value="STDOUT_LOGGING" />
                    </settings>
                </configuration>
                // 这边配置的作用是打印执行的sql语句，mybatis默认是不会打印sql语句的
            ```
    + 之后就是编写controller接口，service，mapper，xml了，xml了
+ mapper中各种参数及结构
    + 创建自定义的mybatis的Mapper.xml文件参考：https://blog.csdn.net/chiwei4115/article/details/100649047
    + 结构
        ```
            <?xml version="1.0" encoding="UTF-8" ?>
            <!DOCTYPE mapper
                    PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
                    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
            <mapper namespace="com.Ivan.web.mapper.MatchMapper">
                // ...sql语句
            </mapper>
        ```
    + 关于mapper.xml获取参数的方式
        + 传入单个参数或单个对象
            + 指定parameterType就好了
                ```
                    mapper.java
                    int updateArticle(Article article);

                    mapper.xml
                    <update id="updateArticle" parameterType="com.Ivan.web.bean.Article">
                        UPDATE article SET
                        title=#{title},mdContent=#{mdContent},editTime=#{editTime}
                        WHERE id=#{id}
                    </update>
                ```
        + 传入多个参数
            + 使用param注明参数类型
                ```
                    mapper.java
                    List<Article> getArticleByUs(@Param("uid") Long uid, @Param("state") Integer state);

                    mapper.xml
                    <select id="getArticleByUs" resultType="com.Ivan.web.bean.Article">
                        SELECT * FROM article WHERE uid=#{uid} AND state=#{state} ORDER BY publishDate DESC
                    </select>
                ```
    + id
        + 上层service中编写的方法名
    + parameterType
        + 接收参数类型
    + resultType
        + 返回参数类型
            ```
                <select id="selectPerson" parameterType="int" resultType="hashmap">
                    SELECT * FROM PERSON WHERE ID = #{id}
                </select>    
            ```
        + 注意
            + 当返回对象是集合或者是list时，返回类型应该是list承载的类型而不是list
    + resultMap
        + 优势
            + 可以自定义返回数据集内容，如果只是返回对象，比如用户对象，可能将密码也返回，导致不安全隐患，这就可以通过自定义数据集避免这个问题
        + 自定义返回数据集
            ```
                <resultMap id="BaseResultMap" type="com.Ivan.web.bean.Article">
                    <id column="id" property="id"/>
                    <result column="title" property="title"/>
                    <result column="uid" property="uid"/>
                    <result column="publishDate" property="publishDate"/>
                    <result column="editTime" property="editTime"/>
                    <result column="state" property="state"/>
                    <result column="mdContent" property="mdContent"/>
                    <result column="summary" property="summary"/>
                </resultMap>
            ```
        + resultMap 
            + id
                + 数据集被引用时的类型名
            + type
                + 数据集含参所属对象
        + 数据内容属性
            + id
                + 唯一标识名
            + result(其他内容)
                + column
                    + mysql数据表中的变量名
                + property
                    + java对象中的变量名
            ```
                <resultMap id="userResultMap" type="User">
                    <id property="id" column="user_id" />
                    <result property="username" column="username"/>
                    <result property="password" column="password"/>
                </resultMap>    
            ```
    + 自增变量获取
        + useGeneratedKeys="true" keyProperty="id"
        + 当对一个java对象进行插入操作时，如果不做任何处理，完成插入操作，虽然数据表中的主键id会自增但是java对象中的id并不会改变，结果是如果将该对象返回到接口上id值将不会与数据库中的id对应，如果想要实现这种对应，加入useGeneratedKeys="true" keyProperty="id"即可
            ```
                <insert id="insert" parameterType="Spares" 
                    useGeneratedKeys="true" keyProperty="id">
                    insert into spares(spares_id,spares_name,
                        spares_type_id,spares_spec)
                    values(#{id},#{name},#{typeId},#{spec})
                </insert>
            ```
    + 
+ mybatis动态sql
    + if
        + 当符合某个条件时，才会引入某些参数
        + 语法
            ```
                <if test = "decision"></if>
            ```
            ```
                <select id="getArticleByUsOther" resultType="com.Ivan.web.bean.Article">
                    SELECT * FROM article
                    WHERE uid=#{uid}
                    AND state=#{state}
                    <if test="state == 2">
                        AND current_timestamp > showTime
                        AND otherId = #{tempId}
                    </if>
                    ORDER BY publishDate DESC
                </select>
            ```
    + choose(when,otherwise)
        + 类似于switch语句
        + 语法
            ```
                <choose>
                    <when test="条件判断语句1">sql语句</when>
                    <when test="条件判断语句2">sql语句</when>
                    <otherwise>sql语句</otherwise>
                </choose>
            ```
            ```
                <select id="findActiveBlogLike"
                    resultType="Blog">
                    SELECT * FROM BLOG WHERE state = ‘ACTIVE’
                    <choose>
                        <when test="title != null">
                        AND title like #{title}
                        </when>
                        <when test="author != null and author.name != null">
                        AND author_name like #{author.name}
                        </when>
                        <otherwise>
                        AND featured = 1
                        </otherwise>
                    </choose>
                </select>
            ```
    + 特殊标签
        + where
            + where 元素知道只有在一个以上的if条件有值的情况下才去插入"WHERE"子句。而且，若最后的内容是"AND"或"OR"开头的，where 元素也知道如何将他们去除。
            ```
                <select id="findActiveBlogLike" resultType="Blog">
                    SELECT * FROM BLOG 
                    <where> 
                        <if test="state != null">
                            state = #{state}
                        </if> 
                        <if test="title != null">
                            AND title like #{title}
                        </if>
                        <if test="author != null and author.name != null">
                            AND author_name like #{author.name}
                        </if>
                    </where>
                </select>
            ```
        + set
            + set 元素会动态前置 SET 关键字，同时也会消除无关的逗号，因为用了条件语句之后很可能就会在生成的赋值语句的后面留下这些逗号
            ```
                <update id="updateAuthorIfNecessary">
                    update Author
                        <set>
                        <if test="username != null">username=#{username},</if>
                        <if test="password != null">password=#{password},</if>
                        <if test="email != null">email=#{email},</if>
                        <if test="bio != null">bio=#{bio}</if>
                        </set>
                    where id=#{id}
                </update>
            ```
    + trim(where,set)
        + trim标签主要有四个属性
            + prefix
                + 起始参数
            + suffix
                + 结束参数
            + prefixOverrides
                + 忽略多余的前缀比如"AND |OR "
            + suffixOverrides
                + 忽略多余的后缀比如","
        + 上面特殊标签where和set等价的trim表示
            ```
                <select id="findActiveBlogLike" resultType="Blog">
                    SELECT * FROM BLOG 
                    <trim prefix="WHERE" prefixOverrides="AND |OR ">
                        <if test="state != null">
                            state = #{state}
                        </if> 
                        <if test="title != null">
                            AND title like #{title}
                        </if>
                        <if test="author != null and author.name != null">
                            AND author_name like #{author.name}
                        </if>
                    </trim>
                </select>

                <update id="updateAuthorIfNecessary">
                    update Author
                        <trim prefix="SET" suffixOverrides=",">
                            <if test="username != null">username=#{username},</if>
                            <if test="password != null">password=#{password},</if>
                            <if test="email != null">email=#{email},</if>
                            <if test="bio != null">bio=#{bio}</if>
                        </trim>
                    where id=#{id}
                </update>
            ```
    + foreach
        + foreach 元素的功能是非常强大的，它允许你指定一个集合，声明可以用在元素体内的集合项和索引变量。它也允许你指定开闭匹配的字符串以及在迭代中间放置分隔符。这个元素是很智能的，因此它不会偶然地附加多余的分隔符。
        + 参数
            + item：集合中元素迭代时的别名，该参数为必选。
            + index：在list和数组中,index是元素的序号，在map中，index是元素的key，该参数可选
            + open：foreach代码的开始符号，一般是(和close=")"合用。常用在in(),values()时。该参数可选
            + separator：元素之间的分隔符，例如在in()的时候，separator=","会自动在元素中间用“,“隔开，避免手动输入逗号导致sql错误，如in(1,2,)这样。该参数可选。
            + close: foreach代码的关闭符号，一般是)和open="("合用。常用在in(),values()时。该参数可选。
            + collection: 要做foreach的对象，作为入参时，List对象默认用"list"代替作为键，数组对象有"array"代替作为键，Map对象没有默认的键。当然在作为入参时可以使用@Param("keyName")来设置键，设置keyName后，list,array将会失效。
        ```
            <select id="selectPostIn" resultType="domain.blog.Post">
                SELECT *
                FROM POST P
                WHERE ID in
                <foreach item="item" index="index" collection="list"
                    open="(" separator="," close=")">
                        #{item}
                </foreach>
            </select>
        ```