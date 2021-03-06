package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.ManagementGroup;
import model.User;

//DB上のuserテーブルに対応するDAO
public class UserDAO {
	// データベース接続に使用する情報
	private final String JDBC_URL = "jdbc:h2:tcp://localhost/~/h2db/actionlogger";
	private final String DB_USER = "sa";
	private final String DB_PASS = "";

	// ユーザーIDを指定して、ユーザー情報を取得
	// ユーザーIDが存在しない場合はnullを返す
	public User get(String userId) {
		User user = null;

		// データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

			// SELECT文の準備
			String sql = "SELECT * FROM user WHERE userid = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, userId);

			// SELECTを実行
			ResultSet rs = pStmt.executeQuery();

			// SELECT文の結果をuserに格納
			while (rs.next()) {
				user = new User();
				user.setUserId(rs.getString("userid"));
				user.setPwdHash(rs.getString("pwdhash"));
				user.setName(rs.getString("name"));
				user.setAddress(rs.getString("address"));
				user.setTel(rs.getString("tel"));
				user.setEmail(rs.getString("email"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return user;
	}
	
	
	public List<User> getGroupShowUserList(String groupId) {
		User user = null;
		List<User> userList = new ArrayList<User>(); 

		// データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

			// SELECT文の準備
			String sql = "SELECT DISTINCT u.userid, u.name, u.address, u.tel, u.email FROM user u, belongs b WHERE b.management_group_id= ? AND u.userid = b.userid";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, groupId);

			// SELECTを実行
			ResultSet rs = pStmt.executeQuery();

			// SELECT文の結果をuserに格納
			while (rs.next()) {
				user = new User();
				user.setUserId(rs.getString("userid"));
				user.setName(rs.getString("name"));
				user.setAddress(rs.getString("address"));
				user.setTel(rs.getString("tel"));
				user.setEmail(rs.getString("email"));
				userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return userList;
	}

	// ユーザーを指定して、ユーザー情報を保存
	// 戻り値:true 成功 , false 失敗
	public boolean save(User user) {
		// データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

			// INSERT文の準備(idは自動連番なので指定しなくてよい）
			String sql = "INSERT INTO user " + "( userid, pwdhash, name, address, tel, email ) "
					+ "VALUES ( ?, ?, ?, ?, ?, ? )";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			// INSERT文中の「?」に使用する値を設定しSQLを完成
			pStmt.setString(1, user.getUserId());
			pStmt.setString(2, user.getPwdHash());
			pStmt.setString(3, user.getName());
			pStmt.setString(4, user.getAddress());
			pStmt.setString(5, user.getTel());
			pStmt.setString(6, user.getEmail());

			// INSERT文を実行
			int result = pStmt.executeUpdate();
			if (result != 1) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//プロフィール編集
	public boolean editProfile(User user) {

		// データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

			// INSERT文の準備(idは自動連番なので指定しなくてよい）
			String sql = "UPDATE user SET name = ?, address = ?, tel = ?, email = ? WHERE  userid = ? ";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			// INSERT文中の「?」に使用する値を設定しSQLを完成
			pStmt.setString(1, user.getName());
			pStmt.setString(2, user.getAddress());
			pStmt.setString(3, user.getTel());
			pStmt.setString(4, user.getEmail());
			pStmt.setString(5, user.getUserId());

			// UPDATE文を実行
			int result = pStmt.executeUpdate();
			if (result != 1) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//パスワード変更
	public boolean editPassword(User user,String userId) {
		// データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {

			// INSERT文の準備(idは自動連番なので指定しなくてよい）
			String sql = "UPDATE user SET pwdhash = ? WHERE  userid = ? ";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			// INSERT文中の「?」に使用する値を設定しSQLを完成
			pStmt.setString(1, user.getPwdHash());
			pStmt.setString(2, userId);

			// UPDATE文を実行
			int result = pStmt.executeUpdate();
			if (result != 1) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
