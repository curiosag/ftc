package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestCursorContextColumn.class, TestCursorContextDetection.class, TestCursorContextExpr.class,
		TestCursorContextTableName.class, TestNameRecognition.class, TestQueryManipulation.class,
		TestQueryHandler.class })
public class AllTests {

}
