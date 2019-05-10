package application;

import java.util.Date;

import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		Department obj = new Department(1, "Books");

		System.out.println(obj);

		Seller seller = new Seller(1, "Eliseu", "eliseu@gmail.com", new Date(), 3000.000, obj);

		System.out.println(seller);

	}

}
