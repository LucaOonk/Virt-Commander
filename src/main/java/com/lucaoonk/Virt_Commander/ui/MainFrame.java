package com.lucaoonk.Virt_Commander.ui;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.desktop.SystemEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import com.lucaoonk.Virt_Commander.Backend.AutoRefreshThread;
import com.lucaoonk.Virt_Commander.Backend.Objects.*;
import com.lucaoonk.Virt_Commander.Backend.Processors.Local.VMDOMProcessor;
import com.lucaoonk.Virt_Commander.Backend.Processors.Local.VMListProcessor;
import com.lucaoonk.Virt_Commander.Backend.Processors.Remote.RemoteVMListProcessor;
import com.lucaoonk.Virt_Commander.UpdateChecker.UpdateChecker;


public class MainFrame extends JFrame implements ActionListener, SystemEventListener{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public JFrame mainFrame;

    private Context context;
    private JButton localButton;
    private JButton remoteButton;
    private JTextField remoteAddress;


    private SwingWorker autoRefresh;

    public MainFrame() {


    }

    public static void exitProgram(){
        System.exit(0);
    }

    public void init() throws Exception {
        this.context = new Context();
        this.mainFrame = new JFrame();
        this.context.mainJFrame = mainFrame;
        

        if(context.local){
            localInit();
        }
    }

    private void localInit() throws Exception{
        ApplicationSettings.readSettingsFile(context);

        if(context.local){
            VMListProcessor t = new VMListProcessor(context);
            t.runCommand();

            for (VM vm : context.getVMList()) {
                VMDOMProcessor.getDetails(vm);
            }
        }else{
            RemoteVMListProcessor t = new RemoteVMListProcessor(context);
            t.runCommand();
        }


        if(context.checkForUpdates){

            UpdateChecker checker = new UpdateChecker(context);
            checker.isNewewVersionAvailable();
        }

        try {



        } catch (Exception e) {
            
            JDialog dialog = new JDialog();
            JPanel panel = new JPanel();
            dialog.setTitle("An error occured!");
            JLabel label = new JLabel("<html><b>Make sure the dependencies are installed!</b></html>");
            panel.setBorder(new EmptyBorder(10,10,10,10));
            panel.add(label);
            dialog.add(panel);
            dialog.setSize(300, 150);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            dialog.setAlwaysOnTop(true);
            dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            throw e;
        }
    }


    public void showMainFrame() throws InterruptedException {
        this.mainFrame = new JFrame();

        mainFrame.getContentPane().setBackground(java.awt.Color.black);

        mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainFrame.setTitle("Virt Commander");

        // if(context.local){
        //     mainFrame.setTitle("Virt Commander | Local");

        // }else{
        //     mainFrame.setTitle("Virt Commander | Remote: "+context.remoteAddress);

        // }
        mainFrame.setSize(new Dimension(1100, 400));

        JMenuBar menuBar = new JMenuBar();

        JMenu settingsMenu = new JMenu("Settings");
        JMenuItem settingsMenuItem = new JMenuItem("Application Settings");

        settingsMenuItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                ApplicationSettingsView settings = new ApplicationSettingsView();
                settings.show(context);

            }

        });
        settingsMenu.add(settingsMenuItem);
        menuBar.add(settingsMenu);

        if(context.updateAvailable){
            
            JMenu updateMenu = new JMenu("New Version Available");
            JMenuItem updateMenuItem = new JMenuItem("Get Update");
            updateMenuItem.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Desktop desktop = java.awt.Desktop.getDesktop();
                        URI oURL = new URI("https://github.com/LucaOonk/Virt-Commander/releases/latest");
                        desktop.browse(oURL);
                    } catch (Exception f) {
                        f.printStackTrace();
                    }
            
                }});

            updateMenu.add(updateMenuItem);
            menuBar.add(updateMenu);

            JDialog dialog = new JDialog();
            JPanel panel = new JPanel();
            dialog.setTitle("A new update is available");
            JLabel label = new JLabel("<html><b>"+context.latestVersion+" is out! Current version is: "+context.getVersion()+"</b></html>");
            panel.setBorder(new EmptyBorder(10,10,10,10));
            panel.add(label);
            
            JButton getUpdateButton = new JButton("Get Update");
            getUpdateButton.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Desktop desktop = java.awt.Desktop.getDesktop();
                        URI oURL = new URI("https://github.com/LucaOonk/Virt-Commander/releases/latest");
                        desktop.browse(oURL);
                    } catch (Exception f) {
                        f.printStackTrace();
                    }
            
                }});

            panel.add(getUpdateButton);

            dialog.add(panel);
            dialog.setSize(300, 150);
            dialog.setLocationRelativeTo(null);
            dialog.setResizable(false);
            dialog.setVisible(true);
            dialog.setAlwaysOnTop(true);
            dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);            
         
        }


        mainFrame.setJMenuBar(menuBar);

        // this.portalpage = new MainPortalPage(this.user, this);
        // mainFrame.setJMenuBar(menu());
        mainFrame.add(new MainContent(context).getContent());

        if(context.autoSizeWindow){
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Integer windowHeight = (screenSize.height / 2);
            Integer windowWidth = (screenSize.width / 2);
            mainFrame.setSize(windowWidth, windowHeight);

        }else{
            mainFrame.setSize(context.windowWidth, context.windowHeight);

        }
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        new AutoRefreshThread(context);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

        if(e.getSource().equals(localButton)){
            System.out.println("Chose local");
            context.local = true;
            context.remoteOrLocal = true;
            try {
                localInit();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        if(e.getSource().equals(remoteButton)){
            System.out.println("Chose remote");
            context.local = false;
            context.remoteOrLocal = true;
            context.remoteAddress = remoteAddress.getText();

        }
        
    }

}