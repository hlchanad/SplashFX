package com.chanhonlun.splash.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Leaderboard {

	List<Person> persons;
	
	public Leaderboard() {
		persons = new ArrayList<Person>();
		
		persons.add(new Person("AAAAA", 10000));
		persons.add(new Person("BBBBB", 8000));
		persons.add(new Person("CCCCC", 6000));
		persons.add(new Person("DDDDD", 4000));
		persons.add(new Person("EEEEE", 2000));
	}
	
	public Iterator<Person> getPeopleIterator() {
		return this.persons.iterator(); 
	}
}
