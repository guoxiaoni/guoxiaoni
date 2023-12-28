import java.io.*;
import java.net.Socket;
import java.util.Scanner;



public class client {
    public static void main(String[] args) throws IOException {


        Socket sk = new Socket("127.0.0.1",10086);

        System.out.println("链接成功");

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("===========欢迎进入聊天室==============");
            System.out.println("1:登入");
            System.out.println("2:注册");
            System.out.println("请输入你的选择");
            String choss = sc.nextLine();
            switch (choss){
                case "1" -> login(sk);
                case "2" -> System.out.println("注册");
                default -> System.out.println("没有这个选择");
            }
        }


    }

    public static void login(Socket sk) throws IOException {

        BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(sk.getOutputStream()));

        System.out.println("==========请输入账号==========");
        Scanner sc = new Scanner(System.in);

        System.out.println("请输入用户名");

        String username = sc.nextLine();

        System.out.println("请输入密码");

        String password = sc.nextLine();

        StringBuilder sb = new StringBuilder();

        sb.append("username=").append(username).append("&password=").append(password);

        bf.write("login");
        bf.newLine();
        bf.flush();


        bf.write(sb.toString());
        bf.newLine();
        bf.flush();


        BufferedReader br = new BufferedReader(new InputStreamReader(sk.getInputStream()));
        String mess = br.readLine();
        System.out.println(mess);

        if ("1".equals(mess)){
            System.out.println("登入成功，开始聊天");
            new Thread(new clientrun(sk)).start();
            talk2all(bf);

        }else if ("2".equals(mess)){
            System.out.println("密码有误，请重新输入");

        }else if ("3".equals(mess)){
            System.out.println("账户不存在");
        }


    }

    private static void talk2all(BufferedWriter bw) throws IOException {
        Scanner sc = new Scanner(System.in);
        while (true){
            String str = sc.nextLine();
            bw.write(str);
            bw.newLine();
            bw.flush();

        }
    }

    static class clientrun implements Runnable {
        Socket sk;
        public clientrun(Socket sk) {
            this.sk=sk;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(sk.getInputStream()));
                    String mgs = br.readLine();
                    System.out.println(mgs);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
