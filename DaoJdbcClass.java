package com.capgemini.citi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.citi.bean.Customer;
import com.capgemini.citi.bean.TransactionEntries;
import com.capgemini.citi.config.JdbcConfig;
import com.capgemini.citi.exception.CustomerExists;
import com.capgemini.citi.exception.CustomerNotFoundException;
import com.capgemini.citi.exception.InsuffiecientBalanceException;
import com.capgemini.citi.exception.SenderReceiverSameException;

public class DaoJdbcClass implements IDao{
	
	Connection connection = null;
	PreparedStatement statement = null;
	long newID = 0;
	

	public DaoJdbcClass() {
		super();
		try {
			this.connection = JdbcConfig.getConnection();
			statement = connection.prepareStatement(getLastestId);

			ResultSet set = statement.executeQuery();
			if (set.next() && set.getLong(1)!=0)
				this.newID = set.getLong(1);
			else
				this.newID = ids;				
		} catch (Exception e) {
			System.out.println("from constructor " + e.getMessage());
		}
		
	}

	@Override
	public String withdraw(double amount, Customer customer) throws InsuffiecientBalanceException {
		try {
			if (amount <= customer.getBalance() - 100) {
				
				//obj set
				statement = connection.prepareStatement(withDrawMoney);
				statement.setDouble(1,customer.getBalance()-amount);
				statement.setDouble(2, customer.getCustId());
				statement.executeUpdate();
				
				customer.setBalance(customer.getBalance()-amount);
				
				//store in transaction
				statement = connection.prepareStatement(insertTransaction);
				statement.setLong(1, customer.getMobileNo());
				statement.setString(2, "DB");
				statement.setDouble(3, amount);
				statement.setDouble(4, customer.getBalance());
				statement.executeUpdate();
				
				return "Rs." + amount + " debited from account "
						+ customer.getCustId() + " on "
						+ LocalDateTime.now() + "\nNew Balance is Rs."
						+ customer.getBalance();
			} else
				throw new InsuffiecientBalanceException(
						"You Have Insufficient Fund.");
		} catch (Exception e) {
			throw new InsuffiecientBalanceException(e.getMessage());
		}
		
		
	}

	@Override
	public String deposit(double amount, Customer customer) throws CustomerNotFoundException {
		try
		{	
			statement = connection.prepareStatement(depositMoney);
			statement.setDouble(1,customer.getBalance()+amount);
			statement.setLong(2, customer.getMobileNo());
			statement.executeUpdate();
			
			customer.setBalance(customer.getBalance()+amount);
			
			//store in transaction
			statement = connection.prepareStatement(insertTransaction);
			statement.setLong(1, customer.getMobileNo());
			statement.setString(2, "CR");
			statement.setDouble(3, amount);
			statement.setDouble(4, customer.getBalance());
			statement.executeUpdate();
			
			return "Rs." + amount + " credited on account "
			+ customer.getCustId() + " on " + LocalDateTime.now()
			+ "\nNew Balance is Rs." + customer.getBalance();
			
		}catch(Exception e)
		{
			throw new CustomerNotFoundException(e.getMessage());
		}
	}

	@Override
	public String fundTransfer(double amount, Customer cust1, Customer cust2)
			throws InsuffiecientBalanceException, SenderReceiverSameException {
		
		withdraw(amount, cust2);
		try {
			deposit(amount, cust1);
		} catch (CustomerNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return amount+"Amount Transferred Successfully";
		
		
	}

	@Override
	public Customer insertCustomer(Customer customer) throws CustomerExists {
		try {
			statement = connection.prepareStatement(checkIfUserExists);
			statement.setLong(1, customer.getMobileNo());
			ResultSet set = statement.executeQuery();

			if (set.next() && set.getInt(1) < 1) {

				customer.setCustId(++newID);

				statement = connection.prepareStatement(insertCustomer);
				statement.setDouble(1, customer.getCustId());
				statement.setString(2, customer.getName());
				statement.setString(3, customer.getEmail());
				statement.setLong(4, customer.getMobileNo());
				statement.setString(5, customer.getPassword());
				statement.setDouble(6, customer.getBalance());
				statement.executeUpdate();

				statement = connection.prepareStatement(insertTransaction);
				statement.setLong(1, customer.getMobileNo());
				statement.setString(2, "CR");
				statement.setDouble(3, customer.getBalance());
				statement.setDouble(4, customer.getBalance());
				statement.executeUpdate();
				return customer;
			} else {
				throw new CustomerExists("Customer Already Exists. Please Login");
			}
		} catch (Exception e) {
			//SQL Exception
			throw new CustomerExists(e.getMessage());
		}
		
		
		
	}

	@Override
	public Customer checkCredentials(long mobileNo) throws SQLException {
		
		
		statement = this.connection.prepareStatement(fetchQuery2);
		statement.setLong(1, mobileNo);
		statement.executeUpdate();

		ResultSet res = statement.getResultSet();
		if (res.next()) {
			Customer customer = new Customer();
			customer.setCustId(res.getDouble(1));
			customer.setName(res.getString(2));
			customer.setMobileNo(res.getLong(3));
			customer.setEmail(res.getString(4));
			customer.setBalance(res.getDouble(5));
			customer.setPassword(res.getString(6));
			return customer;
		} else {
			return null;
		}
	}

	@Override
	public double showBalance(Customer customer) throws SQLException {
		statement = this.connection
				.prepareStatement(showBalanceQuery);
		statement.setLong(1, customer.getMobileNo());

		ResultSet res = statement.executeQuery();
		if (res.next())
			;
		return res.getDouble("BALANCE"); // changes here
		
		
	}

	@Override
	public Customer login(long mobNo, String password) throws CustomerNotFoundException {
		try {
			Customer customer = new Customer();
			statement = connection.prepareStatement(findUser);
			statement.setLong(1, mobNo);
			statement.setString(2, password);
			statement.executeUpdate();

			ResultSet set = statement.getResultSet();
			//if user exists
			if (set.next()) {
				customer.setCustId(set.getLong(1));
				customer.setName(set.getString(2));
				customer.setEmail(set.getString(3));
				customer.setMobileNo(set.getLong(4));
				customer.setPassword(set.getString(5));
				customer.setBalance(set.getDouble(6));
				return customer;
			} else
				throw new CustomerNotFoundException("No User Found");
		} catch (Exception e) {
			throw new CustomerNotFoundException(e.getMessage());
		}
	
	
	
	}

	@Override
	public List<TransactionEntries> printTransaction(Customer customer) {
		List<TransactionEntries> summaryList = new ArrayList();
		try {
			statement = connection.prepareStatement(findTransaction);
			statement.setLong(1, customer.getMobileNo());
			ResultSet set = statement.executeQuery();
			while(set.next())
			{
				TransactionEntries trans = new TransactionEntries(
						set.getString(2),set.getLong(3),
						set.getDouble(4),set.getDouble(5)
						);
				summaryList.add(trans);
			}
			return summaryList;
		} catch (Exception e) {
			
			return summaryList;
		}
		
		
		
	}

}
