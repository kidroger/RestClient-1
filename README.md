# RestClient
A lightweight REST client for Java leveraging the HttpClient Fluent API.

## Motivation
During an AWS Lambda(Java) project, I needed a lightweight REST client for Java, as I need to minimize the deployment package size as small as possible. I couldn't find a good one for this purpose.<br/><br/>
So I made this lightweight REST client, it's built on top of the HttpClient Fluent API. As AWS Java SDK core has already included the HttpClient client, so we just add the Fluent API itself(about 30K) to the final Lambda jar file. It's pretty small, comparing to other REST clents(XXMB).
