package application;

import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//o programa não conhece a implementação, só conhece a interface
		DepartmentDao departmentDao = DaoFactory.CreateDepartmentDao();
		
		Scanner sc = new Scanner(System.in);
		
		
		System.out.println("====== TESTE 1: Department findById ========");
		Department department = departmentDao.findById(3);
		System.out.println(department);

		System.out.println("\n====== TESTE 2: Department findAll ========");
		List<Department>list = departmentDao.findAll();
		list.forEach(System.out::println);
		
		System.out.println("\n====== TESTE 3: Department insert ========");
		Department newDepartment = new Department(1, "Ecommerce");
		departmentDao.insert(newDepartment);
		System.out.println("Inserido: novo ID = " + newDepartment.getId());
		
		System.out.println("\n====== TESTE 4: Department update ========");
		department = departmentDao.findById(4);
		
		department.setName("Padaria");
		departmentDao.update(department);
		
		System.out.println("Atualização completada");
		
		System.out.println("\n====== TESTE 5: Department delete ========");
		System.out.print("Entre com o id para delete: ");
		int id = sc.nextInt();
		departmentDao.deleteById(id);
		
		System.out.println("Deleção completada");
		sc.close();


	}

}
