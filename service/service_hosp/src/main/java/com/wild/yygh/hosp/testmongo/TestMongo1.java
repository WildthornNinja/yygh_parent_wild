package com.wild.yygh.hosp.testmongo;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 测试MongoDB CRUD 小案例一
 *  MongoTemplate
 */
@RestController
@RequestMapping("/mongo1")
public class TestMongo1 {
    @Autowired
    private MongoTemplate mongoTemplate;
    /**
     * 添加
     */
    @GetMapping("/create")
    public void createUser(){
        User user = new User();
        user.setName("wild");
        user.setAge(18);
        user.setEmail("wild@qq.com");
        //mongoTemplate.insert(user); 也可以新增
        User result = mongoTemplate.save(user);
        System.out.println("result = " + result);
    }
    /**
     * 查询所有
     */
    @GetMapping("/findAAll")
    public void findAll(){
        List<User> userList = mongoTemplate.findAll(User.class);
        userList.forEach(System.out::println);
    }
    /**
     * 根据id查询
     */
    @GetMapping("/findId")
    public void getById(){
        User user = mongoTemplate.findById("63817b8256970937407e46ca", User.class);
        System.out.println("user = " + user);
    }
    /**
     * 条件查询
     */
    @GetMapping("/findLike")
    public void findUserList(){
        //创建一个query对象（用来封装所有条件对象)，再创建一个criteria对象（用来构建条件）
        Query query = new Query(Criteria
        .where("name")
        .is("widl")
        .and("age")
        .is(18));
        List<User> userList = mongoTemplate.find(query, User.class);
        userList.forEach(System.out::println);
    }
    /**
     * 模糊查询
     */
    @GetMapping("/findUserList")
    public void findUsersLikeName(){
        //设置查询条件参数 和 正则表达式
        String name = "est";
        String regex = String.format("%s%s%s", "^.*", name, ".*$");
        //添加正则表达式
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);

        Query query = new Query(Criteria
        .where("name").regex(pattern));

        List<User> userList = mongoTemplate.find(query, User.class);
        userList.forEach(System.out::println);

    }
    /**
     * 分页查询
     */
    @GetMapping("/findPage")
    public void findUsersPage(){
        //1.分页参数
        int pageNo = 1; //当前页
        int pageSize = 10; //每页记录数

        //2.查询条件
        String name = "est";
        String regex = String.format("%s%s%s", "^.*", name, ".*$");
        //添加正则表达式
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);

        Query query =new Query(Criteria
                .where("name").regex(pattern));

        //3.统计总记录数 total
        long total = mongoTemplate.count(query, User.class);
        //4.query存储分页参数
        query.skip((pageNo-1)*pageSize).limit(pageSize);
        //进行带条件的分页查询
        List<User> userList = mongoTemplate.find(query, User.class);
        //创建返回结果集
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("userList",userList);
        System.out.println("map = " + map);
    }
    /**
     * 修改
     */
    @GetMapping("/update")
    public void updateUser() {
        //修改对象参数
        User user = mongoTemplate.findById("63817b8256970937407e46ca", User.class);
        user.setName("test_1");
        user.setAge(25);
        user.setEmail("493220990@qq.com");
        //先查询后更新
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        //执行更新对象
        Update update = new Update();
        update.set("name", user.getName());
        update.set("age", user.getAge());
        update.set("email", user.getEmail());
        UpdateResult result = mongoTemplate.upsert(query, update, User.class);
        long count = result.getModifiedCount();
        System.out.println(count);
    }
    /**
     * 删除
     */
    @GetMapping("delete")
    public void delete() {
        Query query =
                new Query(Criteria.where("_id").is("63817b8256970937407e46ca"));
        DeleteResult result = mongoTemplate.remove(query, User.class);
        long count = result.getDeletedCount();
        System.out.println(count);
    }
}
