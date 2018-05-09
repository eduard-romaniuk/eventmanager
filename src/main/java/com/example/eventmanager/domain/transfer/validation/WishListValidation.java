package com.example.eventmanager.domain.transfer.validation;

public class WishListValidation {
    // For create wish list
    public interface New {  }

    // For existing wish list. For example: update etc...
    public interface Exist { }

    public interface UpdateName extends Exist { }

}
