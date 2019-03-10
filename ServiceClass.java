package com.capgemini.citi.service;

import java.sql.SQLException;
import java.util.List;

import com.capgemini.citi.bean.Customer;
import com.capgemini.citi.bean.TransactionEntries;
//import com.capgemini.citi.dao.DaoClass;
import com.capgemini.citi.dao.DaoJdbcClass;
import com.capgemini.citi.exception.CustomerExists;
import com.capgemini.citi.exception.CustomerNotFoundException;
import com.capgemini.citi.exception.InsuffiecientBalanceException;
import com.capgemini.citi.exception.SenderReceiverSameException;

public class ServiceClass implements IService {
	DaoJdbcClass dao = new DaoJdbcClass();

	@Override
	public boolean validateName(String name) {
		if(name.matches(NamePattern))
			return true;
		else
		return false;
	}

	@Override
	public boolean validateMobile(String mobile) {
		if(mobile.matches(mobilePattern))
			return true;
		else
		return false;
	}
	
	public boolean validateUsername(String username){
		if(username.matches(userNamePattern)){
			return true;
		}
		else
			return false;
		
	}

	@Override
	public boolean validateEmail(String email) {
		if(email.matches(emailPattern))
			return true;
		else
		return false;
	}
	
	public boolean validatePassword(String password){
		if(password.matches(passwordPattern))
			return true;
		else
		return false;
		
	}
	public Customer insertCustomer(Customer customer) throws CustomerExists{
		return dao.insertCustomer(customer);
	}
	
	public Customer checkCredentials(long mobNo) throws SQLException{
		
		
		return dao.checkCredentials(mobNo);
		
	}
	public String deposit(double amount,Customer customer) throws CustomerNotFoundException{
		return dao.deposit(amount, customer);
	}
	
	public String withdraw(double amount,Customer customer) throws InsuffiecientBalanceException{
		
		return dao.withdraw(amount, customer);
	}
	
	public double showBalance(Customer customer) throws SQLException{
		return dao.showBalance(customer);
	}
	
	public Customer login(long mobNo,String password){
		
			try {
				return dao.login(mobNo,password);
			} catch (CustomerNotFoundException e) {
				e.printStackTrace();
			}
		 
		return null;
	}
	
	public String fundTransfer(Customer customer1,Customer customer2,double amount) throws InsuffiecientBalanceException, SenderReceiverSameException{
		
		return dao.fundTransfer(amount, customer1, customer2);
	}
	public List<TransactionEntries> printTransaction(Customer customer){
		return dao.printTransaction(customer);
	}
	

}
