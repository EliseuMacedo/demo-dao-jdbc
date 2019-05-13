package application;



import java.util.Date;
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
		
		System.out.println("\n====== TESTE 3: seller findAll ========");
		list = sellerDao.findAll();
		list.forEach(System.out::println);
		
		System.out.println("\n====== TESTE 4: seller insert ========");
		Seller newSeller = new Seller(1, "Greg", "greg@gmail.com", new Date(),4000.0, dep);
		sellerDao.insert(newSeller);
		System.out.println("Inserido: novo ID = " + newSeller.getId());
		
		System.out.println("\n====== TESTE 5: seller update ========");
		seller = sellerDao.findById(2);
		
		seller.setName("Bruce Wayne");
		sellerDao.update(seller);
		
		System.out.println("Atualização completada");
		
		
		
	}

}
