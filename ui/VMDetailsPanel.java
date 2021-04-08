package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Backend.Controllers.VMController;
import Backend.Objects.Context;
import Backend.Objects.Device;
import Backend.Objects.Disk;
import Backend.Objects.VM;

public class VMDetailsPanel extends JPanel{

     
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Context context;
    private VM selectedVM;

    public VMDetailsPanel(Context context) {

        this.context = context;
        context.VMDetailsPanel = this;
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout());
        panel.add(headPanel(), BorderLayout.NORTH);

        panel.add(getVMDetailsPanel(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel headPanel(){
        JPanel panel = new JPanel();
        VM vm = context.getCurrentSelectedVM();
        String headerText;
        if(vm.isRunning()){
            headerText= "<html><b><font size=16>"+vm.getDomain()+"</font><font size=16 color=Green> Running</font></b></html>";
        }else{
            headerText= "<html><b><font size=16>"+vm.getDomain()+"</font><font size=16 color=Red> Shutdown</font></b></html>";
        }
        panel.add(new JLabel(headerText));

        return panel;
    }

    

    private JPanel getVMDetailsPanel(){

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel panel = new JPanel();

        VM vm = context.getCurrentSelectedVM();

        String vmDetails = "<html><table><tr><td><b>Property</b></td><td><b>Value</b></td></tr>";
        vmDetails+= "<tr><td>UUID:</td><td>"+vm.getUUID()+"</td></tr>";
        vmDetails+= "<tr><td>CPU's:</td><td>"+vm.getcpus()+"</td></tr>";
        vmDetails+= "<tr><td>Ram in GB:</td><td>"+Integer.parseInt(vm.getRam()) * 1.024E-6+"</td></tr>";


        String disksString = "";
        int amountOfDisks = 0;
        for (Device dev : vm.getDevices()) {
            if(dev.getClass().getName().equals("Backend.Objects.Disk")){
                amountOfDisks+=1;
                Disk disk = (Disk) dev;
                disksString+= disk.device + ":"+"<br>"+"&nbsp;Location: "+ disk.source + "<br>&nbsp;Type: "+ disk.driver + "<br><br>";

            }

        }
        vmDetails+= "<tr><td>Attached Disks ("+amountOfDisks+") :</td><td>"+disksString+"</td></tr>";

        String forwardedPorts = "";
        for (String port : vm.getForwardedPorts()) {
            forwardedPorts+= port + "<br>";
        }
        vmDetails+= "<tr><td>Forwarded Ports ("+vm.getForwardedPorts().size()+")<br> Protocol::External Port:Internal Port :</td><td>"+forwardedPorts+"</td></tr>";

        vmDetails+= "</table></html>";
        panel.add(new JLabel(vmDetails));

        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(getButtonPanel(), BorderLayout.NORTH);

        return mainPanel;
    }


    private JPanel getButtonPanel(){
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        panel.setLayout(new GridLayout(3,1));
        panel.setSize(300, 400);

        VM vm = context.getCurrentSelectedVM();
            
        JButton button = new JButton("Connect to VM");

        panel.add(button); // now add to jpanel

        button.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(!vm.isRunning()){
                JDialog d = new JDialog(context.mainJFrame, "Error");
  
                // create a label
                JLabel l = new JLabel("The vm is not running");
      
                d.add(l);
      
                // setsize of dialog
                d.setSize(150, 150);
                d.setLocationRelativeTo(null);
                // set visibility of dialog
                d.setVisible(true);
            }else{
                VMController VMC = new VMController(context);
                VMC.connectToVM(vm);
            }



        }
        });

        JButton startbutton = new JButton("Start VM");

        panel.add(startbutton); // now add to jpanel

        startbutton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(vm.isRunning()){
                JDialog d = new JDialog(context.mainJFrame, "Error");
  
                // create a label
                JLabel l = new JLabel("The vm is already running");
      
                d.add(l);
      
                // setsize of dialog
                d.setSize(150, 150);
                d.setLocationRelativeTo(null);
                // set visibility of dialog
                d.setVisible(true);
            }else{
                VMController VMC = new VMController(context);
                VMC.startVM(vm);
            }



        }
        });

        JButton stopbutton = new JButton("Shutdown VM");

        panel.add(stopbutton); // now add to jpanel

        stopbutton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(!vm.isRunning()){
                JDialog d = new JDialog(context.mainJFrame, "Error");
  
                // create a label
                JLabel l = new JLabel("The vm is already shutdown");
      
                d.add(l);
      
                // setsize of dialog
                d.setSize(150, 150);
                d.setLocationRelativeTo(null);
                // set visibility of dialog
                d.setVisible(true);
            }else{
                VMController VMC = new VMController(context);
                VMC.stopVM(vm);
            }



        }
        });

        return panel;
    }
    
    
}
