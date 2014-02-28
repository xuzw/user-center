package moca.user_center.test;

import com.alibaba.fastjson.JSONObject;

public class FastJsonTest {

    public static void main(String[] args) {
        String param = "{\"user\":12}s";
        try {
            JSONObject jsonObject = (JSONObject) JSONObject.parse(param);
            System.out.println(jsonObject);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
