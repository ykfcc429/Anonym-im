package hello.DO;


import lombok.Data;

@Data
public class Greeting {

    private String content;

    private String sender;

    public Greeting(String content,String sender) {
        this.content = content;
        this.sender = sender;
    }
}
