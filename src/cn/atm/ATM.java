package cn.atm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * ATM系统类
 *
 * @author ryanfawcett
 * @since 2024/03/16
 */
public class ATM {
    /**
     * 用户获取用户输入
     */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * 用于存储ATM系统中创建的用户开户账号信息
     */
    private final List<Account> accounts = new ArrayList<>();

    /**
     * 启动ATM系统，展示欢迎界面
     */
    public void start() {
        while (true) {
            System.out.println("---欢迎您进入到了ATM系统---");
            System.out.println("1、用户登录");
            System.out.println("2、用户开户");
            System.out.println("请选择：");
            int command = scanner.nextInt();
            switch (command) {
                case 1:
                    // 用户登录
                    break;
                case 2:
                    // 用户开户
                    createAccount();
                    break;
                default:
                    System.out.println("没有该操作～～");
            }
        }
    }

    /**
     * 用户开户
     */
    private void createAccount() {
        // 1. 创建一个账户对象，用于封装用户的开户信息
        Account account = new Account();

        // 2. 用户输入自己的开户信息，赋值给账户对象
        System.out.println("请输入您的账户名称：");
        account.setUsername(scanner.next());

        while (true) {
            System.out.println("请选择您的性别：");
            char gender = scanner.next().charAt(0);
            if (gender == '男' || gender == '女') {
                account.setGender(gender);
                break;
            } else {
                System.out.println("请输入正确的性别！");
            }
        }

        while (true) {
            System.out.println("请输入您的账户密码：");
            String password = scanner.next();
            System.out.println("请确认您的账户密码：");
            String passwordConfirm = scanner.next();

            if (password.equals(passwordConfirm)) {
                account.setPassword(password);
                break;
            } else {
                System.out.println("您输入的两次密码不一致，请重新输入～～");
            }
        }

        System.out.println("请输入您的取现额度：");
        account.setLimit(scanner.nextDouble());

        // 注意：我们需要为这个账户生成一个卡号
        // 由系统自动生成，8位数字表示，并且唯一，即不能与账户集合中的其他卡号重复
        while (true) {
            String cardId = generateCardId(); // 随机生成一个8位数的账号
            // 在账号集合中查询生成的账号是否存在
            // 如果存在则重新生成，如果不存在则将生成的账号赋值给当前账户并且退出循环
            boolean exist = accounts.parallelStream()
                    .anyMatch(a -> cardId.equals(a.getCardId()));
            if (!exist) {
                account.setCardId(cardId);
                break;
            }
        }

        // 3. 把这个账户对象添加到账户集合中
        accounts.add(account);
        System.out.println("恭喜" + account.getUsername() + "开户成功！您的卡号是：" + account.getCardId());
    }

    /**
     * 生成一个8位数字的随机数
     *
     * @return cardId
     */
    private String generateCardId() {
        // 1. 定义一个用于记录cardId的字符串
        StringBuilder cardId = new StringBuilder();

        // 2. 使用循环随机生成数字，并将生成的数字进行拼接
        // 2.1 定义Random对象，用于随机生成数字
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            int unit = random.nextInt(10); // 表示生成 0 - 9 之间的数字
            cardId.append(unit);
        }
        return cardId.toString();
    }
}
