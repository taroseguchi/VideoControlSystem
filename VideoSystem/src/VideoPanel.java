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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class VideoPanel extends JPanel{
	
	private String[] columnNames = {"ID", "TITLE", "STOCK"};
	JTextField idtf = new JTextField();
	JTextField titletf = new JTextField();
	JTextField stocktf = new JTextField();
	DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0){
		@Override public boolean isCellEditable(int row, int column) {return false;};
	};
	JTable table = new JTable(tableModel);
	JScrollPane sp = new JScrollPane(table);
	JPanel addp = new JPanel();
	JLabel addlb = new JLabel("The same video already exists.");
	JPanel delp = new JPanel();
	JLabel dellb = new JLabel();
	JButton yesbt = new JButton("Yes");
	JButton nobt = new JButton("No");
	JPanel edip = new JPanel();
	JLabel edilb = new JLabel();
	JButton yes1bt = new JButton("Yes");
	JButton no1bt = new JButton("No");
	RentPanel rentp;
	public boolean samevideo =false;
	
	VideoPanel(){
		idtf.setEditable(false);
		JLabel idlb = new JLabel("ID");
		idtf.setColumns(3);
		JLabel titlelb = new JLabel("TITLE");
		titletf.setColumns(10);
		JLabel stocklb = new JLabel("STOCK");
		stocktf.setColumns(3);
		this.add(sp);
		this.add(idlb);
		this.add(idtf);
		this.add(titlelb);
		this.add(titletf);
		this.add(stocklb);
		this.add(stocktf);
		addp.add(addlb);
		this.add(addp);
		addp.setVisible(false);
		delp.add(dellb);
		delp.add(yesbt);
		delp.add(nobt);
		this.add(delp);
		delp.setVisible(false);
		edip.add(edilb);
		edip.add(yes1bt);
		edip.add(no1bt);
		this.add(edip);
		edip.setVisible(false);
		
		try{
			BufferedReader br = new BufferedReader(new FileReader("videodata.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				String[] videodata = new String[3];
				int i = 0;
				while(st.hasMoreTokens()) {
					String next = st.nextToken();
					videodata[i] = next;
					i++;
				}
				tableModel.addRow(videodata);
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
	}
	
	
	void AddRow(){
		edip.setVisible(false);
		delp.setVisible(false);
		String[] adddata = new String[3];
		adddata[0] = idtf.getText();
		adddata[1] = titletf.getText();
		adddata[2] = stocktf.getText();
		boolean newvideo = true;
		int addid = Integer.parseInt(adddata[0]);
		int lastid;
		int rownum = tableModel.getRowCount();
		String lastIdStr = tableModel.getValueAt(rownum - 1, 0).toString();
		lastid = Integer.parseInt(lastIdStr);
		for (int i = 0; i < tableModel.getRowCount(); i++){
			if(adddata[1].equals(tableModel.getValueAt(i, 1).toString())){
				addp.setVisible(true);
				newvideo = false;
			}
		}
		if(newvideo){
			if(addid > lastid){
				tableModel.addRow(adddata);
				titletf.setText("");
				stocktf.setText("");
				lastid++;
			}
			addp.setVisible(false);
		}
		if(rownum != 0){
			idtf.setText("00" + (lastid + 1));
		}else{
			idtf.setText("001");
		}
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
	        	for (String vid:rentp.getVideoId()){
	    			if(vid.equals(tableModel.getValueAt(row, 0).toString())){
	    				dellb.setText("VIDEO_ID:" + tableModel.getValueAt(row, 0).toString()
	    										+ " is rented now.");
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
		edilb.setText("Are you sure you want to change this cell?");
		yes1bt.setVisible(true);
		no1bt.setVisible(true);
		addp.setVisible(false);
		delp.setVisible(false);
		edip.setVisible(true);
		int row = table.getSelectedRow();
		idtf.setText(tableModel.getValueAt(row, 0).toString());
		titletf.setText(tableModel.getValueAt(row, 1).toString());
		stocktf.setText(tableModel.getValueAt(row, 2).toString());
			
		yes1bt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
						for (int i = 0; i < tableModel.getRowCount(); i++){
							if(titletf.getText().equals(tableModel.getValueAt(i, 1).toString())
									&& !idtf.getText().equals(tableModel.getValueAt(i, 0).toString())){
								samevideo = true;
							}
						}
						if(!samevideo){
							tableModel.setValueAt(idtf.getText(), table.getSelectedRow(), 0);
							tableModel.setValueAt(titletf.getText(), table.getSelectedRow(), 1);
							tableModel.setValueAt(stocktf.getText(), table.getSelectedRow(), 2);	
							edip.setVisible(false);
						}else{
							yes1bt.setVisible(false);
							no1bt.setVisible(false);
							edilb.setText("The same video already exists.");
						}
						samevideo = false;
				}
			});
		no1bt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				edip.setVisible(false);
			}
		});
	}
	
	int RentVideo(String vid){
		int stock;
		for (int i = 0; i < tableModel.getRowCount(); i++){
			if(vid.equals(tableModel.getValueAt(i, 0).toString())){
				stock = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
				if(stock == 0)return 2;
				return 0;
			}
		}
		return 1;
	}
	
	void DecrementStock(String vid){
		int stock;
		for (int i = 0; i < tableModel.getRowCount(); i++){
			if(vid.equals(tableModel.getValueAt(i, 0).toString())){
				stock = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
				tableModel.setValueAt(stock - 1, i, 2);
			}
		}
	}

	void ReturnVideo(String vid){
		for (int i = 0; i < tableModel.getRowCount(); i++){
			if(vid.equals(tableModel.getValueAt(i, 0).toString())){
				int chid = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
				tableModel.setValueAt(chid + 1, i, 2);
			}
		}
	}

	
	ArrayList<String> getVideoName(ArrayList<String> vidlist){
		ArrayList<String> vtitlelist = new ArrayList<String>();
		for(String vid:vidlist){
			for (int i = 0; i < tableModel.getRowCount(); i++){
				if(vid.equals(tableModel.getValueAt(i, 0).toString())){
					vtitlelist.add(tableModel.getValueAt(i, 1).toString());
				}
			}
		}
		return vtitlelist;
	}
	
	void getRentPanel(RentPanel rp){
		rentp = rp;
	}
	
	void SaveVideo(){
		try{
			PrintWriter out =
					new PrintWriter(new BufferedWriter(new FileWriter("videodata.txt")));
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
	