package Client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static service.ServiceMessages.FAIL_NAME;

public class History {
    private static PrintWriter pw;

    public static String getFailName(String login) {
        return FAIL_NAME + login + ".txt";
    }

    public static void start(String login) {
        try {
            pw = new PrintWriter(new FileOutputStream(getFailName(login), true), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        if (pw != null) {
            pw.close();
        }
    }

    public static void appendMsg(String msg) {
        pw.println(msg);
    }

    public static String getLast100Msg(String login) {
        if (!Files.exists(Paths.get(getFailName(login)))) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try {
            List<String> historyList = Files.readAllLines(Paths.get(getFailName(login)));
            int startPosition = 0;
            if (historyList.size() > 100) {
                startPosition = historyList.size() - 100;
                for (int i = startPosition; i < historyList.size(); i++) {
                    sb.append(historyList.get(i)).append(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
