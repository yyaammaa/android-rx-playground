package com.yyaammaa.rxplayground.RxBus;

public class GreetingEvent {

  public enum Person {
    ALICE, BOB, CHARLIE
  }

  private final Person mPerson;

  public GreetingEvent(Person person) {
    mPerson = person;
  }

  public Person greetingFrom() {
    return mPerson;
  }

}