import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        bitstuffing();
        bytestuffing();
    }

    public static void bitstuffing() throws IOException {
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        BitStuffing bs=new BitStuffing();
        bs.printInfo();
        System.out.printf("比特填充后的发送帧: %s\n",bs.stuffing());
        System.out.printf("比特删除后的接收帧: %s\n",bs.destuffing());
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------");
    }

    public static void bytestuffing() throws IOException {
        System.out.println("---------------------------------------------------------------------------------------------------------------------------");
        ByteStuffing bs=new ByteStuffing();
        bs.showInfo();
        System.out.printf("字节填充后的发送帧: %s\n",bs.stuffing());
        System.out.printf("字节删除后的接收帧: %s\n",bs.destuffing());
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------");
    }
}
