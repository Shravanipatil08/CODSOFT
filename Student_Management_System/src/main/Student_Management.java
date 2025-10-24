package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import Entity.Student;
import UI.StudentMgtDashBoard;
import Utility.DbUtil;

public class Student_Management {
	
	public static void addStudent(String name,String rollNo,String grade,String email,JFrame window)
	{
		String studName = name;
		String studRoll = rollNo;
		String studGrade = grade;
		String studEmail = email;
		
		int verifiedRNO = validateData(studName,studRoll,studGrade,studEmail,window);
		if(verifiedRNO == -1)
			return;
		
		try(Connection conn = DbUtil.getConnection())
		{
			String sql = " CREATE TABLE IF NOT EXISTS student (name varchar(50),roll int PRIMARY KEY,grade varchar(1),email varchar(50) UNIQUE);";
			try(PreparedStatement pstmt = conn.prepareStatement(sql))
			{
				pstmt.execute();
			}
			
			sql = "Select roll from student where roll = ?";
			try(PreparedStatement pstmt = conn.prepareStatement(sql))
			{
				pstmt.setInt(1, Integer.parseInt(rollNo));
				
				ResultSet rs = pstmt.executeQuery();
				
				if(rs.next())
				{
					JOptionPane.showMessageDialog(window, "Student with "+ verifiedRNO +" already exists ....");
					return;
				}
			}
			
			sql = "INSERT into student values (?,?,?,?);";
			try(PreparedStatement pstmt = conn.prepareStatement(sql))
			{
				pstmt.setString(1,studName);
				pstmt.setInt(2,verifiedRNO);
				pstmt.setString(3,studGrade);
				pstmt.setString(4,studEmail);
				
				pstmt.executeUpdate();
				
				JOptionPane.showMessageDialog(window, studName+" Added Successfully .....");
			}
		}
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(window, e.getMessage());
		}
	}
	
	public static Student searchStudent(String rollNo)
	{
		int roll = Integer.parseInt(rollNo);
		try(Connection conn = DbUtil.getConnection())
		{
			String sql = "Select * from student where roll = ?";
			try(PreparedStatement pstmt = conn.prepareStatement(sql))
			{
				pstmt.setInt(1, roll);
				ResultSet rs = pstmt.executeQuery();
				
				if(rs.next())
				{
					Student stud = new Student();
					stud.setName(rs.getString(1));
					stud.setRollNo(rs.getInt(2));
					stud.setGradeObtained(rs.getString(3));
					stud.setEmailAddress(rs.getString(4));
					return stud;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void editStudent(String name,String rollNo,String oldRollNo,String grade,String email,JFrame window)
	{
		String studName = name;
		String studRoll = rollNo;
		String studoldRoll = oldRollNo;
		String studGrade = grade;
		String studEmail = email;
		
		int validNo = validateData(studName,studRoll,studGrade,studEmail,window);
		int oldNo = Integer.parseInt(studoldRoll);
		
		try(Connection conn = DbUtil.getConnection())
		{
			String sql = " Update student set name = ? , roll = ? , grade = ? , email = ? where roll = ? ";
			try(PreparedStatement pstmt = conn.prepareStatement(sql))
			{
				pstmt.setString(1,studName);
				pstmt.setInt(2,validNo);
				pstmt.setString(3,studGrade);
				pstmt.setString(4,studEmail);
				pstmt.setInt(5, oldNo);
				pstmt.executeUpdate();
				
				JOptionPane.showMessageDialog(window, studName+" Edited Successfully .....");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Object[][] getData(JFrame window)
	{
		List<Object[]> data = new ArrayList<Object[]>();
		
		try(Connection conn = DbUtil.getConnection())
		{
			String sql = "Select * From student";
			try(PreparedStatement pstmt = conn.prepareStatement(sql))
			{
				ResultSet rs = pstmt.executeQuery();
				while(rs.next())
				{
					Object[] studentData = new Object[4];
					studentData[0] = rs.getString(1);
					studentData[1] = rs.getInt(2);
					studentData[2] = rs.getString(3);
					studentData[3] = rs.getString(4);
					data.add(studentData);
				}
			}
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(window, e.getMessage());
		}
		
		Object[][] studentsDataSet = new Object[data.size()][4];
		for(int i=0;i<data.size();i++)
		{
			studentsDataSet[i]=data.get(i);
		}
		
		return studentsDataSet;
		
	}
	
	public static void deleteStudent(String rollNo,JFrame window)
	{
		int validNo = Integer.parseInt(rollNo);
		try(Connection conn = DbUtil.getConnection())
		{
			String sql = "Delete From student where roll = ?";
			try(PreparedStatement pstmt = conn.prepareStatement(sql))
			{
				pstmt.setInt(1, validNo);
				pstmt.executeUpdate();
				JOptionPane.showMessageDialog(window, "Student Deleted Successfully .....");
			}
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(window, e.getMessage());
		}
	}
	
	private static int validateData(String name,String roll,String grade,String email,JFrame window)
	{
		if(name.isEmpty() || name.trim().isEmpty())
		{
			JOptionPane.showMessageDialog(window, "Please Enter name !!");
			return -1;
		}
		
		if(roll == null || !roll.matches("\\d+"))
		{
			JOptionPane.showMessageDialog(window, "Please Enter valid roll number !!");
			return -1;
		}
		
		if(grade == null || grade.trim().isEmpty())
		{
			JOptionPane.showMessageDialog(window, "Please enter the grade !!");
			return -1;
		}
		
		if(!grade.matches("[A-Fa-f](\\+{0,2})"))
		{
			JOptionPane.showMessageDialog(window, "Grade must be between A to F !!");
			return -1;
		}
			
		if(email == null || email.isEmpty())
		{
			JOptionPane.showMessageDialog(window, "Please Fill the email address !!");
			return -1;
		}
		
		int rollNo = 0;
		
		try {
			rollNo = Integer.parseInt(roll);
		}
		catch(NumberFormatException e){
			JOptionPane.showMessageDialog(window, "Enter Valid Number !!");
			return -1;
		}
		return rollNo;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(()->{
			new StudentMgtDashBoard();
		});
	}
}
