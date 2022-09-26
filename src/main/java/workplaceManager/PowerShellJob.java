package workplaceManager;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellNotAvailableException;
import com.profesorfalken.jpowershell.PowerShellResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class PowerShellJob {
    public static void main(String[] args) {
        //Creates PowerShell session (we can execute several commands in the same session)
        try (PowerShell powerShell = PowerShell.openSession()) {
            //Execute a command in PowerShell session
            PowerShellResponse response = powerShell.executeCommand("Get-NetIPConfiguration");

            //Print results
            System.out.println("List Processes:" + response.getCommandOutput());

            powerShell.executeCommand("$Username='admin'");
            powerShell.executeCommand("$Password='nextnext'");
            powerShell.executeCommand("$pass=ConvertTo-SecureString -AsPlainText $Password -Force");
            powerShell.executeCommand("$Cred=New-Object System.Management.Automation.PSCredential -ArgumentList $Username,$pass");

            //Execute another command in the same PowerShell session
            //response = powerShell.executeCommand("$cred = Get-Credential");
            response = powerShell.executeCommand("Invoke-Command -Computer 192.168.0.102 -ScriptBlock {Get-NetIPConfiguration} -Credential $Cred");
           // response = powerShell.executeCommand("Enter-PSSession -Session $s");
           // response = powerShell.executeCommand("Get-NetIPConfiguration");
            //Print results
            System.out.println("BIOS information:" + response.getCommandOutput());
        } catch(PowerShellNotAvailableException ex) {
            //Handle error when PowerShell is not available in the system
            //Maybe try in another way?
        }
    }
}
