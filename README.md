# Play20StartApp

This is a sample application using Play Framework 2.0

## Features
* Sign Up
* Sign In
* Reset password
* Settings Page
* Change email from Settings

## Application details
* No clear password stored in db
* Secure workflow to reset password
* Secure workflow to reset email
* I18n example (en, fr)
* Using Typesafe Plugin Mailer : https://github.com/typesafehub/play-plugins/tree/master/mailer
* Using Twitter Bootstrap 2 : http://twitter.github.com/bootstrap/, theme Spacelab from http://http://bootswatch.com
* Using Less and CoffeeScript
* Using a password generator 

## Try
* Rename conf/email.conf.example in conf/email.conf and check it (smtp, etc...)
* Download Play Framework 2 from http://www.playframework.org/
* Open a terminal in Play20StartApp directory and exec `play run`
* Generate Scala Doc & Javadoc with exec `play app-doc` (task app-doc add in Build.scala file)

## Documentation
* Insecure Cryptography (an explanation of good web app password practices and why they're good) : http://webapp-hardening.heroku.com/insecure_crypto
* Failing with Passwords (a presentation on issues in user authentication) : http://tersesystems.com/2012/02/17/failing-with-passwords
* Everything you ever wanted to know about secure password reset : http://www.troyhunt.com/2012/05/everything-you-ever-wanted-to-know.html

## Promotion
* You can vote for this application on http://www.playmodules.net/demo/14

## Deploy on a Cloud
* Example with Cloudbees, see Readme on branch Cloudbees : https://github.com/yesnault/Play20StartApp/tree/cloudbees

## Licence
* BSD. See LICENSE file

## Deploiement on Cloudbees

* Read Cloudbees Documentation : http://developer.cloudbees.com/bin/view/RUN/Playframework 
* You can now install the plugin cloudbees-deploy and configure sendGrid on your cloudbees account in order to send mail with this service
* Export locally fake alias, allowing Play2 compiling application.conf and mail.conf
<pre><code>
    $ export MYSQL_URL_DB=na
    $ export MYSQL_USERNAME_DB=na
    $ export MYSQL_PASSWORD_DB=na
    $ export SENDGRID_SMTP_HOST=na
    $ export SENDGRID_USERNAME=na
    $ export SENDGRID_PASSWORD=na
</code></pre>

* Create the application and db on CloudBees
<pre><code>
    bees app:create APP_NAME
    bees db:create DB_NAME
</code></pre>

* Deploy the application with play console
<pre><code>
    $ play 
    > cloudbees-deploy-config cloudbees APP_NAME
</code></pre>
Example with APP_NAME = Play20StartApp : 
<pre><code>
    $ play 
    > cloudbees-deploy-config cloudbees Play20StartApp
</code></pre>

* Bind database to the application
<pre><code>
    bees app:bind -a APP_NAME -db DB_NAME -as DB
</code></pre>

(replace APP_NAME and DB_NAME as you want, example : bees app:bin -a yesnault/Play20StartApp -db play20StartApp -as DB)

* Online demo : http://play20startapp.yesnault.cloudbees.net/
* Online Source of this CloudBees Demo : https://github.com/yesnault/Play20StartApp/tree/cloudbees

## Contact
Twitter : @yesnault
 
