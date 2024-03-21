package cn.atm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    private Account currentAccount = null; // 用于记录当前登录的用户卡号信息

    /**
     * 启动ATM系统，展示欢迎界面
     */
    public void start() {
        do {
            System.out.println("---欢迎您进入到了ATM系统---");
            System.out.println("1、用户登录");
            System.out.println("2、用户开户");
            System.out.println("3、退出");
            System.out.println("请选择：");
            int command = scanner.nextInt();
            switch (command) {
                case 1: // 用户登录
                    login();
                    break;
                case 2: // 用户开户
                    createAccount();
                    break;
                case 3: // 退出ATM系统
                    System.exit(0);
                    break;
                default:
                    System.out.println("没有该操作～～");
            }
        } while (true);
    }

    /**
     * 登录
     */
    private void login() {
        if (accounts.isEmpty()) {
            System.out.println("您好，当前系统无账户～～");
            return;
        }

        Account account;
        while (true) {
            System.out.println("请输入您的卡号");
            String cardId = scanner.next();
            // 判断当前系统中是否存在该卡号
            Optional<Account> accountOpt = getAccountByCardId(cardId);
            if (accountOpt.isPresent()) {
                account = accountOpt.get();
                break;
            }
            System.out.println("您输入的卡号不存在！");
        }

        while (true) {
            System.out.println("请输入您的密码");
            String password = scanner.next();
            // 验证密码
            if (password.equals(account.getPassword())) {
                System.out.println("恭喜您" + account.getUsername() + "，您已成功登录ATM系统！");
                currentAccount = account; // 记录当前登录的用户信息

                // 展示登录后的操作
                tnxsAfterLogin();
                return;
            }
            System.out.println("您输入的密码错误！");
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

    /**
     * 登录后的操作
     */
    private void tnxsAfterLogin() {
        while (true) {
            System.out.println(currentAccount.getUsername() + ", 您可以办理以下业务：");
            System.out.println("1、查询账户");
            System.out.println("2、存款");
            System.out.println("3、取款");
            System.out.println("4、转账");
            System.out.println("5、修改密码");
            System.out.println("6、退出");
            System.out.println("7、注销账户");
            System.out.println("请选择：");

            int command = scanner.nextInt();
            switch (command) {
                case 1: // 1. 查询账户
                    getAccountInfo();
                    break;
                case 2: // 2. 存款
                    depositMoney();
                    break;
                case 3: // 3. 取款
                    drawMoney();
                    break;
                case 4: // 4. 转账
                    transferMoney();
                    break;
                case 5: // 5. 修改密码
                case 6: // 6. 退出
                    System.out.println(currentAccount.getUsername() + ", 您已成功退出系统");
                    currentAccount = null;
                    return; // 退出循环
                case 7: // 7. 注销账户
                default:
                    System.out.println("对不起，您输入的操作无效！");
            }
        }
    }

    private void transferMoney() {
        System.out.println("---转账操作---");
        // 1. 判断系统中是否有其他的账户
        if (accounts.size() < 2) {
            System.out.println("当前系统中只有你一个账户，无法为其他账户转账～");
            return;
        }

        // 2. 判断当前账户中是否有钱
        if (currentAccount.getMoney() == 0) {
            System.out.println("您的账户中没有金额，无法为其他账户转账～");
            return;
        }

        // 3. 提示用户输入要转账的账户信息
        System.out.println("请输入需要转账的卡号信息：");
        String transferTo = scanner.next();

        // 4. 判断用户输入的账户信息是否存在
        Optional<Account> transferAccountOpt = getAccountByCardId(transferTo);
        // 5. 如果账户存在，还需要认证待转账用户的户主姓氏
        transferAccountOpt.ifPresent(transferToAccount -> {
            String username = transferToAccount.getUsername();
            System.out.println("您当前要为*" + username.substring(1) + "转账！");

            // 6. 提示用户输入户主姓氏
            int retry = 0;
            while (true) {
                if (retry >= 3) {
                    System.out.println("认证失败！");
                    return;
                }

                System.out.println("请输入待转账用户的姓氏：");
                String lastName = scanner.next();
                retry++;

                // 7. 验证用户输入的姓氏是否正确
                if (lastName.equals(String.valueOf(username.charAt(0)))) {
                    break;
                }

                System.out.println("您输入的户主姓氏错误！");
            }

            // 8. 认证成功，开始转账
            double transferAmount;
            while (true) {
                // 8.1 提示用户输入转账金额
                System.out.println("请输入转账金额：");
                transferAmount = scanner.nextDouble();
                // 8.2 判断用户输入的金额是否超过当前账户余额
                double currentMoney = currentAccount.getMoney();
                if (transferAmount <= currentMoney) {
                    break;
                }
                System.out.println("您输入的转账金额超过您的余额！当前账户中的余额为：" + currentMoney);
            }

            currentAccount.setMoney(currentAccount.getMoney() - transferAmount);
            transferToAccount.setMoney(transferToAccount.getMoney() + transferAmount);
            System.out.println("转账成功，您的账户当前可用余额为：" + currentAccount.getMoney());
        });
    }

    private void drawMoney() {
        System.out.println("---取款操作---");
        // 1. 先判断当前账户中的余额是否大于100，如果不足100元，则无法取钱
        double currentMoney = currentAccount.getMoney();
        if (currentMoney < 100) {
            System.out.println("您的账户余额不足100元，无法在ATM进行取款操作，请到柜台办理业务！");
            return;
        }

        double money;
        while (true) {
            System.out.println("请输入您要取出的金额：");
            money = scanner.nextDouble();

            // 2. 判断用户输入的金额是否大于当前账户设定的取现金额
            double limit = currentAccount.getLimit();
            if (money >= limit) {
                System.out.println("超出每日取款限额，当前限额：" + limit);
                continue;
            }

            // 3. 判断当前账户余额是否足够
            if (currentMoney >= money) {
                // 4. 如果余额充足，则开始扣款
                currentAccount.setMoney(currentMoney - money);
                System.out.println("取款成功，当前账户余额为：" + currentAccount.getMoney());
                break;
            } else {
                System.out.println("余额不足，您的账户当前可用余额为：" + currentMoney);
            }
        }
    }

    /**
     * 存钱
     */
    private void depositMoney() {
        System.out.println("---存钱操作---");
        System.out.println("请输入您要存入的金额：");
        double money = scanner.nextDouble();

        currentAccount.setMoney(currentAccount.getMoney() + money);
        System.out.println("储存成功，当前账户余额为：" + currentAccount.getMoney());
    }

    /**
     * 查询当前登录用户的账户信息
     */
    private void getAccountInfo() {
        System.out.println("---当前您的账户信息如下：---");
        System.out.println("卡号：" + currentAccount.getCardId());
        System.out.println("户主：" + currentAccount.getUsername());
        System.out.println("性别：" + currentAccount.getGender());
        System.out.println("余额：" + currentAccount.getMoney());
        System.out.println("取现额度：" + currentAccount.getLimit());
    }

    /**
     * 通过卡号查找对应的账户信息
     */
    private Optional<Account> getAccountByCardId(String cardId) {
        return accounts.parallelStream()
                .filter(account -> account.getCardId().equals(cardId))
                .findAny();
    }
}
