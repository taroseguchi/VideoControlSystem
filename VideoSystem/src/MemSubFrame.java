import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class MemSubFrame extends JFrame {
	 boolean close = false;
	 JPanel vdp = new JPanel();
	 JLabel lb = new JLabel("VIDEO TITLE");
	 JScrollPane sp = new JScrollPane();
	 DefaultListModel model = new DefaultListModel();
	 JList vdl = new JList(model);
	 
	 public MemSubFrame(ArrayList<String> vtitlelist){
		 ArrayList<String> vtlist = vtitlelist;
		 for(String vtitle:vtlist){
			 model.addElement(vtitle);
		 }
		 sp.getViewport().setView(vdl);
		 sp.setPreferredSize(new Dimension(200, 80));
		 vdp.add(lb);
		 vdp.add(sp);
		 getContentPane().add(vdp);
	 }
}
