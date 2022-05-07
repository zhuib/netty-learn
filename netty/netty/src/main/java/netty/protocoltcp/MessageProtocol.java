package netty.protocoltcp;

/**
 * Date: 2021/6/15 17:14
 *
 * 协议包
 */
public class MessageProtocol {
    private int len;   // 长度是关键，通过长度确定接收的大小
    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
