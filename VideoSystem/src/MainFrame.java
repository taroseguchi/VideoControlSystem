import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class MainFrame extends JFrame{

  JTabbedPane tabbedpane;
  VideoPanel tabPanel1;
  MemberPanel tabPanel2;
  RentPanel tabPanel3;

  public static void main(String[] args){
    MainFrame frame = new MainFrame();

    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frame.setBounds(10, 10, 490, 600);
    frame.setTitle("Rental Video Control System");
    frame.setVisible(true);
    frame.setResizable(false);
  }

  public MainFrame(){
    tabbedpane = new JTabbedPane();
    tabPanel1 = new VideoPanel();
    tabPanel2 = new MemberPanel();
    tabPanel3 = new RentPanel();
    tabPanel1.getRentPanel(tabPanel3);
    tabPanel2.getRentPanel(tabPanel3);
    tabPanel3.getVideoPanel(tabPanel1);
    tabPanel3.getMemberPanel(tabPanel2);
    
    tabbedpane.addTab("Video", tabPanel1);
    tabbedpane.addTab("Member", tabPanel2);
    tabbedpane.addTab("Rent", tabPanel3);
    
	JButton addButton = new JButton("add");
	JButton deleteButton = new JButton("delete");
	JButton editButton = new JButton("edit");
	JButton quitButton = new JButton("quit");

    JPanel actionPanel = new JPanel();
    actionPanel.add(addButton);
    actionPanel.add(deleteButton);
    actionPanel.add(editButton);
    actionPanel.add(quitButton);
    addButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
        	switch(tabbedpane.getSelectedIndex()){
        	case 0:
        		tabPanel1.AddRow();
        		break;
        	case 1:
        		tabPanel2.AddRow();
        		break;
        	case 2:
        		tabPanel3.AddRow();
        		break;
        	}
        }
    });
    deleteButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
        	switch(tabbedpane.getSelectedIndex()){
        	case 0:
        		tabPanel1.DeleteRow();
        		break;
        	case 1:
        		tabPanel2.DeleteRow();
        		break;
        	case 2:
        		tabPanel3.DeleteRow();
        		break;
        	}
        }
    });
    editButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
        	switch(tabbedpane.getSelectedIndex()){
        	case 0:
        		tabPanel1.EditRow();
        		break;
        	case 1:
        		tabPanel2.EditRow();
        		break;
//        	case 2:
//        		tabPanel3.EditRow();
//        		break;
        	}
        }
    });
    quitButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
        	tabPanel1.SaveVideo();
        	tabPanel2.SaveMember();
        	tabPanel3.SaveRent();
        	System.exit(0);
        }
    });
    getContentPane().add(tabbedpane, BorderLayout.CENTER);
    getContentPane().add(actionPanel, BorderLayout.PAGE_END);
  }

}