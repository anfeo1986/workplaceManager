package workplaceManager;

import com.profesorfalken.wmi4java.WMI4Java;
import com.profesorfalken.wmi4java.WMIClass;
import workplaceManager.db.domain.Computer;
import workplaceManager.db.domain.OperationSystem;
import workplaceManager.db.domain.TypeOS;
import workplaceManager.db.domain.components.MotherBoard;

import java.util.List;
import java.util.Map;

public class WMI {

    public static OperationSystem getOperationSystem(String ip) {
        OperationSystem operationSystem = new OperationSystem();
        Map<String, String> value = WMI4Java.get().computerName(ip).namespace("root/cimv2").getWMIObject(WMIClass.WIN32_OPERATINGSYSTEM);
        try {
            operationSystem.setVendor(value.get("Caption"));
            if (operationSystem.getVendor() != null) {
                operationSystem.setTypeOS(operationSystem.getVendor().contains("Windows") ? TypeOS.windows : TypeOS.linux);
            }
            operationSystem.setVersion(value.get("Version"));
        } catch (Exception exception) {
        }
        return operationSystem;
    }

    public static MotherBoard getMotherBoard(String ip) {
        MotherBoard motherBoard = new MotherBoard();
        Map<String, String> value = WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObject(WMIClass.WIN32_BASEBOARD);
        try {
            motherBoard.setManufacturer(value.get("Manufacturer"));
            motherBoard.setModel(value.get("Product"));

        }catch (Exception exception) {

        }

        return motherBoard;
    }

    public static void main(String[] args) {
        //Map<String, String> baseBoard = WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObject("Win32_BaseBoard");
        //Map<String, String> motherBoard = WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObject(WMIClass.WIN32_OPERATINGSYSTEM);
        List<Map<String, String>> PHYSICALMEMORY = WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObjectList(WMIClass.WIN32_PHYSICALMEMORY);
        List<Map<String, String>> PHYSICALMEMORY1 = WMI4Java.get().computerName("10.140.40.6").namespace("root/cimv2").getWMIObjectList(WMIClass.WIN32_PHYSICALMEMORY);
        Map<String, String> SMBIOSMEMORY = WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObject(WMIClass.WIN32_SMBIOSMEMORY);
        Map<String, String> SMBIOSMEMORY6 = WMI4Java.get().computerName("10.140.40.6").namespace("root/cimv2").getWMIObject(WMIClass.WIN32_SMBIOSMEMORY);
        Map<String, String> PHYSICALMEMORYARRAY = WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObject(WMIClass.WIN32_PHYSICALMEMORYARRAY);
        Map<String, String> PHYSICALMEMORYLOCATION = WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObject(WMIClass.WIN32_PHYSICALMEMORYLOCATION);
        Map<String, String> MEMORYDEVICE = WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObject(WMIClass.WIN32_MEMORYDEVICE);
        Map<String, String> logicaldisk = WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObject(WMIClass.WIN32_LOGICALDISK);
        List<Map<String, String>> computerSystem = WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObjectList(WMIClass.WIN32_COMPUTERSYSTEM);

        Map<String, String> processor = WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObject(WMIClass.WIN32_SMBIOSMEMORY);

        OperationSystem operationSystem = WMI.getOperationSystem("10.140.40.5");
        operationSystem = WMI.getOperationSystem("10.140.40.6");


        //Map<String, String> motherBoard=WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObject(WMIClass.WIN32_OPERATINGSYSTEM);
        //Map<String, String> motherBoard1=WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObject(WMIClass.CIM_OPERATINGSYSTEM);
        //Map<String, String> motherBoard2=WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObject(WMIClass.WIN32_OPERATINGSYSTEMQFE);
        //Map<String, String> baseBoard6=WMI4Java.get().computerName("10.140.40.6").namespace("root/cimv2").getWMIObject("Win32_BaseBoard");
        //Map<String, String> motherBoard6=WMI4Java.get().computerName(".").namespace("root/cimv2").getWMIObject("Win32_MotherboardDevice");
        System.out.println("asd");
    }

}
