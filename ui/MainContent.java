package ui;

import javax.swing.JPanel;
import java.awt.*;
import Backend.Objects.Context;

public class MainContent extends JPanel{
    
    private Context context;

    public MainContent(Context context){

        this.context = context;
        context.mainContent = this;
    }

    public JPanel getContent(){

        this.setLayout(new BorderLayout());
        this.add(new ScrollableVMList(context).getPanel(), BorderLayout.WEST);
        this.add(new VMDetailsPanel(context).getPanel(), BorderLayout.CENTER);

        // this.add(new VMListTablePanel(context).getPanel(), BorderLayout.EAST);

        return this;
    }

    public void update(){
        this.removeAll();
        this.getContent();
        this.revalidate();

    }
    
}