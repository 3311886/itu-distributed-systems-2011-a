package bok.labexercise4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { 
	ReplicatedPhonebookServerTest.class, 
	ReplicatedPhonebookServerTest2.class
	}
)
public class TestAll {
}
