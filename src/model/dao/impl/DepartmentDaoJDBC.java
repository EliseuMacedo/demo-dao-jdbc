package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
	
	//Dao tem dependência com a conexão
	public Connection conn;
	
	//Forçar a injeção de dependência com o construtor
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	

	@Override
	public void insert(Department obj) {
		
		//interpretar e planejar o caminho dos dados
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("INSERT INTO department " + "(Name) "
					+ "VALUES " + "(?)", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			int linhasAfetadas = st.executeUpdate();
			
			if (linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Erro inesperado, nenhuma linha afetada");
			}
		}
		catch (SQLException e){
			throw new DbException(e.getMessage());
		}

		finally {
			DB.closeStatement(st);
		}
				
	}

	@Override
	public void update(Department obj) {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"UPDATE department "
					+ "SET Name = ? "  
					+"WHERE Id = ?",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());

			st.executeUpdate();

		} catch (SQLException e) {

			throw new DbException(e.getMessage());
		}

		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(int id) {
		
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"DELETE FROM department WHERE Id = ?");

			st.setInt(1, id);
			//int linhas = st.executeUpdate(); para lançar exceção, if (linhas == 0){throws new DbException("Id não encontrado!!!");
			st.executeUpdate();
			

		} catch (SQLException e) {

			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Department findById(int id) {
		
		// abriu fecha
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT * FROM department "
							+ "WHERE department.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();

			// o resultset aponta para a posição zero, e somente na posição 1 que tem objeto

			if (rs.next()) { // testar se veio algum resultado
				Department dep = instantiateDepartment(rs);
				
				return dep;
			}
			return null;
		} catch (SQLException e) {
			// lançar exceção personalizada
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			// a conexão não fechou
		}
		 
		
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException  {
		
		Department obj = new Department(rs.getInt("Id"), rs.getString("Name"));
		return obj;
	}


	@Override
	public List<Department> findAll() {
		// abriu fecha
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT * FROM department "
					+ "ORDER BY Name");

			rs = st.executeQuery();

			List<Department> list = new ArrayList<Department>();
			// controlar dep pela estrutura map vazia para não repetir
			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) { // testar se veio algum resultado

				Department dep = map.get(rs.getInt("Id"));

				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("Id"), dep);
				}

				Department obj = instantiateDepartment(rs);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			// lançar exceção personalizada
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			// a conexão não fechou
		}

	}

}
