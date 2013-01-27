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

## Licence
* BSD. See LICENSE file

## Deploiement on a Cloud
### Cloudbees
* Online demo : http://play20startapp.yesnault.cloudbees.net/
* Online Source of this CloudBees Demo : https://github.com/yesnault/Play20StartApp/tree/cloudbees (see README : https://github.com/yesnault/Play20StartApp/tree/cloudbees#deploiement-on-cloudbees)

[![Build Status](https://buildhive.cloudbees.com/job/yesnault/job/Play20StartApp/badge/icon)](https://buildhive.cloudbees.com/job/yesnault/job/Play20StartApp/)


## Contact
Twitter : @yesnault
 
