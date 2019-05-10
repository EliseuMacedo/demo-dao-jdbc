package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	
	public static SellerDao CreateSellerDao() {
		
		//opera��es para instanciar os Dao's
		return new SellerDaoJDBC(DB.getConnection());
		
		//Assim a classe exp�e um metodo que retorna o tipo da interface
		//por�m internamente ela instancia uma implementa��o.
		//Macede para n�o precisar expor a implementa��o
	}
}
