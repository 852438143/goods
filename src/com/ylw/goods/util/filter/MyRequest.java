package com.ylw.goods.util.filter;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * 装饰器设计模式,MyRequest类装饰了HttpServletRequest,增强了其中的某些功能
 * Created by 85243 on 2017/3/30.
 */
public class MyRequest extends HttpServletRequestWrapper{
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request
     * @throws IllegalArgumentException if the request is null
     */
    public MyRequest(HttpServletRequest request) {
        super(request);
    }
    Logger log = Logger.getLogger(MyRequest.class);
    /**
     * 增强getParameter方法,使把字符进行解码,
     * 这个解码对应的时encode方法,虽然不知道为什么,但是貌似比new String(value.getBytes("ISO-8859-1"), charset);要好,这个时把iso-8859-1的字符转化为utf-8
     * @param name
     * @return
     */
    @Override
    public String getParameter(String name) {

        String value = super.getParameter(name);
        try {
            if(value!=null&&!value.equals("")){
                log.info("before decode : "+value);
                value= URLDecoder.decode(value,"utf-8");
                log.info("after decode : "+ value);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 增强getParameterMap方法,
     * @return
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String,String[]> map = super.getParameterMap();
        if(map!=null){
        for(String temp :map.keySet()){
            String[] strs = map.get(temp);
            for(int i = 0;i<strs.length;i++){
                try {
                    log.info("before decode : "+strs[i]);
                    strs[i] = URLDecoder.decode(strs[i],"utf-8");
                    log.info("after decode : "+ strs[i]);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        }
        return map;
    }
//    @Override
//    public String[] getParameterValues(String name) {
//        String[] values = super.getParameterValues(name);
//        for(int i = 0; i < values.length; i++) {
//            try {
//                log.info("before decode : "+values[i]);
//                values[i] = URLDecoder.decode(values[i],"utf-8");
//                log.info("after decode : "+ values[i]);
//            } catch (UnsupportedEncodingException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return values;
//    }
}

