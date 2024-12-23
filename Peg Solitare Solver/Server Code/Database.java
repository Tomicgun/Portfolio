import java.util.concurrent.ConcurrentHashMap;

public class Database {
    ConcurrentHashMap<String,Data> map = new ConcurrentHashMap<>();

    public void write(String id, Data data){
        try {
            map.put(id,data);
        } catch (NullPointerException e) {
            System.out.println("NullPointerException");
        }
    }

    public Data read(String id){
        try{
            return map.get(id);
        }catch (NullPointerException e){
            return null;
        }
    }

}
