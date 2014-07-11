import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class MemberPanel extends JPanel{
	
	private String[] columnNames = {"ID", "NAME"};
	JTextField idtf = new JTextField();
	JTextField nametf = new JTextField();
	JButton showbt = new JButton("show");
	DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0){
		@Override public boolean isCellEditable(int row, int column) {return false;};
	};
	JTable table = new JTable(tableModel);
	JScrollPane sp = new JScrollPane(table);
	JPanel delp = new JPanel();
	JLabel dellb = new JLabel();
	JButton yesbt = new JButton("Yes");
	JButton nobt = new JButton("No");
	JPanel addp = new JPanel();
	JLabel addlb = new JLabel();
	JPanel edip = new JPanel();
	JLabel edilb = new JLabel("Are you sure you want to change this cell?");
	JButton yes1bt = new JButton("Yes");
	JButton no1bt = new JButton("No");
	RentPanel rentp;
	
	public MemberPanel(){
		idtf.setEditable(false);
		JLabel idlb = new JLabel("ID");
		idtf.setColumns(3);
		JLabel namelb = new JLabel("NAME");
		nametf.setColumns(10);
		this.add(sp);
		this.add(idlb);
		this.add(idtf);
		this.add(namelb);
		this.add(nametf);
		this.add(showbt);
		delp.add(dellb);
		delp.add(yesbt);
		delp.add(nobt);
		this.add(delp);
		delp.setVisible(false);
		addp.add(addlb);
		this.add(addp);
		addp.setVisible(false);
		edip.add(edilb);
		edip.add(yes1bt);
		edip.add(no1bt);
		this.add(edip);
		edip.setVisible(false);
		
		try{
			BufferedReader br = new BufferedReader(new FileReader("memberdata.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				String[] memberdata = new String[2];
				int i = 0;
				while(st.hasMoreTokens()) {
					String next = st.nextToken();
					memberdata[i] = next;
					i++;
				}
				tableModel.addRow(memberdata);
			}
			br.close();
		}
		catch(IOException e){
			System.out.println(e);
		}
		
		int rownum = tableModel.getRowCount();
		if(rownum != 0){
			String lastIdStr = tableModel.getValueAt(rownum - 1, 0).toString();
			int lid = Integer.parseInt(lastIdStr);
			idtf.setText("00" + (lid + 1));
		}else{
			idtf.setText("001");
		}
		
		showbt.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	        	int row = table.getSelectedRow();
	        	ArrayList<String> vtitlelist = rentp.MemberToVideo(tableModel.getValueAt(row, 0).toString());
	        	MemSubFrame frame = new MemSubFrame(vtitlelist);
			    frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			    frame.setBounds(200, 200, 220, 150);
			    frame.setTitle(tableModel.getValueAt(row, 1).toString());
			    frame.setVisible(true);
			    frame.setResizable(false);
	        }
	    });
	}
	
	void AddRow(){
		edip.setVisible(false);
		String[] adddata = new String[2];
		adddata[0] = idtf.getText();
		adddata[1] = nametf.getText();
		int addid = Integer.parseInt(idtf.getText());
		int lastid;
		int rownum = tableModel.getRowCount();
		String lastIdStr = tableModel.getValueAt(rownum - 1, 0).toString();
		lastid = Integer.parseInt(lastIdStr);
		if(addid > lastid){
			tableModel.addRow(adddata);
			lastid++;
		}
		if(rownum != 0){
			idtf.setText("00" + (lastid + 1));
		}else{
			idtf.setText("001");
		}
		nametf.setText("");
	}
	
	void DeleteRow(){
		yesbt.setVisible(true);
		nobt.setVisible(true);
		edip.setVisible(false);
		addp.setVisible(false);
		dellb.setText("Are you sure you want to delete this row?");
		delp.setVisible(true);
		yesbt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
	        	int row = table.getSelectedRow();
	        	boolean flag = false; 
	        	for (String mid:rentp.getMemberId()){
	    			if(mid.equals(tableModel.getValueAt(row, 0).toString())){
	    				dellb.setText("MEMBER_ID:" + tableModel.getValueAt(row, 0).toString()
	    										+ " is renting videos now.");
	    				yesbt.setVisible(false);
	    				nobt.setVisible(false);
	    				flag = true;
	    			}
	        	}
	        	if(!flag){
	        		tableModel.removeRow(row);
	        		delp.setVisible(false);
	        	}
	        }
	     });
		nobt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				delp.setVisible(false);
		    }
	     });
	}
	
	void EditRow(){
		delp.setVisible(false);
		addp.setVisible(false);
		edip.setVisible(true);
		int row = table.getSelectedRow();
		idtf.setText(tableModel.getValueAt(row, 0).toString());
		nametf.setText(tableModel.getValueAt(row, 1).toString());
		yes1bt.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	        	tableModel.setValueAt(idtf.getText(), table.getSelectedRow(), 0);
	        	tableModel.setValueAt(nametf.getText(), table.getSelectedRow(), 1);
	        	edip.setVisible(false);
	        }
	    });
		no1bt.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	        	edip.setVisible(false);
	        }
	    });
	}
	
	boolean RentVideo(String mid){
		for (int i = 0; i < tableModel.getRowCount(); i++){
			if(mid.equals(tableModel.getValueAt(i, 0).toString())){
				return true;
			}
		}
		return false;
	}
	
	void getRentPanel(RentPanel rp){
		rentp = rp;
	}
	
	void SaveMember(){
		try{
			PrintWriter out =
					new PrintWriter(new BufferedWriter(new FileWriter("memberdata.txt")));
			int rownum = tableModel.getRowCount();
			int columnnum = tableModel.getColumnCount();
			for (int i = 0; i < rownum; i++){
				for (int j = 0; j < columnnum; j++){
					out.print( tableModel.getValueAt(i, j).toString() + " ");
				}
				out.println();
			}
			out.close();
		}
		catch(IOException e){
			System.out.println(e);
		}
	}
}
	