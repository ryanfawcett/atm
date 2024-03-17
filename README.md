# ATM系统

## 一、系统架构搭建、欢迎页设计

### 1.1 准备相关类

- Account：用于存储用户卡号信息
- ATM：ATM系统类，用于实现ATM系统中的各种功能
- App：入口类，用于创建ATM对象，启动ATM系统

#### Account

```java
package cn.atm;

public class Account {
    private String cardId;
    private String username;
    private char gender;
    private String password;
    private double money;
    private double limit;
    
    // ... getter and setter
}
```

## 二、开户功能实现

### 实现步骤

1. 在ATM类中创建一个**ArrayList**的集合，用于存储所有的卡号信息
2. 提示用户输入开户操作
3. 开户
   1. 提示用户输入用户名
   2. 提示用户输入密码
   3. 提示用户输入确认密码
   4. 提示用户输入取现的额度
4. 将该账户信息添加到集合中

## 三、登陆功能实现
## 四、操作页展示、查询账户、退出账户
## 五、存款、取款功能实现
## 六、转账功能实现
## 七、销户功能实现
## 八、用户密码修改