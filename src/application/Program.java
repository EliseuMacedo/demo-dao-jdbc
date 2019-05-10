package application;



import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

	
		//o programa não conhece a implementação, só conhece a interface
		SellerDao sellerDao = DaoFactory.CreateSellerDao();
		
		
		System.out.println("====== TESTE 1: seller findById ========");
		Seller seller = sellerDao.findById(3);
		
		System.out.println(seller);
		
		System.out.println("\n====== TESTE 2: seller findByDepartment ========");
		
		Department dep = new Department(2, null);
		
		List<Seller> list = sellerDao.findByDepartment(dep);
		
		list.forEach(System.out::println);
		
		
		
	}

}
