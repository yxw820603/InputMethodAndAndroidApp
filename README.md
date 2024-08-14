虾搞的项目  
一个C++项目，实现了一个fcitx5的插件，功能很简单，绑定监听一个UDP组播地址，循环由此地址收到的数据，并把数据发送到fcitx5的输入法  

另外一个是android项目，启动一个APP包含一个多行文本框，在文本框内输入的内容，会每隔5秒检查一次，把发生变化的部分，用UDP组播发送出去

这样就可以实现在android手机上输入的文字，被发送到fcitx5的输入法，实现在android手机上输入文字输入到linux系统的程序里。

意思是可以偷懒用android的语音输入法了

开发过程中用到的测试命令，算是学到了点东西

向组播地址 224.0.0.1 的端口 7300 发送字符串  
```echo "Hello, Multicast!" | socat - UDP-DATAGRAM:224.0.0.1:7300,bind=:0```

可以同时运行多个实例来接收相同组播地址的数据
```socat UDP4-RECVFROM:7300,ip-add-membership=224.0.0.1:0.0.0.0,reuseaddr -```


好用难用的，反正能用。 ^-^

编译：
```
rm UDPInput/build -rf
mkdir -p UDPInput/build
cd UDPInput/build
cmake ..
make
cd ../..
./gradlew assembleDebug
```