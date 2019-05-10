package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

	
		//o programa n�o conhece a implementa��o, s� conhece a interface
		SellerDao sellerDao = DaoFactory.CreateSellerDao();
		
		
		System.out.println("====== TESTE 1: seller findById ========");
		Seller seller = sellerDao.findById(3);
		
		System.out.println(seller);
		
	}

}
