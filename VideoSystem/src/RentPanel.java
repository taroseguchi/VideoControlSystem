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

public class RentPanel extends JPanel{
	
	private String[] columnNames = {"MEMBER_ID", "VIDEO_ID"};
	JTextField midtf = new JTextField();
	JTextField vidtf = new JTextField();
	DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0){
		@Override public boolean isCellEditable(int row, int column) {return false;};
	};
	JTable table = new JTable(tableModel);
	JScrollPane sp = new JScrollPane(table);
	JPanel delp = new JPanel();
	JLabel dellb = new JLabel("Are you sure you want to delete this row?");
	JButton yesbt = new JButton("Yes");
	JButton  nobt = new JButton("No");
	JPanel addp = new JPanel();
	JLabel addlb = new JLabel();
	VideoPanel videop;
	MemberPanel memberp;
	
	public RentPanel(){
		JLabel midlb = new JLabel("MEMBER_ID");
		midtf.setColumns(3);
		JLabel vidlb = new JLabel("VIDEO_ID");
		vidtf.setColumns(3);
		this.add(sp);
		this.add(midlb);
		this.add(midtf);
		this.add(vidlb);
		this.add(vidtf);
		delp.add(dellb);
		delp.add(yesbt);
		delp.add(nobt);
		this.add(delp);
		delp.setVisible(false);
		addp.add(addlb);
		this.add(addp);
		addp.setVisible(false);
		
		try{
			BufferedReader br = new BufferedReader(new FileReader("rentdata.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				String[] rentdata = new String[2];
				int i = 0;
				while(st.hasMoreTokens()) {
					String next = st.nextToken();
					rentdata[i] = next;
					i++;
				}
				tableModel.addRow(rentdata);
			}
			br.close();
		}
		catch(IOException e){
			System.out.println(e);
		}
	}
	
	void AddRow(){
		String[] adddata = new String[2];
		adddata[0] = midtf.getText();
		adddata[1] = vidtf.getText();
		if((videop.RentVideo(adddata[1]) == 0) && memberp.RentVideo(adddata[0])){
			tableModel.addRow(adddata);
			videop.DecrementStock(adddata[1]);
			addp.setVisible(false);
		}else if(!memberp.RentVideo(adddata[0])){
			addlb.setText("     Member_ID:" + adddata[0] + " does not exist!     ");
			addp.setVisible(true);
		}else if(videop.RentVideo(adddata[1]) == 1){
			addlb.setText("     VIDEO_ID:" + adddata[1] + " does not exist!     ");
			addp.setVisible(true);
		}else if(videop.RentVideo(adddata[1]) == 2){
			addlb.setText("     VIDEO_ID:" + adddata[1] + " is out of stock!    ");
			addp.setVisible(true);
		}
	}
	
	void DeleteRow(){
		addp.setVisible(false);
		delp.setVisible(true);
		yesbt.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	        	int row = table.getSelectedRow();
	        	videop.ReturnVideo(tableModel.getValueAt(row, 1).toString());
	        	tableModel.removeRow(row);
	        	delp.setVisible(false);
	        }
	    });
		nobt.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	        	delp.setVisible(false);
	        }
	    });
	}
	
	
	ArrayList<String> MemberToVideo(String mid){
		ArrayList<String> vidlist = new ArrayList<String>();
		for (int i = 0; i < tableModel.getRowCount(); i++){
			if(mid.equals(tableModel.getValueAt(i, 0).toString())){
				vidlist.add(tableModel.getValueAt(i, 1).toString());
			}
		}
		return videop.getVideoName(vidlist);
	}
	
	void getVideoPanel(VideoPanel vp){
		videop = vp;
	}
	
	void getMemberPanel(MemberPanel mp){
		memberp = mp;
	}
	
	ArrayList<String> getVideoId(){
		ArrayList<String> vid = new ArrayList<String>();
		for (int i = 0; i < tableModel.getRowCount(); i++){
			if (!vid.contains(tableModel.getValueAt(i, 1).toString()))
				vid.add(tableModel.getValueAt(i, 1).toString());
		}
		return vid;
	}
	
	ArrayList<String> getMemberId(){
		ArrayList<String> mid = new ArrayList<String>();
		for (int i = 0; i < tableModel.getRowCount(); i++){
			if (!mid.contains(tableModel.getValueAt(i, 0).toString()))
				mid.add(tableModel.getValueAt(i, 0).toString());
		}
		return mid;
	}
	
	void SaveRent(){
		try{
			PrintWriter out =
					new PrintWriter(new BufferedWriter(new FileWriter("rentdata.txt")));
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
