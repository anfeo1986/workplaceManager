package workplaceManager;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class TerminalLinux {
    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            try {
                new BufferedReader(new InputStreamReader(inputStream, "Cp866")).lines()
                        .forEach(consumer);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
    public static String getOsName(String ip) {
        String osName = "";

        String command = String.format("ssh -o \"StrictHostKeyChecking no\" %s@%s " +
                        "\"cat /etc/*release | grep VERSION; " +
                        "exit\"",
                "administrator", ip);

        try {
            String homeDirectory = System.getProperty("user.home");
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
            assert exitCode == 0;
        } catch (Exception exception) {

        }


        /*String command = String.format("ssh -o \"StrictHostKeyChecking no\" %s@%s " +
                        "\"cat /etc/*release | grep VERSION; " +
                        "exit\"",
                "administrator", ip);*/
        startCommand(command);

        return osName;
    }

    private static List<String> getParameter(String ip, String command) {
        List<String> listConfig = new ArrayList<>();

        return listConfig;
    }

    private static boolean startCommand(String command) {
        boolean isSuccess = false;
        Process p;
        String error = "";
        String key = "-----BEGIN OPENSSH PRIVATE KEY-----\n" +
                "b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAABlwAAAAdzc2gtcn\n" +
                "NhAAAAAwEAAQAAAYEApQnCPubupnznyNuq7IObcGpKg7KlgjFIwvxG8WlDPf1K6/t3SSsT\n" +
                "biiFqXUkxrX1EMTkat2RmwSfk/M8B/Cuc5hEV8Ij3GDmZYy3vb3ayoHx283JAWth1I5jsa\n" +
                "17/80kOM3JytHHf0sMUw/GwQTQuqXueVwMWGO7aDP+PiIjqJZofmTT+7+RcHfhDjqv75ll\n" +
                "rgXfQsLDRuHM0D56lm1evnP9p/0xZ1k7upmvGRRUM74itbD4WQ4HRQdVxyW07lrgnYwiKJ\n" +
                "DzZioB+jNPkf+zoY3eexP77etRMvPSJjBGVGFlWgCe3y9R8uZu4klN/BBhqb0qqyRTR36v\n" +
                "QmBsYAgaSUv2zePjcFrSYJSdgua8BAHYsNAgJL+ydx+eKjUEd8u9AT+195Y1d2vNaJES+g\n" +
                "ZfiqXzrjUaIVnbtLdGaHMCwO3s4SeDqxgEUnTNWVx6U8SI3bb4sLLBUNzd+kBTLizuy+GF\n" +
                "nBZ0E/EhcSllmF18X5NiKyP6FFiNnB5mUiwsocbFAAAFmAGHXYIBh12CAAAAB3NzaC1yc2\n" +
                "EAAAGBAKUJwj7m7qZ858jbquyDm3BqSoOypYIxSML8RvFpQz39Suv7d0krE24ohal1JMa1\n" +
                "9RDE5GrdkZsEn5PzPAfwrnOYRFfCI9xg5mWMt7292sqB8dvNyQFrYdSOY7Gte//NJDjNyc\n" +
                "rRx39LDFMPxsEE0Lql7nlcDFhju2gz/j4iI6iWaH5k0/u/kXB34Q46r++ZZa4F30LCw0bh\n" +
                "zNA+epZtXr5z/af9MWdZO7qZrxkUVDO+IrWw+FkOB0UHVccltO5a4J2MIiiQ82YqAfozT5\n" +
                "H/s6GN3nsT++3rUTLz0iYwRlRhZVoAnt8vUfLmbuJJTfwQYam9KqskU0d+r0JgbGAIGklL\n" +
                "9s3j43Ba0mCUnYLmvAQB2LDQICS/sncfnio1BHfLvQE/tfeWNXdrzWiREvoGX4ql8641Gi\n" +
                "FZ27S3RmhzAsDt7OEng6sYBFJ0zVlcelPEiN22+LCywVDc3fpAUy4s7svhhZwWdBPxIXEp\n" +
                "ZZhdfF+TYisj+hRYjZweZlIsLKHGxQAAAAMBAAEAAAGARVPGHzpPAezySPPN5zCZuS1NQ+\n" +
                "llkT02vYHQI1T+hCnEsUCfKUNJFDs9eqPU4QxKd9LdyO5uXdohVrsUeplmDvTlsJ/DBK5H\n" +
                "2ly0SqZYJD1V5emaCFidbfaqdlbUAe6gyqPXorVgwl46PSIhca+eJunRNXWvUeoDLZC5uk\n" +
                "wJAqa8GmwsiFI+vCn/hkSs509e4S1iTd8WvM3ZhZjZUht7czGyWkEH4cwgRRCN91EM1feH\n" +
                "CR/gEzjLYPnl1JRIXuAZUdnUqdpniwOr/WqDcwAIMOVK4L1JCTLa4bztbJppUKOADJ9Rgo\n" +
                "y9Fgyjw0/Sm20SIVgkWJ6hlZXsX/D8Mt+FdaAKTi9Rnd5w5djScEMQBAtq4CMbR2uOFF1p\n" +
                "I29g4iwqxlPnWmwYsTTNg7o6bV3+SqUsZwRT/9YTLdSqqpauUgF85/iua5sZj69DZjNAZz\n" +
                "39nrs36oL5Wy/ZINtulwvXvOacgk62qSTdPY1mVBv9LzsqY6zj9yJxvK3Deeu8eqbBAAAA\n" +
                "wQCh62y4i6UVdwlcECUALIp4AThi+6LZEzbUNyoGaS9QzDaYD9udzjsp5UBYaQ9q+K1C+C\n" +
                "Lbq9cwXiiHNxyK0H2QuboO8RoKeDZZp3T+elTcHcr44aTU6yieNIPCqCNvKFduyS+8U3jB\n" +
                "CRmlv9VGxE9mLrpNbCHYYddEJ8uslM+56KCJg2XDBwW8Hl0jcAluNwBLYs1W9hpI7Xq31a\n" +
                "S/TuIIS29ih/ClM50x0065vgT0KR+f/VoaXyqs/oQx/nnHnRIAAADBANnW1/EquHPe19aq\n" +
                "eiHn43P9bIjlf+oakSDtgqaEQ3g+APckvHWpPwp78Yul5DfViQsUKeMW6oD2vd9dQ2fG1x\n" +
                "qwktyYVir3RE6rnvJ0ibZP5Sib80LzraxI2BcxcbVQOby1pSDfPq9M15YX2OM8eBcdLhRZ\n" +
                "NFYJ8i4I7AeoGIl7/m6CcZToT0krv8v6WEoInl90CPfiXPuqgvLOmHffSfRl8cpw50v20F\n" +
                "g0WDJOv2j2hZirWqzm4PIFmd+a/yICnQAAAMEAwfMC88R4raMym/0GfCr5/vs3u/xdfcNI\n" +
                "3vmnNfOO2uc4t5jIML32ww4r4U7eLX83VcD/SVq8x7nSpU0anQ2v+BUmxaF52AOesZzjn9\n" +
                "mK2zr41iqVkI24bh1WuIfIYbBzwKMCLjkQglPm2I60vvJp2pJSStRpbbVRLEv3LxFkeQXj\n" +
                "RojRGltTtMmXbGc5IcvKYvO0D/ldQ5xJR7ae/y814TmKsoolbxpWupElQ6rH8X68jq8UpR\n" +
                "xgXY12NkexmKhJAAAAHWFkbWluaXN0cmF0b3JAREVTS1RPUC1JUDY1NDBWAQIDBAU=\n" +
                "-----END OPENSSH PRIVATE KEY-----\n";
        String key_pub = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQClCcI+5u6mfOfI26rsg5twakqDsqWCMUjC/EbxaUM9/Urr+3dJKxNuKIWpdSTGtfUQxORq3ZGbBJ+T8zwH8K5zmERXwiPcYOZljLe9vdrKgfHbzckBa2HUjmOxrXv/zSQ4zcnK0cd/SwxTD8bBBNC6pe55XAxYY7toM/4+IiOolmh+ZNP7v5Fwd+EOOq/vmWWuBd9CwsNG4czQPnqWbV6+c/2n/TFnWTu6ma8ZFFQzviK1sPhZDgdFB1XHJbTuWuCdjCIokPNmKgH6M0+R/7Ohjd57E/vt61Ey89ImMEZUYWVaAJ7fL1Hy5m7iSU38EGGpvSqrJFNHfq9CYGxgCBpJS/bN4+NwWtJglJ2C5rwEAdiw0CAkv7J3H54qNQR3y70BP7X3ljV3a81okRL6Bl+KpfOuNRohWdu0t0ZocwLA7ezhJ4OrGARSdM1ZXHpTxIjdtviwssFQ3N36QFMuLO7L4YWcFnQT8SFxKWWYXXxfk2IrI/oUWI2cHmZSLCyhxsU= administrator@DESKTOP-IP6540V\n";

        String path = System.getProperty("user.home");
        //String fileStr = "C://Users//administrator//.ssh//id_rsa";
        String fileStr = path + "\\.ssh\\id_rsa";
        String filePubStr = path + "\\.ssh\\id_rsa.pub";
        File newFile = new File(fileStr);
        File newFilePub = new File(filePubStr);
        try {
            newFile.createNewFile();
            newFilePub.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(fileStr, false)) {
            writer.write(key);
            writer.flush();
        } catch (Exception exception) {

        }
        try (FileWriter writer = new FileWriter(filePubStr, false)) {
            writer.write(key_pub);
            writer.flush();
        } catch (Exception exception) {

        }
        try {
            String s;
            p = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);

            BufferedReader brError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            String err = "";

            while ((err = brError.readLine()) != null) {
                error += err;
                System.out.println("error: " + err);
            }

            p.waitFor();

            System.out.println("exit: " + p.exitValue());
            if (p.exitValue() == 0) {
                isSuccess = true;
            }
            p.destroy();
        } catch (Exception e) {
            e.printStackTrace();
            error = e.toString();
        }
        newFile.delete();
        newFilePub.delete();
        return isSuccess;
    }

    public static void main(String[] args) {
        TerminalLinux.getOsName("192.168.0.104");
    }
}
