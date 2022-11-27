package com.wild.yygh.hosp.testmongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    //符合SpringData方法规范的接口
    List<User> findByName(String name);

    List<User> findByNameLike(String name);
}
