package test;

import com.ylw.goods.po.User;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 85243 on 2017/3/30.
 */
public class Demo1 {
    /**
     * 了解String(value.getBytes("ISO-8859-1"), "utf-8") 的作用
     * 目测只能把iso-8859准华为utf-8
     * @throws UnsupportedEncodingException
     */
    @Test
    public void test1() throws UnsupportedEncodingException {
        String str = "https ://asfd/&aa=bb";
        System.out.println(str.endsWith("&aa=bb"));
        str = "/goods/BookServlet?method=getPagerByCatogory&cid=5F79D0D246AD4216AC04E9C5FAB3199E&pc=2&pc=3";
        int index = str.lastIndexOf("&pc=");
        System.out.println(str.substring(0,index));
        String value="傻狗";
        value = new String(value.getBytes("ISO-8859-1"), "utf-8");
        System.out.println(value);
    }

    /**
     * 查看字符编码问题
     * @throws UnsupportedEncodingException
     */
    @Test
    public void test2() throws UnsupportedEncodingException {
        String str = "http://localhost:8080/goods/BookServlet?method=getPagerByAuthor&author=%e6%98%8e%e6%97%a5%e7%a7%91%e6%8a%80";
        str = URLDecoder.decode(str,"utf-8");
        System.out.println(str);
        str = "%E5%85%A5%E9%97%A8";
        str = URLDecoder.decode(str,"utf-8");
        System.out.println(str);
    }

    /**
     * 遍历map时是否可以更改数据
     *试试证明foreach 遍历的时候不能修改原数组数据
     * 第二次验证貌似是能修改数据的,应该是不能修改原来数据的引用,用一个类试了下,发现可以修改该类中的数据,就和string和类的区别一样,应该foreach不能修改该对象的引用
     */
    @Test
    public  void test3(){
        Map<String,String[]> map = new HashMap<>();
        map.put("a", new String[]{"aa", "bb"});
        for(String temp :map.keySet()){
            for(String t: map.get(temp)){
                t="shagou";
            }
        }
        for(String temp :map.keySet()){
            for(String t: map.get(temp)){
                System.out.println(t);
            }
        }

        List<User> users = new ArrayList<>();
        users.add(new User());users.get(0).setLoginname("shagou");
        users.add(new User());
        users.get(1).setLoginname("bbbb");
        for(User user : users){
            user.setLoginname("aa");
        }
        System.out.println(users);

        String [] strs = new String[]{"aa","bbb"};
        for(String temp :strs){
            temp= "shagou";
        }
        for(String temp :strs){
            System.out.println(temp);
        }
        for(int i=0 ;i<strs.length;i++){
            strs[i]="shagou";
        }
        for(String temp :strs){
            System.out.println(temp);
        }
    }

    @Test
    public void test4(){
        String str = "";
        Object[] objs = str.split(",");//这个是按,把字符串隔开,原本字符串为第一个,所以第一个为"";
        System.out.println(objs.length);
    }

    /**
     * 对list添加对象时每个对象都需要新建,要不然给list添加的时同一个对象,list只是对象的引用,修改对象,List跟着改变
     */
    @Test
    public void test5(){
        User user = new User();
        List<User> users = new ArrayList<>();
        for(int i = 0;i<5;i++){
            user.setLoginname("shagou"+i);
            users.add(user);
        }
        //发现所有的数据都是shagou4,
        System.out.println(users);
        user.setLoginname("傻狗");
        System.out.println(users);

    }
}
