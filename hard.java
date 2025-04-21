package com.example.bank.model;

import jakarta.persistence.*;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String holderName;
    private double balance;

    // Getters and Setters
}

# transaction.jav
  package com.example.bank.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int fromAccount;
    private int toAccount;
    private double amount;
    private LocalDateTime timestamp;

    // Getters and Setters
}


<!DOCTYPE hibernate-configuration PUBLIC 
"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

<hibernate-configuration>
    <session-factory>
        <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/bankdb</property>
        <property name="hibernate.connection.username">root</property>
        <property name="hibernate.connection.password">password</property>

        <property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>
        <property name="hibernate.hbm2ddl.auto">update</property>
        <property name="hibernate.show_sql">true</property>

        <mapping class="com.example.bank.model.Account"/>
        <mapping class="com.example.bank.model.Transaction"/>
    </session-factory>
</hibernate-configuration>


  package com.example.bank.service;

import com.example.bank.model.Account;
import com.example.bank.model.Transaction;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.time.LocalDateTime;

public class BankService {
    private static SessionFactory factory;

    static {
        factory = new Configuration().configure().buildSessionFactory();
    }

    public void transferMoney(int fromId, int toId, double amount) {
        Transaction tx = null;

        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();

            Account from = session.get(Account.class, fromId);
            Account to = session.get(Account.class, toId);

            if (from.getBalance() < amount) {
                throw new RuntimeException("Insufficient funds");
            }

            from.setBalance(from.getBalance() - amount);
            to.setBalance(to.getBalance() + amount);

            session.update(from);
            session.update(to);

            Transaction t = new Transaction();
            t.setFromAccount(fromId);
            t.setToAccount(toId);
            t.setAmount(amount);
            t.setTimestamp(LocalDateTime.now());

            session.save(t);

            tx.commit();
            System.out.println("Transaction successful!");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Transaction failed: " + e.getMessage());
        }
    }
}


package com.example.bank;

import com.example.bank.service.BankService;

public class MainApp {
    public static void main(String[] args) {
        BankService bankService = new BankService();

        // Example: Transfer 100 from Account 1 to Account 2
        bankService.transferMoney(1, 2, 100.0);
    }
}
