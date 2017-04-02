package org.gruppe2;


public class LoggerTest {
/*
	Logger logger;

	@Before
	public void setup() {
		logger = new Logger();
	}
	
	@After
	public void done() {
		logger.done();
	}
	
	@Test
	public void LogFileExists() {
		logger.record("LogFileExistsTest!");
		assertTrue("Logger should create a text file upon creation.", logger.getLog().exists());
	}

	@Test
	public void LogFileRecordsInput() throws Exception {
		logger.record("LogRecordsInputTest!");
		boolean containsInput = false;
		String loggedString = "All your base are belong to us.";
		for (int i = 0; i < 50; i++) {
			if (i==31)  // Random placement
				logger.record(loggedString);
			else
				logger.record("noise" + i);
		}
		Scanner parser = new Scanner(logger.getLog());
		while (parser.hasNextLine()) {
			String currentLine = parser.nextLine();
			if (currentLine.contains(loggedString))
				containsInput = true;
		}
		assertTrue("The text file should contain the logged String.", containsInput);
		parser.close();
	}
	*/

}
