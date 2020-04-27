import java.io.IOException;

public class BitStuffing {
    private String flag;
    private String src;
    private String dest;

    BitStuffing() throws IOException {
        // 从配置文件中获取
        ConfigReader cfg = new ConfigReader("bitconfig.properties");
        this.flag = cfg.GetValue("FlagString");
        this.src = cfg.GetValue("InfoString");
        cfg.PrintConfig();
        // 添加帧
        this.dest = this.enframe(encode(this.src));
    }

    // 打印配置信息
    public void printInfo()
    {
        System.out.printf("帧的起始标志: %s \t 帧的数据信息: %s \t 帧的结束标志: %s\n",this.flag,this.src,this.flag);
    }

    // 比特填充
    public static String encode(String data){
        return data.replace("11111","111110");
    }

    // 删去比特填充的部分
    public  static String decode(String data){
        return data.replace("111110","11111");
    }

    // 添加前后帧
    public String enframe(String data){
        return this.flag+data+this.flag;
    }

    // 移除前后帧
    public String deframe(String data){
        return data.substring(flag.length(),data.length()-flag.length());
    }

    // 初始化比特填充
    public String stuffing(){
        return this.dest;
    }

    // 比特填充复原
    public String destuffing(){
        return decode(deframe(this.dest));
    }

}
