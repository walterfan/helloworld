<!-- TOC -->
  * [Quick Start](#quick-start)
    * [Step 1: Generate the Base Spring Boot Project](#step-1-generate-the-base-spring-boot-project)
    * [Step 2: Add Database Configuration](#step-2-add-database-configuration)
    * [Step 3: Create a MyBatis Mapper](#step-3-create-a-mybatis-mapper)
    * [Step 4: Create a Mapper XML (Optional)](#step-4-create-a-mapper-xml-optional)
    * [Step 5: Add a Service Layer (Optional)](#step-5-add-a-service-layer-optional)
    * [Step 6: Add a Controller](#step-6-add-a-controller)
    * [Step 7: Run the Application](#step-7-run-the-application)
    * [Step 8: Test the Application](#step-8-test-the-application)
  * [create database tables automatically](#create-database-tables-automatically)
    * [Step 1: Add JPA Dependencies (for Schema Generation Only)](#step-1-add-jpa-dependencies-for-schema-generation-only)
    * [Step 2: Annotate Model Classes](#step-2-annotate-model-classes)
    * [Step 3: Configure application.properties or application.yml](#step-3-configure-applicationproperties-or-applicationyml)
    * [Step 4: Use MyBatis for Data Access](#step-4-use-mybatis-for-data-access)
    * [Step 5: Run the Application](#step-5-run-the-application)
    * [Alternative: Use MyBatis with Flyway/Liquibase for Schema Management](#alternative-use-mybatis-with-flywayliquibase-for-schema-management)
      * [Using Flyway](#using-flyway)
      * [Using Liquibase](#using-liquibase)
<!-- TOC -->

## Quick Start

### Step 1: Generate the Base Spring Boot Project

Use the Spring Initializr CLI to generate a Spring Boot project with MyBatis dependencies.

Command:

curl https://start.spring.io/starter.zip \
-d dependencies=mybatis,spring-data-jpa,mysql \
-d name=mybatis-demo \
-d packageName=com.github.walterfan.hellomybatis \
-o mybatis-demo.zip

Explanation:

	* dependencies=mybatis,spring-data-jpa,mysql: Adds MyBatis, JPA, and MySQL support.
	* name=mybatis-demo: Sets the project name.
	* packageName=com.github.walterfan.hellomybatis: Sets the base package for your Java code.
	* -o mybatis-demo.zip: Outputs the project as a ZIP file.

Unzip the project:
```
unzip mybatis-demo.zip
cd mybatis-demo
```

### Step 2: Add Database Configuration

Modify the application.properties or application.yml file to include your database configuration.

Example: src/main/resources/application.properties

```
spring.datasource.url=jdbc:mysql://localhost:3306/my_database
spring.datasource.username=root
spring.datasource.password=yourpassword

mybatis.mapper-locations=classpath:mappers/*.xml
mybatis.configuration.map-underscore-to-camel-case=true
```

### Step 3: Create a MyBatis Mapper

1.	Create a Mapper Interface:
* Example: src/main/java/com/example/mybatisdemo/mapper/UserMapper.java

```java
package com.github.walterfan.hellomybatis.mapper;

import com.github.walterfan.hellomybatis.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(int id);

    @Insert("INSERT INTO users(name, email) VALUES(#{name}, #{email})")
    void insert(User user);

    @Update("UPDATE users SET name = #{name}, email = #{email} WHERE id = #{id}")
    void update(User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    void delete(int id);

    @Select("SELECT * FROM users")
    List<User> findAll();
}

```

2.	Create a Model Class:
* Example: src/main/java/com/example/mybatisdemo/model/User.java

```java
package com.github.walterfan.hellomybatis.model;

public class User {
private int id;
private String name;
private String email;

    // Getters and setters
}
```


### Step 4: Create a Mapper XML (Optional)

If you prefer XML-based mappings, add an XML file under src/main/resources/mappers.

	* Example: src/main/resources/mappers/UserMapper.xml
```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.github.walterfan.hellomybatis.mapper.UserMapper">
    <select id="findById" resultType="com.github.walterfan.hellomybatis.model.User">
        SELECT * FROM users WHERE id = #{id}
    </select>
</mapper>
```



### Step 5: Add a Service Layer (Optional)

For cleaner architecture, add a service to use the mapper.

	* Example: src/main/java/com/example/mybatisdemo/service/UserService.java

```java
package com.github.walterfan.hellomybatis.service;

import com.github.walterfan.hellomybatis.mapper.UserMapper;
import com.github.walterfan.hellomybatis.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<User> getAllUsers() {
        return userMapper.findAll();
    }
}
```


### Step 6: Add a Controller

Create a REST controller to expose endpoints.

	* Example: src/main/java/com/example/mybatisdemo/controller/UserController.java

```java
package com.github.walterfan.hellomybatis.controller;

import com.github.walterfan.hellomybatis.model.User;
import com.github.walterfan.hellomybatis.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
```


### Step 7: Run the Application

Use the Maven wrapper to run your Spring Boot application:
```
./mvnw spring-boot:run
```

### Step 8: Test the Application

	* Start your MySQL database.
	* Test the REST API endpoints using tools like Postman or curl. For example:
```
curl http://localhost:8080/users

```

This setup gives you a functional Spring Boot project with MyBatis for database access.


## create database tables automatically


We can use MyBatis for ORM and let Hibernate or Spring handle table creation in the background. 

MyBatis will manage your queries, while Hibernate will only handle schema generation at application startup. Here’s how you can achieve this:

### Step 1: Add JPA Dependencies (for Schema Generation Only)

Even though you won’t use Hibernate as the ORM, you’ll need JPA/Hibernate dependencies for table creation.

Maven:
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
    <scope>compile</scope>
</dependency>
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.3.1</version>
</dependency>
```
Gradle:
```
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:2.3.1'
```
This setup allows you to use MyBatis as the main ORM and JPA/Hibernate only for table creation.

### Step 2: Annotate Model Classes

Use JPA annotations like @Entity, @Table, @Column, and @Id for table definitions, even if you’re not using Hibernate for ORM.

Example:
```java
package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    // Getters and setters
}
```

### Step 3: Configure application.properties or application.yml

Tell Spring Boot to use Hibernate for table creation, while MyBatis handles queries.

For application.properties:
```
# Enable schema generation
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/your_database?createDatabaseIfNotExist=true
spring.datasource.username=your_username
spring.datasource.password=your_password

# MyBatis configuration
mybatis.type-aliases-package=com.example.demo.model
mybatis.mapper-locations=classpath:mappers/*.xml
```

For application.yml:
```
spring:
    jpa:
        hibernate:
            ddl-auto: update
    datasource:
        url: jdbc:mysql://localhost:3306/your_database?createDatabaseIfNotExist=true
        username: your_username
        password: your_password
mybatis:
    type-aliases-package: com.example.demo.model
    mapper-locations: classpath:mappers/*.xml
```
  * ddl-auto=update: Automatically creates or updates tables based on JPA annotations.
  * MyBatis settings: MyBatis works as the primary ORM using your defined *.xml mapper files.

### Step 4: Use MyBatis for Data Access

Define MyBatis mappers and SQL statements to handle queries and CRUD operations.

Mapper Interface:
```java
package com.example.demo.mapper;

import com.example.demo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id);

    void insert(User user);
}
```
Mapper XML (Optional):

Place the XML file in src/main/resources/mappers.
```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.example.demo.mapper.UserMapper">
    <insert id="insert" parameterType="com.example.demo.model.User">
        INSERT INTO users (name, email) VALUES (#{name}, #{email})
    </insert>
</mapper>
```

### Step 5: Run the Application

When you run the application:

	1.	Hibernate will automatically create tables based on JPA annotations.
	2.	MyBatis will execute SQL queries for data access.

Advantages of This Setup

	1.	MyBatis for ORM: MyBatis handles SQL, giving you fine-grained control over queries.
	2.	Automatic Table Creation: Hibernate simplifies schema management.
	3.	No Heavy Use of Hibernate: Hibernate only manages the schema and does not interfere with query execution.

### Alternative: Use MyBatis with Flyway/Liquibase for Schema Management


If you want more control over schema creation (e.g., versioning or avoiding runtime schema changes), you can integrate Flyway or Liquibase into your project.

#### Using Flyway

	1.	Add Dependency
```
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

	2.	Create Migration Scripts

Place SQL scripts in the src/main/resources/db/migration folder with names like V1__Create_User_Table.sql:

```
CREATE TABLE IF NOT EXISTS users (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(255) NOT NULL,
email VARCHAR(255) UNIQUE NOT NULL
);
```

	3.	Enable Flyway

Spring Boot will automatically run the migration scripts on application startup.

#### Using Liquibase

	1.	Add Dependency
```
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
</dependency>
```

	2.	Define Changesets
Create a changelog.xml file in src/main/resources/db/changelog:
```
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.8.xsd">

    <changeSet id="1" author="author">
        <createTable tableName="users">
            <column name="id" type="BIGINT" autoIncrement="true">
                <constraints primaryKey="true"/>
            </column>
            <column name="name" type="VARCHAR(255)">
                <constraints nullable="false"/>
            </column>
            <column name="email" type="VARCHAR(255)">
                <constraints unique="true" nullable="false"/>
            </column>
        </createTable>
    </changeSet>
</databaseChangeLog>
```

	3.	Configure Liquibase
Spring Boot will automatically execute the changelog on startup.
