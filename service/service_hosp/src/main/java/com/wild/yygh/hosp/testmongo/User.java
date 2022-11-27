package com.wild.yygh.hosp.testmongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("User")//指定MongoDB中的集合
public class User {
    @Id //mongodb  中的主键 _id
    private String id;
    private String name;
    private Integer age;
    private String email;
    private String createDate;
}