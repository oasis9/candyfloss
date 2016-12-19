package dank.meme.waystones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class SQLManager implements Listener {

	Connection connection;
	String host;
	String port = "3306";
	String database;
	String username;
	String password;
	
	public SQLManager(String host, String database, String username, String password) {
		this.host = host;
		this.database = database;
		this.username = username;
		this.password = password;
	}
	
	public SQLManager(String host, String port, String database, String username, String password) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
	}
	
	public synchronized void open() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public synchronized void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void statement(String statement) {
		open();
		try (PreparedStatement stmt = connection.prepareStatement(
				statement)) {
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	public synchronized ResultSet get(String table) {
		open();
		try {
			PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM `" + table + "`");
			return statement.executeQuery();
		} catch (SQLException ex) {
			return null;
		} finally {
			close();
		}
	}

	public synchronized ResultSet get(String table, Map<String, String> fields) {
		open();
		try {
			String where = " WHERE ";
			for (Entry<String, String> e : fields.entrySet())
				where += (where == " WHERE " ? "" : "AND ") + e.getKey() + "=" + e.getValue();
			PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM `" + table + "`" + (fields.size() > 0 ? where : ""));
			return statement.executeQuery();
		} catch (SQLException ex) {
			return null;
		} finally {
			close();
		}
	}

	public synchronized Object advancedGet(String field, String table, String where, Object equal) {
		open();
		try {
			PreparedStatement statement = connection
					.prepareStatement("SELECT " + field + " FROM `" + table + "` WHERE " + where + "=?;");
			statement.setString(1, String.valueOf(equal));

			ResultSet set = statement.executeQuery();
			set.next();

			Object val = set.getObject(field);
			set.close();

			return val;
		} catch (SQLException ex) {
			return "";
		} finally {
			close();
		}
	}

	public synchronized void insert(String table, List<String> fields, List<String> values) {
		open();
		try {
			PreparedStatement statement_settings = connection
					.prepareStatement("INSERT INTO `" + table + "` (" + StringUtils.join(fields.toArray(), ",") + ") values(" + StringUtils.join(values.toArray(), ",") + ");");
			statement_settings.execute();
			statement_settings.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			close();
		}
	}	

	public synchronized void set(Player player, String field, String table, Object value) {
		open();
		try {
			PreparedStatement statement = connection
					.prepareStatement("UPDATE `" + table + "` SET " + field + "=? WHERE uuid=?;");
			statement.setObject(1, value);
			statement.setString(2, String.valueOf(player.getUniqueId()));

			statement.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			close();
		}
	}
}