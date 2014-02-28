package moca.user_center.test;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlEncoderTest {

    public static void main(String[] args) throws Exception {
        String encoding = "UTF-8";

        System.out.println(URLEncoder.encode("{\"userName\":\"xuzw\", \"password\":\"pwd\"}", encoding));

        System.out.println(URLDecoder.decode("%7B%22userName%22%3A%22xuzw%22%2C+%22password%22%3A%22pwd%22%7D", encoding));
        System.out.println(URLDecoder.decode("{\"userName\":\"xuzw\",+\"password\":\"pwd\"}", encoding));
    }

}
