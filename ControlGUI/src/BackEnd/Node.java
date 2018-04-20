package BackEnd;

public class Node {

    public Node (String name , String type){
        this.name = name;
        this.type = type;
    }

    private String name ;
    private String type;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

}
