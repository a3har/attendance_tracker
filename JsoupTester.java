import java.io.IOException;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.Iterator;
import org.jsoup.select.Elements;
import java.util.Map;


public class JsoupTester {
  public static void main(String[] args) throws IOException {

        final String USER_AGENT = "\"Mozilla/5.0 (Windows NT\" +\n" + "          \" 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2\"";
        String loginFormUrl = "https://www.rajagiritech.ac.in/stud/Parent/";
        String loginActionUrl = "https://www.rajagiritech.ac.in/stud/Parent/varify.asp";
        String username = "u1603149";
        String password = "160147";

        HashMap<String, String> cookies = new HashMap<>();
        HashMap<String, String> formData = new HashMap<>();


        Connection.Response loginForm = Jsoup.connect(loginFormUrl).method(Connection.Method.GET).userAgent(USER_AGENT).execute();
        Document loginDoc = loginForm.parse(); // this is the document that contains response html


        cookies.putAll(loginForm.cookies()); // save the cookies, this will be passed on to next request


        formData.put("user", username);
        formData.put("pass", password);
        formData.put("I1.x", "0");
        formData.put("I1.y", "0");


        Connection.Response homePage = Jsoup.connect(loginActionUrl)
         .cookies(cookies)
         .data(formData)
         .method(Connection.Method.POST)
         .userAgent(USER_AGENT)
         .execute();

        Document afterLogin = homePage.parse();
        Elements links = afterLogin.getElementsByClass("scroller");

        String scrollerText = links.text();
        String[] arr = scrollerText.split(" : ");
        String name = new String(arr[1]);
        scrollerText=null;
        arr=null;
        System.out.println(name+"\n\n\n");

        cookies.clear();
        cookies.putAll(homePage.cookies());

        Document doc = Jsoup.connect("https://www.rajagiritech.ac.in/stud/KTU/Parent/Leave.asp")
        .timeout(5000)
        .data("code","2019S6CS-C")
        .cookies(cookies)
        .get();

        //System.out.println(doc.html());
        Element classesMissed = doc.select("td").first();
        String str = new String(classesMissed.text());


        Map<String, Integer> map = new HashMap<String, Integer>();
        int counter = 0;
        String sub;
        int stringLen = str.length() - 4;
        for (int i = 0; i < stringLen ; i++) {
            sub = str.substring(i, i + 5);
            if (map.containsKey(sub)) {
                counter = map.get(sub);
                counter++;
                map.put(sub, counter);
                counter = 0;
            } else {
                map.put(sub, 1);
            }
        }

        //System.out.println(map.keySet().toString());
        map.entrySet().removeIf(entry -> entry.getKey().contains("-"));
        map.entrySet().removeIf(entry -> entry.getKey().contains(" "));
        map.entrySet().removeIf(entry -> entry.getKey().contains("a"));
        map.entrySet().removeIf(entry -> entry.getKey().contains("9S"));
        map.entrySet().removeIf(entry -> entry.getKey().contains("18"));
        map.entrySet().removeIf(entry -> entry.getKey().contains("17"));
        map.entrySet().removeIf(entry -> entry.getValue().toString().equals("1"));

        map.forEach((key, value) -> System.out.println(key + ":" + value));
        //for (Element cl:classesMissed){
        //  System.out.println(cl.text());
        //}
   }
}
