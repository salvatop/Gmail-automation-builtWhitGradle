# Automated test for Gmail account written wiht selenium webdriver and java

The test is supposed to:
- Login to Gmail
- Compose an email from subject and body as mentioned in src/test/resources/test.properties
- Label email as "Social"
- Send the email to the same account which was used to login (from and to addresses would be the same)
- Wait for the email to arrive in the Inbox
- Mark email as starred
- Open the received email
- Verify email came under proper Label i.e. "Social"
- Verify the subject and body of the received email
- Generate test execution report at the end

# Prerequisites:
- ChromeDriver 2.44 , JDK 8+
- Any IDE

# Development Environment:
- Modify src/test/resources/test.properties to point to ChromeDriver's path on your system
- On any terminal, move to the project's root folder and execute the following command:
    - ./gradlew clean test
