package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import Entity.Student;
import main.Student_Management;

public class StudentMgtDashBoard extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7917204231042757894L;
	private JTable table;
	private DefaultTableModel model;
	private Font font = new Font("Segoe UI",Font.PLAIN,14);
	
	public StudentMgtDashBoard()
	{
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		FlatSVGIcon svgIcon = new FlatSVGIcon("icons/app_logo.svg",32,32);
		setIconImage(svgIcon.getImage());
		setTitle("Welcome to Student Management System");
		setSize(1200,800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		
		JPanel sideBar = new JPanel();
		sideBar.setBackground(new Color(33,47,60));
		sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
		sideBar.setBorder(new EmptyBorder(20,10,20,10));
		sideBar.setPreferredSize(new Dimension(200,0));
		
		JLabel title = new JLabel("Students",JLabel.CENTER);
		title.setForeground(Color.WHITE);
		title.setFont(new Font("Segoe UI",Font.BOLD,20));
		title.setIcon(new FlatSVGIcon("icons/student.svg",20,20));
		title.setAlignmentX(CENTER_ALIGNMENT);
		sideBar.add(title);
		sideBar.add(Box.createRigidArea(new Dimension(0,20)));
		
		JButton btnAdd = createSideBarBtn("Add Student","add.svg");
		JButton btnEdit = createSideBarBtn("Edit Student","edit.svg");
		JButton btnSearch = createSideBarBtn("Search Student","search.svg");
		JButton btnDelete = createSideBarBtn("Delete Student","delete.svg");
		JButton btnRefresh = createSideBarBtn("Refresh Table","refresh.svg");
		JButton btnExit = createSideBarBtn("Exit","exit.svg");
		
		sideBar.add(btnAdd);
		sideBar.add(Box.createVerticalStrut(10));
		sideBar.add(btnEdit);
		sideBar.add(Box.createVerticalStrut(10));
		sideBar.add(btnSearch);
		sideBar.add(Box.createVerticalStrut(10));
		sideBar.add(btnDelete);
		sideBar.add(Box.createVerticalStrut(10));
		sideBar.add(btnRefresh);
		sideBar.add(Box.createVerticalGlue());
		sideBar.add(btnExit);
		
		add(sideBar,BorderLayout.WEST);
		
		String[] cols = {"Name","Roll No","Grade","Email Address"};
		
		model = new DefaultTableModel(cols,0)
		{
			@Override
			public boolean isCellEditable(int row,int column)
			{
				return false;
			}
		};
		
		table = new JTable(model)
		{
			protected void paintComponent(Graphics g)
			{
				FlatSVGIcon icon = new FlatSVGIcon("icons/database.svg",64,64);
				super.paintComponent(g);
				if(getRowCount() == 0)
				{
					Graphics2D g2 = (Graphics2D) g;
					
					int x = (getWidth() - icon.getWidth()) / 2;
					int y = (getHeight() - icon.getHeight()) / 2 - 20;
					icon.paintIcon(this, g2, x, y);
					
					g2.setColor(Color.GRAY);
					g2.setFont(new Font("Segoe UI",Font.ITALIC,16));
					String message = "No Records in database. Add Students to display data !!";
					int textWidth = g2.getFontMetrics().stringWidth(message);
					g2.drawString(message,(getWidth() - textWidth) / 2, y + icon.getIconHeight() + 20);
				}
			}
		};
		table.setFont(font);
		table.setRowHeight(25);
		table.setShowGrid(true);
		table.setGridColor(Color.GRAY);
		table.setIntercellSpacing(new Dimension(1,1));
		table.setFillsViewportHeight(true);
		
		table.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,17));
		table.getTableHeader().setBackground(new Color(33,47,60));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setOpaque(true);
		
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
		
		sorter.setComparator(1, (o1,o2) ->{
			Integer i1 = Integer.parseInt(o1.toString());
			Integer i2 = Integer.parseInt(o2.toString());
			return i1.compareTo(i2);
		});
		
		table.setRowSorter(sorter);
		
		List<SortKey> sortKeys = new ArrayList<>();
		sortKeys.add(new SortKey(2,SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(new EmptyBorder(10,10,10,10));
		add(scrollPane,BorderLayout.CENTER);
		
		refreshTable();
		
		btnAdd.addActionListener(e -> showAddStudentDialog());
		btnEdit.addActionListener(e -> showEditStudentDialog());
		btnSearch.addActionListener(e -> showSearchStudentDialog());
		btnDelete.addActionListener(e -> showDeleteStudentDialog());
		btnRefresh.addActionListener(e -> refreshTable());
		btnExit.addActionListener(e -> System.exit(0));
		
		setVisible(true);
		
	}
	
	private JButton createSideBarBtn(String text,String icon)
	{
		JButton btn = new JButton(text);
		btn.setAlignmentX(CENTER_ALIGNMENT);
		btn.setHorizontalAlignment(SwingConstants.LEFT);
		btn.setVerticalAlignment(SwingConstants.CENTER);
		btn.setHorizontalTextPosition(SwingConstants.RIGHT);
		btn.setVerticalTextPosition(SwingConstants.CENTER);
		btn.setMaximumSize(new Dimension(200,50));
		
		btn.setFont(new Font("Segoe UI",Font.BOLD,14));
		btn.setForeground(Color.WHITE);
		btn.setBackground(new Color(52,73,94));
		
		btn.setIcon(new FlatSVGIcon("icons/"+icon,24,24));
		btn.setIconTextGap(10);
		
		btn.setFocusPainted(false);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btn.putClientProperty("JButton.buttonType", "roundRect");
		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e)
			{
				btn.setBackground(new Color(41,128,185));
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				btn.setBackground(new Color(52,73,94));
			}
		});
		return btn;
	}
	
	private void refreshTable()
	{
		model.setRowCount(0);
		Object[][] data = Student_Management.getData(this);
		if(data != null)
		{
			for(Object[] row : data)
			{
				model.addRow(row);
			}
		}
	}
	
	private void showAddStudentDialog()
	{
		JTextField name = new JTextField();
		JTextField roll = new JTextField();
		JTextField grade = new JTextField();
		JTextField email = new JTextField();
		
		Object[] fields = {
				"Name:",name,
				"Roll No:",roll,
				"Grade:",grade,
				"Email:",email
		};
		
		int option = JOptionPane.showConfirmDialog(this, fields,"Add Student",JOptionPane.OK_CANCEL_OPTION);
		if(option == JOptionPane.OK_OPTION)
		{
			Student_Management.addStudent(name.getText(),roll.getText(),grade.getText(),email.getText(),this);
			refreshTable();
		}
	}
	
	private void showEditStudentDialog()
	{
		String roll = JOptionPane.showInputDialog(this,"Enter Roll No:");
		if(roll == null || roll.isEmpty()) return;
		
		Student s = Student_Management.searchStudent(roll);
		if(s == null)
		{
			JOptionPane.showMessageDialog(this, "Student not found!");
			return;
		}
		
		JTextField name = new JTextField(s.getName());
		JTextField rollField = new JTextField(String.valueOf(s.getRollNo()));
		JTextField grade = new JTextField(s.getGradeObtained());
		JTextField email = new JTextField(s.getEmailAddress());
		
		Object[] fields = {
				"Name:",name,
				"Roll No:",rollField,
				"Grade:",grade,
				"Email:",email
		};
		
		int option = JOptionPane.showConfirmDialog(this, fields,"Edit Student",JOptionPane.OK_CANCEL_OPTION);
		if(option == JOptionPane.OK_OPTION)
		{
			Student_Management.editStudent(name.getText(), rollField.getText(), roll, grade.getText(), email.getText(), this);
			refreshTable();
		}
	}
	
	private void showSearchStudentDialog()
	{
		String roll = JOptionPane.showInputDialog(this,"Enter Roll No to Search:");
		if(roll == null || roll.isEmpty()) return;
		
		Student s = Student_Management.searchStudent(roll);
		if(s == null)
		{
			JOptionPane.showMessageDialog(this, "No Student Found!");
			return;
		}
		
		Object[] fields = {
				"Student Details:",
				"Name: "+ s.getName(),
				"Roll No: "+ s.getRollNo(),
				"Grade: "+ s.getGradeObtained(),
				"Email: "+ s.getEmailAddress()
		};
		
		JOptionPane.showMessageDialog(this,fields);
	}
	
	private void showDeleteStudentDialog()
	{
		String roll = JOptionPane.showInputDialog(this,"Enter Roll No to Delete:");
		if(roll == null || roll.isEmpty()) return;
		
		int confirm = JOptionPane.showConfirmDialog(this, "Are you sure want to delete?","Confirm",JOptionPane.YES_NO_OPTION);
		if(confirm == JOptionPane.YES_OPTION)
		{
			Student_Management.deleteStudent(roll, this);
			refreshTable();
		}
	}
}
