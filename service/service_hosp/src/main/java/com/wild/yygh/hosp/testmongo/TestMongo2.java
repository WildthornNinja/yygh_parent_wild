package com.wild.yygh.hosp.testmongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mongo2")
public class TestMongo2 {
    @Autowired
    private UserRepository userRepository;
    /**
     * 添加
     */
    @GetMapping("/create")
    public void createUser() {
        User user = new User();
        user.setAge(20);
        user.setName("张三");
        user.setEmail("3332200@qq.com");
        User result = userRepository.save(user);
        System.out.println("result = " + result);
    }
    /**
     * 查询所有
     */
    @GetMapping("/findAll")
    public void findUser() {
        List<User> userList = userRepository.findAll();
        userList.forEach(System.out::println);

    }
    /**
     * 根据id查询
     */
    @GetMapping("/findId")
    public void getById() {
        User user = userRepository.findById("63836f1e1b91db5a42cf37eb").get();
        System.out.println(user);
    }
    /**
     * 条件查询
     */
    @GetMapping("/findQuery")
    public void findUserList() {
        User user = new User();
        user.setName("张三");
        user.setAge(20);
        //创建查询条件对象
        Example<User> userExample = Example.of(user);
        List<User> userList = userRepository.findAll(userExample);
        userList.forEach(System.out::println);
    }
    /**
     * 模糊查询
     */
    @GetMapping("/findLike")
    public void findUsersLikeName(){
        //查询条件
        User user = new User();
        user.setName("三");

        //创建匹配器，即如何使用查询条件
        //构建对象
        ExampleMatcher matcher = ExampleMatcher.matching()
                //改变默认字符串匹配方式：模糊查询
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                //改变默认大小写忽略方式：忽略大小写
                .withIgnoreCase(true);
        //条件模板
        Example<User> userExample = Example.of(user,matcher);
        List<User> userList = userRepository.findAll(userExample);
        userList.forEach(System.out::println);

    }
    /**
     * 分页查询
     */
    @GetMapping("/findPage")
    public void findUsersPage(){
        //排序对象
        Sort sort = Sort.by(Sort.Direction.DESC, "age");
        //0为第一页，每页显示条目10页
        Pageable pageable = PageRequest.of(0, 10, sort);
        //创建匹配器，即如何使用查询条件
        //构建对象
        ExampleMatcher matcher = ExampleMatcher.matching()
                //改变默认字符串匹配方式：模糊查询
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                //改变默认大小写忽略方式：忽略大小写
                .withIgnoreCase(true);
        //查询条件
        User user = new User();
        user.setName("三");
        Example<User> userExample = Example.of(user, matcher);
        //创建实例
        Example<User> example = Example.of(user, matcher);
        Page<User> pages = userRepository.findAll(example, pageable);
        System.out.println(pages);
        pages.forEach(System.out::println);

    }
    /**
     * 修改
     */
    @GetMapping("/update")
    public void updateUser() {
        User user = userRepository.findById("63836f1e1b91db5a42cf37eb").get();
        user.setName("张三_1");
        user.setAge(25);
        user.setEmail("883220990@qq.com");
        User save = userRepository.save(user);
        System.out.println(save);
    }
    /**
     * 删除
     */
    @GetMapping("/delete")
    public void delete() {
        userRepository.deleteById("63817b8256970937407e46ca");
    }

    /**
     * 符合SpringData方法规范的接口方法
     */
    @GetMapping("/testMethod1")
    public void testMethod1(){
        List<User> userList = userRepository.findByName("张三");
        userList.forEach(System.out::println);

    }
    @GetMapping("/testMethod2")
    public void testMethod2() {
        List<User> userList = userRepository.findByNameLike("张");
        userList.forEach(System.out::println);
    }

}
