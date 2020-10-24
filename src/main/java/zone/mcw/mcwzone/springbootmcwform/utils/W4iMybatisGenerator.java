package zone.mcw.mcwzone.springbootmcwform.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author W4i create 2020/9/25 10:00
 */
public class W4iMybatisGenerator {
	//driver
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

	//驼峰转换
	private String trans(String str) {
		while (str.indexOf("_") != -1) {
			str = str.substring(0, str.indexOf("_")) + str
					.substring(str.indexOf("_") + 1, str.indexOf("_") + 2).toUpperCase() + str
					.substring(str.indexOf("_") + 2);
			trans(str);
		}
		return str;
	}

	private Connection getConnection(String url, String username, String pawd) throws ClassNotFoundException, SQLException {
		// 注册 JDBC 驱动
		Class.forName(JDBC_DRIVER);
		// 打开链接
		return DriverManager.getConnection(url, username, pawd);
	}

	private List<String> getTable(Statement stmt) throws SQLException {
		String sql = "select * from information_schema.TABLES where TABLE_SCHEMA=(select database()) order by TABLE_NAME";
		ResultSet rs = stmt.executeQuery(sql);
		List<String> tableNameList = new ArrayList<String>();
		while (rs.next()) {
			String tableName = rs.getString("TABLE_NAME");
			tableNameList.add(tableName);
		}
		return tableNameList;
	}

