package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	
	public static SellerDao CreateSellerDao() {
		
		//operações para instanciar os Dao's
		return new SellerDaoJDBC(DB.getConnection());
		
		//Assim a classe expõe um metodo que retorna o tipo da interface
		//porém internamente ela instancia uma implementação.
		//Macede para não precisar expor a implementação
	}
}
