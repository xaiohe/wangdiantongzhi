package com.everflourish.act.db;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DbManager {
	/*private static JdbcTemplate jdbcTemplate;

	public static JdbcTemplate getJdbcTemplate() {
		return DbManager.jdbcTemplate;
	}
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		DbManager.jdbcTemplate = jdbcTemplate;
	} 
	
	public static Map<String, Object> queryForMap(String sql,Object... args){
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, args);
		if(list.isEmpty()){
			return null;
		}
		return list.get(0);//jdbcTemplate.queryForMap(sql, args);
	}
	
	public static <T> T queryForObject(String sql,Class<T> requiredType,Object... args){
		List<T> tList = queryForList(sql, requiredType, args);
		if(tList == null ||tList.isEmpty()){
			return null;
		}
		return tList.get(0);//jdbcTemplate.queryForObject(sql, requiredType, args);
	}
	
	public static <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) {
		return jdbcTemplate.queryForList(sql, elementType, args);
	}
	
	public static List<Map<String, Object>> queryForList(String sql, Object... args){
		return jdbcTemplate.queryForList(sql, args);
	}
	
	
	public static int update(String sql, Object... args){
		return jdbcTemplate.update(sql, args);
	}
	
	public static int[] batchUpdate(String sql, List<Object[]> batchArgs){
		int size = batchArgs.size();
		int[] resultInt = new int[size];
		List<Object[]> batchArgs200 = new ArrayList<Object[]>();
		int i=0;
		int resultIndex = 0;
		for(;i<size;++i){			
			batchArgs200.add(batchArgs.get(i));
			if(batchArgs200.size() % 200 == 0){
				int[] resultInt200 = jdbcTemplate.batchUpdate(sql, batchArgs200);
				batchArgs200.clear();
				for(int j=0;j<resultInt200.length;++j){
					resultInt[resultIndex++] = resultInt200[j];
				}
			}
		}
		if(!batchArgs200.isEmpty()){
			int[] resultInt200 = jdbcTemplate.batchUpdate(sql, batchArgs200);
			batchArgs200 = null;
			for(int j=0;j<resultInt200.length;++j){
				resultInt[resultIndex++] = resultInt200[j];
			}
		}
		
		return resultInt;//jdbcTemplate.batchUpdate(sql, batchArgs);

	}
	
	*//**

	 * 插入数据并返回自动增长的主键值

	 * @param sql

	 * @param args

	 * @return

	 *//*
	public static Number insert(final String sql,final Object... args){
		PreparedStatementCreator preparedStatement = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(args);
				pss.setValues(ps);
				return ps;
			}
		};
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(preparedStatement, keyHolder);
		return keyHolder.getKey();
	}
	
	public static List<Object[]> queryForListObjects(String sql,Object... args){
		RowMapper<Object[]> rm = new ObjectsRowMapper();
		return jdbcTemplate.query(sql,rm, args);
	}
	public static Object[] queryForObjects(String sql,Object... args){
		ResultSetExtractor<Object[]> rse = new ObjectsResultSetExtractor();
		return jdbcTemplate.query(sql,rse, args);
	}
	private static class ObjectsResultSetExtractor implements ResultSetExtractor<Object[]>{
		@Override
		public Object[] extractData(ResultSet rs) throws SQLException, DataAccessException {
			ResultSetMetaData md = rs.getMetaData(); //得到结果集(rs)的结构信息，比如字段数、字段名等

            int columnCount = md.getColumnCount();
            Object[] objs = new Object[columnCount];
			for (int i = 0; i < columnCount;++i) {
                objs[i] = rs.getObject(i+1);
            }
			return objs;
		}
		
	}
	private static class ObjectsRowMapper implements RowMapper<Object[]> {
		public ObjectsRowMapper() {}
		private int columnCount = 0;
		boolean flag = true;
		@Override
		public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
			if(flag){
				flag = false;
				ResultSetMetaData md = rs.getMetaData(); //得到结果集(rs)的结构信息，比如字段数、字段名等

	            this.columnCount = md.getColumnCount();
			}
			
			Object[] objs = new Object[this.columnCount];
			for (int i = 0; i < this.columnCount;++i) {
                objs[i] = rs.getObject(i+1);
            }
			return objs;
		}
	}
	
	public static <T> T queryForBean(String sql,Class<T> clazz,Object... args){
		List<T> listBean = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper<T>(clazz));
		if(listBean==null || listBean.isEmpty()){
			return null;
		}
		return listBean.get(0);
	}
	public static <T> List<T> queryForListBean(String sql,Class<T> clazz,Object... args){
		List<T> listBean = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper<T>(clazz));
		return  listBean;
	}*/
	public static DbTemplate wxApp;
	@Autowired
	public void setWorldCup(
			@Qualifier("dataSource") DataSource dataSource,
			@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory
			) {
		DbManager.wxApp = new DbTemplate(new JdbcTemplate(dataSource), sqlSessionFactory);
	}
	
}