	//生成sql语句
	private void generatorTable(String tableName, String path, Statement stmt) throws SQLException, IOException {
		// 执行查询
		String sql = "select * from information_schema.COLUMNS where TABLE_SCHEMA = (select database()) and TABLE_NAME=\"" + tableName
				+ "\" order by ORDINAL_POSITION";
		ResultSet rs = stmt.executeQuery(sql);
		String ifTest = "";
		String selectOr2 = "";
		String priKey = "";
		String insert1 = "";
		String insert2 = "";
		String update = "";
		String update1 = "";
		String update11 = "";
		String update2 = "";
		while (rs.next()) {
			String columnName = rs.getString("COLUMN_NAME");
			String pri = rs.getString("COLUMN_KEY");
			String columnName1 = trans(columnName);
			if (pri.equals("PRI")) {
				priKey = columnName;
			} else {
				update = update
						+ "            <if test=\"" + columnName1 + " != null\">\n"
						+ "                " + columnName + " = #{" + columnName1 + "} or\n"
						+ "            </if>\n";
				update2 = update2 + "            " + columnName + " = #{" + columnName1 + "}\n";
				update1 = update1
						+ "            <if test=\"newRecord." + columnName1 + " != null\">\n"
						+ "                " + columnName + " = #{newRecord." + columnName1 + "} ,\n"
						+ "            </if>\n";
			}
			update11 = update11
					+ "            <if test=\"oldRecord." + columnName1 + " != null\">\n"
					+ "                " + columnName + " = #{oldRecord." + columnName1 + "} and\n"
					+ "            </if>\n";
			ifTest = ifTest + columnName1 + " != null||";
			selectOr2 = selectOr2
					+ "            <if test=\"" + columnName1 + " != null\">\n"
					+ "                " + columnName + " = #{" + columnName1 + "} or \n"
					+ "            </if>\n";

			insert1 = insert1
					+ "            <if test=\"" + columnName1 + " != null\">\n"
					+ "                " + columnName + ",\n"
					+ "            </if>\n";
			insert2 = insert2
					+ "            <if test=\"" + columnName1 + " != null\">\n"
					+ "                #{" + columnName1 + "},\n"
					+ "            </if>\n";
		}
		String deleteByPrimaryKey = "\t/**\n"
				+ "\t * 通过主键删除\n"
				+ "\t *\n"
				+ "\t */\n"
				+ "\tint deleteByPrimaryKey(Integer " + trans(priKey) + ");\n";

		String str = tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
		String insertByEntity = "\t/**\n"
				+ "\t * 通过实体类插入\n"
				+ "\t *\n"
				+ "\t */\n"
				+ "\tint insertByEntity(" + trans(str) + " record);\n";

		String updateEntityByEntity = "\t/**\n"
				+ "\t * 通过实体类更新实体类（无值不更新）\n"
				+ "\t *\n"
				+ "\t */\n"
				+ "\tint updateEntityByEntity(" + trans(str) + " newRecord , " + trans(
				str) + " oldRecord);\n";
		String selectByEntity = "\t/**\n"
				+ "\t * 通过实体类查询（条件带and）\n"
				+ "\t *\n"
				+ "\t */\n"
				+ "\tList<" + trans(str) + "> selectByEntity(" + tableName.substring(0, 1)
				.toUpperCase() + trans(tableName.substring(1)) + " record);\n";
		String selectByEntityOr = "\t/**\n"
				+ "\t * 通过实体类查询（条件带or）\n"
				+ "\t *\n"
				+ "\t */\n"
				+ "\tList<" + trans(str) + "> selectByEntityOr(" + tableName.substring(0, 1)
				.toUpperCase() + trans(tableName.substring(1)) + " record);\n";
		String updateByPrimaryKey = "\t/**\n"
				+ "\t * 通过实体类更新，若数据未空则更新未空\n"
				+ "\t *\n"
				+ "\t */\n"
				+ "\tint updateByPrimaryKey(" + trans(str)
				+ " record);\n";
		String selectByPage = "\t/**\n"
				+ "\t * 分页查询\n"
				+ "\t * @param startPage\n"
				+ "\t * @param pageSize\n"
				+ "\t * @param order\n"
				+ "\t * @param desc\n"
				+ "\t * @return\n"
				+ "\t */\n"
				+ "\tList<" + trans(str) + "> selectByPage(int startPage ,int pageSize,String order,String desc);\n";
		String selectByPageSql = ""
				+ "        select * from " + tableName + " order by ${order}\n"
				+ "        <if test=\"desc==1\">\n"
				+ "            desc\n"
				+ "        </if>\n"
				+ "        limit #{startPage},#{pageSize}\n";

		String selectByEntityOrSql = "        select * from " + tableName + "\n"
				+ "        <trim suffixOverrides=\" or \">\n"
				+ "            <if test=\"" + ifTest.substring(0, ifTest.length() - 2) + "\">\n"
				+ "                where\n"
				+ "            </if>\n"
				+ selectOr2 + "        </trim>\n";
		String selectByEntitySql = "==========================selectByEntity============================================\n" + selectByEntityOrSql
				.replaceAll(" or ", " and ");
		selectByEntityOrSql = "==========================selectByEntityOr============================================\n" + selectByEntityOrSql;

		String deleteByPrimaryKeySql =
				"==========================deleteByPrimaryKey============================================\n" + "    delete from " + tableName + "\n"
						+ "    where " + priKey + " = #{" + trans(priKey) + "}";
		String insertByEntitySql =
				"\n==========================insertByEntity============================================\n" + "        insert into " + tableName
						+ "\n"
						+ "        <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n"
						+ insert1
						+ "        </trim>\n"
						+ "        <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">\n"
						+ insert2
						+ "        </trim>\n";
		String updateByPrimaryKeySql =
				"==========================updateByPrimaryKey============================================\n" + "        update " + tableName
						+ "\n"
						+ "        set \n"
						+ update2
						+ "        where " + priKey + " = #{" + trans(priKey) + "}\n";
		String updateEntityByEntitySql = "==========================updateEntityByEntitySql============================================\n"
				+ "        update " + tableName + "\n"
				+ "        set \n"
				+ "        <trim suffixOverrides=\",\">\n"
				+ update1
				+ "        </trim>\n"
				+ "        where\n"
				+ "        <trim  suffixOverrides=\"and\">\n"
				+ update11
				+ "        </trim>\n";
		String interfaceStr =
				deleteByPrimaryKey + insertByEntity + updateByPrimaryKey + updateEntityByEntity + selectByEntity + selectByEntityOr + selectByPage;
		System.out.println(interfaceStr);
		String sqlStr =
				deleteByPrimaryKeySql + insertByEntitySql + updateByPrimaryKeySql + updateEntityByEntitySql + selectByEntitySql + selectByEntityOrSql
						+ selectByPageSql;
		System.out.println(sqlStr);
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();// mkdirs创建多级目录
		}
		File sqlFile = new File(path + "/" + tableName + "Sql.txt");
		File interfaceFile = new File(path + "/" + tableName + "Interface.txt");
		if (!sqlFile.exists()) {
			sqlFile.createNewFile();// 创建目标文件
		}
		if (!interfaceFile.exists()) {
			interfaceFile.createNewFile();// 创建目标文件
		}
		// 三、向目标文件中写入内容
		// FileWriter(File file, boolean append)，append为true时为追加模式，false或缺省则为覆盖模式
		FileWriter writerSql = null;
		writerSql = new FileWriter(sqlFile, true);
		writerSql.append(sqlStr);
		writerSql.flush();
		FileWriter writerInterFace = null;
		writerInterFace = new FileWriter(interfaceFile, true);
		writerInterFace.append(interfaceStr);
		writerInterFace.flush();
		rs.close();
	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
		String url = "url";
		String username = "username";
		String password = "password";
		String path = "D:\\workSpace\\asd";
		W4iMybatisGenerator w4iMybatisGenerator = new W4iMybatisGenerator();
		Connection connection = w4iMybatisGenerator.getConnection(url, username, password);
		Statement stmt = connection.createStatement();
		//全跑
//		List<String> tableList = w4iMybatisGenerator.getTable(stmt);
//		for (String s : tableList) {
//			w4iMybatisGenerator.generatorTable(s, path, stmt);
//		}
//		List<String> list = new ArrayList<>();
//		list.add("log_form");
//		list.add("modular");
//		list.add("reply");
//		list.add("form");
//		for (String s : list) {
//			w4iMybatisGenerator.generatorTable(s, path, stmt);
//
//		}
//		单跑
		w4iMybatisGenerator.generatorTable("ban", path, stmt);
		stmt.close();
		connection.close();
	}
}
