package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
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
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	// o dao vai ter uma depend�ncia com a conex�o
	private Connection conn;

	// para for�ar a inje��o de dependencia vou colocar um construtor
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO seller " + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());

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

		} catch (SQLException e) {

			throw new DbException(e.getMessage());
		}

		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Seller obj) {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "  
					+"WHERE Id = ?",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());

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
					"DELETE FROM seller WHERE Id = ?");

			st.setInt(1, id);
			//int linhas = st.executeUpdate(); para lan�ar exce��o, if (linhas == 0){throws new DbException("Id n�o encontrado!!!");
			st.executeUpdate();
			

		} catch (SQLException e) {

			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}

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

			// o resultset aponta para a posi��o zero, e somente na posi��o 1 que tem objeto

			if (rs.next()) { // testar se veio algum resultado
				Department dep = instantiateDepartment(rs);
				Seller objSeller = instantiateSeller(rs, dep);
				return objSeller;
			}
			return null;
		} catch (SQLException e) {
			// lan�ar exce��o personalizada
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			// a conex�o n�o fechou
		}

	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {

		Seller obj = new Seller(rs.getInt("Id"), rs.getString("Name"), rs.getString("Email"), rs.getDate("BirthDate"),
				rs.getDouble("BaseSalary"), dep);
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
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "ORDER BY Name");

			rs = st.executeQuery();

			List<Seller> list = new ArrayList<Seller>();
			// controlar dep pela estrutura map vazia para n�o repetir
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
		} catch (SQLException e) {
			// lan�ar exce��o personalizada
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			// a conex�o n�o fechou
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {

		// abriu fecha
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? " + "ORDER BY Name");

			st.setInt(1, department.getId());
			rs = st.executeQuery();

			List<Seller> list = new ArrayList<Seller>();
			// controlar dep pela estrutura map vazia para n�o repetir
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
		} catch (SQLException e) {
			// lan�ar exce��o personalizada
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			// a conex�o n�o fechou
		}
	}

}
