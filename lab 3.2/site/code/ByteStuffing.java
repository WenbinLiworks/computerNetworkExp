
public class ByteStuffing {
    private static String ESC="1B"; // 选取ESC填充 对应ASCII码为1B
    private String flag;
    private String src;
    private String dest;

    // 字节填充
    ByteStuffing() {
        ConfigReader cfg=new ConfigReader("byteconfig.properties");
        cfg.PrintConfig();
        this.flag=cfg.GetValue("FlagString");
        this.src=cfg.GetValue("InfoString");
        this.dest=stuffing(this.src);
    }

    // 显示配置信息
    public void showInfo()
    {
        System.out.printf("帧的起始标志: %s \t 帧的数据信息: %s \t 帧的结束标志: %s\n",this.flag,this.src,this.flag);
    }

    // 字节填充函数
    public String encode(String data){
        String code="";
        for (int i=0;i<data.length();i+=2) {
            // 每次取2位16进制数，如果等于FlagString或者ESC就在它的前面加上ESC字符
            String tmp=data.substring(i,i+2);
            if (tmp.equals(ESC) || tmp.equals(this.flag)) {
                code+=ESC;
            }
            code+=tmp;
        }
        return code;
    }

    // 字节填充函数 反向
    public String decode(String data){
        String code="";
        for (int i=0;i<data.length();i+=2) {
            String tmp=data.substring(i,i+2);
            if (tmp.equals(ESC)) {
                i+=2;
                tmp=data.substring(i,i+2);
            }
            code+=tmp;
        }
        return code;
    }

    // 加入前后帧
    public String enframe(String data){
        return this.flag+data+this.flag;
    }

    // 删除前后帧
    public String deframe(String data){
        return data.substring(flag.length(),data.length()-flag.length());

    }



    //
    public String stuffing(String data){
        return enframe(encode(data));
    }

    public String stuffing(){
        return this.dest;
    }

    public String destuffing(String data){
        return decode(deframe(data));
    }

    public String destuffing(){
        return destuffing(this.dest);
    }

}
