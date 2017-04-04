package com.ylw.goods.util.page;

/**
 * 用来保存参数,操作符,和值的类,方便动态的执行sql语句
 * Created by 85243 on 2017/3/29.
 */
public class Expression {
    private String name;
    private String operator;
    private String value;

    public Expression() {
    }

    public Expression(String name, String operator, String value) {
        this.name = name;
        this.operator = operator;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Expression{" +
                "name='" + name + '\'' +
                ", operator='" + operator + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
