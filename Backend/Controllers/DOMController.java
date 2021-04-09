package Backend.Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Backend.Objects.Context;

public class DOMController {

    public static void defineDomain(String domainLocation, String domainName, Context context){

        try {
            Runtime.getRuntime().exec("virsh define "+domainLocation+domainName+".xml");
            System.out.println("virsh define "+domainLocation+domainName+".xml");

        } catch (IOException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }


    }

    public static void undefineDomain( String domainName, Context context){

        try {
            Runtime.getRuntime().exec("virsh undefine "+domainName);
            context.updateCurrentSelectedVM(context.getVMList().get(0));

        } catch (IOException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }
        context.refresh();



    }

    public static void createDomain(String domainLocation, String domainName, Context context){

        try {
            Process process = Runtime.getRuntime().exec("virsh create "+domainLocation+domainName+".xml");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";

            while ((line = reader.readLine()) != null) {

                System.out.println(line);
                
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }


    }
}
