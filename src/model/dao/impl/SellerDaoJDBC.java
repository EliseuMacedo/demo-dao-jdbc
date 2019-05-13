package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	// o dao vai ter uma dependência com a conexão
	private Connection conn;

	// para forçar a injeção de dependencia vou colocar um construtor
	public SellerDaoJDBC(Connection conn) {

		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Seller findById(int id) {

		// abriu fecha
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();

			// o resultset aponta para a posição zero, e somente na posição 1 que tem objeto

			if (rs.next()) { // testar se veio algum resultado
				Department dep = instantiateDepartment(rs);
				Seller objSeller = instantiateSeller(rs,dep);
				return objSeller;
			}
			return null;
		} 
		catch (SQLException e) {
			// lançar exceção personalizada
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			// a conexão não fechou
		}

	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {

		Seller obj = new Seller(rs.getInt("Id"), rs.getString("Name"), rs.getString("Email"),rs.getDate("BirthDate"), rs.getDouble("BaseSalary"), dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
	
		Department dep = new Department(rs.getInt("DepartmentId"), rs.getString("DepName"));
		return dep;
		
	}

	@Override
	public List<Seller> findAll() {
		// abriu fecha
				PreparedStatement st = null;
				ResultSet rs = null;

				try {
					st = conn.prepareStatement(
							"SELECT seller.*,department.Name as DepName " 
							+ "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " 
							+"ORDER BY Name");

					rs = st.executeQuery();

					
					List<Seller> list = new ArrayList<Seller>();
					//controlar dep pela estrutura map vazia para não repetir
					Map<Integer, Department> map = new HashMap<>();
					
					while (rs.next()) { // testar se veio algum resultado
						
						Department dep = map.get(rs.getInt("DepartmentId"));
						
						if (dep == null) {
							dep = instantiateDepartment(rs);
							map.put(rs.getInt("DepartmentId"), dep);
						}
						
						Seller obj = instantiateSeller(rs, dep);
						list.add(obj);
					}
					return list;
				} 
				catch (SQLException e) {
					// lançar exceção personalizada
					throw new DbException(e.getMessage());
				} 
				finally {
					DB.closeStatement(st);
					DB.closeResultSet(rs);
					// a conexão não fechou
				}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {

		// abriu fecha
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " 
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id " 
					+ "WHERE DepartmentId = ? "
					+"ORDER BY Name");

			st.setInt(1, department.getId());
			rs = st.executeQuery();

			
			List<Seller> list = new ArrayList<Seller>();
			//controlar dep pela estrutura map vazia para não repetir
			Map<Integer, Department> map = new HashMap<>();
			
			while (rs.next()) { // testar se veio algum resultado
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}
			return list;
		} 
		catch (SQLException e) {
			// lançar exceção personalizada
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			// a conexão não fechou
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
