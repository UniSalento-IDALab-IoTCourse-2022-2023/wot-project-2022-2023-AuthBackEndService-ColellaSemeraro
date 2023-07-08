package it.unisalento.pas.smartcitywastemanagement.di;
import org.springframework.stereotype.Component;

@Component
public class MySQLDBConnection implements IDBConnection {
    @Override
    public void connetti(){
        System.out.println("Connesso al database MySQL");
    }

}
