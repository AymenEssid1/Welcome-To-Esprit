package tn.esprit.springfever.tools;

public class ResourceNotFoundException extends  Exception{

    public ResourceNotFoundException(String message){
        super(message);
    }

    public ResourceNotFoundException(String achievement, String id, Long id1) {
    }
}
//ResourceNotFoundException is a custom exception that you need to create yourself in order to handle the scenario when a user with a specific ID is not found.