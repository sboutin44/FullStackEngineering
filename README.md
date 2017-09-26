
# Full Stack Engineering Exercise

Hi Brightwheel engineers, 

Here you will find my app answering a downtime 
problem during an email service provider outage.

## Launch the app:

To see how it works you need to:

1. Install the environment:
    - JDK 1.8
    - Gradle 2.3+

2. Download the code.

3. Insert the keys:
    - Put the API key values file `Keys.java` 

3. Build the code:
    - execute in a terminal `gradle build`
    (make sure Gradle is in your PATH)

4. Run the app:
    - `java -jar build/libs/gs-rest-service-0.1.0.jar`

5. Send an HTTP POST request with your Json to:
    - `http://localhost:8080/email`


All frameworks and libraries are downloaded thanks to the build.gradle file. The main framework I used to build
the HTTP service is Spring Boot. Here is a list of all the frameworks presents in this project:

 - Java Platform, Standard Edition 8
 - Spring Boot: for the main application running the HTTP service.
 - Jackson: a powerful Json parser.
 - Jsoup: a HTML parser for formatting the html body of the email.


A word on this exercise:

<div align="justify">
The application waits for a POST request at the `http://localhost:8080/email` endpoint, and sends the email through an 
email service provider selected in a list of different providers. If there is an error on the provider side, ie. if the returned HTTP status
  is of the form 5xx, the application choses another provider, and sends again... And so on until there's no remaining
  provider. In the exercise, there's only two of them: Mailgun and Sendgrid. However, I experienced troubles
  setting up an account at Sendgrid: while creating a Mailgun account has been easy
and quick, for Sendgrid it has been another story. I then tried with other providers: at Sendinblue, Mailchimp,
Sparkpost and Amazon SES, but either they didn't support transactional emails, or they didn't allow sending emails
to a single recipient. Finally I found Elasticemail which allowed me to do it, and thus to send an email to the address
in the Json provided in the POST request.

I choose email providers within a *for* loop so one can try to send the email with one provider at a time, this way we can add
further providers later. However, each provider has a different API and sets of parameters for POST requests,
so I defined a send method for each of these providers.

The tests are not finished yet, I experienced some issues when trying to create "simulated" email providers on a separate
local endpoint.
To correctly test the Mailgun and Elasticemail send functions, I tried to create dedicated 'fakeservice' enpoints to
send back specific status and exceptions using the actuator package provided by Spring boot. However the import didn't work.
Then I tried to create a second FakeService class using the @Controller annotation mapped to a 'fakeservice',
in addition to the EmailController class used for the 'email' endpoint. But to do so, the Springboot application must be
running at the same time, and the fake endpoint could not be reached since the associated FakeService wasn't in running
in parallel.
What I'm going to try is then to create a second Java project to create fake services at specific endpoints, 
and launch their executions when running the tests for the main HTTP service.
</div>