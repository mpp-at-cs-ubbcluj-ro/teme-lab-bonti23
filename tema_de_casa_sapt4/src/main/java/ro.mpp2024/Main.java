package ro.mpp2024;

import ro.mpp2024.domain.User;
import ro.mpp2024.repository.RepositoryDBUser;
import ro.mpp2024.repository.RepositoryUser;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        RepositoryUser userRepo=new RepositoryDBUser(props);
        userRepo.save(new User("Bontidean Alexandra","bonti", "1234"));
        System.out.println("Toate masinile din db");
        for(User user:userRepo.findAll())
            System.out.println(user);
    }
}
