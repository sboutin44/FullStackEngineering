Full Stack Engineering Exercise

Hi Brightwheel engineers, here you will find the application answering the problem of
a downtime during an email service provider outage.

How it works

To see how it works you need to:

1) Download the code.

2) Install the environment:
JDK 1.8
Gradle 2.3+

3) Build the code:
Execute in a terminal (make sure gradle is in your $path):
gradle build

4) Run the app:
java -jar build/libs/gs-rest-service-0.1.0.jar

5) Send an http POST request with your Json to:
http://localhost:8080/email


Every thing (frameworks/libraries) is downloaded with the build.gradle file. The main framework I use to build
the Http service is Spring Boot. Here is a list of all the frameworks presents in this project:

Java Platform, Standard Edition 8

springframework:
    - spring boot to build the main application running the http service.
jackson:
    - "formerly known as the standard JSON library for Java, as the "best JSON parser for Java", as their
    developper wrote on Github.
jsoup:
    - a HTML parser for pretty-printing the htlml body of the email.


A word on this exercise:

This application gets a POST request to the 'email' endpoint, and send the email through an email service providers
 selected in a list of different providers. If there is an error on the provider side, ie. if the returned http status
  is of the form 5xx, the application chose another provider, and send again... And so on until there's no remaining
  provider. In the exercise, there's only two of them: Mailgun and Sendgrid. However, I experienced troubles
  setting up an account at Sendgrid: while creating a Mailgun account has been easy
and quick, for Sendgrid it has been another story. I then tried with other providers: at Sendinblue, Mailchimp,
Sparkpost and Amazon SES, but either they didn't supported transactional email, or they didn't allow sending emails
to a single recipient. Finally I found Elasticemail which allowed me to do it, and thus to send an email to the address
in the Json provided in the POST request.

I used a for loop email providers so we can try to send the email one after the other, this way we can add
further providers if needed. However, each provider has a different API and sets of parameters for POST request,
so I defined a send method for each of these providers.

The tests are not finished yet, I experienced some issues when trying to create "simulated" email providers.
To correctly test the Mailgun and Elasticemail send functions, I tried to create dedicated 'fakeservice' enpoints to
send back specific status and exceptions using the actuator package provided by Spring boot. However the import didn't work.
In replacement, I tried to create a second FakeService class using the @Controller annotation mapped to a 'fakeservice',
in addition to the EmailController class used for the 'email' endpoint. But to do so, the Springboot application must be
running at the same time, and the fake endpoint couldn't be reached since the associated FakeService wasn't in a parellel.
What I'm going to try is then to create a second project representing the FakeService at a specific endpoint,
and launch it's execution when running the tests for the main Http service.