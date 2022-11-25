package workplaceManager;

import com.profesorfalken.wmi4java.WMI4Java;
import com.profesorfalken.wmi4java.WMIClass;
import workplaceManager.db.domain.OperationSystem;
import workplaceManager.db.domain.components.*;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class WMI {
    private static final String getManufacturerMotherboard = "BASEBOARD get Manufacturer";
    private static final String getModelMotherboard = "BASEBOARD get Product";

    private static final String getVendorOperationSystem = "OS get Caption";
    private static final String getVersionOperationSystem = "OS get Version";

    private static final String getModelProcessor = "CPU get Name";
    private static final String getNumberOfCoresProcessor = "CPU get NumberOfCores";
    private static final String getFrequencyProcessor = "CPU get MaxClockSpeed";
    private static final String getSocketProcessor = "CPU get SocketDesignation";

    private static final String getModelRam = "MEMORYCHIP get Manufacturer";
    private static final String getTypeRam = "MEMORYCHIP get SMBIOSMemoryType";
    private static final String getAmountRam = "MEMORYCHIP get Capacity";
    private static final String getFrequencyRam = "MEMORYCHIP get Speed";
    private static final String getDeviceLocatorRam = "MEMORYCHIP get DeviceLocator";

    private static final String getModelHardDrive = "DISKDRIVE where \"InterfaceType!='USB'\" get Model";

    private static final String getModelVideoCard = "path WIN32_VideoController get Name";

    public static OperationSystem getOperationSystem(String ip) throws IOException {
        OperationSystem operationSystem = new OperationSystem();
        if (!ping(ip)) {
            throw new IOException("Заданный узел не доступен");
        }

        operationSystem.setVendor(getParameter(ip, getVendorOperationSystem).get(0));
        operationSystem.setVersion(getParameter(ip, getVersionOperationSystem).get(0));

        return operationSystem;
    }

    public static MotherBoard getMotherBoard(String ip) throws IOException {
        MotherBoard motherBoard = new MotherBoard();
        if (!ping(ip)) {
            throw new IOException("Заданный узел недоступен");
        }

        motherBoard.setManufacturer(getParameter(ip, getManufacturerMotherboard).get(0));
        motherBoard.setModel(getParameter(ip, getModelMotherboard).get(0));

        return motherBoard;
    }

    public static List<Processor> getProcessorList(String ip) throws IOException {
        List<Processor> processorList = new ArrayList<>();
        if (!ping(ip)) {
            throw new IOException("Заданный узел недоступен");
        }

        List<String> parameterList = getParameter(ip, getModelProcessor);
        for (String parameter : parameterList) {
            Processor processor = new Processor();
            processor.setModel(parameter);
            processorList.add(processor);
        }

        parameterList = getParameter(ip, getNumberOfCoresProcessor);
        int count = 0;
        for (String parameter : parameterList) {
            processorList.get(count).setNumberOfCores(parameter);
            count++;
        }

        parameterList = getParameter(ip, getFrequencyProcessor);
        count = 0;
        for (String parameter : parameterList) {
            processorList.get(count).setFrequency(parameter);
            count++;
        }

        parameterList = getParameter(ip, getSocketProcessor);
        count = 0;
        for (String parameter : parameterList) {
            processorList.get(count).setSocket(parameter);
            count++;
        }

        return processorList;
    }

    public static List<Ram> getRamList(String ip) throws IOException {
        List<Ram> ramList = new ArrayList<>();
        if (!ping(ip)) {
            throw new IOException("Заданный узел недоступен");
        }

        List<String> parameterList = getParameter(ip, getModelRam);
        for (String parameter : parameterList) {
            Ram ram = new Ram();
            ram.setModel(parameter);
            ramList.add(ram);
        }

        parameterList = getParameter(ip, getTypeRam);
        int count = 0;
        for (String parameter : parameterList) {
            parameter = parameter.replaceAll("[^0-9]", "");
            if ("20".equals(parameter)) {
                ramList.get(count).setTypeRam(TypeRam.DDR);
            } else if ("21".equals(parameter)) {
                ramList.get(count).setTypeRam(TypeRam.DDR2);
            } else if ("22".equals(parameter)) {
                ramList.get(count).setTypeRam(TypeRam.DDR2_FB_DIMM);
            } else if ("24".equals(parameter)) {
                ramList.get(count).setTypeRam(TypeRam.DDR3);
            } else if ("26".equals(parameter)) {
                ramList.get(count).setTypeRam(TypeRam.DDR4);
            }
            count++;
        }

        parameterList = getParameter(ip, getAmountRam);
        count = 0;
        for (String parameter : parameterList) {
            parameter = parameter.replaceAll("[^0-9]", "");
            Long amount = Long.parseLong(parameter);
            amount = amount / 1024 / 1024 / 1024;
            ramList.get(count).setAmount(amount + "Gb");
            count++;
        }

        parameterList = getParameter(ip, getFrequencyRam);
        count = 0;
        for (String parameter : parameterList) {
            ramList.get(count).setFrequency(parameter);
            count++;
        }

        parameterList = getParameter(ip, getDeviceLocatorRam);
        count = 0;
        for (String parameter : parameterList) {
            ramList.get(count).setDeviceLocator(parameter);
            count++;
        }

        return ramList;
    }

    public static List<HardDrive> getHardDriveList(String ip) throws IOException {
        List<HardDrive> hardDriveList = new ArrayList<>();
        if (!ping(ip)) {
            throw new IOException("Заданный узел недоступен");
        }

        List<String> parameterList = getParameter(ip, getModelHardDrive);
        for (String parameter : parameterList) {
            HardDrive hardDrive = new HardDrive();
            hardDrive.setModel(parameter);
            hardDriveList.add(hardDrive);
        }

        return hardDriveList;
    }

    public static List<VideoCard> getVideoCard(String ip) throws IOException {
        List<VideoCard> videoCardList = new ArrayList<>();
        if (!ping(ip)) {
            throw new IOException("Заданный узел недоступен");
        }

        List<String> parameterList = getParameter(ip, getModelVideoCard);
        for (String parameter : parameterList) {
            VideoCard videoCard = new VideoCard();
            videoCard.setModel(parameter);
            videoCardList.add(videoCard);
        }

        return videoCardList;
    }


    private static List<String> getParameter(String ip, String command) throws IOException {
        List<String> listConfig = new ArrayList<>();
        Runtime rt = Runtime.getRuntime();
        String cmd = "cmd /c WMIC /node:\"" + ip + "\" " + command;
        Process pr = rt.exec(cmd);

        BufferedReader readerException = new BufferedReader(new InputStreamReader(pr.getErrorStream(), "Cp866"));
        String error = "";
        String line = "";
        while ((line = readerException.readLine()) != null) {
            if (line != null && !line.isEmpty()) {
                error += line + "\r\n";
            }
        }
        readerException.close();
        if (!error.isEmpty()) {
            pr.destroy();
            throw new IOException(error);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream(), "Cp866"));
        line = "";
        int count = 0;
        while ((line = reader.readLine()) != null) {
            if (count == 0) {
                count++;
                continue;
            }
            if (line != null && !line.isEmpty()) {
                //System.out.println(line);
                listConfig.add(line.trim());
            }
        }
        reader.close();
        pr.destroy();

        return listConfig;
    }

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


    public static void main(String[] args) {
        for(int i=2;i<=200;i++) {

            Date date = new Date();
            String ip = "10.140.40."+i;
            System.out.println(ip);
            if(!ping(ip)) {
                System.out.println("Нет пинга");
                continue;
            }
            try {
                OperationSystem operationSystem = getOperationSystem(ip);
                System.out.println(operationSystem);
            } catch (Exception exception) {
                System.out.println("OperationSystem: " + exception.getMessage());
            }
            try {
                MotherBoard motherBoard = getMotherBoard(ip);
                System.out.println(motherBoard);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
            try {
                List<Processor> processorList = getProcessorList(ip);
                for(Processor processor : processorList) {
                    System.out.println(processor);
                }
            } catch (Exception exception) {
                System.out.println("MotherBoard: " + exception.getMessage());
            }
            try {
                List<Ram> ramList = getRamList(ip);
                for(Ram ram : ramList) {
                    System.out.println(ram);
                }
            } catch (Exception exception) {
                System.out.println("ramList: " + exception.getMessage());
            }
            try {
                List<HardDrive> hardDriveList = getHardDriveList(ip);
                for(HardDrive hardDrive:hardDriveList) {
                    System.out.println(hardDrive);
                }
            } catch (Exception exception) {
                System.out.println("hardDriveList: " + exception.getMessage());
            }
            try {
                List<VideoCard> videoCardList = getVideoCard(ip);
                for(VideoCard videoCard:videoCardList) {
                    System.out.println(videoCard);
                }
            } catch (Exception exception) {
                System.out.println("videoCardList: " + exception.getMessage());
            }
            Date date1 = new Date();
            System.out.println(date1.getTime() - date.getTime());
        }




        List<Map<String, String>> videocontroller = WMI4Java.get().computerName("10.140.40.12").namespace("root/cimv2").getWMIObjectList(WMIClass.WIN32_VIDEOCONTROLLER);

        List<Map<String, String>> diskdrive6 = WMI4Java.get().computerName("10.140.40.99").namespace("root/cimv2").getWMIObjectList(WMIClass.WIN32_DISKDRIVE);
        List<Map<String, String>> videocontroller6 = WMI4Java.get().computerName("10.140.40.99").namespace("root/cimv2").getWMIObjectList(WMIClass.WIN32_VIDEOCONTROLLER);


        //Map<String, String> motherBoard=WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObject(WMIClass.WIN32_OPERATINGSYSTEM);
        //Map<String, String> motherBoard1=WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObject(WMIClass.CIM_OPERATINGSYSTEM);
        //Map<String, String> motherBoard2=WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObject(WMIClass.WIN32_OPERATINGSYSTEMQFE);
        //Map<String, String> baseBoard6=WMI4Java.get().computerName("10.140.40.6").namespace("root/cimv2").getWMIObject("Win32_BaseBoard");
        //Map<String, String> motherBoard6=WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObject("Win32_MotherboardDevice");
        System.out.println("asd");
    }

}
