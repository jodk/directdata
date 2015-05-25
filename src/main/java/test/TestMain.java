package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by zhangdekun on 15-5-11-下午2:37.
 */
public class TestMain {
    public static void main(String[] args) {
        System.out.println("running ....");

        rnwrap(100);
    }

    public static Stack<String> rn(int n) {
        Stack<String> stack = new Stack<>();
        Stack<String> news = new Stack<>();
        for (int i = 9; i >= 1; i--) {
            stack.add(i + "");
            if (i != 1) {
                stack.add("");
            }
        }
        for (int i = 0; i < 8; i++) {
            String c1 = stack.pop();
            String opt = stack.pop();
            String c2 = stack.pop();
            if (n % 3 == 0) {
                Integer temp = Integer.valueOf(c1) * 10 + Integer.valueOf(c2);
                stack.push(temp.toString());
            } else {
                news.push(c1);
                if (n % 3 == 1) {
                    news.push("+");
                } else {
                    news.push("-");
                }
                stack.push(c2);
            }
            n = n / 3;
        }
        if (!stack.isEmpty()) {
            news.push(stack.pop());
        }
        return news;
    }

    public static List<String> rnwrap(int total) {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < Math.pow(3, 8); i++) {
            Stack<String> st = rn(i);
            int sum = 0;
            int opt = 0;

            Stack<String> nst = new Stack<>();
            while (!st.isEmpty()) {
                nst.push(st.pop());
            }
            StringBuffer sb = new StringBuffer("");
            while (!nst.isEmpty()) {
                String s1 = nst.pop();
                sb.append(s1);
                if (s1.equals("+")) {
                    opt = 1;
                } else if (s1.equals("-")) {
                    opt = 2;
                } else {
                    if (opt == 1) {
                        sum = sum + Integer.valueOf(s1);
                    } else if (opt == 2) {
                        sum = sum - Integer.valueOf(s1);
                    } else {
                        sum = sum + Integer.valueOf(s1);
                    }
                    opt = 0;
                }
            }
            if (sum == total) {
                System.out.println(sb);
            }
        }
        return result;
    }
}
