import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;



public class Server {
    static ArrayList<Socket> list = new ArrayList<>();
    public static void main(String[] args) throws IOException {



        ServerSocket ss = new ServerSocket(10086);


        Properties pp = new Properties();

        FileInputStream fip = new FileInputStream("C:\\Users\\a1837\\IdeaProjects\\TCP2\\src\\nihao.txt");

        pp.load(fip);

        fip.close();

        while (true){

            Socket accept = ss.accept();

            new Thread(new runt(accept,pp)).start();
        }

    }
}

class runt implements Runnable {

    Socket socket;

    Properties properties;
    public runt (Socket socket,Properties properties){

        this.socket = socket;
        this.properties = properties;

    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String line = br.readLine();
                switch (line){
                    case "login" -> login(br);
                    case "register" -> System.out.println("用户选择了注册操作");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login(BufferedReader br) throws IOException {


        String read = br.readLine();

        String[] A = read.split("&");
        String username = A[0].split("=")[1];
        String password = A[1].split("=")[1];

        System.out.println("输入的账号名是"+username);
        System.out.println("输入的密码是"+password);

        if (properties.containsKey(username)){

            String rightPassword = properties.get(username) + "";
            if (rightPassword.equals(password)){
                passwprd1("1");
                Server.list.add(socket);
                talk2all(br,username);
            }else {

                passwprd1("2");
            }
            
        }else{
            passwprd1("3");
        }
    }

    private void talk2all(BufferedReader br,String username) throws IOException {
        while (true){
            String bread = br.readLine();
            System.out.println(username+"发来消息"+bread);

            for (Socket s : Server.list) {

                passwprd1(s,username+"发来消息"+bread);

            }

        }

    }

    public void passwprd1(String mess) throws IOException {
        BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bf.write(mess);
        bf.newLine();
        bf.flush();
    }
    public void passwprd1(Socket s,String mess) throws IOException {
        BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        bf.write(mess);
        bf.newLine();
        bf.flush();

    }


}