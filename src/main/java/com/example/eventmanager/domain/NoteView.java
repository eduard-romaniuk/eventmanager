package com.example.eventmanager.domain;

public class NoteView {
    public interface ShortView {}
    public interface FullView extends ShortView, UserView.ShortView{}
}
