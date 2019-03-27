package com.everflourish.act.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class DbTemplate {
	
	
	public DbTemplate(JdbcTemplate jdbcTemplate, SqlSessionFactory sqlSessionFactory) {
		super();
		this.jdbcTemplate = jdbcTemplate;
		this.sqlSessionFactory = sqlSessionFactory;
	}
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/*public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}*/
	private SqlSessionFactory sqlSessionFactory;
	
	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	/*public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}*/

	public Map<String, Object> queryForMap(String sql,Object... args){
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, args);
		if(list.isEmpty()){
			return null;
		}
		return list.get(0);//jdbcTemplate.queryForMap(sql, args);
	}
	
	public <T> T queryForObject(String sql,Class<T> requiredType,Object... args){
		return jdbcTemplate.queryForObject(sql, requiredType, args);
	}
	
	public <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) {
		return jdbcTemplate.queryForList(sql, elementType, args);
	}
	
	public List<Map<String, Object>> queryBatchForList(String sql, Map<String, Object> params){
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		return namedParameterJdbcTemplate.queryForList(sql, params);
	}
	
	public List<Map<String, Object>> queryForList(String sql, Object... args){
		return jdbcTemplate.queryForList(sql, args);
	}
	
	public int update(String sql, Object... args){
		return jdbcTemplate.update(sql, args);
	}
	
	public int[] batchUpdate(String sql, List<Object[]> batchArgs){
		return jdbcTemplate.batchUpdate(sql, batchArgs);

	}
	
	/**

	 * 插入数据并返回自动增长的主键值

	 * @param sql

	 * @param args

	 * @return

	 */
	public Number insert(final String sql,final Object... args){
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
		return keyHolder.getKey().intValue();
	}
	
	public void insert(final String sql) {
		jdbcTemplate.update(sql);
	}
	
	public List<Object[]> queryForListObjects(String sql,Object... args){
		RowMapper<Object[]> rm = new ObjectsRowMapper();
		return jdbcTemplate.query(sql,rm, args);
	}
	public Object[] queryForObjects(String sql,Object... args){
		ResultSetExtractor<Object[]> rse = new ObjectsResultSetExtractor();
		return jdbcTemplate.query(sql,rse, args);
	}
	private class ObjectsResultSetExtractor implements ResultSetExtractor<Object[]>{
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
	private class ObjectsRowMapper implements RowMapper<Object[]> {
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
	
	public <T> T queryForBean(String sql,Class<T> clazz,Object... args){
		List<T> listBean = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper<T>(clazz));
		if(listBean==null || listBean.isEmpty()){
			return null;
		}
		return listBean.get(0);
	}
	public <T> List<T> queryForListBean(String sql,Class<T> clazz,Object... args){
		List<T> listBean = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper<T>(clazz));
		return  listBean;
	}
	/**
	 * 开启事务
	 * @param runnable
	 */
	/*@Transactional
	public void tx(Runnable runnable) {
		runnable.run();
	}*/
	
	public SqlSession openSession(){
		return sqlSessionFactory.openSession();		
	}
	
	/*@Transactional
	public void txAndOpenSession(Runnable runnable){
		SqlSession session = sqlSessionFactory.openSession();
		try {
			runnable.run();
		} finally {
		    session.close();
		}
	}*/
}
