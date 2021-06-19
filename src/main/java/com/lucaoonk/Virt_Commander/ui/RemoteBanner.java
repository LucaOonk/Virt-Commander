package com.lucaoonk.Virt_Commander.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;

import com.lucaoonk.Virt_Commander.Backend.Objects.Context;

public class RemoteBanner extends JPanel{

    private Context context;
 
    public RemoteBanner(Context context){
        this.context = context;
    }
    
    public JPanel getPanel(){

        this.context = context;
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(getBanner(), BorderLayout.CENTER);

        panel.add(topLeftPanel(), BorderLayout.WEST);

        

        return panel;
    }

    private JPanel getBanner(){
        JPanel panel = new JPanel();
        panel.add(new JLabel("<html><b>Workspace: Remote ("+context.remoteAddress+")</b></html>"));

        return panel;
    }

    private JPanel topLeftPanel(){
        JPanel panel = new JPanel();
        panel.add(new JLabel("<html><b>Auto refresh rate ("+context.autoRefreshRate+" Sec)</b></html>"));

        return panel;
    }
}
