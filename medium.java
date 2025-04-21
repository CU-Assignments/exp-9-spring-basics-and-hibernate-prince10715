<?xml version='1.0' encoding='utf-8'?>
<!DOCTYPE hibernate-configuration PUBLIC 
    "-//Hibernate/Hibernate Configuration DTD 3.0//EN" 
    "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/your_db</property>
        <property name="hibernate.connection.username">root</property>
        <property name="hibernate.connection.password">your_password</property>
        <property name="dialect">org.hibernate.dialect.MySQLDialect</property>
        <property name="hibernate.hbm2ddl.auto">update</property>
        <mapping class="Student"/>
    </session-factory>
</hibernate-configuration>

  # student.java
  import jakarta.persistence.*;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int age;

    public Student() {}

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
 dao.java
   import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class StudentDAO {
    private SessionFactory factory;

    public StudentDAO() {
        factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public void createStudent(String name, int age) {
        Session session = factory.openSession();
        session.beginTransaction();
        Student student = new Student(name, age);
        session.save(student);
        session.getTransaction().commit();
        session.close();
    }

    public Student readStudent(int id) {
        Session session = factory.openSession();
        Student student = session.get(Student.class, id);
        session.close();
        return student;
    }

    public void updateStudent(int id, String name, int age) {
        Session session = factory.openSession();
        session.beginTransaction();
        Student student = session.get(Student.class, id);
        student.setName(name);
        student.setAge(age);
        session.update(student);
        session.getTransaction().commit();
        session.close();
    }

    public void deleteStudent(int id) {
        Session session = factory.openSession();
        session.beginTransaction();
        Student student = session.get(Student.class, id);
        session.delete(student);
        session.getTransaction().commit();
        session.close();
    }
}
  #mainapp.java
    public class MainApp {
    public static void main(String[] args) {
        StudentDAO dao = new StudentDAO();

        dao.createStudent("Prince Kumar", 22);

      
        Student student = dao.readStudent(1);
        System.out.println("Read: " + student.getName());

     
        dao.updateStudent(1, "Prince K.", 23);

       
        dao.deleteStudent(1);
    }
}
