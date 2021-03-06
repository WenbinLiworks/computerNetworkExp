# computerNetworkExp
计算机网络编程实验

### Data-Link Layer

>  The Data Link Layer Experiment

## 实验 1 编程

循环冗余校验 CRC 生成和校验程序。

**配置文件关键要点：**

待发送的数据信息二进制比特串（32 位）:

```
InfoString1=0110XXXXXXXXXXXXXXXXXXXX110
```

收发双方预定的生成多项式采用 `CRC-CCITT=X16+X12+X5+1`，对应的二进制比特串（17 位）：

```
GenXString=10001000000100001
```

接收的数据信息二进制比特串（32 位）：

```
InfoString2=0110XXXXXXXXXXXXXXXXXXXX110
```

**程序运行屏幕输出要点：**

- 首先显示待发送的数据信息二进制比特串
- 然后显示收发双方预定的生成多项式采用 `CRC-CCITT`，对应的二进制比特串
- 计算循环冗余校验码 `CRC-Code`
- 显示生成的 `CRC-Code`，以及带校验和的发送帧
- 显示接收的数据信息二进制比特串，以及计算生成的 `CRC-Code`
- 计算余数
- 显示余数，为零表示无错，不为零表示出错

## 实验 2 编程

实现透明传输程序。

**要求：分别实现零比特填充和字节填充。**

### 比特填充配置文件关键要点：

- 待发送的数据信息二进制比特串（32 位）

```
InfoString1=0110XXXXXX11111111111XXXXXXXX110
```

- 帧起始和结束标志二进制比特串

```
FlagString=01111110
```

### 比特填充程序运行屏幕输出关键要点：

- 屏幕显示帧起始标志、帧数据信息和帧结束表示
- 比特填充，显示比特填充后的发送帧
- 显示比特删除后的接收帧

### 字节填充配置文件关键要点：

- 待发送的数据信息十六进制串（64 位）

```
InfoString1=347D7E807E40AA7D
```

- 帧起始和结束标志十六进制串

```
FlagString=7E
```

### 字节填充程序运行屏幕输出关键要点：

- 屏幕显示帧起始标志、帧数据信息和帧结束表示
- 字节填充，显示字节填充后的发送帧
- 显示字节删除后的接收帧

## 实验 3 编程

**基于停止等待协议的可靠通信。**

采用 UDP Socket 编程接口作为模拟物理层接口实现帧的发送和接收，协议采用单工方式进行数据通信。假设 Host1 要向 Host2 发送大文件，通过数据链路层的帧每次完成数据块的可靠传输，采用停止等待协议，差错编码采用 CRC-CCITT 标准。以教材协议 3 为基础，在帧末尾增加 CRC 校验字段。

### 发送程序配置文件关键要点：

- 数据传输目的 UDP 端口：

```
UDPPort=8888
```

- 增添发送过滤程序，模拟传输出错或丢数据帧，下面两项指明每发送多少帧出现一次出错或丢帧，此例表示每 10 帧中一帧出错，每 10 帧中一帧丢失

```
FilterError=10
FilterLost=10
```

### 发送程序运行屏幕输出关键要点：

- 显示 `next_frame_to_send` 变量的值，以及正在发送帧的编号
- 显示经过过滤器后是正确发送、模拟传输出错还是模拟帧丢失（实际没有发送）
- 显示接收到确认帧，确认帧的确认序号
- 或者显示超时
- 回到开始重复一直到文件发送完成

### 接收程序运行屏幕输出关键要点：

- 显示 `frame_expected` 变量的值，
- 接收帧是否出错（CRC 余数是否为零），正确则显示接收帧的发送帧序号
- 显示发送回确认帧，以及确认帧的确认序号
- 回到开始重复一直到文件接收完成

## 实验 4 编程

**基于连续 ARQ 协议的可靠通信。**

采用 UDP Socket 编程接口作为模拟物理层接口实现帧的发送和接收，协议采用双工方式进行数据通信。假设 Host1 和 Host2 分别向对方发送大文件，Host1 先发送一帧到 Host2，通过数据链路层的帧每次完成数据块的可靠传输，采用 GBN 协议，差错编码采用 CRC-CCITT 标准。以教材协议 5 为基础，在帧末尾增加 CRC 校验字段。

### 配置文件关键要点：

- 数据传输目的 UDP 端口

```
UDPPort=8888
```

- 增添发送过滤程序，模拟传输出错或丢数据帧，下面两项指明每发送多少帧出现一次出错或丢帧，此例表示每 10 帧中一帧出错，每 10 帧中一帧丢失

```
FilterError=10
FilterLost=10
```

### Host1 程序运行屏幕输出关键要点：

- 显示 `ack_expected`、`next_frame_to_send` 和 `frame_expected` 变量的值，以及正在发送帧的编号和确认序号
- 显示经过过滤器后是正确发送、模拟传输出错还是模拟帧丢失（实际没有发送）
- 显示接收到对方帧，该帧的发送序号和确认序号，以及当前 frame_expected` 变量的值
- 或者显示超时，重传帧的发送序号，以及 `ack_expected`、`next_frame_to_send` 变量的值
- 回到开始重复一直到文件发送完成

### Host2 程序运行屏幕输出关键要点：

- 显示 `frame_expected` 变量的值
- 接收帧是否出错（CRC 余数是否为零），正确则显示接收帧的发送帧序号
- 显示 `ack_expected`、`next_frame_to_send` 和 `frame_expected` 变量的值，以及正在发送帧的编号和确认序号
- 显示经过过滤器后是正确发送、模拟传输出错还是模拟帧丢失（实际没有发送）
- 或者显示超时，重传帧的发送序号，以及 `ack_expected`、`next_frame_to_send` 变量的值
- 回到开始重复一直到文件接收完成
