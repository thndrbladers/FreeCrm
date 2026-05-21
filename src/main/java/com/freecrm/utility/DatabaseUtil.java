package com.freecrm.utility;

import com.freecrm.base.Base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class DatabaseUtil {

	private static final ThreadLocal<Connection> threadConnection = new ThreadLocal<>();

	private DatabaseUtil() {
	}

	public static Connection getConnection() {
		try {
			Connection conn = threadConnection.get();

			if (conn == null || conn.isClosed() || !conn.isValid(3)) {
				conn = DriverManager.getConnection(Base.getConfig("db.url"), Base.getConfig("db.username"),
						Base.getConfig("db.password"));
				threadConnection.set(conn);
			}

			return conn;

		} catch (SQLException e) {
			throw new RuntimeException("Failed to get DB connection", e);
		}
	}

	public static void closeConnection() {
		Connection conn = threadConnection.get();
		if (conn == null)
			return;
		try {
			if (!conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			threadConnection.remove();
		}
	}

	public static List<Object[]> getQueryData(String sql) {
		Statement stmt;
		ResultSet rs;
		ResultSetMetaData rsmd;
		int noOfCols;
		List<Object[]> list = new ArrayList<>();
		Object[] temp;
		try {
			stmt = getConnection().createStatement();
			rs = stmt.executeQuery(sql);
			rsmd = rs.getMetaData();
			noOfCols = rsmd.getColumnCount();
			while (rs.next()) {
				temp = new Object[noOfCols];
				for (int i = 1; i <= noOfCols; i++) {
					temp[i - 1] = rs.getString(i);

				}

				list.add(temp);
			}

			rs.close();
			stmt.close();
			closeConnection();
		} catch (SQLException e) {

			e.printStackTrace();
		}

		for (Object[] o : list) {
			System.out.println(Arrays.toString(o));
		}

		return list;

	}

	// test
	/*
	 * public static void main(String[] args) throws SQLException {
	 * 
	 * Connection conn =
	 * DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/FREEPDB1",
	 * "freecrm_svt", "Freecrm.rdg@123"); Statement stmt = conn.createStatement();
	 * 
	 * ResultSet rs = stmt.executeQuery("SELECT * FROM companies");
	 * ResultSetMetaData rsMetaData = rs.getMetaData();
	 * 
	 * int noOfCols = rsMetaData.getColumnCount();
	 * 
	 * while (rs.next()) { for (int i = 1; i <= noOfCols; i++) {
	 * System.out.print(rs.getString(i) + " ||  "); } System.out.println(); }
	 * 
	 * 
	 * Base b = new Base();
	 * 
	 * List<Object[]> temp = DatabaseUtil.getQueryData("SELECT * FROM companies");
	 * 
	 * }
	 */
}