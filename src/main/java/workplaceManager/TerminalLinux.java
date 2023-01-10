package workplaceManager;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class TerminalLinux {
    public static class CancelableReader extends BufferedReader {

        private final ExecutorService executor;
        private Future future;
        private boolean cancelTimeOut = false;

        public boolean isCancelTimeOut() {
            return cancelTimeOut;
        }

        public CancelableReader(Reader in) {
            super(in);
            executor = Executors.newSingleThreadExecutor();
        }

        @Override
        public String readLine() {

            future = executor.submit(super::readLine);

            try {
                return (String) future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (CancellationException e) {
                return null;
            }

            return null;

        }

        public void cancelRead() {
            cancelTimeOut = true;
            future.cancel(true);
        }

    }

    private static final String fileKeyStr = "id_rsa";
    private static final String fileKeyPubStr = "id_rsa.pub";

   /* public static String error = "";

    public static String getError() {
        return error;
    }*/

    public static boolean ping(String ip) {
        try {
            InetAddress geek = InetAddress.getByName(ip);
            if (geek.isReachable(5000)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception exception) {
            return false;
        }
    }

    public static String getOsName(String ip) throws Exception {
        String osName = "";

        if (!ping(ip)) {
            throw new Exception("Нет пинга");
        }

        String commandName = String.format("ssh -o \"StrictHostKeyChecking no\" %s@%s " +
                        "\"cat /etc/*release | grep NAME; " +
                        "exit\"",
                "administrator", ip);

        String commandVersion = String.format("ssh -o \"StrictHostKeyChecking no\" %s@%s " +
                        "\"cat /etc/*release | grep VERSION; " +
                        "exit\"",
                "administrator", ip);

        try {
           /* String homeDirectory = System.getProperty("user.home");
            String commandCmd = String.format("cmd.exe /c ping 192.168.0.105", homeDirectory);
            Process process;
            boolean isWindows = System.getProperty("os.name")
                    .toLowerCase().startsWith("windows");
            if (isWindows) {
                process = Runtime.getRuntime()
                        .exec(command);
            } else {
                process = Runtime.getRuntime()
                        .exec(String.format("sh -c ls %s", homeDirectory));
            }
            StreamGobbler streamGobbler =
                    new StreamGobbler(process.getInputStream(), System.out::println);
            Executors.newSingleThreadExecutor().submit(streamGobbler);
            int exitCode = process.waitFor();
            assert exitCode == 0;*/
        } catch (Exception exception) {

        }

        List<String> resultList = new ArrayList<>();
        startCommand(commandName, resultList);

        return osName;
    }

    private static void createFileSshKey(String key, String fileName) {
        String fileStr = System.getProperty("user.home") + "\\.ssh\\" + fileName;

        File newFile = new File(fileStr);
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(fileStr, false)) {
            writer.write(key);
            writer.flush();
        } catch (Exception exception) {

        }
    }

    private static void deleteFileSshKey(String fileName) {
        String fileStr = System.getProperty("user.home") + "\\.ssh\\" + fileName;

        File file = new File(fileStr);

        if (file.exists()) {
            file.delete();
        }
    }

    private static Thread startThreadTimeOut(CancelableReader reader, Process process) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5000);
                reader.cancelRead();
                process.destroy();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        return thread;
    }

    private static boolean startCommand(String command, List<String> resultList) throws Exception {
        boolean isSuccess = false;
        Process p;
        String error = "";
        try {
            String s;
            p = Runtime.getRuntime().exec(command);
            //BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "Cp866"));
            CancelableReader br = new CancelableReader(new InputStreamReader(p.getInputStream(), "Cp866"));
            /*new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    br.cancelRead();
                    p.destroy();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();*/
            Thread thread = startThreadTimeOut(br, p);

            //if (br.ready()) {
            while ((s = br.readLine()) != null) {
                if (!StringUtils.isEmpty(s)) {
                    resultList.add(s.trim());
                }
                System.out.println("line: " + s);
            }
            thread.interrupt();
            //} else {
            //    error += "Ошибка выполнения команды: " + command + System.lineSeparator();
            //}
            if (br.isCancelTimeOut()) {
                error += "Время ожидания выполнения команды истекло: " + command + System.lineSeparator();
            } else {
                //BufferedReader brError = new BufferedReader(new InputStreamReader(p.getErrorStream(), "Cp866"));
                CancelableReader brError = new CancelableReader(new InputStreamReader(p.getErrorStream(), "Cp866"));

                startThreadTimeOut(brError, p);
                String err = "";

                //if (brError.ready()) {
                    while ((err = brError.readLine()) != null) {
                        error += err;
                    }
                //}
                if (brError.isCancelTimeOut()) {
                    error += "Время ожидания выполнения команды истекло: " + command + System.lineSeparator();
                }

                if (StringUtils.isEmpty(error)) {
                    p.waitFor();

                    //System.out.println("exit: " + p.exitValue());
                    if (p.exitValue() == 0) {
                        isSuccess = true;
                    }
                }
                p.destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
            error += e.toString();
        }

        if (!StringUtils.isEmpty(error) && !isSuccess) {
            throw new Exception(error);
        }

        return isSuccess;
    }

    public static void main(String[] args) {
        try {
            TerminalLinux.getOsName("10.140.40.250");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
