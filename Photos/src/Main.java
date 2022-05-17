import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int q = sc.nextInt();
        int curPage = 1;
        String url = "https://picsum.photos/v2/list?page=1&limit=200";
        ArrayList<String> list = new ArrayList<>();
        while (curPage < 10) {
            url = "https://picsum.photos/v2/list?page=" +
                    Integer.toString(curPage) + "&limit=200";
            String s = null;
            try {
                URLConnection urlConnection = new URL(url).openConnection();
                BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
                int k;
                while ((k = in.read()) != -1) {
                    s += (char) k;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String regexPhoto = "\"id\".+?\"}";
            Pattern pattern = Pattern.compile(regexPhoto);
            Matcher matcher = pattern.matcher(s);
            while (matcher.find()) {
                String cur = s.substring(matcher.start(), matcher.end());
                System.out.println(cur);
                String regex = "\"width\":[0-9]+,\"height\":[0-9]+";
                Pattern pattern1 = Pattern.compile(regex);
                Matcher matcher1 = pattern1.matcher(s);
                String t = s.substring(matcher1.start(), matcher1.end());
                int width = Integer.parseInt(t.substring(8, 12));
                int height = Integer.parseInt(t.substring(22, cur.length()));
                if ((width > 2000 && height > 4000) || (width > 4000 && height > 2000)) {
                    list.add(cur.substring(0, cur.length() - 1));
                }
            }
            if (list.size() < q) {
                curPage++;
            } else {
                break;
            }
        }
        try {
            FileWriter o = new FileWriter("photos.csv");
            BufferedWriter out = new BufferedWriter(o);
            for (int i = 0; i < q; i++) {
                String cur = list.get(i);
                String regex = "\"id\":\"[0-9]+\"";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(cur);
                String t = cur.substring(matcher.start(), matcher.end());
                out.write(t + " ");
                regex = "\"author\":\".?+\"";
                pattern = Pattern.compile(regex);
                matcher = pattern.matcher(cur);
                t = cur.substring(matcher.start(), matcher.end());
                out.write(t + " ");
                regex = "\"url\":\".?+\"";
                pattern = Pattern.compile(regex);
                matcher = pattern.matcher(cur);
                t = cur.substring(matcher.start(), matcher.end());
                out.write(t + " ");
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
