package com.example.eventmanager.domain.transfer.validation;

public class ItemValidation {

    // For create items
    public interface New {  }

    // For existing items. For example: update etc...
    public interface Exist { }

    public interface UpdateName extends Exist { }

    public interface UpdatePriority extends Exist { }

    public interface UpdateDescription extends Exist { }

    public interface UpdateTags extends Exist { }

    public interface AddLikes extends Exist { }
}
